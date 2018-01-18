package com.cs.auth.base;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthMgr {

	private static AuthConfig authConf = init();

	private AuthMgr() {

	}

	private static AuthConfig init() {

		try {
			String confFile = System.getProperty("AUTH_CONFIG_FILE");

			if (confFile == null) {
				confFile = "/config/auth.config.yml";
			}

			return new AuthConfig(confFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static AuthServletFilter getServletFilter(HttpServletRequest req,HttpServletResponse res) {
		return authConf.getAuthServletFilter(req,res);
	}

	public static void init(FilterConfig filterConfig) throws ServletException {

		AuthServletFilter[] asfs = authConf.getAllAuthServletFilters();

		for (AuthServletFilter asf : asfs) {
			initAuthServletFilter(asf, filterConfig);
		}

	}

	public static void initAuthServletFilter(AuthServletFilter asf, FilterConfig filterConfig) {

		try {

			if (asf != null)
				asf.init(filterConfig);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void destroy() {

		AuthServletFilter[] asfs = authConf.getAllAuthServletFilters();

		for (AuthServletFilter asf : asfs) {
			if (asf != null)
				asf.destroy();
		}
	}

}