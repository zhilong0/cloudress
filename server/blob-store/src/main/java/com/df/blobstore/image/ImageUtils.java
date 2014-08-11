package com.df.blobstore.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.imgscalr.Scalr;

import com.df.blobstore.image.exception.UnknownImageFormatException;
import com.google.common.io.ByteStreams;

public class ImageUtils {

	private static final String[] JPEG_FORMAT_NAMES = { "JPEG", "jpeg", "JPG", "jpg" };

	private static final String[] GIF_FORMAT_NAMES = { "GIF", "gif" };

	private static final String[] PNG_FORMAT_NAMES = { "PNG", "png", };

	private static final String[] BMP_FORMAT_NAMES = { "BMP", "bmp" };

	public static ImageFormat guessImageFormat(byte[] data) throws IOException {
		ImageIO.setUseCache(false);
		ImageInputStream in = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
		Iterator<ImageReader> readerIter = ImageIO.getImageReaders(in);
		while (readerIter.hasNext()) {
			String formatName = readerIter.next().getFormatName();
			if (Arrays.asList(JPEG_FORMAT_NAMES).contains(formatName)) {
				return ImageFormat.JPEG;
			} else if (Arrays.asList(GIF_FORMAT_NAMES).contains(formatName)) {
				return ImageFormat.GIF;
			} else if (Arrays.asList(PNG_FORMAT_NAMES).contains(formatName)) {
				return ImageFormat.PNG;
			} else if (Arrays.asList(BMP_FORMAT_NAMES).contains(formatName)) {
				return ImageFormat.BMP;
			}
		}
		throw new UnknownImageFormatException();
	}

	public static BufferedImage resize(BufferedImage image, int width, int height) {
		return Scalr.resize(image, width, height);
	}

	public static BufferedImage createBufferedImage(byte[] data, ImageFormat format) throws IOException {
		if (format == null) {
			format = guessImageFormat(data);
		}
		ImageInputStream imageIn = ImageIO.createImageInputStream(new ByteArrayInputStream(data));
		Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(format.name());
		if (readers.hasNext()) {
			ImageReader reader = readers.next();
			reader.setInput(imageIn);
			return reader.read(0);
		}
		throw new UnknownImageFormatException();
	}

	public static Image createImage(InputStream in, String owner, String imageName) throws IOException {
		if (in == null) {
			throw new IllegalArgumentException("Parameter In must not be null");
		}
		ByteArrayOutputStream copy = new ByteArrayOutputStream();
		ByteStreams.copy(in, copy);
		byte[] bytes = copy.toByteArray();
		ImageFormat format = guessImageFormat(bytes);
		BufferedImage bufferedImage = createBufferedImage(bytes, format);
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		ImageAttributes attributes = new ImageAttributes(imageName, width, height, format);
		attributes.setOwner(owner);
		attributes.setCreatedDate(new Date());
		ByteArrayOutputStream newBytes = new ByteArrayOutputStream();
		ImageIO.setUseCache(false);
		ImageIO.write(bufferedImage, format.name(), newBytes);
		Image image = new Image(attributes, newBytes.toByteArray());
		return image;
	}

	public static Image createImage(byte[] bytes, String owner, String imageName) throws IOException {
		if (bytes == null) {
			throw new IllegalArgumentException("Parameter bytes must not be null");
		}

		return createImage(new ByteArrayInputStream(bytes), owner, imageName);
	}
}
