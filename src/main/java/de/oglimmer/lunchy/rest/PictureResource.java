package de.oglimmer.lunchy.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import org.apache.commons.lang3.RandomStringUtils;

import de.oglimmer.lunchy.database.PicturesDao;
import de.oglimmer.lunchy.database.generated.tables.records.PicturesRecord;
import de.oglimmer.lunchy.rest.dto.Picture;
import de.oglimmer.lunchy.services.FileServices;
import de.oglimmer.lunchy.services.LunchyProperties;

@Path("pictures")
public class PictureResource {

	@SneakyThrows(value = IOException.class)
	@GET
	@Path("{filename}")
	public Response load(@PathParam("filename") String filename) {
		if (filename.contains("..")) {
			throw new RuntimeException("Illegal filename:" + filename);
		}

		String fileExt = FileServices.getFileExtension(filename);
		String mt;
		switch (fileExt) {
		case ".jpg":
		case ".jpeg":
			mt = "image/jpeg";
			break;
		case ".gif":
			mt = "image/gif";
			break;
		case ".png":
			mt = "image/png";
			break;
		default:
			mt = MediaType.APPLICATION_OCTET_STREAM;
		}

		FileInputStream fis = new FileInputStream(LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename);

		return Response.ok(fis, mt).build();
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Picture create(@Context HttpServletRequest request, CreateParam input) {
		return update(request, null, input);
	}

	@SneakyThrows(value = IOException.class)
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public Picture update(@Context HttpServletRequest request, @PathParam("id") Integer id, CreateParam input) {
		PicturesRecord rec = new PicturesRecord();

		if (input.getId() == null || input.getId() == 0) {
			rec.setFkuser((Integer) request.getSession(false).getAttribute("userId"));
			rec.setCreatedon(new Timestamp(new Date().getTime()));
			rec.setFklocation(input.getFklocation());
			rec.setFilename(RandomStringUtils.randomAlphanumeric(32) + FileServices.getFileExtension(input.getOriginalFilename()));
			rec.setCaption(input.getCaption());

			moveFromTmpToPermanentDir(input, rec);
			scaleIfTooLarge(rec);
		} else {
			rec = PicturesDao.INSTANCE.getById(input.getId());
			rec.setCaption(input.getCaption());
		}

		PicturesDao.INSTANCE.store(rec);
		Picture retObj = new Picture();
		BeanMappingProvider.INSTANCE.getMapper().map(rec, retObj);
		return retObj;
	}

	private void scaleIfTooLarge(PicturesRecord rec) throws IOException {
		PictureScaler ps = new PictureScaler(rec.getFilename());
		if (ps.scale()) {
			ps.moveOriginalFileToBackup();
			ps.saveToDisk();
		}
	}

	private void moveFromTmpToPermanentDir(CreateParam input, PicturesRecord rec) throws IOException {
		FileServices.move(input.getUniqueId(), rec.getFilename());
	}

	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class CreateParam extends Picture {
		private String uniqueId;
		private String originalFilename;
	}
}
