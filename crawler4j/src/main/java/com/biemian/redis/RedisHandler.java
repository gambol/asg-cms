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

	private static JedisPool pool;
	private static Jedis jedis;
	
	private String host = "localhost";
	private int port = 6379;
	
	private int EXPIRE_TIME = 30 * 86400;  // 最长时间 30天

	private RedisHandler() {
		pool = new JedisPool(new JedisPoolConfig(), host, port);

		jedis = pool.getResource();
		//jedis.auth("password");

	};

	public static RedisHandler getInstance() {
		if (instance == null) {
			instance = new RedisHandler();
			return instance;
		}

		return instance;
	}

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
	
	public static void main(String[] args) throws Exception{
		String key = "3";
		RedisHandler.getInstance().setKey(key);
		
		System.out.println(RedisHandler.getInstance().hasKey(key));
		
		Thread.sleep(3000);
		System.out.println(RedisHandler.getInstance().hasKey(key));
	}
}
