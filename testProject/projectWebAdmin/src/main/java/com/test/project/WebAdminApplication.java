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
public class WebAdminApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(WebAdminApplication.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WebAdminApplication.class);

	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(WebAdminApplication.class, args);
	}

}