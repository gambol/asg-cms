package com.biemian.db.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.biemian.db.common.JDBCUtils;
import com.biemian.db.common.QueryResult;
import com.biemian.db.domain.ToFetchUrl;

public class URLDao {

	public static QueryResult<ToFetchUrl> loadAllToFetchUrl() {
		LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
		orderBy.put("id", true);
		String param = " fetched = false ";
		return JDBCUtils.getScrollData(ToFetchUrl.class, 0, -1, orderBy, param);
	}
	
	public static QueryResult<ToFetchUrl> load20ToFetchUrl() {
		LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
		orderBy.put("id", true);
		String param = " fetch = 'n' ";
		return JDBCUtils.getScrollData(ToFetchUrl.class, -1, 20, orderBy, param);
	}
	
	public static void updateUrl(ToFetchUrl toFetchUrl) {
		JDBCUtils.update(toFetchUrl, false);
	}
	
	public static void insertNewUrl(String url) {
		ToFetchUrl toFetch = new ToFetchUrl();
		toFetch.setUrl(url);
		
		JDBCUtils.insert(toFetch);
	}
	
	public static void main(String[] args) {
		QueryResult<ToFetchUrl> qr = loadAllToFetchUrl();
		System.out.println(qr.getRecordtotal());
	//	System.out.println(qr.getResultlist().get(0).getUrl());
	}
	
}
