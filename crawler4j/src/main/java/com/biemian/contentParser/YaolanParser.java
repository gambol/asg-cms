package com.biemian.contentParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.biemian.utils.TextUtils;

public class YaolanParser extends ContentParser {

	public YaolanParser(String content) {
		super(content);
	}

	public YaolanParser(String content, String url) {
		super(content, url);
	}

	public YaolanParser(String content, String url, int noUse) {
		super(content, url, 0);
	}

	// query
	private String titleQuery = "div#final_content>h1";
	private String subTitleQuery;
	private String publishTimeQuery = "div#final_content>p.author>span";
	private String authorQuery;
	private String contentQuery = "div#content_p";
	private String summaryQuery = "div#abstract>h3";
	private String crumbQuery;

	protected void parseTitle() {
		title = jsd.getHtml(titleQuery);
	}

	protected void parseSubTitle() {
		subTitle = jsd.getHtml(subTitleQuery);
	}

	protected void paserAuthor() {
		author = jsd.getHtml(authorQuery);
	}

	protected void paserContent() {
		content = jsd.getHtml(contentQuery);
	}

	protected void parseSummary() {
		summary = jsd.getHtml(summaryQuery);
	}

	protected void parseCrumb() {
		crumb = jsd.getHtml(crumbQuery);
	}

	protected void parsePublishTime() {
		Elements e = jsd.getElements(publishTimeQuery);
		if (e != null) {
			if (e.size() < 2) {
				publishTime = e.text();
			}

			Element rightEl = e.get(1);
			publishTime = rightEl.text();
		}
	}

	protected void parseContent() {
		Elements e = jsd.getElements(contentQuery);
		try {
			if (e != null) {
				Element rightEl = e.get(0);

				String text = rightEl.text();
				int daoduPosition = text.indexOf("相关阅读");
				if (daoduPosition != -1) {
					String rightContent = text.substring(0, daoduPosition);
					content = rightContent;
				} else {
					content = text;
				}

				content = TextUtils.changeContinuesSpaceToBr(content);
			}
		} catch (Exception ex) {

		}
	}

	String urlPattern = "http://www.yaolan.com/\\w+/\\w+.shtml";
	Pattern p = Pattern.compile(urlPattern);

	public boolean shouldUseThisParser() {
		Matcher m = p.matcher(url);
		return m.find();
	}

	public boolean parseOk() {
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)
				|| TextUtils.isEmpty(publishTime)) {
			logger.debug("[Yaolan] parse Error:" + this.toString());
			return false;
		}

		return true;
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
		YaolanParser ylp = new YaolanParser(html,
				"http://www.yaolan.com/news/201301161730044.shtml", 0);
		System.out.println(ylp.shouldUseThisParser());

		ylp.parseContent();
		ylp.parseTitle();
		ylp.parsePublishTime();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
		System.out.println(ylp.getPublishTime());
	}
}
