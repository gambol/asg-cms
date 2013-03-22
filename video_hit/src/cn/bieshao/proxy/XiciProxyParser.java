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
 * xici的parser
 * http://www.xici.net.co/wn
 * @author zhenbao.zhou
 *
 */
public class XiciProxyParser extends AbstractParser {
	private final static Logger logger = Logger.getLogger(XiciProxyParser.class);


	public XiciProxyParser() {
	}
	
	public List<Proxy> parse() {
		List<Proxy> proxyList = new ArrayList<Proxy>();
		if (doc == null) {
			return proxyList;
		}

		Elements es = doc.select("table#ip_list");
		Element table = es.first();

		Elements trs = table.getElementsByTag("tr");
		for (Element tr : trs) {
			try {
				Elements tds = tr.getElementsByTag("td");
				if (tds.size() == 0) {
					continue;
				}
				
				if (tds.size() != 9) {
					logger.warn("解释错误了！！, tds的长度不为9 : " + tds.size());
					continue;
				}

				String ip = tds.get(1).text().trim();
				// System.out.println("ip:" + ip);
				String portStr = tds.get(2).text().trim();
				String type = tds.get(5).text().trim();
				String country = tds.get(3).text().trim();
				if (!type.contains("HTTP")) {
					continue;
				}

				int port = Integer.parseInt(portStr);
				Element speedE = tds.get(6);

				Element selectDiv = speedE.select("div.bar").first();
				String speedStr = selectDiv.attr("title");
				String[] tmpArray = speedStr.split("秒");
				double speed = Double.parseDouble(tmpArray[0]);
				if (speed > 7) {
					logger.debug("speed too large, skip it. speed:" + speed);
					continue;
				}
				
				Proxy p = new Proxy();
				p.setIp(ip);
				p.setPort(port);
				p.setComments(country + " " +speedStr);
				p.setFrom("xici");
				proxyList.add(p);
			} catch (Exception e) {
				logger.info("error in XiciProxy: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return proxyList;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://www.xici.net.co/wn/55";
		Document doc = Jsoup.connect(url).get();
		XiciProxyParser pp = new XiciProxyParser();
		pp.setDoc(doc);
		List<Proxy> l = pp.parse();
		for(Proxy p : l) {
		    System.out.println(HTTPUtils.verifyProxy(p.getIp(), p.getPort()) + " " + p);
		}
		System.out.println(l.size());
	}
}
