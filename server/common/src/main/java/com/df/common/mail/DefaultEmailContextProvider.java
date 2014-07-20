package com.df.common.mail;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class DefaultEmailContextProvider implements EmailContextProvider, BeanFactoryAware {

	private String sentFrom;

	private String ip;

	private int port;

	private String contextPath;

	private BeanFactory beanFactory;

	public String getHyperLink(boolean secure, String path, Map<String, String> getParameters) {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		if (attributes != null && attributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
			if (port <= 0) {
				port = request.getServerPort();
			}
			if (contextPath == null) {
				contextPath = request.getContextPath();
			}

			if (ip == null) {
				ip = request.getServerName();
			}
		}

		if (contextPath == null) {
			try {
				ServletContext sc = beanFactory.getBean(ServletContext.class);
				contextPath = sc.getContextPath();
			} catch (NoSuchBeanDefinitionException ex) {
			}
		}

		if (ip == null) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
			}
		}

		if (port == -1) {
			port = 8080;
		}
		String scheme = secure ? "https" : "http";
		String prefix = "";
		if (port == 80) {
			prefix = String.format("%s://%s%s", scheme, ip, contextPath);
		} else {
			prefix = String.format("%s://%s:%d%s", scheme, ip, port, contextPath);
		}

		StringBuffer gets = new StringBuffer();
		Set<String> names = getParameters.keySet();
		for (String name : names) {
			String value = getParameters.get(name);
			if (value != null) {
				try {
					value = URLEncoder.encode(value, "utf-8");
				} catch (UnsupportedEncodingException ex) {
					// Should support UTF8;
				}
				if (gets.length() != 0) {
					gets.append(String.format("&%s=%s", name, value));
				} else {
					gets.append(String.format("%s=%s", name, value));
				}
			} else {
				if (gets.length() != 0) {
					gets.append(String.format("&%s=", name));
				} else {
					gets.append(String.format("%s=", name));
				}
			}
		}

		if (gets.toString().length() == 0) {
			return prefix + path;
		}

		if (path.endsWith("/")) {
			return prefix + path + "?" + gets.toString();
		} else {
			int lastSlashIndex = path.lastIndexOf("/");
			String lastPart = path.substring(lastSlashIndex + 1, path.length());
			if (lastPart.contains("?")) {
				if (lastPart.endsWith("&")) {
					return prefix + path + gets.toString();
				} else {
					return prefix + path + "&" + gets.toString();
				}
			} else {
				return prefix + path + "?" + gets.toString();
			}
		}
	}

	public String getSentFrom() {
		return this.sentFrom;
	}

	public void setSentFrom(String sentFrom) {
		this.sentFrom = sentFrom;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getContext() {
		return contextPath;
	}

	public void setContextPath(String contextPath) {
		if (!contextPath.startsWith("/")) {
			this.contextPath = "/" + contextPath;
		} else {
			this.contextPath = contextPath;
		}
	}

	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

}
