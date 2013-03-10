package com.bieshao.model;

import java.io.Serializable;

import cn.bieshao.common.DBColumnName;
import cn.bieshao.common.DBTableName;

/**
 * 各种视频网站的帐号
 * @author zhenbao.zhou
 *
 */

@DBTableName(value="website_account")
public class WebSiteAccount implements Serializable {

    @DBColumnName(value = "id")
    private int id;
    
    @DBColumnName(value = "username")
    private String username;
    @DBColumnName(value = "password")
    private String password; 
    @DBColumnName(value = "web")
    private String web;  //网站的名字
    @DBColumnName(value = "disabled")
    private boolean disabled;
    
    public boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getWeb() {
        return web;
    }
    public void setWeb(String web) {
        this.web = web;
    }
    
    
}
