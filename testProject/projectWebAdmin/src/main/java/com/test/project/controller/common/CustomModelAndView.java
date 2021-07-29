package com.test.project.controller.common;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.servlet.View;

public class CustomModelAndView extends org.springframework.web.servlet.ModelAndView {
	private static final String RESULT_DATA = "resultData";

	public CustomModelAndView() {
		super();
	}

	public CustomModelAndView(String viewName, Map<String, ?> model) {
		super(viewName, model);
	}

	public CustomModelAndView(String viewName, String modelName, Object modelObject) {
		super(viewName, modelName, modelObject);
	}

	public CustomModelAndView(String viewName) {
		super(viewName);
	}

	public CustomModelAndView(View view, Map<String, ?> model) {
		super(view, model);
	}

	public CustomModelAndView(View view, String modelName, Object modelObject) {
		super(view, modelName, modelObject);
	}

	public CustomModelAndView(View view) {
		super(view);
	}

	public CustomModelAndView addResultData(String name, Object value){
		Map<String, Object> resultData = null;

		if(!getModelMap().containsAttribute(RESULT_DATA)){
			resultData = new HashMap<String, Object>();
			addObject(RESULT_DATA, resultData);
		}
		else{
			resultData = (Map<String, Object>) getModelMap().get(RESULT_DATA);
		}

		resultData.put(name, value);
		return this;
	}

	public CustomModelAndView setExcel(String key, Object data, String... headers){
		addObject(ExcelView.EXCEL_KEYS, key);
		addObject(ExcelView.EXCEL_HEADER, headers);
		addObject(ExcelView.EXCEL_DATA, data);
		return this;
	}
}
