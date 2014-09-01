package com.df.blobstore.image.http;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.df.blobstore.image.ImageKey;
import com.df.blobstore.image.ImageServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DefaultImageServlet extends ImageServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected ImageKey getImageKeyFromRequest(HttpServletRequest request) {
		String pathInfo = request.getPathInfo();
		if (pathInfo != null) {
			String[] pathes = pathInfo.split("/");
			if (pathes.length >= 2) {
				if(pathes[1].lastIndexOf(".") ==-1){
					return new ImageKey(pathes[1]);
				}else{
					return new ImageKey(pathes[1].substring(0, pathes[1].lastIndexOf(".")));
				}
			}
		}
		return null;
	}

	@Override
	protected void initImageService(ServletConfig config) {
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		ImageServiceImpl imgService = wc.getAutowireCapableBeanFactory().getBean(ImageServiceImpl.class);
		this.setImageService(imgService);
	}

	@Override
	protected String getOwnerFromRequest(HttpServletRequest request) {
		return ImageOwnerHolder.getImageOwner();
	}

	@Override
	protected void initImageReferenceFactory(ServletConfig config) {
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		ImageDetailsFactory irf = wc.getAutowireCapableBeanFactory().getBean(ImageDetailsFactory.class);
		this.setImageReferenceFactory(irf);
	}

	@Override
	protected void initObjectMapper(ServletConfig config) {
		WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
		ObjectMapper objectMapper = wc.getAutowireCapableBeanFactory().getBean(ObjectMapper.class);
		this.setObjectMapper(objectMapper);
	}

	@Override
	protected String getImageNameFromRequest(HttpServletRequest request) {
		return null;
	}
}
