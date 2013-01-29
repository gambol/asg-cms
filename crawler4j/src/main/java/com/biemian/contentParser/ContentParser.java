package com.biemian.contentParser;

import org.apache.log4j.Logger;

import com.biemian.crawler.JSoupDealer;

import edu.uci.ics.crawler4j.url.WebURL;

/**
 * 
 * @author zhenbao.zhou
 *
 */
public abstract class ContentParser {
	
	public final static Logger logger = Logger.getLogger("parser"); 

	protected String url;
	protected String htmlData;
	
	// 基本内容
	protected String title = "";
	protected String subTitle = "";
	protected String publishTime = "";
	protected String author = "";
	protected String content = "";
	protected String summary = "";
	protected String crumb = "";
	
	protected JSoupDealer jsd;
	
	// query
	protected String titleQuery;
	protected String subTitleQuery;
	protected String publishTimeQuery;
	protected String authorQuery;
	protected String contentQuery;
	protected String summaryQuery;
	protected String crumbQuery;
	
	/**
	 * 是否可以使用这个parser
	 * @return
	 */
	public boolean shouldUseThisParser() {
		return true;
	}
	
	/**
	 * parse结果如何?
	 * @return
	 */
	public boolean parseOk() {
		return true;
	}
	

	public void store() {
		
	}
	
	/**
	 * 测试时使用的一个方法
	 * @param htmlData
	 * @param url
	 */
	public ContentParser(String htmlData, String url, int noUse) {
		jsd = new JSoupDealer(null, url);
		this.htmlData = htmlData;
		this.url = url;
	}
	
	public ContentParser(String htmlData, String url) {
		jsd = new JSoupDealer(htmlData);
		this.htmlData = htmlData;
		this.url = url;
	}
	
	public ContentParser(String htmlData) {
		jsd = new JSoupDealer(htmlData);
		this.htmlData = htmlData;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHtmlData() {
		return htmlData;
	}
	public void setHtmlData(String htmlData) {
		this.htmlData = htmlData;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSubTitle() {
		return subTitle;
	}
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getCrumb() {
		return crumb;
	}
	public void setCrumb(String crumb) {
		this.crumb = crumb;
	}
	public JSoupDealer getJsd() {
		return jsd;
	}
	public void setJsd(JSoupDealer jsd) {
		this.jsd = jsd;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("=======================");
		sb.append("url:").append(url).append("\n");
		sb.append("title:").append(title).append("\n");
		sb.append("content:").append(content).append("\n");
		sb.append("publishTime:").append(publishTime).append("\n");
		sb.append("subTitle:").append(subTitle).append("\n");
		sb.append("crumb:").append(crumb);
		return sb.toString();
	}
	
	/**
	 * 正常流程下工作的函数
	 */
	public void doParse() {
		
	}

}
