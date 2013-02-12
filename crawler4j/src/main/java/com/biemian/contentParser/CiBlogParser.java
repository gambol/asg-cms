package com.biemian.contentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

import com.biemian.db.dao.ArticleDao;
import com.biemian.db.domain.Article;
import com.biemian.utils.TextUtils;

public class CiBlogParser extends ContentParser {
	private static final String urlPattern = "http://blog.ci123.com/\\w+/entry/\\w+";
	private static final Pattern p = Pattern.compile(urlPattern);

	public CiBlogParser(String content) {
		super(content);
	} 

	public CiBlogParser(String content, String url) {
		super(content, url);
	} 

	private String titleQuery = "div.content>h3";
	private String contentQuery = "div.content>div";

	public boolean shouldUseThisParser() {
		Matcher m = p.matcher(url);
		return m.find();
	}

	public boolean parseOk() {
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			logger.debug("[ciBlog] parse Error:" + this.toString());
			return false;
		}

		return true;
	}

	protected void parseTitle() {
		title = jsd.getText(titleQuery);
	}

	protected void parseContent() {
		/**
		 * 取出符合要求的第二个div
		 */
		content = jsd.getHtmlForCiBlog(contentQuery);
	}
	
	public void store() {
		int channelId = 13;
		ArticleDao.insert(title, content, "", "", channelId);
	}

	@Override
	public void doParse() {
		parseContent();
		parseTitle();
	}

	public static void main(String[] args) {
		String html = "";
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/parenting/article2007_45757286972.shtml");
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/news/201301271019007.shtml");
		CiBlogParser ylp = new CiBlogParser(html,
				"http://blog.ci123.com/linliang2000/entry/1409827");
		System.out.println(ylp.shouldUseThisParser());
		ylp.doParse();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
		System.out.println(ylp.parseOk());
	}

}
