package com.test.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ServiceApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceApplication.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(ServiceApplication.class, args);
	}
	
}
