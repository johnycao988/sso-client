package com.cly.cache;

import java.util.Properties;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;

public class KVRedisService implements KeyValue {
	
	private Jedis jedis;

	private final static String REDIS_SERVER_URL = "com.cly.kv.redis.server.ip";

	private final static String REDIS_SERVER_PORT = "com.cly.kv.redis.server.port";

	private final static String REDIS_SERVER_AUTH = "com.cly.kv.redis.server.auth";

	@Override
	public void setExpire(String key, int seconds) {
		jedis.expire(key, seconds);
	}

	@Override
	public void set(String key, String value) {

		jedis.set(key, value);		
	}

	@Override
	public String get(String key) {

		return jedis.get(key);
	}

	@Override
	public void append(String key, String value) {

		jedis.append(key, value);
	}

	@Override
	public void delete(String key) {
		
		jedis.del(key);
	}

	@Override
	public void initProperties(Properties p) {

		String serverUrl = p.getProperty(REDIS_SERVER_URL);

		String me = " is not set.";

		if (serverUrl == null) {
			Logger.getGlobal().info(REDIS_SERVER_URL + me);
		}

		String port = p.getProperty(REDIS_SERVER_PORT);

		if (serverUrl == null) {
			Logger.getGlobal().info(REDIS_SERVER_PORT + me);
		}

		String auth = p.getProperty(REDIS_SERVER_AUTH);

		if (serverUrl == null) {
			Logger.getGlobal().info(REDIS_SERVER_AUTH + me);
		}

		if (serverUrl != null && port != null) {
			jedis = new Jedis(serverUrl, Integer.parseInt(port));
			jedis.auth(auth);
		}

	}

	@Override
	public void set(String key, String value, int expireSeconds) {
		
		this.set(key, value);
		
		this.setExpire(key, expireSeconds);

	}

}
