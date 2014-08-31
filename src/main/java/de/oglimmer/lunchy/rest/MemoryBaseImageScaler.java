package de.oglimmer.lunchy.rest;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr.Method;

public class MemoryBaseImageScaler extends ImageScaler {

	private InputStream inputStream;

	public MemoryBaseImageScaler(int maxWidth, int maxHeight, Method method, InputStream is) {
		super(maxWidth, maxHeight, method);
		this.inputStream = is;
	}

	public InputStream getScaledInputStream(String type) throws IOException {
		scale();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ImageIO.write(scaledImage, type, output);
		return new ByteArrayInputStream(output.toByteArray());
	}

	@Override
	protected BufferedInputStream getBufferedInputStream() throws IOException {
		return new BufferedInputStream(inputStream);
	}

	@Override
	protected void loadImage() throws IOException {
		originalImage = ImageIO.read(inputStream);
	}
};