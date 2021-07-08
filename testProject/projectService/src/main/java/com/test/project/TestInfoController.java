package com.test.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.project.service.TestInfoService;

@RestController
public class TestInfoController {

	@Autowired TestInfoService testInfoService;
	
	@RequestMapping("/test")
	public Object test() {
		
		return testInfoService.testInfo();
	}
}
