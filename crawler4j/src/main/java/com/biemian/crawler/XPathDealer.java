package com.biemian.crawler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.transform.TransformerException;

//import org.apache.html.dom.HTMLDocumentImpl;
//import org.apache.xpath.XPathAPI;
//import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** 
 *  use the more niubi's jsoup
 *  xpath is fucking
 */
@Deprecated
public class XPathDealer {
		
	/*
	private Document doc;
	
	public XPathDealer(String content) {
		parserToDoc(content);
	}
	
	// 通过url，将相应的html解析为DOM文档
	public void getDocument(String url) {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(url);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		doc = parser.getDocument();
	}
	
	// 通过url，将相应的html解析为DOM文档
	public void parserToDoc(String content) {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(new InputSource(content));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		doc = parser.getDocument();
	}

	// 通过XPath定位具体的节点
	public  NodeList getExactNode(String xp) {
		NodeList list = null;
		if (xp == null) {
			return list;
		}
		
		xp = xp.toUpperCase();
		try {
			list = XPathAPI.selectNodeList(doc, xp);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static String getAllText(Node node) {
		String value = "";
		if (node == null) {
			return null;
		}
		value = node.getTextContent();
		if (value != null) {
			value = value.trim();
		}
		
		return value;
	}
	
	public static String getHTML(Node node){
		
	}
	
	public static String getFirstText(Node node) {
		String value = "";
		if (node == null) {
			return null;
		}
				
		node = node.getFirstChild();
		if (node != null) {
			value = node.getNodeValue();
		}
		
		return value == null ? null : value.trim();
	}

	public static void main(String[] args) {
		String baidu = "http://www.baidu.com/";
		String bPath = "//HTML//BODY//DIV[2]//P//MAP//AREA";

		String yyt = "http://www.yaolan.com/preconception/";
		String yPath = "/html/BODY/SECTION[3]/ARTICLE/ARTICLE/DIV[2]/DIV/p";

		XPathDealer xpd = new XPathDealer("<html></html>");
		// 根据xpath获得指定的节点
		NodeList list = xpd.getExactNode(yPath);
		System.out.println("符合条件的结点个数为：" + list.getLength());
		for (int i = 0; i < list.getLength(); i++) {
			System.out.println(getFirstText(list.item(i)));
		}
	}
	
	*/
}