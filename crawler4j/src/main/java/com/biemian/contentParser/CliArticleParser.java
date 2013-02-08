package com.biemian.contentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.StringUtils;

import com.biemian.db.dao.ArticleDao;
import com.biemian.db.domain.Article;
import com.biemian.utils.TextUtils;

public class CliArticleParser extends ContentParser {
	String urlPattern = "http://www.ci123.com/article.php/\\w+";
	Pattern p = Pattern.compile(urlPattern);

	public CliArticleParser(String content) {
		super(content);
	} 

	public CliArticleParser(String content, String url) {
		super(content, url, 0);
	} 

	private String titleQuery = "div.con>h1";
	private String publishTimeQuery = "div.p_details_t>span.fr";
	private String contentQuery = "div.best_con_wz>p";

	public boolean shouldUseThisParser() {
		Matcher m = p.matcher(url);
		return m.find();
	}

	public boolean parseOk() {
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)
				|| TextUtils.isEmpty(publishTime)) {
			logger.debug("[YaolanAsk] parse Error:" + this.toString());
			return false;
		}

		return true;
	}

	protected void parseTitle() {
		title = jsd.getText(titleQuery);
	}

	protected void parsePublishTime() {
		publishTime = jsd.getText(publishTimeQuery);
	}

	protected void parseContent() {
		content = jsd.getText(contentQuery);
	}
	
	public void store() {
		int channelId = 11;
		ArticleDao.insert(title, content, "", publishTime, channelId);
	}

	@Override
	public void doParse() {
		parseContent();
		parseTitle();
		parsePublishTime();
	}

	public static void main(String[] args) {
		String html = "";
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/parenting/article2007_45757286972.shtml");
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/news/201301271019007.shtml");
		CliArticleParser ylp = new CliArticleParser(html,
				"http://ask.yaolan.com/question/130119174155a46bba33.html");
		ylp.parseContent();
		ylp.parseTitle();
		ylp.parsePublishTime();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
		System.out.println(ylp.getPublishTime());
	}

}
