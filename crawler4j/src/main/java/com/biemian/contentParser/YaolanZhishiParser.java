package com.biemian.contentParser;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.compress.utils.IOUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.biemian.utils.HTTPGet;
import com.biemian.utils.IOUtil;
import com.biemian.utils.TextUtils;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

public class YaolanZhishiParser extends ContentParser {

	public YaolanZhishiParser(String content) {
		super(content);
	}

	public YaolanZhishiParser(String content, String url) {
		super(content, url);
	}

	public YaolanZhishiParser(String content, String url, int noUse) {
		super(content, url, 0);
	}

	// query
	private String titleQuery = "h1.title";

	protected void parseTitle() {
		title = jsd.getHtml(titleQuery);
	}

	protected void parseContent() {
		String contentAreaQuery = "div.area";
		StringBuffer sb = new StringBuffer();
		Elements e = jsd.getElements(contentAreaQuery);
		try {
			if (e != null) {
				Element allContents = e.get(0);
				for (Element childNode : allContents.getAllElements()) {
					if (childNode.className().equals("subTitle")) {
						sb.append("<strong>").append(childNode.text())
								.append("</strong><br/>");
					} else if (childNode.classNames().contains("explain")) {
						sb.append(childNode.text()).append("<br/>");
					} else if (childNode.classNames().contains("question")) {
						sb.append("<div class=\"question\">")
								.append(childNode.text()).append("</div>");
					} else if (childNode.classNames().contains("answer")) {
						sb.append("<div class=\"answer\">")
								.append(childNode.text()).append("</div>");
					}
				}
			}
		} catch (Exception ex) {

		}
		content = sb.toString();
	}

	String urlPattern = "http://www.yaolan.com/zhishi/\\w+";
	Pattern p = Pattern.compile(urlPattern);

	public boolean shouldUseThisParser() {
		Matcher m = p.matcher(url);
		return m.find();
	}

	public boolean parseOk() {
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			logger.debug("[YaolanZhishi] parse Error:" + this.toString());
			return false;
		}

		return true;
	}

	@Override
	public void doParse() {
		try {
			parseContent();
			parseTitle();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[zhishi]url:" + url + " content:" + htmlData);
		}
	}

	public static void main(String[] args) {

		List<String> htmlList = IOUtil
				.inputMoreRows("/media/E44E85334E850012/b.html");
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/parenting/article2007_45757286972.shtml");

		// String html =
		// HTTPGet.get("http://www.yaolan.com/zhishi/fenmianxunhao/");
		StringBuffer sb = new StringBuffer();
		for (String s : htmlList) {
			sb.append(s);
		}

		String html = sb.toString();

		System.out.println(html);
		YaolanZhishiParser ylp = new YaolanZhishiParser(html,
				"http://www.yaolan.com/zhishi/fenmianxunhao/");
		ylp.parseContent();
		ylp.parseTitle();
		// ylp.parsePublishTime();

		System.out.println(ylp.getTitle());
		System.out.println(ylp.getContent());
	}
}
