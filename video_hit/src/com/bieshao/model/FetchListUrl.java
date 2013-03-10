package com.bieshao.model;

import java.io.Serializable;

import cn.bieshao.common.DBColumnName;
import cn.bieshao.common.DBTableName;

/**
 * 获取视频url的 列表页url
 * @author zhenbao.zhou
 *
 */

@DBTableName(value="fetch_list_url")
public class FetchListUrl implements Serializable {

    @DBColumnName(value = "id")
    private int id;

    @DBColumnName(value = "url")
    private String url;

    @DBColumnName(value = "disabled")
    private boolean disabled;
    
    @DBColumnName(value = "web")
    private String web;  //网站的名字

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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }
}
