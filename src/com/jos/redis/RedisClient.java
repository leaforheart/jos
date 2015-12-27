package com.jos.redis;

import com.jos.common.util.PropertiesUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClient {
	private static String path = new RedisClient().getClass().getResource("")+"redis.properties";
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), PropertiesUtil.getProperty(path, "host"),Integer.parseInt(PropertiesUtil.getProperty(path, "port")));
	private RedisClient() {}
	
	public static Jedis getJedis() {
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
