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
public class Ci123Seeds {

	/**
	 * right now the seed is always this urls.
	 * in futre i should put it in a db
	 * @return
	 */
	public List<String> getSeedList() {
		String blog = "http://blog.ci123.com/";
		String index = "http://www.ci123.com/index1.html";
		String cate1 = "http://www.ci123.com/category.php/84";
		String cate2 = "http://www.ci123.com/category.php/34";
		String cate3 = "http://www.ci123.com/category.php/222";
		String cate4 = "http://www.ci123.com/category.php/36";
		String cate5 = "http://www.ci123.com/category.php/35";
		String cate6 = "http://www.ci123.com/category.php/33";
		
		List<String> resultList = new ArrayList<String>();
		String iyayaUrl = "http://www.iyaya.com/yuer/zhinan-";
		for(int i = 0; i < 6; i++) {
			resultList.add(iyayaUrl + i);
		}
		
		resultList.add("http://www.iyaya.com/yuer/zhinan-4562");
		resultList.add("http://www.iyaya.com/yuer/zhinan-782");
		resultList.add(cate5);
		resultList.add(cate6);
		resultList.add(index);
		resultList.add(cate3);
		resultList.add(cate4);
		resultList.add(cate1);
		resultList.add(blog);
		resultList.add(cate2);
		
		String firstPage = "http://www.yaolan.com/";
		String zhishiPage = "http://www.yaolan.com/zhishi/";
		String newsPage = "http://www.yaolan.com/news/";
		String healthPage = "http://www.yaolan.com/health/";
		String aIndexPage = "http://www.yaolan.com/index/";
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
