package com.taobao.arthas.manage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 跨域请求
 * 
 * @author zhaojindong
 *
 */
public class CrossDomainFilter implements Filter {
	private static final Logger LOG = LoggerFactory.getLogger(CrossDomainFilter.class);

	private final int time = 20 * 24 * 60 * 60;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		// 添加参数，允许任意domain访问
		resp.setHeader("Access-Control-Allow-Origin", "*");
		// 这个allow-headers要配为*，这样才能允许所有的请求头 --- update by zxy in 2018-10-19
		resp.setHeader("Access-Control-Allow-Headers", "*");
		resp.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
		resp.setHeader("Access-Control-Max-Age", time + "");
		chain.doFilter(request, resp);
	}

	@Override
	public void destroy() {
	}

}
