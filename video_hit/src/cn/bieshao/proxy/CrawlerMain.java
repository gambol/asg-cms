package cn.bieshao.proxy;

import java.util.ArrayList;
import java.util.List;

public class CrawlerMain {
	public void xiziCrawl() {
		List<String> urlList = new ArrayList<String>();
		String nn = "http://www.xici.net.co/nn/";
		for(int i = 1; i < 99; i++) {
			urlList.add(nn+i);
		}
		
		String wn = "http://www.xici.net.co/wn/";
		for(int i = 1; i < 70; i++) {
			urlList.add(wn + i);
		}
		
		String wt = "http://www.xici.net.co/wt/";
		for(int i = 1; i < 26; i++) {
			urlList.add(wt + i);
		}
		
		String nt = "http://www.xici.net.co/nt/";
		for(int i = 1; i < 5; i++) {
			urlList.add(nt + i);
		}
		
		CommonCrawler cc = new CommonCrawler(urlList);
		cc.run(XiciProxyParser.class);
	}
	
	public void cnproxyCrawl() {
		List urlList = new ArrayList<String>();
		String cnProxyUrl = "http://www.cnproxy.com/proxy";
		for (int i = 1; i < 11; i++) {
			urlList.add(cnProxyUrl + i + ".html");
		}
		CommonCrawler cc = new CommonCrawler(urlList);
		cc.run(CnProxyParser.class);
	}
	
	public static void main(String[] args) throws Exception {
		CrawlerMain cm = new CrawlerMain();
		cm.cnproxyCrawl();
	//	cm.xiziCrawl();
	}
}
