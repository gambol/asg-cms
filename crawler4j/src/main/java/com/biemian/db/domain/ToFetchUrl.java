package com.biemian.db.domain;

import com.biemian.db.common.DBColumnName;
import com.biemian.db.common.DBTableName;

@DBTableName(value = "to_fetch_url")
public class ToFetchUrl {

	@DBColumnName(value = "id")
	private int id;
	@DBColumnName(value = "url")
	private String url;
	
	@DBColumnName(value = "fetched")
	private boolean fetched;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isFetched() {
		return fetched;
	}
	public void setFetched(boolean fetched) {
		this.fetched = fetched;
	}
	
	
}
