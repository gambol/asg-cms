package cn.bieshao.proxy;

import java.util.List;

import org.apache.log4j.Logger;

import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

public class ProxyService {
	private final static Logger logger = Logger.getLogger(ProxyService.class);
	/**
	 * 更新这个proxy的类型 和 type
	 * 暂时同时保留 proxy_type 和 disabled 2个状态，慢慢改成只用proxy_type.
	 * add comment in 2013-3-23
	 * @param pl
	 */
	public static void coverAllProxy(List<Proxy> pl) {
		//DBTool.execute("update proxy set disabled='true', disable_time = now()");
		int i = 0;
		for(Proxy p : pl) {
		    int proxyType = HTTPUtils.verifyProxy(p.getIp(), p.getPort());
		    
		    List<Proxy> proxyList = ProxyDao.selectProxy(p.getIp(), p.getPort()).getPageList();
		    
			if (proxyList.size() == 0) {
			    // 这个代理之前不存在
			     switch (proxyType) {
	                case HTTPUtils.ANONYMOUS_PROXY:
	                case HTTPUtils.WORKING_PROXY:
	                    p.setProxyType(proxyType);
	                    p.setDisabled(false);
	                    logger.info("insert proxy:" + p.getIp() + ":" + p.getPort() + " proxy_type:" + proxyType);
	                    ProxyDao.insertProxy(p);
	                    i++;
	                    break;
	                default:
	                    logger.debug("error in verify. ip:" + p.getIp() + " port:" + p.getPort());
	                    break;
	            }
			} else {
			    // 已经存在过的
			    Proxy newProxy = proxyList.get(0);
			    
			    switch (proxyType) {
                case HTTPUtils.ANONYMOUS_PROXY:
                case HTTPUtils.WORKING_PROXY:
                    newProxy.setProxyType(proxyType);
                    if (newProxy.isDisabled() || newProxy.getProxyType() != proxyType) {
                        newProxy.setDisabled(false);
                        logger.info("update proxy:" + p.getIp() + ":" + p.getPort() + " proxy type:" + proxyType);
                        JDBCUtils.update(newProxy, false);
                        i++;
                    }
                    break;
                default:
                    logger.debug("error in verify. ip:" + p.getIp() + " port:" + p.getPort());
                    if (!newProxy.isDisabled() || newProxy.getProxyType() != proxyType) {
                        newProxy.setDisabled(true);
                        newProxy.setProxyType(proxyType);
                        JDBCUtils.update(newProxy, false);
                    }
                    break;
			    }
			}
		}
		logger.info("we has " + pl.size() +" proxies, total insert:" + i + " proxies");
	}
}
