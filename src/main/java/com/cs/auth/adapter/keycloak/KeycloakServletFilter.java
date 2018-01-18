package com.cs.auth.adapter.keycloak;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cs.auth.base.AuthException;
import com.cs.auth.base.AuthLogger;
import com.cs.auth.base.AuthServletFilter;
import com.cs.auth.base.AuthSession;

public class KeycloakServletFilter extends AuthServletFilter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		if (!validateSession(request, response))
			return;

		chain.doFilter(request, response);

	}

	private boolean validateSession(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		try {
			AuthSession as = AuthSession.getAuthSession(req, res, KeycloakSession.class, this.authProp);

			if (as.needLogin()) {
				as.forwardLoginPage();
				return false;
			}

			return true;
		} catch (AuthException e) {
			AuthLogger.error(e);
			return false;
		}

	}

}
