package cn.bieshao.proxy;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bieshao.model.Proxy;

/**
 * http://51dai.li/http_anonymous.html
 * @author zhenbao.zhou
 *
 */
public class WYDLProxyParser extends AbstractParser {
	private final static Logger logger = Logger.getLogger(WYDLProxyParser.class);


	public WYDLProxyParser() {
	}
	
	public List<Proxy> parse() {
		List<Proxy> proxyList = new ArrayList<Proxy>();
		if (doc == null) {
			return proxyList;
		}

		Elements es = doc.select("div#tb>table");
		Element table = es.first();

		Elements trs = table.getElementsByTag("tr");
		for (Element tr : trs) {
			try {
				Elements tds = tr.getElementsByTag("td");
				if (tds.size() != 4) {
					logger.debug("解释错误了！！, tds的长度不为4 : " + tds.size());
					continue;
				}

				String ip = tds.get(1).text().trim();
				// System.out.println("ip:" + ip);
				String portStr = tds.get(2).text().trim();
				String country = tds.get(3).text().trim();

				int port = Integer.parseInt(portStr);
				
				Proxy p = new Proxy();
				p.setIp(ip);
				p.setPort(port);
				p.setComments(country);
				p.setFrom("51daili");
				proxyList.add(p);
			} catch (Exception e) {
				logger.info("error in WYDLProxyParser: " + e.getMessage());
				e.printStackTrace();
			}
		}

		return proxyList;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://51dai.li/http_non_anonymous.html";
		Document doc = Jsoup.connect(url).get();
		WYDLProxyParser pp = new WYDLProxyParser();
		pp.setDoc(doc);
		List l = pp.parse();
		System.out.println(l.size());
	}
}
