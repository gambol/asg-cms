package cn.bieshao.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

/**
 * 专门从cnproxy来抓取
 * @author zhenbao.zhou
 *
 */
@Deprecated
public class CnProxyCrawler {

	List<String> urlList;
	public static final String urlTmpl = "http://www.cnproxy.com/proxy";
	// "http://www.cnproxy.com/proxy2.html";
	
	private final static Logger logger = Logger.getLogger(CnProxyCrawler.class);
	
	public CnProxyCrawler() {
		urlList = new ArrayList<String>();
		for (int i = 1; i < 11; i++) {
			String url = urlTmpl + i + ".html";
			urlList.add(url);
		}
	}
	
	public void run() {
		List<Proxy> allProxy = new ArrayList<Proxy>();
		for(String url : urlList) {
			Document doc = null;
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (doc == null) {
				logger.warn("error in get this content. url:" + url);
				continue;
			}
			CnProxyParser pp = new CnProxyParser(doc);
			List<Proxy> proxyList = pp.parse();
			allProxy.addAll(proxyList);
		}
		
		logger.info("Proxy Crawler result. size:" + allProxy.size());
		ProxyService.coverAllProxy(allProxy);
	}
	
	public static void main(String[] args) {
		CnProxyCrawler pc = new CnProxyCrawler();
		pc.run();
	}
}
