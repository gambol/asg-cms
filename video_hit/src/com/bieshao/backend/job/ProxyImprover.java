package com.bieshao.backend.job;

import java.util.List;

import org.apache.log4j.Logger;

import cn.bieshao.proxy.CrawlerMain;
import cn.bieshao.proxy.ProxyHandler;
import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

/**
 * 
 * 1, 扫描proxy表， 将已经失效的proxy至为disabled 2, 抓取proxy url， 发现新的proxy 3, 更新proxyMap
 * 
 * @author zhenbao.zhou
 * 
 */
public class ProxyImprover implements Runnable {

	private final static Logger logger = Logger.getLogger(ProxyImprover.class);

	public synchronized static void clean() {
		logger.info("start proxy clean");
		List<Proxy> proxyList = ProxyDao.selectAvailableProxy();

		for (Proxy p : proxyList) {
            int proxyType = HTTPUtils.verifyProxy(p.getIp(), p.getPort());

            switch (proxyType) {
            case HTTPUtils.ANONYMOUS_PROXY:
            case HTTPUtils.WORKING_PROXY:
                if (p.isDisabled() || p.getProxyType() != proxyType) {
                    p.setDisabled(false);
                    p.setProxyType(proxyType);
                    JDBCUtils.update(p, false);
                }
                break;
            default:
                if (!p.isDisabled() || p.getProxyType() != proxyType) {
                    p.setDisabled(true);
                    p.setProxyType(proxyType);
                    JDBCUtils.update(p, false);
                }
                break;
            }
		}
		logger.info("stop proxy clean");
	}

	/**
	 * 抓新的url
	 */
	public synchronized static void searchNew() {
		CrawlerMain cm = new CrawlerMain();
		
		cm.cnproxyCrawl();
		cm.xiziCrawl();
		cm.czproxyCrawl();
		cm.wydlProxyCrawl();
	}

	@Override
	public void run() {
		// System.out.println("hehe html");
		try {
			ProxyImprover.clean();
			ProxyImprover.searchNew();
			ProxyHandler.reloadProxy();
			// new ChannelAction().allChannelToHTML(context);
			// new ArticleAction().dealNewArticle(context);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		clean();
//		searchNew();
	}
}
