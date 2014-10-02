package com.df.blobstore.image.fs;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.df.blobstore.bundle.BundleKey;
import com.df.blobstore.bundle.BundleService;
import com.df.blobstore.bundle.fs.FileSystemBundleKey;
import com.df.blobstore.bundle.fs.FileSystemBundleService;
import com.df.blobstore.image.ImageAttributes;
import com.df.blobstore.image.ImageAttributesLoader;
import com.df.blobstore.image.ImageFormat;
import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageServiceRoute;
import com.df.blobstore.image.ImageStoreException;
import com.google.common.io.BaseEncoding;

public class FileSystemImageServiceRoute implements ImageServiceRoute, ImageAttributesLoader {

	private File imageRootDirectory;

	private IdGenerator idGenerator = new UUIDGenerator();

	private static final Charset utf8 = Charset.forName("utf-8");

	private static final String ANONYMOUS_FOLDER = "Anonymous";

	public FileSystemImageServiceRoute(File imageRootDirectory) {
		this.imageRootDirectory = imageRootDirectory;
	}

	public FileSystemImageServiceRoute(String path) {
		this(new File(path));
	}

	@Override
	public BundleService getBundleService(ImageKey imageKey) {
		return new FileSystemBundleService(imageRootDirectory);
	}

	@Override
	public BundleKey resolveBundleKey(ImageKey imageKey) {
		ImageAttributes attributes = this.loadImageAttributes(imageKey);
		String owner = attributes.getOwner();
		if (StringUtils.isEmpty(owner)) {
			owner = ANONYMOUS_FOLDER;
		}
		String relativePath = "";
		if (attributes.getCreatedDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String dateStr = dateFormat.format(attributes.getCreatedDate());
			relativePath += File.separator + dateStr;
		}
		relativePath += owner;
		relativePath += File.separator + this.resolveImageUniqueId(imageKey);
		relativePath += "_" + attributes.getWidth() + "_" + attributes.getHeigth();
		relativePath += "." + attributes.getFormat().name().toLowerCase();
		return new FileSystemBundleKey(relativePath);
	}

	public ImageAttributes loadImageAttributes(ImageKey imageKey) {
		BaseEncoding base64 = BaseEncoding.base64Url();
		byte[] bytes = base64.decode(imageKey.getKey());
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			int uniqueIdLength = in.readInt();
			byte[] uniqueId = new byte[uniqueIdLength];
			in.readFully(uniqueId);
			int width = in.readInt();
			int heigth = in.readInt();
			int formatIntValue = in.readShort();
			ImageFormat format = ImageFormat.fromIntValue(formatIntValue);
			if (format == null) {
				throw new ImageStoreException("unknow or invalid image format:%s", formatIntValue);
			}
			int nextLength = in.readInt();
			byte[] imageName = new byte[nextLength];
			in.readFully(imageName);
			ImageAttributes attributes = new ImageAttributes(new String(imageName, utf8), width, heigth, format);
			nextLength = in.readInt();
			if (nextLength == 0) {
				attributes.setOwner(null);
			} else {
				byte[] owner = new byte[nextLength];
				in.readFully(owner);
				attributes.setOwner(new String(owner, utf8));
			}
			long createdDate = in.readLong();
			if (createdDate != -1) {
				attributes.setCreatedDate(new Date(createdDate));
			}
			return attributes;
		} catch (IOException ex) {
			throw new ImageStoreException("Invalid image key, cannot resolve image attributes.");
		}
	}

	public String resolveImageUniqueId(ImageKey imageKey) {
		BaseEncoding base64 = BaseEncoding.base64Url();
		byte[] bytes = base64.decode(imageKey.getKey());
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		try {
			int uniqueIdLength = in.readInt();
			byte[] uniqueId = new byte[uniqueIdLength];
			in.readFully(uniqueId);
			return new String(uniqueId, utf8);
		} catch (IOException ex) {
			throw new ImageStoreException("Invalid image key, cannot resolve image attributes.");
		}
	}

	@Override
	public ImageKey hash(ImageAttributes attributes) {
		byte[] uniqueId = idGenerator.randomUniqueId(attributes).getBytes(utf8);
		byte[] owner = new byte[0];
		if (attributes.getOwner() != null) {
			owner = attributes.getOwner().getBytes(utf8);
		}
		byte[] imageName = new byte[0];
		if (attributes.getName() != null) {
			imageName = attributes.getName().getBytes(utf8);
		}
		int maxBufferSize = 512;
		int length = 4 * 2 + 2 + 4 + imageName.length + 4 + owner.length + 4 + uniqueId.length + 8;
		if (length > maxBufferSize) {
			throw new ImageStoreException("owner+uniqueId length canot exceed " + (512 - 4 * 6));
		}
		ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.putInt(uniqueId.length);
		buffer.put(uniqueId);
		buffer.putInt(attributes.getWidth());
		buffer.putInt(attributes.getHeigth());
		buffer.putShort((short) attributes.getFormat().intValue());
		buffer.putInt(imageName.length);
		buffer.put(imageName);
		buffer.putInt(owner.length);
		buffer.put(owner);
		long createdDate = -1;
		if (attributes.getCreatedDate() != null) {
			createdDate = attributes.getCreatedDate().getTime();
		}
		buffer.putLong(createdDate);
		BaseEncoding base64 = BaseEncoding.base64Url();
		return new ImageKey(base64.encode(buffer.array()));
	}

	@Override
	public ImageAttributesLoader getImageAttributesLoader() {
		return this;
	}

	@Override
	public BundleService getThumbnailBundleService(ImageKey imageKey, int width, int heigth) {
		return this.getBundleService(imageKey);
	}

	@Override
	public BundleKey resolveThumbnailBundleKey(ImageKey imageKey, int width, int heigth) {
		ImageAttributes attributes = this.loadImageAttributes(imageKey);
		String owner = attributes.getOwner();
		if (StringUtils.isEmpty(owner)) {
			owner = ANONYMOUS_FOLDER;
		}
		String relativePath = "";
		if (attributes.getCreatedDate() != null) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			String dateStr = dateFormat.format(attributes.getCreatedDate());
			relativePath += File.separator + dateStr;
		}
		relativePath += owner;
		relativePath += File.separator + this.resolveImageUniqueId(imageKey);
		relativePath += "_" + width + "_" + heigth;
		relativePath += "." + attributes.getFormat().name().toLowerCase();
		return new FileSystemBundleKey(relativePath);
	}
}
