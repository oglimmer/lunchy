package de.oglimmer.lunchy.rest;

import java.io.File;
import java.io.IOException;

import org.imgscalr.Scalr;

import de.oglimmer.lunchy.services.FileServices;
import de.oglimmer.lunchy.services.LunchyProperties;

public class UploadImageScaler {

	private static final int MAX_HEIGHT = 720;
	private static final int MAX_WIDTH = 1200;

	private DiskBasedImageScaler scaler;
	private String filename;
	private File originalFile;

	public UploadImageScaler(String filename) {
		this.filename = filename;
		String fullQualFilename = LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename;
		originalFile = new File(fullQualFilename);
		scaler = new DiskBasedImageScaler(fullQualFilename, MAX_WIDTH, MAX_HEIGHT, Scalr.Method.ULTRA_QUALITY);
	}

	public void moveOriginalFileToBackup() throws IOException {
		String path = LunchyProperties.INSTANCE.getBackupDestinationPath();
		if (path == null) {
			path = LunchyProperties.INSTANCE.getPictureDestinationPath();
		}
		File backupFile = new File(path + "/" + filename + "_original" + FileServices.getFileExtension(filename));
		FileServices.move(originalFile, backupFile);
	}

	public boolean scale() throws IOException {
		return scaler.scale();
	}

	public void saveToDisk() throws IOException {
		scaler.overwriteToDisk();
	}

}
