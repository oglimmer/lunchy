package de.oglimmer.lunchy.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import lombok.Getter;

import org.imgscalr.Scalr;

import de.oglimmer.lunchy.services.FileServices;
import de.oglimmer.lunchy.services.LunchyProperties;

public class EmailNotificationImageScaler {

	private static final int MAX_WIDTH = 480;
	private static final int MAX_HEIGHT = 240;

	private DiskBasedImageScaler scaler;
	@Getter
	private File tmpScaledFile;

	public EmailNotificationImageScaler(String filename, Path tmpDir) throws IOException {
		tmpScaledFile = new File(tmpDir + "/" + filename);
		if (!tmpScaledFile.exists()) {
			createScaledVersion(filename);
		}
	}

	private void createScaledVersion(String filename) throws IOException {
		String originalFilename = LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename;
		copyFileToTmp(new File(originalFilename));

		scaler = new DiskBasedImageScaler(tmpScaledFile.getCanonicalPath(), MAX_WIDTH, MAX_HEIGHT, Scalr.Method.AUTOMATIC);
		if (scale()) {
			saveToDisk();
		}
	}

	private void copyFileToTmp(File originalFile) throws IOException {
		FileServices.copy(originalFile, tmpScaledFile);
	}

	private boolean scale() throws IOException {
		return scaler.scale();
	}

	private void saveToDisk() throws IOException {
		scaler.saveToDisk();
	}

}
