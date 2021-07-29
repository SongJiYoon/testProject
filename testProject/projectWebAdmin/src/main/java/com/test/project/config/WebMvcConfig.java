package com.test.project.config;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles3.SimpleSpringPreparerFactory;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;

import com.test.project.controller.common.CustomMappingJacksonJsonView;
import com.test.project.controller.common.ExcelView;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

	/*
	 * @Bean public ContextLoaderListener requestContextListener() { return new
	 * ContextLoaderListener(); }
	 */

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.ignoreAcceptHeader(true).defaultContentType(new MediaType("text", "html", StandardCharsets.UTF_8))
				.mediaType("inc", new MediaType("text", "inc", StandardCharsets.UTF_8))
				.mediaType("htm", new MediaType("text", "htm", StandardCharsets.UTF_8))
				.mediaType("html", new MediaType("text", "html", StandardCharsets.UTF_8))
				.mediaType("json", new MediaType("application", "json", StandardCharsets.UTF_8))
				.mediaType("jsonp", new MediaType("application", "javascript", StandardCharsets.UTF_8))
				.mediaType("xml", new MediaType("application", "xml", StandardCharsets.UTF_8))
				.mediaType("xls", new MediaType("application", "vnd.ms-excel", StandardCharsets.UTF_8));
	}
//	

//    @Bean
//    public TilesViewResolver tilesViewResolver() {
//        final TilesViewResolver tilesViewResolver = new TilesViewResolver();
//        tilesViewResolver.setViewClass(TilesView.class);
//        tilesViewResolver.setOrder(1);
//        return tilesViewResolver;
//    }
//	

	@Bean
	public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
		ContentNegotiatingViewResolver contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
		contentNegotiatingViewResolver.setOrder(1);

		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
		urlBasedViewResolver.setViewClass(TilesView.class);
		urlBasedViewResolver.setOrder(1);

		InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
		internalResourceViewResolver.setPrefix("/WEB-INF/view/");
		internalResourceViewResolver.setSuffix(".jsp");
		internalResourceViewResolver.setContentType("text/inc");
		internalResourceViewResolver.setOrder(2);

		List<ViewResolver> viewResolversList = new ArrayList<ViewResolver>();
		viewResolversList.add(urlBasedViewResolver);
		viewResolversList.add(internalResourceViewResolver);

		contentNegotiatingViewResolver.setViewResolvers(viewResolversList);

		List<View> viewList = new ArrayList<View>();

		viewList.add(new CustomMappingJacksonJsonView());
		viewList.add(new ExcelView());

		contentNegotiatingViewResolver.setDefaultViews(viewList);

		return contentNegotiatingViewResolver;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(new String[] { "classpath:tiles.xml" });
		tilesConfigurer.setCheckRefresh(true);
		tilesConfigurer.setPreparerFactoryClass(SimpleSpringPreparerFactory.class);
		return tilesConfigurer;
	}

//
//	@Override
//	public void addFormatters(FormatterRegistry registry) {
//		registry.addFormatterForFieldAnnotation(new CustomAnnotationFormatterFactory());
//	}
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(new AccessInterceptor())
//				.addPathPatterns("/*/*");
//	}
//
//
//	
//	
//	/**
//	 * 로그인 필터
//	 * 
//	 * @return
//	 */
//	@Bean
//	public FilterRegistrationBean<LoginCheckFilter> loginCheckFilterBean() {
//
//		FilterRegistrationBean<LoginCheckFilter> registrationBean = new FilterRegistrationBean<>();
//		registrationBean.setFilter(new LoginCheckFilter());
//		registrationBean.addUrlPatterns("*.json");
//		registrationBean.addUrlPatterns("*.html");
//
//		return registrationBean;
//	}
//
//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/").setViewName("forward:/index.jsp");
//	}

}