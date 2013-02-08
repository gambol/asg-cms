package com.biemian.redis;

import org.apache.log4j.Logger;

import com.biemian.utils.TextUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis related work
 * 
 * @author zhenbao.zhou
 * 
 */
public class RedisHandler {
	private final static Logger logger = Logger.getLogger(RedisHandler.class);
	
	private static RedisHandler instance;

	private Jedis jedis;
	
	private static String host = "localhost";
	private static int port = 6379;
	private static JedisPool pool = new JedisPool(new JedisPoolConfig(), host, port);
	
	private static int EXPIRE_TIME = 30 * 86400;  // 最长时间 30天

	public RedisHandler() {
		jedis = pool.getResource();
		//jedis.auth("password");

	};
	
	/** 
	 * 判断一个key已经存在
	 * @param key
	 */
	public boolean hasKey(String key) {
		return (jedis.get(key) != null);
	}
	
	/**
	 * 添加一个key，设置过期时间是30天
	 * @param key
	 */
	public void setKey(String key) {
		//jedis.set(key, "1");
		//jedis.setex(key, 30, "days");
		jedis.setex(key, EXPIRE_TIME, "1");
	}
	
	public void removeKey(String key) {
		jedis.del(key);
	}
	
	/**
	 * redis 与 url之间的东西
	 * @param url
	 * @return
	 */
	public boolean isThisUrlHandled(String url) {
		if (url == null) {
			return false;
		}
		
		String md5 = TextUtils.md5(url);
		if (hasKey(md5)) {
			return false;
		} else {
			setKey(md5);
		}
		return true;
	}
	
	public void destory() {
		pool.returnResource(jedis);
	}
	
	public static void main(String[] args) throws Exception{
		String key = "3";

		/*
		for(int i = 0; i < 200000; i++) {
			RedisHandler.getInstance().setKey(i + "");
		}
		*/
		
		RedisHandler rh = new RedisHandler();
		System.out.println(rh.hasKey("1245"));
		rh.destory();
	}
}
