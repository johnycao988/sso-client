package com.cly.auth.base; 

public class AuthSecurityContext {

	private final String id;

	private final AccessToken accessToken;

	public AuthSecurityContext(String id, AccessToken accessToken) {

		this.id = id;
		this.accessToken = accessToken;

	}

	public String getId() {
		return id;
	}

	public AccessToken getAccessToken() throws AuthException{
	 
		return accessToken;
	} 

 
}
