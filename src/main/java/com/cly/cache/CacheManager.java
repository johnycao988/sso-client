package com.cly.cache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
 
public class CacheManager {

	private static HashMap<String, CacheService> hmCacheService = new HashMap<String, CacheService>();

	public static void addCacheService(String cacheName, CacheService cacheService) {
		hmCacheService.put(cacheName, cacheService);
	}

	public static CacheService getCacheService(String cacheName) {

		return hmCacheService.get(cacheName);
	}

	public static void shutdown() {

		synchronized (hmCacheService) {

			Iterator<Entry<String, CacheService>> it = hmCacheService.entrySet().iterator();

			while (it.hasNext()) {

				Entry<String, CacheService> e = it.next();

				CacheService cs = e.getValue();

				cs.close();

			}

		}

		hmCacheService.clear();

	}

}
