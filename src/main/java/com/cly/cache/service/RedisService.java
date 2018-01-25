package com.cly.cache.service;

import com.cly.cache.CacheService;

import redis.clients.jedis.Jedis;

public class RedisService implements CacheService{
	
	private Jedis jedis;
	
	public void init(String serverUrl, int serverPort, String authPass) {

		if (jedis != null)
			return;

		jedis = new Jedis(serverUrl, serverPort);
		
		jedis.auth(authPass);

	}

	@Override
	public void put(String key, Object value, int expireSeconds) { 
		
		jedis.set(key, (String)value);
		jedis.expire(key, expireSeconds);
	}
	
	@Override
	public void put(String key, Object value) { 
		jedis.set(key, (String)value);
	}
	

	@Override
	public Object get(String key) { 
		return jedis.get(key);
	}



	@Override
	public void remove(String key) { 
		jedis.del(key);
	}
 

	@Override
	public void close() {  
		jedis.close();
	}
	

}
