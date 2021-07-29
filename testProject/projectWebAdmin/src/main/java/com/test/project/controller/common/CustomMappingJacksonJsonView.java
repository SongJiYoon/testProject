package com.test.project.controller.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import com.test.project.service.common.CustomObjectMapper;

@Component
public class CustomMappingJacksonJsonView extends org.springframework.web.servlet.view.json.MappingJackson2JsonView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		setObjectMapper(new CustomObjectMapper());
		super.renderMergedOutputModel(model, request, response);
	}

}
