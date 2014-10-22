package com.tt.spec.locality.http.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class BaiduMessageConverter extends MappingJackson2HttpMessageConverter {

	public BaiduMessageConverter() {
		List<MediaType> parentSupportedMediaType = this.getSupportedMediaTypes();
		ArrayList<MediaType> mediaTypes = new ArrayList<MediaType>(parentSupportedMediaType);
		mediaTypes.add(new MediaType("text", "javascript", DEFAULT_CHARSET));
		this.setSupportedMediaTypes(mediaTypes);
	}
}
