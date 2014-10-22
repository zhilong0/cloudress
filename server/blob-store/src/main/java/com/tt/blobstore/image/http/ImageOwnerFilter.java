package com.tt.blobstore.image.http;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class ImageOwnerFilter implements javax.servlet.Filter

{
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if (matchRequest(request)) {
			ImageOwnerHolder.setImageOwner(getImageOwner(request));
		}

	}

	protected abstract String getImageOwner(ServletRequest request);

	protected abstract boolean matchRequest(ServletRequest request);

}
