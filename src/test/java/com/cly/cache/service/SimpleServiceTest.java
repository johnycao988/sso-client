package com.cly.cache.service;

import org.junit.Test;

import com.cly.cache.CacheManager;
import com.cly.cache.CacheService;

import junit.framework.Assert; 

@SuppressWarnings("deprecation")
public class SimpleServiceTest {

 
	@Test
	public void testAA() throws InterruptedException{
		
		SimpleService ss=new SimpleService();
		
		String sn="SimpleService";
		
		CacheManager.addCacheService(sn, ss);
		
		CacheService cs=CacheManager.getCacheService(sn);
		
		String key1="key1";
		String value1="value1";		
		
		String key2="key2";
		String value2="value2";
		
		cs.put(key1, value1);
		cs.put(key2, value2,1);
		
		String rtnV1=(String)cs.get(key1);
		String rtnV2=(String)cs.get(key2);
		
		Assert.assertEquals(value1, rtnV1);
		Assert.assertEquals(value2, rtnV2);
		
		Thread.sleep(2000);
			
		rtnV1=(String)cs.get(key1);
		rtnV2=(String)cs.get(key2);
		
		Assert.assertEquals(value1, rtnV1);
		Assert.assertEquals(null, rtnV2); 
		
		CacheManager.shutdown();
		
		
		
	}

}
