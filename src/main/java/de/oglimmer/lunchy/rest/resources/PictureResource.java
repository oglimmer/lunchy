package de.oglimmer.lunchy.rest.resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.PictureDao;
import de.oglimmer.lunchy.database.dao.UserPictureVoteDao;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;
import de.oglimmer.lunchy.rest.MemoryBaseImageScaler;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.UploadImageScaler;
import de.oglimmer.lunchy.rest.dto.PictureCreateInput;
import de.oglimmer.lunchy.rest.dto.PictureResponse;
import de.oglimmer.lunchy.rest.dto.PictureUpdateInput;
import de.oglimmer.lunchy.services.Community;
import de.oglimmer.lunchy.services.DateCalculation;
import de.oglimmer.lunchy.services.FileServices;
import de.oglimmer.lunchy.services.LunchyProperties;

@Slf4j
@Path("pictures")
public class PictureResource {

	private static final int SMALL_PIC_SIZE = 200;
	private static final int LARGE_PIC_SIZE = 464;
	public static final int SMALL_PIC_BOUNDRY = 767;

	@SneakyThrows(value = IOException.class)
	@GET
	@Path("{filename}")
	public Response load(@PathParam("filename") String filename, @QueryParam("size") Integer screenWidth) {
		// NEVER REMOVE THIS!!! SECURITY check to avoid reading all files on the filesystem
		if (filename.contains("..") || filename.contains("/")) {
			throw new RuntimeException("Illegal filename:" + filename);
		}
		String mediaType = FileServices.getMediaTypeFromFileExtension(filename);
		InputStream is = new FileInputStream(LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename);
		if (screenWidth != null) {
			MemoryBaseImageScaler imageScaler = new MemoryBaseImageScaler(
					screenWidth < SMALL_PIC_BOUNDRY ? SMALL_PIC_SIZE : LARGE_PIC_SIZE, 9999, Scalr.Method.SPEED, is);
			is = imageScaler.getScaledInputStream(FileServices.getFileType(filename));
		}
		return Response.ok(is, mediaType).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SneakyThrows(value = IOException.class)
	public PictureResponse create(@Context HttpServletRequest request, PictureCreateInputWithTmp inputDto) {
		PicturesRecord rec = new PicturesRecord();

		rec.setFkCommunity(Community.get(request));
		rec.setFkUser(SessionProvider.INSTANCE.getLoggedInUserId(request));
		rec.setCreatedOn(new Timestamp(new Date().getTime()));
		rec.setFilename(RandomStringUtils.randomAlphanumeric(32) + FileServices.getFileExtension(inputDto.getOriginalFilename()));
		rec.setUpVotes(0);

		BeanMappingProvider.INSTANCE.map(inputDto, rec);

		moveFromTmpToPermanentDir(inputDto.getUniqueId(), rec.getFilename());
		scaleIfTooLarge(rec);

		return storeRec(rec);

	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public PictureResponse update(@Context HttpServletRequest request, @PathParam("id") Integer id, PictureUpdateInput inputDto) {
		PicturesRecord rec = PictureDao.INSTANCE.getById(id, Community.get(request));
		BeanMappingProvider.INSTANCE.map(inputDto, rec);
		return storeRec(rec);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}/vote")
	public void vote(@Context HttpServletRequest request, @PathParam("id") Integer id, PictureVoteInput inputDto) {
		PicturesRecord rec = PictureDao.INSTANCE.getById(id, Community.get(request));
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		UsersPicturesVotesRecord vote = UserPictureVoteDao.INSTANCE.getByParents(id, userId);
		if ("up".equalsIgnoreCase(inputDto.getDirection()) && vote == null) {
			vote = new UsersPicturesVotesRecord();
			vote.setFkCommunity(Community.get(request));
			vote.setFkPicture(id);
			vote.setFkUser(userId);
			vote.setCreatedOn(DateCalculation.INSTANCE.getNow());
			UserPictureVoteDao.INSTANCE.store(vote);
			rec.setUpVotes(rec.getUpVotes() + 1);
			PictureDao.INSTANCE.store(rec);
		} else if ("down".equalsIgnoreCase(inputDto.getDirection()) && vote != null) {
			rec.setUpVotes(rec.getUpVotes() - 1);
			PictureDao.INSTANCE.store(rec);
			UserPictureVoteDao.INSTANCE.delete(vote.getId(), Community.get(request));
		} else {
			log.warn("vote called for {} with direction {} but user {}'s vote didn't match", id, inputDto.getDirection(), userId);
		}
	}

	private PictureResponse storeRec(PicturesRecord rec) {
		PictureDao.INSTANCE.store(rec);
		return BeanMappingProvider.INSTANCE.map(rec, PictureResponse.class);
	}

	private void scaleIfTooLarge(PicturesRecord rec) throws IOException {
		UploadImageScaler ps = new UploadImageScaler(rec.getFilename());
		if (ps.scale()) {
			ps.moveOriginalFileToBackup();
			ps.saveToDisk();
		}
	}

	private void moveFromTmpToPermanentDir(String fromFileName, String toFileName) throws IOException {
		FileServices.move(fromFileName, toFileName);
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class PictureCreateInputWithTmp extends PictureCreateInput {
		private String uniqueId;
		private String originalFilename;
	}

	@Data
	public static class PictureVoteInput {
		private String direction;
	}
}
