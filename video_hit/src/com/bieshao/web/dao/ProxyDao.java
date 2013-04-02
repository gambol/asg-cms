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
	public static List<Proxy> selectAvailableProxy(boolean needAnomous) {
		LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
		String param = " disabled = false";
		if (needAnomous) {
		    param = " proxy_type = 2";
		}
		PageResult<Proxy> pr = JDBCUtils.getPageData(Proxy.class, 10000, 1, orderBy, param);

		return pr.getPageList();
	}
	
	
	public static int selectProxyNum(Proxy p) {
		return JDBCUtils.getCount(Proxy.class, "ip = ? and port = ?", p.getIp(), p.getPort());
	}
	
	public static PageResult<Proxy> selectProxy(String ip, int port) {
        return JDBCUtils.getPageData(Proxy.class, 10, 1, "ip = ? and port = ?", ip, port);
    }
	
}
