package com.tt.http.rs.resteasy;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class SpringContextLoaderListener extends ContextLoaderListener {

	private SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();

	public void contextInitialized(ServletContextEvent event) {
		boolean scanProviders = false;
		boolean scanResources = false;

		String sProviders = event.getServletContext().getInitParameter("resteasy.scan.providers");
		if (sProviders != null) {
			scanProviders = Boolean.valueOf(sProviders.trim());
		}
		String scanAll = event.getServletContext().getInitParameter("resteasy.scan");
		if (scanAll != null) {
			boolean tmp = Boolean.valueOf(scanAll.trim());
			scanProviders = tmp || scanProviders;
			scanResources = tmp || scanResources;
		}
		String sResources = event.getServletContext().getInitParameter("resteasy.scan.resources");
		if (sResources != null) {
			scanResources = Boolean.valueOf(sResources.trim());
		}

		if (scanProviders || scanResources) {
			throw new RuntimeException(
					"You cannot use resteasy.scan, resteasy.scan.resources, or resteasy.scan.providers with the SpringContextLoaderLister as this may cause serious deployment errors in your application");
		}

		super.contextInitialized(event);
	}

	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);
		this.springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}
