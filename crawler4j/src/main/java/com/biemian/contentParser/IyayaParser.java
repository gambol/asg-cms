package com.biemian.contentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

import com.biemian.db.dao.ArticleDao;
import com.biemian.db.domain.Article;
import com.biemian.redis.RedisHandler;
import com.biemian.utils.TextUtils;

public class IyayaParser extends ContentParser {
	private static final String urlPattern = "http://www.iyaya.com/yuer/zhinan";
	private static final Pattern p = Pattern.compile(urlPattern);

	public IyayaParser(String content) {
		super(content);
	} 

	public IyayaParser(String content, String url) {
		super(content, url, 0);
	} 

	private String titleQuery = "div#yr-article>h1>span";
	private String contentQuery = "div#yr-article-body";

	public boolean shouldUseThisParser() {
		Matcher m = p.matcher(url);
		return m.find();
	}

	public boolean parseOk() {
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			logger.debug("[CiArticle] parse Error:" + this.toString());
			return false;
		}

		return true;
	}

	protected void parseTitle() {
		title = jsd.getText(titleQuery);
	}

	protected void parseContent() {
		content = jsd.getHtmlWithBasic(contentQuery);
	}
	
	public void store() {
		int channelId = 1;
		
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
		IyayaParser ylp = new IyayaParser(html,
				"http://www.iyaya.com/yuer/zhinan-1438");
		System.out.println(ylp.shouldUseThisParser());
		ylp.doParse();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
	}

}