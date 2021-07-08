package com.test.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.project.service.mapper.intf.TestInfoMapper;

@Service
public class TestInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(TestInfoService.class);
	
	@Autowired TestInfoMapper test;
	
	public Object testInfo() {
		
		return test.selectTest();
	}
}
