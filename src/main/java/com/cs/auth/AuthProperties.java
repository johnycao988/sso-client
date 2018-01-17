package com.cs.auth;

public interface AuthProperties {
	
	public String getId();
	
	public String getName();
	
	public String getServerRootUrl();
	
	public String getServletFilterClassName();
	
	public String getAuthUrl();
	
	public String getTokenUrl();
	
	public boolean isDefault();
 
	public String getProperty(String name);

}
