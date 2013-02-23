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
 * 纯真的代理
 * http://www.cz88.net/proxy/http_10.aspx
 * @author zhenbao.zhou
 *
 */
public class CZProxyParser  extends AbstractParser {
	private final static Logger logger = Logger.getLogger(CZProxyParser.class);


	public CZProxyParser() {
	}
	
	public List<Proxy> parse() {
		List<Proxy> proxyList = new ArrayList<Proxy>();
		if (doc == null) {
			return proxyList;
		}

		Elements es = doc.select("div.Main>table");
		Element table = es.first();

		Elements trs = table.getElementsByTag("tr");
		for (Element tr : trs) {
			try {
				Elements tds = tr.getElementsByTag("td");
				if (tds.size() != 5) {
					logger.debug("解释错误了！！, tds的长度不为5 : " + tds.size());
					continue;
				}

				String ip = tds.get(0).text().trim();
				// System.out.println("ip:" + ip);
				String portStr = tds.get(1).text().trim();
				String country = tds.get(4).text().trim();

				int port = Integer.parseInt(portStr);
				
				Proxy p = new Proxy();
				p.setIp(ip);
				p.setPort(port);
				p.setComments(country);
				p.setFrom("cz88");
				proxyList.add(p);
			} catch (Exception e) {
				logger.info("error in CZproxy: " + e.getMessage());
				e.printStackTrace();
			}
		}

		return proxyList;
	}

	public static void main(String[] args) throws Exception {
		String url = "http://www.cz88.net/proxy/http_10.aspx";
		Document doc = Jsoup.connect(url).get();
		CZProxyParser pp = new CZProxyParser();
		pp.setDoc(doc);
		List l = pp.parse();
		System.out.println(l.size());
	}
}
