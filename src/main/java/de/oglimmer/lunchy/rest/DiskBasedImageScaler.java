package de.oglimmer.lunchy.rest;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr.Method;

import de.oglimmer.lunchy.services.FileServices;

public class DiskBasedImageScaler extends ImageScaler {

	private File fullQualOriginalFile;

	public DiskBasedImageScaler(String fullQualFilename, int maxWidth, int maxHeight, Method method) {
		super(maxWidth, maxHeight, method);
		this.fullQualOriginalFile = new File(fullQualFilename);
	}

	public void overwriteToDisk() throws IOException {
		ImageIO.write(scaledImage, FileServices.getFileType(fullQualOriginalFile.getCanonicalPath()), fullQualOriginalFile);
	}

	public void saveToDisk(File file) throws IOException {
		ImageIO.write(scaledImage, FileServices.getFileType(file.getCanonicalPath()), file);
	}

	@Override
	protected void loadImage() throws IOException {
		originalImage = ImageIO.read(fullQualOriginalFile);
	}

	@Override
	protected BufferedInputStream getBufferedInputStream() throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(fullQualOriginalFile));
	}
}
