package com.cly.auth.base;

import javax.servlet.Filter;

public abstract class AuthServletFilter implements Filter {

	private AuthEnv authEnv;

	public AuthServletFilter() {

	}

	public void setAuthProperties(AuthEnv authEnv) {

		this.authEnv = authEnv;

	}

	public AuthEnv setAuthProperties() {

		return this.authEnv;

	}

	public abstract Class<?> getAuthEnvClass();

}
