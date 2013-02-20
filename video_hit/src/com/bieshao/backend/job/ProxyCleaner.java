package com.bieshao.backend.job;

import java.util.List;

import org.apache.log4j.Logger;

import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

/**
 * 扫描proxy表， 将已经失效的proxy至为disabled
 * @author zhenbao.zhou
 *
 */
public class ProxyCleaner implements Runnable{
	private final static Logger logger = Logger.getLogger(ProxyCleaner.class);
	
	public synchronized static void clean() {
		logger.info("start proxy clean");
		List<Proxy> proxyList = ProxyDao.selectAvailableProxy();
		
		for(Proxy p : proxyList) {
			if (!verifyProxy(p)) {
				p.setDisabled(true);
				p.setDisable_time(DateUtil.getCurrentTimestamp());
				JDBCUtils.update(p, false);
				logger.info("set this proxy to disabled. ip: " + p.getIp() + " port:" + p.getPort());
			}
		}
		logger.info("stop proxy clean");
	}
	
	public static boolean verifyProxy(Proxy p) {
		if (p == null) {
			return false;
		}
		
		return  HTTPUtils.verifyProxy(p.getIp(), p.getPort());
	}

		@Override
	public void run() {
//		System.out.println("hehe html");
		try {
			ProxyCleaner.clean();
			//new ChannelAction().allChannelToHTML(context);
			//new ArticleAction().dealNewArticle(context);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		clean();
	}
}
