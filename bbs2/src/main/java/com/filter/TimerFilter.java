package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class TimerFilter implements Filter{
	private FilterConfig config;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 필터 클래스가 만들어질 때 한번 실행
		this.config = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 전 작업
		long before = System.currentTimeMillis();
		
		// 다음 필터 또는 마지막 필터이면 endPoint(Servlet 또는 JSP) 실행
		chain.doFilter(request, response);
		
		// 후 작업
		long after = System.currentTimeMillis();
		String uri = null;
		if(request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest)request;
			uri = req.getRequestURI();
			
			config.getServletContext().log(uri + " : " + (after-before)+"ms");
		}
	}

	@Override
	public void destroy() {
		// 필터 클래스가 소멸되기 전에 한번 실행
		
	}

}
