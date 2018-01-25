package com.cly.auth.base;
 

public abstract class AccessToken {

	protected String token;
	protected String refreshToken;
	protected int expireSeconds;
	protected int refreshExpireSeconds;
	protected String type;
	

	protected AccessToken(String responseJsonToken) throws AuthException {

	}

	public String getToken() {
		return token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public int getExpireSeconds() {
		return expireSeconds;
	}

	public int getRefreshExpireSeconds() {
		return refreshExpireSeconds;
	}

	public String getType() {
		return type;
	}

	public String getJSONToken() {

		return TokenUtil.toJSONToken(this);

	}

	public String toString() {
		return this.getToken();
	}

}
