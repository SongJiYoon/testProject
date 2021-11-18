package com.test.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class WebApiApplication extends SpringBootServletInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(WebApiApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WebApiApplication.class);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(WebApiApplication.class, args);
	}
	
	
}
