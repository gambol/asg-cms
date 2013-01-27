package com.biemian.contentParser;

public class ParserWorker {
	
	// 将来写个牛逼的函数把这个垃圾替代掉
	public ContentParser getParserFactory(String url, String content) {
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
			System.out.println("parser is null");
			return false;
		}
		
		if (parser.shouldUseThisParser()) {
			parser.doParse();
			System.out.println(parser.toString());
			return parser.parseOk();
		}
		
		System.out.println(parser.toString());
		return false;
	}
	
	public static void main(String[] args) {
		YaolanZhishiParser yzp = new YaolanZhishiParser("", "http://www.yaolan.com/zhishi/baobaobanzhengyuliunian/", 1);
		ParserWorker pw = new ParserWorker();
		System.out.println(pw.parse(yzp));
	}
}
