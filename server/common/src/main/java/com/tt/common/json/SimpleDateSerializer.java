package com.tt.common.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SimpleDateSerializer extends JsonSerializer<Date> {

	private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static DateFormat getDateFormat() {
		return formatter;
	}

	@Override
	public void serialize(Date value, JsonGenerator gen, SerializerProvider paramSerializerProvider) throws IOException, JsonProcessingException {
		gen.writeString(formatter.format(value));
	}
}