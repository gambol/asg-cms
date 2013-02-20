package com.bieshao.web.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;

import cn.bieshao.common.PageResult;
import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;

public class ProxyDao {

	private final static Logger logger = Logger.getLogger(ProxyDao.class);
	
	public static int insertProxy(Proxy p) {
        return JDBCUtils.insert(p);
    }
	
	/**
	 * 选出所有可以使用的proxy
	 * @return
	 */
	public static List<Proxy> selectAvailableProxy() {
		LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
		PageResult<Proxy> pr = JDBCUtils.getPageData(Proxy.class, 10000, 1, orderBy, " disabled = false");
		return pr.getPageList();
	}
	
	public static int selectProxyNum(Proxy p) {
		return JDBCUtils.getCount(Proxy.class, "ip = ? and port = ?", p.getIp(), p.getPort());
	}
	
	/**
	 * 判断这个host之前是否已经存在， 如果已经存在，那么就不需要插入。
	 * 如果数据多了之后，可以改成将原来的都删除
	 * @param pl
	 */
	public static void coverAllProxy(List<Proxy> pl) {
		//DBTool.execute("update proxy set disabled='true', disable_time = now()");
		int i = 0;
		for(Proxy p : pl) {
			if (selectProxyNum(p) == 0) {
				logger.info("insert proxy:" + p.getIp() + ":" + p.getPort());
				insertProxy(p);
				i++;
			}
		}
		logger.info("total insert:" + i + " proxies");
	}
	
	
}
