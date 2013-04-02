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

	private final int MIN_HTML_LENGTH = 1000; //通过代理抓到的最小html长度。。 如果小于这个长度，一般可以认为这个代理有问题
	private final static Logger logger = Logger.getLogger(CommonCrawler.class);

	public CommonCrawler(List<String> urls) {
		urlList = urls;
	}

	public <T extends AbstractParser> void run(final Class<T> _c) {
		List<Proxy> allProxy = new ArrayList<Proxy>();
		List<Proxy> originProxyList = ProxyDao.selectAvailableProxy(false);
		Proxy workingProxy = null;
		for (String url : urlList) {
			Document doc = null;
			try {
			    // 由于有代理网站已经封了我的ip，所以我需要先设置一个代理
				//doc = Jsoup.connect(url).get();
			    String content = null;
			    if (workingProxy == null) {
			        for(Proxy p : originProxyList) {
			            content = HTTPUtils.getContentWithProxy(url, p.getIp(), p.getPort());
			            if (content != null && content.length() > MIN_HTML_LENGTH) {
			                workingProxy = p;
			                break;
			            }
			        }
			        
			        //如果遍历了所有的代理，还是拿不到内容，那就只能使用本机了
	                if (workingProxy == null) {
	                    content = HTTPUtils.getContent(url);
	                    if (content != null) {
	                        // 本机可以用，以后都用本机抓吧
	                        workingProxy = new Proxy();
	                    }
	                }
			    } else {
			        content = HTTPUtils.getContentWithProxy(url, workingProxy.getIp(), workingProxy.getPort());
			    }
			    
			    if (content != null) {
			        doc = Jsoup.parse(content);
			    }
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
		    int proxyType = HTTPUtils.verifyProxy(p.getIp(), p.getPort());
		    switch (proxyType) {
    		    case HTTPUtils.ANONYMOUS_PROXY:
    		    case HTTPUtils.WORKING_PROXY:
    		        p.setProxyType(proxyType);
    		        newProxyList.add(p);
    		        break;
    		    default:
    		        break;
		    }
		}
		
		return newProxyList;
	}
	
	public static void main(String[] args) throws Exception {

		CommonCrawler cc = new CommonCrawler(null);

	}
}
