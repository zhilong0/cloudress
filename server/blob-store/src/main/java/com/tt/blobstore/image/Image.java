package com.tt.blobstore.image;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.tt.blobstore.bundle.Blob;
import com.tt.blobstore.bundle.BlobDescriptor;
import com.tt.blobstore.bundle.BundleValue;

public class Image implements Blob {

	public static final int RGB2GRAY = 0x01;

	private ImageAttributes imageAttributes;

	private byte[] data;

	private static final String NAME_DESC_NAME = "name";

	private static final String WIDTH_DESC_NAME = "width";

	private static final String HEIGTH_DESC_NAME = "heigth";

	private static final String FORMAT_DESC_NAME = "format";

	private static final String OWNER_DESC_NAME = "owner";

	private static final String CREATED_DESC_NAME = "created";

	public Image() {
	}

	public Image(ImageAttributes imageAttributes) {
		this.imageAttributes = imageAttributes;
	}

	public Image(ImageAttributes imageAttributes, byte[] data) {
		this.imageAttributes = imageAttributes;
		this.data = data;
	}

	@Override
	public BundleValue getBundleValue() {
		if (data != null) {
			return new ImageBundleValue();
		} else {
			return new BundleValue() {

				@Override
				public int getSize() {
					return 0;
				}

				@Override
				public InputStream getDataInBundle() {
					return new ByteArrayInputStream(new byte[0]);
				}
			};
		}
	}

	public ImageAttributes getImageAttributes() {
		return imageAttributes;
	}

	public void setImageAttributes(ImageAttributes imageAttributes) {
		this.imageAttributes = imageAttributes;
	}

	public byte[] getData() {
		return data;
	}

	class ImageBundleValue implements BundleValue {
		@Override
		public InputStream getDataInBundle() {
			return new DataInputStream(new ByteArrayInputStream(data));
		}

		@Override
		public int getSize() {
			return data.length;
		}
	}

	@Override
	public void readBundleValue(InputStream input) throws IOException {
		this.data = new byte[input.available()];
		input.read(data);
	}

	@Override
	public BlobDescriptor getBlobDescriptor() {
		BlobDescriptor blobDescriptor = new BlobDescriptor();
		if (imageAttributes != null) {
			blobDescriptor.setAttribute(NAME_DESC_NAME, imageAttributes.getName());
			blobDescriptor.setAttribute("width", imageAttributes.getWidth());
			blobDescriptor.setAttribute("heigth", imageAttributes.getHeigth());
			blobDescriptor.setAttribute("format", imageAttributes.getFormat().name());
			blobDescriptor.setAttribute("owner", imageAttributes.getOwner());
			blobDescriptor.setAttribute("created", imageAttributes.getCreatedDate());
		}
		return blobDescriptor;
	}

	@Override
	public void setBlobDescriptor(BlobDescriptor descriptor) {
		this.imageAttributes = new ImageAttributes((String) descriptor.getAttribute(NAME_DESC_NAME));
		int width = (descriptor.getAttribute(WIDTH_DESC_NAME) == null ? 0 : (Integer) descriptor.getAttribute(WIDTH_DESC_NAME));
		int heigth = (descriptor.getAttribute(HEIGTH_DESC_NAME) == null ? 0 : (Integer) descriptor.getAttribute(WIDTH_DESC_NAME));
		this.imageAttributes.setWidth(width);
		this.imageAttributes.setHeigth(heigth);
		this.imageAttributes.setOwner((String) descriptor.getAttribute(OWNER_DESC_NAME));
		String format = (String) descriptor.getAttribute(FORMAT_DESC_NAME);
		if (format != null) {
			this.imageAttributes.setFormat(ImageFormat.valueOf(format));
		}
		this.imageAttributes.setCreatedDate((Date) descriptor.getAttribute(CREATED_DESC_NAME));

	}
}