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
	private Jedis jedis;

	private static String host = "localhost";
	private static int port = 6379;
	private static JedisPoolConfig config = new JedisPoolConfig();
	static {
		config.setMaxActive(50);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setMaxIdle(5);
		config.setMinIdle(5);
		config.setTestWhileIdle(true);
	}
	
	private static JedisPool pool = new JedisPool(config, host,
			port);

	private static int EXPIRE_TIME = 30 * 86400; // 最长时间 30天

	public RedisHandler() {
		jedis = pool.getResource();
		// jedis.auth("password");

	};

	/**
	 * 判断一个key已经存在
	 * 
	 * @param key
	 */
	public boolean hasKey(String key) {
		return (jedis.get(key) != null);
	}

	/**
	 * 添加一个key，没有过期时间
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		// jedis.set(key, "1");
		// jedis.setex(key, 30, "days");
		//jedis.setex(key, EXPIRE_TIME, "1");
		jedis.set(key, "1");
	}

	public void removeKey(String key) {
		jedis.del(key);
	}

	/**
	 * redis 与 url之间的东西
	 * 
	 * @param url
	 * @return
	 */
	public boolean isThisUrlNew(String url) throws Exception {

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

	public static void main(String[] args) throws Exception {
		String key = "3";

		/*
		 * for(int i = 0; i < 200000; i++) { RedisHandler.getInstance().setKey(i
		 * + ""); }
		 */

		RedisHandler rh = new RedisHandler();
		System.out.println(rh.hasKey("1245"));
		rh.destory();
	}
}
