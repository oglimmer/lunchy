package de.oglimmer.lunchy.web.servlet;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import com.google.common.io.ByteStreams;

import de.oglimmer.lunchy.services.LunchyProperties;

@Slf4j
@WebServlet(value = "/upload", name = "upload-servlet")
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String resumableIdentifier = req.getParameter("flowIdentifier");
		try (FileOutputStream fos = new FileOutputStream(LunchyProperties.INSTANCE.getTmpPath() + "/" + resumableIdentifier)) {
			log.debug("Uploaded file {} with {} bytes", resumableIdentifier, ByteStreams.copy(req.getInputStream(), fos));
		}
	}
}
