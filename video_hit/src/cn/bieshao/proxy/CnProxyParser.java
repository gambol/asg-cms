package cn.bieshao.proxy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.bieshao.utils.HTTPUtils;

import com.bieshao.model.Proxy;

/**
 * 处理cnproxy的代理
 * version 1: 2013-2-15 这个版本的代码很傻， 原理是:
 *    z="3";m="4";k="2";l="9";d="0";b="5";i="7";w="6";r="8";c="1";
 *    port里写的是 document.write(":"+r+d+r+d)
 * @author zhenbao.zhou
 *
 */
public class CnProxyParser extends AbstractParser {

	private final static Logger logger = Logger.getLogger(CnProxyParser.class);
	
	public CnProxyParser() {};
	
	public CnProxyParser(Document d) {
		doc = d;
	}
	
	@Override
	public List<Proxy> parse() {
		List<Proxy> proxyList = new ArrayList<Proxy>();
		if (doc == null) {
			return proxyList;
		}
		
		Elements es = doc.select("div#proxylisttb>table");
		Element table = null;
		for(Element e : es) {
			if (e.text().contains("IP") && e.text().contains("Type")) {
				table = e;
				break;
			}
		}
		if (table == null){
			return proxyList;
		}
		Map alphaValue = parseJs();
		
		Elements trs = table.getElementsByTag("tr");
		for(Element tr : trs) {
			Elements tds = tr.getElementsByTag("td");
			if (tds.size() != 4)  {
				logger.error("解释错误了！！, tds的长度不为4 : "  + tds.html());
				continue;
			}
			
			String ip = tds.get(0).text().trim();
		//  System.out.println("ip:" + ip);
			
			String comment = tds.get(3).text().trim();
			String portHtml = tds.get(0).getElementsByTag("script").html();
			String[] arrays = portHtml.split("\\+|\\)");
			StringBuffer portBuffer = new StringBuffer();
			for(String a : arrays) {
				if(alphaValue.containsKey(a)) {
					portBuffer.append(alphaValue.get(a));
				}
			}
		//	System.out.println(portBuffer.toString());
			
			int port = 0;
			try {
				if (portBuffer.length() > 0){
					port = Integer.parseInt(portBuffer.toString());
				}
			} catch (Exception e){
				logger.warn("parse port 错误:" + e.getMessage());
			}
			if (port != 0) {
				Proxy p = new Proxy();
				p.setComments(comment);
				p.setIp(ip);
				p.setPort(port);
				p.setFrom("cnProxy");
				proxyList.add(p);
			}
		}
		
		return proxyList;
	}
	
	/**
	 * 取出每个字母代表的数字
	 * @return
	 */
	public Map parseJs() {
		Map result = new HashMap();
		Elements es = doc.getElementsByTag("script");
		Element rightE = null;
		for(Element e : es) {
			String html = e.html();
			for(int i = 0; i < 10; i++) {
				if (!html.contains("=\"" + i + "\"")) {
					break;
				}
				rightE = e;
			}
		}
		
		String html = rightE.html();
		String[] arrays = html.split(";");
		for(String str : arrays) {
			String[] kv = str.split("=");
			if (kv.length != 2) continue;
			
			result.put(kv[0], kv[1].replace("\"", ""));
		}
		return result;
	}
	
	public static void main(String[] args) throws Exception{
		String url = "http://www.cnproxy.com/proxy3.html";
		Document doc = Jsoup.connect(url).get();
		CnProxyParser pp = new CnProxyParser(doc);
		List<Proxy> l = pp.parse();
        for(Proxy p : l) {
            System.out.println(HTTPUtils.verifyProxy(p.getIp(), p.getPort()) + " " + p);
        }
		System.out.println(l);
	}
}
