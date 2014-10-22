package com.tt.blobstore.image.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import com.google.common.io.Closer;
import com.tt.blobstore.image.Image;
import com.tt.blobstore.image.ImageFormat;
import com.tt.blobstore.image.ImageKey;
import com.tt.blobstore.image.ImageServiceImpl;

public abstract class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ImageServiceImpl imageService;

	private ImageDetailsFactory imageReferenceFactory;

	private ObjectMapper objectMapper;

	private static final String UTF8_ENCODING = "UTF-8";

	private static final String FORM_DATA_MIME_TYPE = "multipart/form-data";

	private static final Logger logger = LoggerFactory.getLogger(ImageServlet.class);

	public void setImageReferenceFactory(ImageDetailsFactory imageReferenceFactory) {
		this.imageReferenceFactory = imageReferenceFactory;
	}

	protected ImageDetailsFactory getImageReferenceFactory() {
		return imageReferenceFactory;
	}

	protected ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public void setObjectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		initImageService(config);
		initImageReferenceFactory(config);
		initObjectMapper(config);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		ImageKey imageKey = this.getImageKeyFromRequest(req);
		Closer closer = Closer.create();
		if (imageKey == null) {
			resp.setStatus(404);
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				int width = this.getImageWidth(req);
				int heigth = this.getImageHeigth(req);
				Image image = null;
				if (width <= 0 || heigth < 0) {
					image = imageService.fetchImage(imageKey);
				} else {
					image = imageService.fetchImage(imageKey, width, heigth);
				}
				ImageFormat format = image.getImageAttributes().getFormat();
				resp.setContentType(format.getMIMEType());
				in = closer.register(image.getBundleValue().getDataInBundle());
				resp.setContentLength(image.getBundleValue().getSize());
				out = resp.getOutputStream();
				byte[] buf = new byte[1024];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
			} catch (Throwable ex) {
				String msg = "Cannot get image from key " + imageKey.toString();
				logger.error(msg, ex);
				resp.setStatus(404);
			} finally {
				closer.close();
			}
		}
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ImageKey imageKey = this.getImageKeyFromRequest(req);
		if (imageKey != null) {
			try {
				imageService.deleteImage(imageKey);
			} catch (Throwable ex) {
				logger.warn("Failed to delete image " + imageKey, ex);
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		byte[] imageData = readImageData(req);
		ImageKey key = imageService.uploadImage(new ByteArrayInputStream(imageData), getOwnerFromRequest(req), getImageNameFromRequest(req));
		ImageDetails reference = imageReferenceFactory.createImageDetails(key);
		resp.setContentType("application/json");
		objectMapper.writeValue(resp.getOutputStream(), reference);
	}

	protected byte[] readImageData(HttpServletRequest req) throws IOException {
		ServletInputStream in = req.getInputStream();
		byte[] content = new byte[req.getContentLength()];
		int length = 0;
		int offset = 0;
		while (length != -1) {
			length = in.read(content, offset, 1024);
			offset += length;
		}
		if (req.getContentType().equalsIgnoreCase(FORM_DATA_MIME_TYPE)) {
			return content;
		}
		BaseEncoding base64 = BaseEncoding.base64();
		String encoding = req.getCharacterEncoding();
		if (encoding == null) {
			encoding = UTF8_ENCODING;
		}
		return base64.decode(new String(content, Charset.forName(encoding)));
	}

	protected abstract ImageKey getImageKeyFromRequest(HttpServletRequest request);

	protected abstract int getImageWidth(HttpServletRequest request);

	protected abstract int getImageHeigth(HttpServletRequest request);

	protected abstract String getOwnerFromRequest(HttpServletRequest request);

	protected abstract String getImageNameFromRequest(HttpServletRequest request);

	protected abstract void initImageService(ServletConfig config);

	protected abstract void initImageReferenceFactory(ServletConfig config);

	protected abstract void initObjectMapper(ServletConfig config);

	protected void setImageService(ImageServiceImpl imageService) {
		this.imageService = imageService;
	}

}
