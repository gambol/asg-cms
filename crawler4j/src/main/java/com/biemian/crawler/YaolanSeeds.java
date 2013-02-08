package com.biemian.crawler;

import java.util.ArrayList;
import java.util.List;

import com.biemian.redis.RedisHandler;

/**
 * 添加yaolan相关的种子
 * 每次启动时,需要新增种子
 * @author zhenbao.zhou
 *
 */
public class YaolanSeeds {

	/**
	 * right now the seed is always this urls.
	 * in futre i should put it in a db
	 * @return
	 */
	public List<String> getSeedList() {
		String firstPage = "http://www.yaolan.com/";
		String zhishiPage = "http://www.yaolan.com/zhishi/";
		String newsPage = "http://www.yaolan.com/news/";
		String healthPage = "http://www.yaolan.com/health/";
		String aIndexPage = "http://www.yaolan.com/index/";
		
		List<String> resultList = new ArrayList<String>();
		resultList.add(firstPage);
		resultList.add(zhishiPage);
		resultList.add(newsPage);
		resultList.add(healthPage);
		resultList.add(aIndexPage);
		
		removeSeedInRedis(resultList);
		
		return resultList;
	}
	
	private void removeSeedInRedis(List<String> list) {
		RedisHandler rh = new RedisHandler();
		for (String s : list) {
			rh.removeKey(s);
		}
		rh.destory();
	}
}
