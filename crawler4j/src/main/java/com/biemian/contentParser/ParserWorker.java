package com.biemian.contentParser;

import org.apache.log4j.Logger;

import com.biemian.redis.RedisHandler;

public class ParserWorker {
	private final static Logger logger = Logger.getLogger(ParserWorker.class);
	
	// 将来写个牛逼的函数把这个垃圾替代掉
	public ContentParser getParserFactory(String url, String content) {
		CiArticleParser cap = new CiArticleParser(content, url);
		if (cap.shouldUseThisParser())
			return cap;
		
		CiBlogParser cbp = new CiBlogParser(content, url);
		if (cbp.shouldUseThisParser())
			return cbp;
		
		IyayaParser ip = new IyayaParser(content, url);
		if (ip.shouldUseThisParser())
			return ip;
		
		YaolanParser yp = new YaolanParser(content, url);
		if (yp.shouldUseThisParser())
			return yp;
		
		YaolanAskParser yap = new YaolanAskParser(content, url);
		if (yap.shouldUseThisParser())
			return yap;
		
		YaolanZhishiParser yzp = new YaolanZhishiParser(content, url);
		if (yzp.shouldUseThisParser()) 
			return yzp;
		
		return null;
	}
	
	public <T extends ContentParser> boolean parse(final T parser) {
		if (parser == null) {
		//	System.out.println("parser is null");
			return false;
		}
		
		if (parser.shouldUseThisParser()) {
			parser.doParse();
			
			// 以后增加一个字段，判断这个页面是否已经存储过
			// 规则是， 用store_+url的md5 做key
			if (parser.parseOk()) {
				String key = parser.getUrl() + "store";
				RedisHandler rh = new RedisHandler();
				if (rh.isThisUrlNew(key)) {
					logger.info("[store]store it, url:" + parser.getUrl());
					parser.store();
				} else {
					logger.info("[store]already has it. donot store. url:" + parser.getUrl());
				}
			}
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		YaolanZhishiParser yzp = new YaolanZhishiParser("", "http://www.yaolan.com/zhishi/baobaobanzhengyuliunian/", 1);
		ParserWorker pw = new ParserWorker();
		System.out.println(pw.parse(yzp));
	}
}
