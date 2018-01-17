package com.cly.cache;

import java.util.Properties;

public interface KeyValue {
	
	public void initProperties(Properties p);
	
    public void setExpire(String key, int seconds);
	
	public  void set(String key, String value) ;
	
	public  void set(String key, String value, int expireSeconds) ;

	public  String get(String key);
	
	public  void append(String key, String value);

	public  void delete(String key);	 
	
	 
		
}


