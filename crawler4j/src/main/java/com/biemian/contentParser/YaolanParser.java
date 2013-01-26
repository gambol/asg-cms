package com.biemian.contentParser;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class YaolanParser extends ContentParser {

	public YaolanParser(String content) {
		super(content);
	}

	public YaolanParser(String content, String url) {
		super(content, url);
	}

	// query
	private String titleQuery = "div#final_content>h1";
	private String subTitleQuery;
	private String publishTimeQuery;
	private String authorQuery = "div#final_content>p.author>span";
	private String contentQuery = "div#content_p";
	private String summaryQuery = "div#abstract>h3";
	private String crumbQuery;

	protected void parseTitle () {
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
	
	protected void paserSummary() {
		summary = jsd.getHtml(summaryQuery);
	}
	
	protected void paserCrumb() {
		crumb = jsd.getHtml(crumbQuery);
	}
	
	protected void paserPublishTime() {
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
		if (e != null) {
			Element rightEl = e.get(0);
			String html = rightEl.html();
			int daoduPosition = html.indexOf("<strong>相关阅读：</strong>");
			String rightContent = html.substring(0, daoduPosition);
			//  将div封闭
			content = rightContent;
		}
	}
	
	public static void main(String[] args) {
		String html = ""; 
		YaolanParser ylp = new YaolanParser(html, "http://www.yaolan.com/parenting/article2007_45757286972.shtml");
		ylp.parseContent();
		ylp.parseTitle();
		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
	}
}
