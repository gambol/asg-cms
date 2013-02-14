package com.bieshao.shua;

public abstract class Shua {
	
	// 刷的次数
	public int times;
	
	// url
	public String url;
	
	// id
	public  int id;
	
	public void generateShuaUrl() throws Exception {
		
	}
	
	public void shua() throws Exception{
		
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
