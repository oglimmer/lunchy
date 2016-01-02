package de.oglimmer.lunchy.rest.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.id.uuid.UUID;
import org.imgscalr.Scalr;
import org.jooq.Record;

import de.oglimmer.lunchy.beanMapping.BeanMappingProvider;
import de.oglimmer.lunchy.database.dao.PictureDao;
import de.oglimmer.lunchy.database.dao.UpdatesDao;
import de.oglimmer.lunchy.database.dao.UserPictureVoteDao;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.database.generated.tables.records.UsersPicturesVotesRecord;
import de.oglimmer.lunchy.image.DiskBasedImageScaler;
import de.oglimmer.lunchy.image.UploadImageScaler;
import de.oglimmer.lunchy.rest.SessionProvider;
import de.oglimmer.lunchy.rest.dto.MailImage;
import de.oglimmer.lunchy.rest.dto.PictureCreateInput;
import de.oglimmer.lunchy.rest.dto.PictureResponse;
import de.oglimmer.lunchy.rest.dto.PictureUpdateInput;
import de.oglimmer.lunchy.security.Permission;
import de.oglimmer.lunchy.security.SecurityProvider;
import de.oglimmer.lunchy.services.CommunityService;
import de.oglimmer.lunchy.services.DateCalcService;
import de.oglimmer.lunchy.services.FileService;
import de.oglimmer.lunchy.services.LunchyProperties;

@Slf4j
@Path("pictures")
public class PictureResource {

	private static final int SMALL_PIC_SIZE = 650;
	private static final int LARGE_PIC_SIZE = 650;
	public static final int SMALL_PIC_BOUNDRY = 767;

	@SneakyThrows(value = IOException.class)
	@GET
	@Path("{filename}")
	public Response load(@PathParam("filename") String filename, @QueryParam("size") String screenWidth) {
		// NEVER REMOVE THIS!!! SECURITY check to avoid reading all files on the filesystem
		if (filename.contains("..") || filename.contains("/")) {
			throw new RuntimeException("Illegal filename:" + filename);
		}
		String mediaType = FileService.getMediaTypeFromFileExtension(filename);
		InputStream inputStream = new InputStreamProvider(filename, screenWidth).getInputStream();
		return Response.ok(inputStream, mediaType).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MailImage> queryPictures(@Context HttpServletRequest request, @QueryParam("startPos") Integer startPos,
			@QueryParam("numberOfRecords") Integer numberOfRecords) {
		List<Record> recordList = UpdatesDao.INSTANCE.getPictures(CommunityService.get(request), startPos, numberOfRecords,
				SessionProvider.INSTANCE.getLoggedInUserId(request));
		return BeanMappingProvider.INSTANCE.mapListCustomDto(recordList, MailImage.class);
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@SneakyThrows(value = IOException.class)
	public PictureResponse create(@Context HttpServletRequest request, PictureCreateInputWithTmp inputDto) {
		PicturesRecord rec = new PicturesRecord();

		rec.setFkCommunity(CommunityService.get(request));
		rec.setFkUser(SessionProvider.INSTANCE.getLoggedInUserId(request));
		rec.setCreatedOn(DateCalcService.getNow());
		rec.setFilename(UUID.randomUUID().toString() + FileService.getFileExtension(inputDto.getOriginalFilename()));
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
		PicturesRecord rec = PictureDao.INSTANCE.getById(id, CommunityService.get(request));
		if (SecurityProvider.INSTANCE.checkRightOnSession(request, Permission.ADMIN)
				|| SessionProvider.INSTANCE.getLoggedInUserId(request) == rec.getFkUser()) {
			BeanMappingProvider.INSTANCE.map(inputDto, rec);
			return storeRec(rec);
		}
		return null;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}/vote")
	public void vote(@Context HttpServletRequest request, @PathParam("id") Integer id, PictureVoteInput inputDto) {
		PicturesRecord rec = PictureDao.INSTANCE.getById(id, CommunityService.get(request));
		Integer userId = SessionProvider.INSTANCE.getLoggedInUserId(request);
		UsersPicturesVotesRecord vote = UserPictureVoteDao.INSTANCE.getByParents(id, userId);
		if ("up".equalsIgnoreCase(inputDto.getDirection()) && vote == null) {
			vote = new UsersPicturesVotesRecord();
			vote.setFkCommunity(CommunityService.get(request));
			vote.setFkPicture(id);
			vote.setFkUser(userId);
			vote.setCreatedOn(DateCalcService.getNow());
			UserPictureVoteDao.INSTANCE.store(vote);
			rec.setUpVotes(rec.getUpVotes() + 1);
			PictureDao.INSTANCE.store(rec);
		} else if ("down".equalsIgnoreCase(inputDto.getDirection()) && vote != null) {
			rec.setUpVotes(rec.getUpVotes() - 1);
			PictureDao.INSTANCE.store(rec);
			UserPictureVoteDao.INSTANCE.delete(vote.getId(), CommunityService.get(request));
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
		FileService.move(fromFileName, toFileName);
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

	@Data
	public static class PictureChangeCaptionInput {
		private String caption;
	}

	@AllArgsConstructor
	class InputStreamProvider {

		private String filename;
		private String screenWidth;

		public InputStream getInputStream() throws IOException, FileNotFoundException {
			if (screenWidth != null && !screenWidth.trim().isEmpty()) {
				return getScaledImageStream();
			} else {
				return new FileInputStream(getDefauledScaledFilename());
			}
		}

		private InputStream getScaledImageStream() throws IOException, FileNotFoundException {
			int screenWidthInt = convertScreenWidth();
			int scaledWidth = screenWidthInt < SMALL_PIC_BOUNDRY ? SMALL_PIC_SIZE : LARGE_PIC_SIZE;

			File customScaledFile = new File(getCustomScaledFilename(scaledWidth));
			if (!customScaledFile.exists()) {
				String picToLoad = getOriginalFilename();
				if (!new File(picToLoad).exists()) {
					picToLoad = getDefauledScaledFilename();
				}
				scaleAndSave(scaledWidth, customScaledFile, picToLoad);
			}
			return new FileInputStream(customScaledFile);
		}

		private void scaleAndSave(int scaledWidth, File scaledFile, String originalFile) throws IOException {
			synchronized (PictureResource.class) {
				DiskBasedImageScaler dbis = new DiskBasedImageScaler(originalFile, scaledWidth, 9999, Scalr.Method.ULTRA_QUALITY);
				dbis.scale();
				dbis.saveToDisk(scaledFile);
			}
		}

		private String getDefauledScaledFilename() {
			return LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename;
		}

		private String getOriginalFilename() {
			return LunchyProperties.INSTANCE.getBackupDestinationPath() + "/" + filename + "_original"
					+ FileService.getFileExtension(filename);
		}

		private String getCustomScaledFilename(int scaledWidth) {
			return getDefauledScaledFilename() + "_" + scaledWidth + FileService.getFileExtension(filename);
		}

		private int convertScreenWidth() {
			try {
				return Integer.parseInt(screenWidth);
			} catch (NumberFormatException e) {
				return SMALL_PIC_BOUNDRY;
			}
		}
	}
}
