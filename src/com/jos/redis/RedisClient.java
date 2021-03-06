package com.jos.redis;

import com.jos.common.util.Constants;
import com.jos.common.util.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private static String path = Constants.REDIS_PATH;
	private static JedisPool pool = null;
	private RedisClient() {}
	
	public static Jedis getJedis() {
		if(pool==null) {
			JedisPoolConfig jpc = new JedisPoolConfig();
			jpc.setMaxTotal(100);
			jpc.setMaxIdle(100);
			jpc.setMinIdle(8);
			pool = new JedisPool(jpc, PropertiesUtil.getProperty(path, "host"),Integer.parseInt(PropertiesUtil.getProperty(path, "port")));
		}
		return pool.getResource();
	}
	
	public static void destroy() {
		pool.destroy();
	}
	
	@SuppressWarnings("deprecation")
	public static void returnResource(Jedis resource) {
		if(resource.isConnected()) {
			pool.returnResource(resource);
		} else {
			pool.returnBrokenResource(resource);
		}
	}

}
