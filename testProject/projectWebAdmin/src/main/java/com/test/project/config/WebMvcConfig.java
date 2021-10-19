package com.test.project.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.test.project.controller.common.AccessInterceptor;
import com.test.project.controller.common.CustomAnnotationFormatterFactory;
import com.test.project.controller.common.LoginCheckFilter;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/")
				.addResourceLocations("classpath:/resources/").addResourceLocations("classpath:/static/")
				.addResourceLocations("classpath:/public/");
	}

	// thymeleaf layout
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}
	
	// modelAndView json 사용
	@Bean
    MappingJackson2JsonView jsonView(){
        return new MappingJackson2JsonView();
    }

	
//	@Bean
//	public ContentNegotiatingViewResolver resolver() {
//		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
//		
//		List<View> viewList = new ArrayList<View>();
//		
//		viewList.add(new CustomMappingJacksonJsonView());
//		viewList.add(new ExcelView());
//		
//		resolver.setDefaultViews(viewList);
//		
//		return resolver;
//		
//	}

//	@Override
//	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//
//		ConfigureContentNegotiation views = new  
//		
//		// set path extension to true
//		configurer// set favor parameter to false
//				  .favorParameter(false)
//				  // ignore the accept headers
//				  .ignoreAcceptHeader(true).
//				// dont use Java Activation Framework since we are manually specifying the
//				// mediatypes required below
//				useJaf(false).defaultContentType(MediaType.APPLICATION_JSON).mediaType("xml", MediaType.APPLICATION_XML)
//				.mediaType("json", MediaType.APPLICATION_JSON);
//	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new CustomAnnotationFormatterFactory());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**");
	}

	/**
	 * 로그인 필터
	 * 
	 * @return
	 */
	@Bean
	public FilterRegistrationBean<LoginCheckFilter> loginCheckFilterBean() {

		FilterRegistrationBean<LoginCheckFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new LoginCheckFilter());
		registrationBean.addUrlPatterns("*");

		return registrationBean;
	}
//
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:common/index.html");
	}

}