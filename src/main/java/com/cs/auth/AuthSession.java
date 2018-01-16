package com.cs.auth;

import java.util.HashMap;

public class AuthSession {

	private HashMap<String, Object> hmSess;

	private long timeCounts = 0l;

	public AuthSession(long maxExpiredSeconds) {

	}

	public long getTimeCounts() {
		return timeCounts;
	}

	public void increaseTimeCounts(long counts) {
		timeCounts += counts;
	}

	public Object getAttribute(String name) {

		timeCounts = 0l;

		return hmSess.get(name);

	}

	public void setAttribute(String name, Object value) {

		timeCounts = 0l;

		hmSess.put(name, value);

	}

}
