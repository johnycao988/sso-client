package com.cs.auth;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AuthServletFilter implements Filter {

	public abstract void init(FilterConfig filterConfig) throws ServletException;

	public abstract void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	public abstract void destroy();

	public abstract void initConfigProperties(Properties configProp) throws AuthException;
}
