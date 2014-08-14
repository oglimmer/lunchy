package de.oglimmer.lunchy.rest;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.oglimmer.lunchy.services.FileServices;
import de.oglimmer.lunchy.services.LunchyProperties;

public class PictureScaler {

	private static final int MAX_HEIGHT = 720;
	private static final int MAX_WIDTH = 1200;

	private String filename;
	private File originalFile;
	private int imageWidth;
	private int imageHeight;
	private BufferedImage originalImage;
	private BufferedImage scaledImage;

	public PictureScaler(String filename) {
		this.filename = filename;
		originalFile = new File(LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename);
	}

	public boolean scale() throws IOException {
		loadImage();
		readDimensions();
		if (isScalingNeeded()) {
			calcDimAndScale();
			return true;
		}
		return false;
	}

	private void calcDimAndScale() throws IOException {
		int newWidth = imageWidth;
		int newHeight = imageHeight;
		if (newWidth > MAX_WIDTH) {
			newHeight *= (Double.valueOf(MAX_WIDTH) / newWidth);
			newWidth = MAX_WIDTH;
		}
		if (newHeight > MAX_HEIGHT) {
			newWidth *= (Double.valueOf(MAX_HEIGHT) / newHeight);
			newHeight = MAX_HEIGHT;
		}

		createShrinkedImage(newWidth, newHeight);
	}

	public void moveOriginalFileToBackup() throws IOException {
		File backupFile = new File(LunchyProperties.INSTANCE.getPictureDestinationPath() + "/" + filename + "_original"
				+ FileServices.getFileExtension(filename));
		FileServices.move(originalFile, backupFile);
	}

	private boolean isScalingNeeded() {
		return imageWidth > MAX_WIDTH || imageHeight > MAX_HEIGHT;
	}

	private void loadImage() throws IOException {
		originalImage = ImageIO.read(originalFile);
	}

	private void readDimensions() {
		imageWidth = originalImage.getWidth();
		imageHeight = originalImage.getHeight();
	}

	public void saveToDisk() throws IOException {
		ImageIO.write(scaledImage, FileServices.getFileType(filename), originalFile);
	}

	private void createShrinkedImage(int width, int height) throws IOException {
		double scaleX = (double) width / imageWidth;
		double scaleY = (double) height / imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		scaledImage = bilinearScaleOp.filter(originalImage, new BufferedImage(width, height, originalImage.getType()));
	}

}
