package com.tt.blobstore.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.ByteStreams;
import com.tt.blobstore.bundle.BundleKey;
import com.tt.blobstore.bundle.BundleService;
import com.tt.blobstore.image.ImageServiceConfig.ThumbnailDef;
import com.tt.blobstore.image.exception.ImageException;

public class ImageServiceImpl implements ImageService {

	private ImageServiceRoute serviceRoute;

	private ImageServiceConfig config = new ImageServiceConfig();

	private static final Logger logger = LoggerFactory.getLogger(ImageServiceImpl.class);

	public ImageServiceImpl(ImageServiceRoute serviceRoute) {
		this.serviceRoute = serviceRoute;
	}

	public ImageServiceImpl(ImageServiceRoute serviceRoute, ImageServiceConfig config) {
		this.serviceRoute = serviceRoute;
		this.config = config;
	}

	private Thumbnail createImageThumbnail(ImageKey key, ImageFormat format, BufferedImage bufferedImage, int width, int heigth) throws IOException {
		BufferedImage tbi = ImageUtils.resize(bufferedImage, width, heigth);
		byte[] thumbnailData = ImageUtils.dumpImage(tbi, format);
		return new Thumbnail(key, format, thumbnailData);
	}

	@Override
	public ImageKey uploadImage(InputStream in, String owner, String imageName) {
		Image image = null;
		byte[] data = null;
		try {
			ByteArrayOutputStream copy = new ByteArrayOutputStream();
			ByteStreams.copy(in, copy);
			data = copy.toByteArray();
			image = ImageUtils.createImage(data, owner, imageName);
			image.getImageAttributes().setCreatedDate(new Date());
			ImageFormat format = image.getImageAttributes().getFormat();
			ImageKey imageKey = serviceRoute.hash(image.getImageAttributes());
			BundleService bundleService = serviceRoute.getBundleService(imageKey);
			bundleService.addBlob(image, serviceRoute.resolveBundleKey(imageKey));
			BufferedImage bufferedImage = ImageUtils.createBufferedImage(data, format);
			for (ThumbnailDef thumbnailDef : config.getThumbnailDefs()) {
				int width = thumbnailDef.getWidth();
				int heigth = thumbnailDef.getHeigth();
				Thumbnail thumbnail = this.createImageThumbnail(imageKey, format, bufferedImage, width, heigth);
				BundleService thumbnailBundleService = serviceRoute.getThumbnailBundleService(imageKey, width, heigth);
				thumbnailBundleService.addBlob(thumbnail, serviceRoute.resolveBundleKey(imageKey));
			}
			return imageKey;
		} catch (IOException ex) {
			throw new ImageException(ex, "Failed to upload image");
		}
	}

	@Override
	public void deleteImage(ImageKey imageKey) {
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		bundleService.deleteBlob(bundleKey);
		for (ThumbnailDef thumbnailDef : config.getThumbnailDefs()) {
			int width = thumbnailDef.getWidth();
			int heigth = thumbnailDef.getHeigth();
			BundleService tbs = serviceRoute.getThumbnailBundleService(imageKey, width, heigth);
			BundleKey tbk = serviceRoute.resolveThumbnailBundleKey(imageKey, width, heigth);
			try {
				tbs.deleteBlob(tbk);
			} catch (Throwable ex) {
				logger.warn("Failed to delete image thumbnail with width=" + width + ", heigth=" + heigth, ex);
			}
		}
	}

	@Override
	public Image fetchImage(ImageKey imageKey) {
		Image image = new Image();
		BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
		if (bundleKey == null) {
			return null;
		}
		BundleService bundleService = serviceRoute.getBundleService(imageKey);
		boolean succeed = bundleService.fetchBlob(image, bundleKey);
		if (succeed && image.getImageAttributes() == null) {
			ImageAttributes attributes = serviceRoute.getImageAttributesLoader().loadImageAttributes(imageKey);
			image.setImageAttributes(attributes);
		}
		return succeed ? image : null;
	}

	@Override
	public ImageAttributes getImageAttributes(ImageKey imageKey) {
		return serviceRoute.getImageAttributesLoader().loadImageAttributes(imageKey);
	}

	@Override
	public Image fetchImage(ImageKey imageKey, int width, int heigth) {
		ImageAttributes attributes = this.getImageAttributes(imageKey);
		if (attributes == null) {
			return null;
		} else {
			if (attributes.getWidth() == width && attributes.getHeigth() == heigth) {
				Image image = new Image();
				image.setImageAttributes(attributes);
				BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
				BundleService bundleService = serviceRoute.getBundleService(imageKey);
				boolean succeed = bundleService.fetchBlob(image, bundleKey);
				if (succeed) {
					return succeed ? image : null;
				}
			} else if (config.getThumbnailDefs().contains(new ThumbnailDef(width, heigth))) {
				Thumbnail thumbnail = new Thumbnail();
				BundleKey bundleKey = serviceRoute.resolveThumbnailBundleKey(imageKey, width, heigth);
				BundleService bundleService = serviceRoute.getThumbnailBundleService(imageKey, width, heigth);
				boolean succeed = bundleService.fetchBlob(thumbnail, bundleKey);
				if (succeed) {
					return new Image(attributes, thumbnail.getData());
				}
			}
			Image image = new Image();
			image.setImageAttributes(attributes);
			BundleKey bundleKey = serviceRoute.resolveBundleKey(imageKey);
			BundleService bundleService = serviceRoute.getBundleService(imageKey);
			boolean succeed = bundleService.fetchBlob(image, bundleKey);
			if (succeed) {
				try {
					BufferedImage bi = ImageUtils.createBufferedImage(image.getData(), attributes.getFormat());
					BufferedImage tbi = ImageUtils.resize(bi, width, heigth);
					byte[] tdata = ImageUtils.dumpImage(tbi, attributes.getFormat());
					attributes.setWidth(width);
					attributes.setHeigth(heigth);
					return new Image(attributes, tdata);
				} catch (IOException ex) {
					logger.error("Failed to resize image ", ex);
					return null;
				}
			}
			return null;
		}
	}

}
