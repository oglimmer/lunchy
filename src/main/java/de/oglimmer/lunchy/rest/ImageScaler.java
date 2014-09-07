package de.oglimmer.lunchy.rest;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

abstract public class ImageScaler {

	final private int maxWidth;
	final private int maxHeight;
	final private Method method;

	private int imageWidth;
	private int imageHeight;
	protected BufferedImage originalImage;
	protected BufferedImage scaledImage;

	public ImageScaler(int maxWidth, int maxHeight, Method method) {
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.method = method;
	}

	abstract protected void loadImage() throws IOException;

	abstract protected BufferedInputStream getBufferedInputStream() throws IOException;

	public boolean scale() throws IOException {
		loadImage();
		readDimensions();
		if (isScalingNeeded()) {
			calcDimAndScale();
			return true;
		} else {
			scaledImage = originalImage;
		}
		return false;
	}

	private void calcDimAndScale() throws IOException {
		int newWidth = imageWidth;
		int newHeight = imageHeight;
		if (newWidth > maxWidth) {
			newHeight *= (Double.valueOf(maxWidth) / newWidth);
			newWidth = maxWidth;
		}
		if (newHeight > maxHeight) {
			newWidth *= (Double.valueOf(maxHeight) / newHeight);
			newHeight = maxHeight;
		}

		createShrinkedImage(newWidth, newHeight);
	}

	private boolean isScalingNeeded() {
		return imageWidth > maxWidth || imageHeight > maxHeight;
	}

	private void readDimensions() {
		imageWidth = originalImage.getWidth();
		imageHeight = originalImage.getHeight();
	}

	private void createShrinkedImage(int width, int height) throws IOException {
		BufferedImage tmpImg = fixRotation();
		scaledImage = Scalr.resize(tmpImg, method, width, height);
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
		switch (getOrientation(getBufferedInputStream())) {
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

	private static int getOrientation(BufferedInputStream bis) throws IOException {
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(bis, false);
			Directory directory = metadata.getDirectory(ExifIFD0Directory.class);
			if (directory != null) {
				return directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
			return 1;
		} catch (MetadataException | ImageProcessingException me) {
			return 1;
		}
	}

}
