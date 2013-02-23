package com.bieshao.model;

import cn.bieshao.common.DBColumnName;
import cn.bieshao.common.DBTableName;

import java.io.Serializable;
import java.sql.Timestamp;
@DBTableName(value="proxy")
public class Proxy implements Serializable {

    @DBColumnName(value = "id")
    private int id;
    @DBColumnName(value = "ip")
    private String ip;
    @DBColumnName(value = "port")
    private int port;
    @DBColumnName(value = "disabled")
    private boolean disabled;
    @DBColumnName(value = "ping")
    private int ping;
    @DBColumnName(value = "add_time")
    private Timestamp addTime;
    @DBColumnName(value = "disable_time")
    private Timestamp disable_time;
    @DBColumnName(value = "comments")
    private String comments;
    
    @DBColumnName(value = "from_source")
    private String from;
    
	public int getId() {
		return id;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public int getPing() {
		return ping;
	}
	public void setPing(int ping) {
		this.ping = ping;
	}
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	public Timestamp getDisable_time() {
		return disable_time;
	}
	public void setDisable_time(Timestamp disable_time) {
		this.disable_time = disable_time;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public String toString() {
		return ip + ":" + port;
	}
   
	public static void main(String[] args) {
		Proxy p = new Proxy();
		p.setIp("asdf");
		System.out.println(p.hashCode());
	}
}
