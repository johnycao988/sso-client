package com.cly.auth.base;

import javax.servlet.Filter;

public abstract class AuthServletFilter implements Filter {

	private AuthEnv authEnv;

	public AuthServletFilter() {

	}

	public void setAuthEnv(AuthEnv authEnv) {

		this.authEnv = authEnv;

	}

	public AuthEnv getAuthEnv() {

		return this.authEnv;

	}

	public abstract Class<?> getAuthEnvClass();

}
