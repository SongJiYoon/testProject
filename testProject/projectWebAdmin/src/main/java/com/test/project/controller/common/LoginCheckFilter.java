package com.test.project.controller.common;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCheckFilter implements Filter {

	private String ajaxHeader = "AJAX";

	public void init(FilterConfig config) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		String sess = (String) httpRequest.getSession().getAttribute(CommonController.SESS_USER_INFO);

		if (sess == null && isAjaxRequest(httpRequest)) {
			res.sendError(HttpServletResponse.SC_FORBIDDEN);
		} else {
			chain.doFilter(httpRequest, res);
		}
	}

	public void destroy() {

	}

	private boolean isAjaxRequest(HttpServletRequest req) {
		return req.getHeader(ajaxHeader) != null && req.getHeader(ajaxHeader).equals(Boolean.TRUE.toString());
	}
}