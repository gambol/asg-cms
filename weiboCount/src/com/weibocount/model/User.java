package com.weibocount.model;

import com.weibocount.common.DBColumnName;
import com.weibocount.common.DBTableName;

import java.io.Serializable;
import java.sql.Timestamp;
@DBTableName(value="user")
public class User implements Serializable {

    @DBColumnName(value = "id")
    private int id;
    @DBColumnName(value = "email")
    private String email;
    @DBColumnName(value = "password")
    private String password;
    @DBColumnName(value = "username")
    private String username;
    @DBColumnName(value = "info")
    private String info;
    @DBColumnName(value = "zipCode")
    private String zipCode;
    @DBColumnName(value = "group_id")
    private int groupId;
    @DBColumnName(value = "ip")
    private String ip;
    @DBColumnName(value = "create_date")
    private Timestamp createDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
}
