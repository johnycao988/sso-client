package com.cs.auth.base;

import java.util.Calendar;

public abstract class Token {

	public final static String TOKEN_TYPE_BEARER = "bearer";

	public final static String TOKEN_TYPE_BASIC = "basic";

	protected String token;

	protected String refreshToken;

	protected String tokenType;

	protected String tokenId;

	protected String sessionState;

	protected int expireInSeconds;

	protected int refreshExpireInSeconds;

	protected final long initTime;

	private final String jsonToken;

	protected Token(String jsonToken) {
		this.initTime = Calendar.getInstance().getTimeInMillis();
		this.jsonToken = jsonToken;
	}

	public final String getJSONToken() {
		return this.jsonToken;
	}

	public String toString() {
		return getJSONToken();
	}

	public String getTokenType() {
		return tokenType;
	}

	public String getSessionState() {
		return sessionState;
	}

	public String getToken() {
		return this.token;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public String getTokenId() {
		return tokenId;
	}

	public int expireInSeconds() {
		return this.expireInSeconds;
	}

	public int refreshExpireInSeconds() {
		return this.refreshExpireInSeconds;
	}

	public boolean isExpired() {

		long td = (Calendar.getInstance().getTimeInMillis() - this.initTime) / 1000;

		if (td >= this.expireInSeconds)

			return true;

		return false;
	}

	public boolean isRefreshExpired() {

		long td = (Calendar.getInstance().getTimeInMillis() - this.initTime) / 1000;

		if (td >= this.refreshExpireInSeconds)

			return true;

		return false;
	}


	
}
