package com.biemian.contentParser;

import java.io.File;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.biemian.utils.IOUtil;

public class JsoupTest {
	public static void main(String[] args) throws Exception {

		File input = new File("/media/E44E85334E850012/a.html");
		Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
		
		Elements e = doc.select("h1.title");
		System.out.println(e.html());
		
		List<String> htmlList = IOUtil.inputMoreRows("/media/E44E85334E850012/a.html");
		// YaolanParser ylp = new YaolanParser(html,
		// "http://www.yaolan.com/parenting/article2007_45757286972.shtml");
		
		//String html = HTTPGet.get("http://www.yaolan.com/zhishi/fenmianxunhao/");
		StringBuffer sb = new StringBuffer();
		for(String s : htmlList) {
			sb.append(s);
		}
		
		
		doc = Jsoup.parse(sb.toString());
		e =  doc.select("h1.title");
		System.out.println(e.html());
	}
}
