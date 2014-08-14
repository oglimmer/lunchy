package de.oglimmer.lunchy.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileServices {

	public static void move(String uniqueId, String filename) throws IOException {
		File file = new File(LunchyProperties.INSTANCE.getTmpPath() + "/" + uniqueId);
		File newFile = new File(LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename);

		move(file, newFile);
	}

	public static void move(File file, File newFile) throws IOException {
		Files.move(file.toPath(), newFile.toPath());
	}

	public static String getFileExtension(String originalFilename) {
		String type = getFileType(originalFilename);
		if (!type.isEmpty()) {
			return "." + type;
		}
		return "";
	}

	public static String getFileType(String originalFilename) {
		if (originalFilename.contains(".")) {
			return originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
		}
		return "";
	}

}
