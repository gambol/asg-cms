package com.biemian.contentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

import com.biemian.db.dao.ArticleDao;
import com.biemian.db.domain.Article;
import com.biemian.utils.TextUtils;

public class CiArticleParser extends ContentParser {
	String urlPattern = "http://www.ci123.com/article.php/\\w+";
	Pattern p = Pattern.compile(urlPattern);

	public CiArticleParser(String content) {
		super(content);
	} 

	public CiArticleParser(String content, String url) {
		super(content, url, 0);
	} 

	private String titleQuery = "div.hack_border>h1";
	private String contentQuery = "div#articletext";

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
		int channelId = 10;
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
		CiArticleParser ylp = new CiArticleParser(html,
				"http://www.ci123.com/article.php/52442");
		System.out.println(ylp.shouldUseThisParser());
		ylp.doParse();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
	}

}
