package cn.bieshao.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import cn.bieshao.utils.HTTPUtils;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

public class CommonCrawler {
	List<String> urlList;
//	public static final String urlTmpl = "http://www.cnproxy.com/proxy";
	// "http://www.cnproxy.com/proxy2.html";

	private final static Logger logger = Logger.getLogger(CommonCrawler.class);

	public CommonCrawler(List<String> urls) {
		urlList = urls;
	}

	public <T extends AbstractParser> void run(final Class<T> _c) {
		List<Proxy> allProxy = new ArrayList<Proxy>();
		for (String url : urlList) {
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
			try {
				T pp = _c.newInstance();
				pp.setUrlList(urlList);
				pp.setDoc(doc);
				List<Proxy> proxyList = pp.parse();
				allProxy.addAll(proxyList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		logger.info("Proxy Crawler result. size:" + allProxy.size());
//		List rightProxy = pruneProxy(allProxy);
//		logger.info("after prune, proxy Crawler result. size:" + rightProxy.size());
		
		ProxyService.coverAllProxy(allProxy);
	}
	
	/**
	 * 验证proxyList里的proxy，将可以正常使用的proxy返回
	 * @param proxyList
	 * @return
	 */
	public List<Proxy> pruneProxy(List<Proxy> proxyList) {
		List<Proxy> newProxyList = new ArrayList<Proxy>();
		for(Proxy p : proxyList) {
			if (HTTPUtils.verifyProxy(p.getIp(), p.getPort())) {
				newProxyList.add(p);
			}
		}
		
		return newProxyList;
	}
	
	public static void main(String[] args) throws Exception {

		CommonCrawler cc = new CommonCrawler(null);

	}
}
