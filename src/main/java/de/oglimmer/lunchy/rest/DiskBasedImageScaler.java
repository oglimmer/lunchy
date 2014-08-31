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

	private String filename;
	private File originalFile;

	public DiskBasedImageScaler(String filename, int maxWidth, int maxHeight, Method method) {
		super(maxWidth, maxHeight, method);
		this.filename = filename;
		this.originalFile = new File(filename);
	}

	public void saveToDisk() throws IOException {
		ImageIO.write(scaledImage, FileServices.getFileType(filename), originalFile);
	}

	@Override
	protected void loadImage() throws IOException {
		originalImage = ImageIO.read(originalFile);
	}

	@Override
	protected BufferedInputStream getBufferedInputStream() throws FileNotFoundException {
		return new BufferedInputStream(new FileInputStream(originalFile));
	}
}
