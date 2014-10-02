package com.df.spec.image.config;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.df.blobstore.image.ImageServiceConfig;

public class ImageServiceConfigFactoryBean extends AbstractFactoryBean<ImageServiceConfig> {

	@Override
	public Class<?> getObjectType() {
		return ImageServiceConfig.class;
	}

	@Override
	protected ImageServiceConfig createInstance() throws Exception {
		ImageServiceConfig config = new ImageServiceConfig();
		config.saveThumbnail(240, 180);
		config.saveThumbnail(120, 90);
		config.saveThumbnail(40, 30);
		return config;
	}

}
