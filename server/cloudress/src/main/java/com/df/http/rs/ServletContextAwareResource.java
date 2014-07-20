package com.df.http.rs;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;

public abstract class ServletContextAwareResource {

	@Autowired
	private ServletContext servletContext;

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	protected ServletContext getServletContext() {
		return servletContext;
	}
}
