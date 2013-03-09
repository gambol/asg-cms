package com.bieshao.shua;

public abstract class Shua {
	
	// 刷的次数
	public int num;
	
	// url
	public String url;
	
	// id
	public  int id;
	
	public final static int EVERY_STEP = 30;
	public final static int SLEEP_TIME = 2000;
	/**
	 * 暴露出来的接口
	 */
	public void doJob() {
		
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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
