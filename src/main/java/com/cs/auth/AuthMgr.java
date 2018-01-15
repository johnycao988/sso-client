package com.cs.auth;

import java.io.FileInputStream;
import java.util.Properties;

public class AuthMgr {

	private static AuthServletFilter servletFilter = null;

	private AuthMgr() {

	}

	public static void initServletFilter() throws AuthException {

		try (FileInputStream inStream = new FileInputStream(System.getProperty("AUTH_CONFIG_FILE"))) {

			Properties p = new Properties();

			p.load(inStream);

			String oauth2FilterClass = p.getProperty("AUTH2.SERVLET.FILTER.CLASS");

			if (oauth2FilterClass == null)
				throw new AuthException("AUTH2.SERVLET.FILTER.CLASS is not set.");

			servletFilter = (AuthServletFilter) Class.forName(oauth2FilterClass).newInstance();

			servletFilter.initConfigProperties(p);

		} catch (Exception e) {
			throw new AuthException(e);
		}

	}

	public static AuthServletFilter getServletFilter() {
		return servletFilter;
	}

}