package de.oglimmer.lunchy.rest;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

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
		BufferedImage tmpImg = fixRotation();
		scaledImage = Scalr.resize(tmpImg, Scalr.Method.QUALITY, width, height);
	}

	/**
	 * 'Modern' devices store a picture in an arbitrary orientation and add the meta data (EXIF) to tell you how you need to look at it.
	 * This method moves the pictures into the right orientation.
	 * 
	 * @return
	 * @throws IOException
	 */
	private BufferedImage fixRotation() throws IOException {
		BufferedImage tmpImg = originalImage;
		// http://sylvana.net/jpegcrop/exif_orientation.html
		switch (getOrientation(originalFile)) {
		case 1:
			break;
		case 2:
			tmpImg = Scalr.rotate(tmpImg, Scalr.Rotation.FLIP_HORZ);
			break;
		case 3:
			tmpImg = Scalr.rotate(tmpImg, Scalr.Rotation.CW_180);
			break;
		case 4:
			BufferedImage step1ImgA = Scalr.rotate(tmpImg, Scalr.Rotation.CW_180);
			tmpImg = Scalr.rotate(step1ImgA, Scalr.Rotation.FLIP_HORZ);
			break;
		case 5:
			BufferedImage step1ImgB = Scalr.rotate(tmpImg, Scalr.Rotation.CW_90);
			tmpImg = Scalr.rotate(step1ImgB, Scalr.Rotation.FLIP_HORZ);
			break;
		case 6:
			tmpImg = Scalr.rotate(tmpImg, Scalr.Rotation.CW_90);
			break;
		case 7:
			BufferedImage step1ImgC = Scalr.rotate(tmpImg, Scalr.Rotation.CW_270);
			tmpImg = Scalr.rotate(step1ImgC, Scalr.Rotation.FLIP_HORZ);
			break;
		case 8:
			tmpImg = Scalr.rotate(tmpImg, Scalr.Rotation.CW_270);
			break;
		}
		return tmpImg;
	}

	private static int getOrientation(File imageFile) throws IOException {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
			Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
			return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
		} catch (MetadataException | ImageProcessingException me) {
			return 1;
		}
	}

}
