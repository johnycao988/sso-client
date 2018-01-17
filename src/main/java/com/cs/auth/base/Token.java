package com.cs.auth.base;

public interface Token {
	
	public final static String TOKEN_TYPE_BEARER="bearer";
	public final static String TOKEN_TYPE_BASIC="basic";
	
	public String getToken();
	public String getRefreshToken();
	public String getTokenId();
	public String getTokenType();
	public String getSessionState();
	
	
}

 