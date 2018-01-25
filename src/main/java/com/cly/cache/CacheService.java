package com.cly.cache;
 

public interface CacheService { 	 
	
	public void put(String key, Object value, int expireSeconds) ;
 
	public Object get(String key);
	
	public void put(String key,Object value);
	
	public void remove(String key); 
	
	public void close();	
	

}
