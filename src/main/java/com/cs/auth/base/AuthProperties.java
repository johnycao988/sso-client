package com.cs.auth.base;

public interface AuthProperties {
	
	public String getAuthServerId();
	
	public String getAuthServerName();
	
	public String getAuthServerRootUrl();
	
	public String getAuthServletFilterClassName();
	
	public String getAuthUri();
	
	public String getTokenUri();
	
	public boolean isDefaultAuthServer();
 
	public String getAuthProperty(String name);
	
	public String getClientId();
	
	public String getClientSecret();
	
	public String getClientRedirectRootUrl();
	

}
