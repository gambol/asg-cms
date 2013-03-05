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
	 * 判断这个host之前是否已经存在， 如果已经存在，那么就不需要插入。
	 * @param pl
	 */
	public static void coverAllProxy(List<Proxy> pl) {
		//DBTool.execute("update proxy set disabled='true', disable_time = now()");
		int i = 0;
		for(Proxy p : pl) {
		    boolean isDisable = !HTTPUtils.verifyProxy(p.getIp(), p.getPort());
		    
		    List<Proxy> proxyList = ProxyDao.selectProxy(p.getIp(), p.getPort()).getPageList();
		    
			if (proxyList.size() == 0) {
				if (isDisable) {
					logger.info("insert proxy:" + p.getIp() + ":" + p.getPort());
					ProxyDao.insertProxy(p);
					i++;
				} else {
					logger.debug("error in verify. ip:" + p.getIp() + " port:" + p.getPort());
				}
			} else {
			    Proxy newProxy = proxyList.get(0);
			    if (isDisable != newProxy.isDisabled()) {
                    logger.info("update proxy:" + p.getIp() + ":" + p.getPort() + " disabled status:" + isDisable);
                    newProxy.setDisabled(isDisable);
                    JDBCUtils.update(newProxy, false);
                    i++;
                } else {
                    logger.debug("the same with verify result");
                }
			}
		}
		logger.info("we has " + pl.size() +" proxies, total insert:" + i + " proxies");
	}
}
