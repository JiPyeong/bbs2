package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CharacterEncodingFilter implements Filter{
	private String charset;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		charset = filterConfig.getInitParameter("charset");
		if(charset==null || charset.length()==0)
			charset = "UTF-8";	
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		if(request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest)request;
			if(req.getMethod().equalsIgnoreCase("POST")) {
				req.setCharacterEncoding(charset);
			}
		}
		
		// 다음 필터 또는 필터의 마지막이면 resource 실행
		chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		
		
	}
	
}
