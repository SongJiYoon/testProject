package com.test.project.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class CommonService {

	@Value("${test.name}")
	static String test;
	
	static Logger logger = LoggerFactory.getLogger(CommonService.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			System.out.println("됌?");
			logger.info("돼네?");
			
			System.out.println(test);
			
	}

}
