package com.cs.auth.base;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public abstract class AuthServletFilter implements Filter {

	protected AuthProperties authProp;

	public void setAutProperties(AuthProperties ap) {

		this.authProp = ap;
	}

	public AuthProperties getAuthProperties() {
		return authProp;
	}

	public abstract void init(FilterConfig filterConfig) throws ServletException;

	public abstract void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException;

	public abstract void destroy();

}
