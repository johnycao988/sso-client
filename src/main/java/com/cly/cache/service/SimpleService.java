package com.cly.cache.service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.cly.cache.CacheService;

public class SimpleService implements CacheService {

	HashMap<String, _CacheData> hmCacheData = new HashMap<String, _CacheData>();

	Timer timer = new Timer();

	public SimpleService() {

		_DataExpireCheckTask task = new _DataExpireCheckTask(this.hmCacheData);

		timer.schedule(task, 1000, 1000);

	}

	@Override
	public void put(String key, Object value, int expireSeconds) {

		if (null == key)
			return;

		_CacheData data = new _CacheData(value, expireSeconds);

		hmCacheData.put(key, data);

	}

	@Override
	public void put(String key, Object value) {

		this.put(key, value, -1);
	}

	@Override
	public Object get(String key) {

		_CacheData data = hmCacheData.get(key);

		if (null == data)
			return null;

		if (data.isExpired()) {
			this.remove(key);
			return null;
		} else
			return data.getData();

	}

	@Override
	public void remove(String key) {

		this.hmCacheData.remove(key);

	}

	@Override
	public void shutdown() {

		timer.cancel();

		this.hmCacheData.clear();

	}

}

class _DataExpireCheckTask extends TimerTask {

	private HashMap<String, _CacheData> hmCacheData;

	public _DataExpireCheckTask(HashMap<String, _CacheData> hmCacheData) {
		this.hmCacheData = hmCacheData;
	}

	@Override
	public void run() {

		synchronized (hmCacheData) {

			Iterator<Entry<String, _CacheData>> it = hmCacheData.entrySet().iterator();

			while (it.hasNext()) {

				Entry<String, _CacheData> e = it.next();
				String key = e.getKey();
				_CacheData data = e.getValue();
				if (data.isExpired()) {
					hmCacheData.remove(key);
				}

			}

		}

	}

}

class _CacheData {

	private Object data;

	private int expireSeconds;

	private long lastAccessTime;

	public _CacheData(Object data, int expireSeconds) {

		this.data = data;
		this.expireSeconds = expireSeconds;
		this.lastAccessTime = Calendar.getInstance().getTimeInMillis();

	}

	public Object getData() {
		this.lastAccessTime = Calendar.getInstance().getTimeInMillis();
		return this.data;
	}

	public boolean isExpired() {

		if (this.expireSeconds <= 0)
			return false;

		long ct = (Calendar.getInstance().getTimeInMillis() - this.lastAccessTime) / 1000;

		if (ct >= this.expireSeconds)
			return true;
		else
			return false;

	}

}
