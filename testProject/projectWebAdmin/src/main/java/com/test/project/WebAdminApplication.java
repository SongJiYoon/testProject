package com.test.project;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.test.project.config.WebMvcConfig;

@EnableAspectJAutoProxy
@SpringBootApplication
public class WebAdminApplication extends SpringBootServletInitializer {

	private static final Logger log = LoggerFactory.getLogger(WebAdminApplication.class);

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//		return builder.sources(WebAdminApplication.class);
//
//	}
//
//    @Override
//    public void onStartup(ServletContext servletContext) throws ServletException {
//    
//        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
//        context.setServletContext(servletContext);    // @EnableWebMvc 적용을 위한 필수 세팅
//        context.register(WebMvcConfig.class);
//        context.refresh();
//
//        DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
//        ServletRegistration.Dynamic app = servletContext.addServlet("dispatcher", dispatcherServlet);
//        app.addMapping("*.html");
//        app.addMapping("*.json");
//        app.addMapping("*.xls");
//        app.addMapping("*.inc");
//    }
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpringApplication.run(WebAdminApplication.class, args);
	}

}
