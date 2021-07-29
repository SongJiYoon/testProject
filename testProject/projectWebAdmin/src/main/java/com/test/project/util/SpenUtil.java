package com.test.project.util;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.test.project.service.common.CustomObjectMapper;

public class SpenUtil {
	public static boolean accessCheck(List<String> grpRoles, String role){
		return grpRoles.contains(role);
	}

	public static String json(Object value) throws JsonGenerationException, JsonMappingException, IOException{
		CustomObjectMapper objectMapper = new CustomObjectMapper();
		return objectMapper.writeValueAsString(value);
	}

	public static <T> T fromJson(String json, Class<T> cls) throws JsonGenerationException, JsonMappingException, IOException{
		CustomObjectMapper objectMapper = new CustomObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(json, cls);
	}
}
