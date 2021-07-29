package com.test.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class testController {

	@RequestMapping("test")
	public Object test() {
		System.out.println("들어는오냐!");
		return "test";
	}
}
