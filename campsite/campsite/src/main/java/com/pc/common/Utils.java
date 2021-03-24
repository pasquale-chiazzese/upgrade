package com.pc.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Utils {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	static {
		MAPPER.registerModule(new JavaTimeModule());
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	public static ObjectMapper getMapper() {
		return MAPPER;
	}
}
