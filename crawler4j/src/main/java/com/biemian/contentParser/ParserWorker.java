package com.biemian.contentParser;

import org.apache.log4j.Logger;

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
			if (parser.parseOk()) {
				logger.info("store it, url:" + parser.getUrl());
				parser.store();
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
