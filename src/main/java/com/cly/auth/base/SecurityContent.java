package com.cly.auth.base;


public abstract class SecurityContent {

	private String id;
	
	private AccessToken accessToken;
	
	public SecurityContent(String id) {

		this.id=id;
	}
	
	public String getId(){
		return id;
	}

	public AccessToken getAccessToken(){
		return accessToken;
	}

	
}
