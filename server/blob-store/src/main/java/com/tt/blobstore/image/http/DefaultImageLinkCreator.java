package com.tt.blobstore.image.http;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import com.tt.blobstore.image.ImageAttributes;
import com.tt.blobstore.image.ImageKey;

public class DefaultImageLinkCreator implements ImageLinkCreator, ServletContextAware {

	private String imageRequestPrefix;

	private ServletContext servletContext;

	public DefaultImageLinkCreator() {
	}

	public DefaultImageLinkCreator(String imageRequestPrefix) {
		this.imageRequestPrefix = imageRequestPrefix;
	}

	public void setImageRequestPrefix(String imageRequestPrefix) {
		this.imageRequestPrefix = imageRequestPrefix;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public String createImageLink(ImageKey imageKey, ImageAttributes attributes) {
		String suffix = "";
		String link = "";
		if (attributes != null) {
			suffix = attributes.getFormat().getFileSuffix().toLowerCase();
		}
		if (imageRequestPrefix != null) {
			link = imageRequestPrefix + "/" + imageKey + suffix;
		} else {
			link = "/" + imageKey + suffix;
		}

		if (suffix.length() == 0) {
			link = link.substring(0, link.length() - 1);
		}
		if (servletContext != null) {
			if (link.startsWith("/")) {
				return servletContext.getContextPath() + link;
			} else {
				return servletContext.getContextPath() + "/" + link;
			}
		} else {
			return link;
		}

	}

}
