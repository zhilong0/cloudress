package com.df.http.rs.interceptor;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

@Provider
@ConstrainedTo(RuntimeType.SERVER)
public class ResponseSampleLogInterceptor implements ReaderInterceptor, WriterInterceptor {

	private UriInfo uri;

	private Request request;

	private File outPutDirectory;

	private boolean enabled;

	private ObjectMapper objectMapper;

	protected Providers providers;

	private Charset charset = Charset.forName("utf8");

	protected static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

	public ResponseSampleLogInterceptor() {
		this.setOutPutPath(System.getProperty("user.home") + "/rest-sample");
	}

	@Context
	public void setUri(UriInfo uri) {
		this.uri = uri;
	}

	@Context
	public void setProviders(Providers providers) {
		this.providers = providers;
	}

	@Context
	public void setRequest(Request request) {
		this.request = request;
	}

	public void setOutPutPath(String outPutPath) {
		this.outPutDirectory = new File(outPutPath);
		if (!this.outPutDirectory.exists() && this.outPutDirectory.isDirectory()) {
			throw new IllegalArgumentException(outPutPath + " must be a directory");
		}
		if (this.outPutDirectory.exists() && !this.outPutDirectory.canWrite()) {
			throw new IllegalArgumentException("Must have write permission to " + outPutPath);
		}
		if (!outPutDirectory.exists()) {
			outPutDirectory.mkdir();
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
		if (!enabled) {
			context.proceed();
		} else {
			List<String> urls = uri.getMatchedURIs();
			Object entity = context.getEntity();
			ObjectMapper mapper = this.getObjectMapper(context.getMediaType());
			if (urls.size() > 0 && entity != null && mapper != null) {
				String seperator = System.getProperty("line.separator");
				ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
				String url = urls.get(0).replace("/", ".");
				String method = request.getMethod();
				File file = new File(outPutDirectory, url);
				if (!file.exists()) {
					file.createNewFile();
				}
				BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(file));
				try {
					buffer.write(("METHOD	   :" + method + seperator).getBytes(charset));
					buffer.write(("URL		   :" + urls.get(0) + seperator).getBytes(charset));
					buffer.write(("QUERY PARAMETERS:" + uri.getQueryParameters() + seperator).getBytes(charset));
					buffer.write(("PATH PARAMETERS :" + uri.getPathParameters() + seperator).getBytes(charset));
					buffer.write(("PATH SEGMENTS   :" + uri.getPathSegments() + seperator).getBytes(charset));

					if (entity != null) {
						buffer.write(("SAMPLE           :" + seperator).getBytes(charset));
						String jsonValue = writer.writeValueAsString(entity);
						buffer.write(jsonValue.getBytes(charset));
					}
				} finally {
					buffer.close();
				}
			}

			context.proceed();
		}
	}

	protected ObjectMapper getObjectMapper(MediaType mediaType) {
		if (this.objectMapper != null) {
			return this.objectMapper;
		}

		if (this.providers != null) {
			ContextResolver<ObjectMapper> resolver = this.providers.getContextResolver(ObjectMapper.class, mediaType);
			if (resolver == null) {
				resolver = this.providers.getContextResolver(ObjectMapper.class, null);
			}
			if (resolver != null) {
				return ((ObjectMapper) resolver.getContext(ObjectMapper.class));
			}
		}

		return DEFAULT_MAPPER;
	}

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
		if (!enabled) {
			return context.proceed();
		} else {
			List<String> urls = uri.getMatchedURIs();
			if (urls.size() > 0) {
				String seperator = System.getProperty("line.separator");
				String url = urls.get(0).replace("/", ".");
				String method = request.getMethod();
				File file = new File(outPutDirectory, url);
				if (!file.exists()) {
					file.createNewFile();
				}
				BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(file));
				try {
					buffer.write(("METHOD	   :" + method + seperator).getBytes(charset));
					buffer.write(("URL		   :" + urls.get(0) + seperator).getBytes(charset));
					buffer.write(("QUERY PARAMETERS:" + uri.getQueryParameters() + seperator).getBytes(charset));
					buffer.write(("PATH PARAMETERS :" + uri.getPathParameters() + seperator).getBytes(charset));
					buffer.write(("PATH SEGMENTS   :" + uri.getPathSegments() + seperator).getBytes(charset));
				} finally {
					buffer.close();
				}
			}

			return context.proceed();
		}
	}

}
