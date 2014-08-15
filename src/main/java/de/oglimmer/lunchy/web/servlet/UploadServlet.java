package de.oglimmer.lunchy.web.servlet;

import java.io.DataOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import de.oglimmer.lunchy.services.LunchyProperties;

@Slf4j
@WebServlet(value = "/upload", name = "upload-servlet")
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private LoadingCache<String, Set<Long>> chunkCache;

	public UploadServlet() {
		chunkCache = CacheBuilder.newBuilder().expireAfterAccess(3, TimeUnit.HOURS).build(new CacheLoader<String, Set<Long>>() {
			public Set<Long> load(String key) {
				return new HashSet<>();
			}
		});
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String flowIdentifier = req.getParameter("flowIdentifier");
		long flowChunkNumber = Long.parseLong(req.getParameter("flowChunkNumber"));

		if (isChunkAlreadyWritten(flowIdentifier, flowChunkNumber)) {
			respondChunkAlreadyWritten(resp, flowIdentifier, flowChunkNumber);
		} else {
			respondChunkNotWritten(resp, flowIdentifier, flowChunkNumber);
		}
	}

	private void respondChunkNotWritten(HttpServletResponse resp, String flowIdentifier, long flowChunkNumber) throws IOException {
		log.debug("ID:{} has chunk:{} not yet!", flowIdentifier, flowChunkNumber);
		resp.sendError(HttpServletResponse.SC_NOT_FOUND);
	}

	private void respondChunkAlreadyWritten(HttpServletResponse resp, String flowIdentifier, long flowChunkNumber) throws IOException {
		log.debug("ID:{} has chunk:{} already written!", flowIdentifier, flowChunkNumber);
		resp.getWriter().print("Uploaded.");
	}

	@SneakyThrows(value = ExecutionException.class)
	private boolean isChunkAlreadyWritten(String flowIdentifier, long flowChunkNumber) {
		return chunkCache.get(flowIdentifier).contains(new Long(flowChunkNumber));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String flowIdentifier = req.getParameter("flowIdentifier");
		File originalFile = new File(LunchyProperties.INSTANCE.getTmpPath() + "/" + flowIdentifier);

		long flowChunkNumber = Long.parseLong(req.getParameter("flowChunkNumber"));
		long flowChunkSize = Long.parseLong(req.getParameter("flowChunkSize"));
		long flowTotalSize = Long.parseLong(req.getParameter("flowTotalSize"));
		long flowTotalChunks = Long.parseLong(req.getParameter("flowTotalChunks"));

		log.debug("ID:{} with chunk {} write for {}/{} ", flowIdentifier, flowChunkNumber, flowChunkSize, flowTotalSize);

		writeChunkToFile(req, flowChunkNumber, flowChunkSize, flowTotalSize, originalFile);
		addCache(flowIdentifier, flowChunkNumber, flowTotalChunks);

	}

	private void writeChunkToFile(HttpServletRequest req, long flowChunkNumber, long flowChunkSize, long flowTotalSize, File originalFile)
			throws IOException, FileNotFoundException {
		try (InputStream from = req.getInputStream()) {
			try (RandomAccessFile raf = new RandomAccessFile(originalFile, "rw")) {
				raf.setLength(flowTotalSize);
				raf.seek((flowChunkNumber - 1) * flowChunkSize);
				copyBytes(from, raf);
			}
		}
	}

	private synchronized void addCache(String flowIdentifier, long flowChunkNumber, long flowTotalChunks) {
		try {
			Set<Long> chunkSet = chunkCache.get(flowIdentifier);
			chunkSet.add(new Long(flowChunkNumber));
			if (flowTotalChunks == chunkSet.size()) {
				chunkCache.invalidate(flowIdentifier);
			}
		} catch (ExecutionException e) {
			log.error("Failed to add chunk to cache", e);
		}
	}

	private void copyBytes(InputStream in, DataOutput out) throws IOException {
		byte[] buf = new byte[0x1000];
		while (true) {
			int r = in.read(buf);
			if (r == -1) {
				break;
			}
			out.write(buf, 0, r);
		}
	}

}
