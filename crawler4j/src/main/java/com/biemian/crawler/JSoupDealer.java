package com.biemian.crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JSoupDealer {
	Document doc;
	
	
	/**
	 * 方便进行调试的一个方法
	 * @param content
	 * @param url
	 */
	public JSoupDealer(String content, String url) {
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			
		}
	}
	
	public JSoupDealer(String content) {
		doc = Jsoup.parse(content);
	}
	
	public String getHtml(String query) {
		Elements e = doc.select(query);
		return e.html();
	}
	
	public String getText(String query) {
		Elements e = doc.select(query);
		return e.text();
	}
	
	public Elements getElements(String query) {
		return doc.select(query);
	}
	
	public static void main(String[] args) throws IOException {

        String url = "Http://www.qunar.com/";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

        print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
