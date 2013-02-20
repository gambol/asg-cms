package com.bieshao.model;

import cn.bieshao.common.DBColumnName;
import cn.bieshao.common.DBTableName;

import java.io.Serializable;
import java.sql.Timestamp;

@DBTableName(value="todo_job")
public class TodoJob implements Serializable {

    @DBColumnName(value="id")
    private int id;
    @DBColumnName(value="url")
    private String url;
    @DBColumnName(value="add_time")
    private Timestamp addTime;
    @DBColumnName(value="valid")
    private boolean valid = true;
    @DBColumnName("num")
    private int num;
    @DBColumnName("deal_time")
    private Timestamp dealTime;
    @DBColumnName("user_ip")
    private String userIp;
    @DBColumnName("user_name")
    private String userName;
    
    // 处理时使用的class名字
    @DBColumnName("job_class")
    private String jobClass;
    
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

	public Timestamp getAddTime() {
		return addTime;
	}

	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public Timestamp getDealTime() {
		return dealTime;
	}

	public void setDealTime(Timestamp dealTime) {
		this.dealTime = dealTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getJobClass() {
		return jobClass;
	}

	public void setJobClass(String jobClass) {
		this.jobClass = jobClass;
	}
    
}
