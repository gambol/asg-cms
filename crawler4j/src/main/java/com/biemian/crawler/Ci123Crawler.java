package com.biemian.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.List;
import java.util.regex.Pattern;

import com.biemian.contentParser.ContentParser;
import com.biemian.contentParser.ParserWorker;
import com.biemian.redis.RedisHandler;
import com.biemian.utils.HTTPGet;
import com.biemian.utils.TextUtils;

/**
 * @author zhenbao.zhou
 */
public class Ci123Crawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");
	
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();

		if (FILTERS.matcher(href).matches()) {
			return false;
		}
		
		if (href.indexOf("user.ci123.com") > 0 || href.indexOf("ping.ci123.com") > 0 
				|| href.indexOf("bbs.ci123.com") > 0) {
			return false;
		}

		if (href.indexOf("ci123.com") < 0 && 
				href.indexOf("http://www.iyaya.com/yuer/zhinan-") < 0) {
			return false;
		}

		RedisHandler rh = new RedisHandler();
		boolean re = rh.isThisUrlNew(url.getURL());
		rh.destory();
		// 小于2层的url，都需要再被抓取
		if (url.getDepth() < 2) {
			logger.info("depth < 2.  vist it. url:" + url);
			return true;
		}
		
		if (!re) {
			logger.info("already has it. skip vist. url:" + url);
		}

		return re;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();

		// System.out.println("URL: " + url);

		try {
			if (page.getParseData() instanceof HtmlParseData) {
				HtmlParseData htmlParseData = (HtmlParseData) page
						.getParseData();
				ParserWorker pw = new ParserWorker();
				ContentParser cp = pw.getParserFactory(url,
						htmlParseData.getHtml());
				pw.parse(cp);
			}

		} catch (Exception e) {
		}
		// System.out.println("=============");
	}
}
