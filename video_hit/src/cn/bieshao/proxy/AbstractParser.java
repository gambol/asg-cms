package cn.bieshao.proxy;

import java.util.List;

import org.jsoup.nodes.Document;

import com.bieshao.model.Proxy;

public class AbstractParser {

	public List<String> urlList;
	Document doc;
	String htmlData;
	
	public List<String> getUrlList() {
		return urlList;
	}



	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}



	public Document getDoc() {
		return doc;
	}



	public void setDoc(Document doc) {
		this.doc = doc;
	}



	public List<Proxy> parse() {return null;};
}
