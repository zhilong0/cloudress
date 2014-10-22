package com.tt.blobstore.image;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.tt.blobstore.bundle.Blob;
import com.tt.blobstore.bundle.BlobDescriptor;
import com.tt.blobstore.bundle.BundleValue;

public class Thumbnail implements Blob, Serializable {

	private static final long serialVersionUID = 1L;

	private ImageKey imageKey;

	private byte[] data;

	private ImageFormat format;

	private static final String FORMAT_DESC_NAME = "format";

	public Thumbnail() {
	}

	public Thumbnail(ImageKey key, ImageFormat format, byte[] data) {
		this.data = data;
		this.format = format;
	}

	public byte[] getData() {
		return data;
	}

	public ImageFormat getFormat() {
		return format;
	}

	public ImageKey getImageKey() {
		return imageKey;
	}

	@Override
	public BundleValue getBundleValue() {
		if (data != null) {
			return new ThumbnailBundleValue();
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

	class ThumbnailBundleValue implements BundleValue {
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
	public BlobDescriptor getBlobDescriptor() {
		BlobDescriptor blobDescriptor = new BlobDescriptor();
		blobDescriptor.setAttribute(FORMAT_DESC_NAME, format.name());
		return blobDescriptor;

	}

	@Override
	public void setBlobDescriptor(BlobDescriptor descriptor) {
		String format = (String) descriptor.getAttribute(FORMAT_DESC_NAME);
		if (format != null) {
			this.format = ImageFormat.valueOf(format);
		}
	}

	@Override
	public void readBundleValue(InputStream input) throws IOException {
		this.data = new byte[input.available()];
		input.read(data);
	}

}
