package com.weibocount.model;

import com.weibocount.common.DBColumnName;
import com.weibocount.common.DBTableName;

import java.io.Serializable;
import java.sql.Timestamp;


@DBTableName(value="server_sys_info")
public class ServerSysInfo implements Serializable{

    @DBColumnName(value = "id")
    private int id;
    @DBColumnName(value = "name")
    private String name;
    @DBColumnName(value = "category_id")
    private int categoryId;
    @DBColumnName(value = "refresh_date")
    private Timestamp refreshDate;
    @DBColumnName(value = "score")
    private double score;
    @DBColumnName(value = "ping")
    private int ping;
    @DBColumnName(value = "server_num")
    private int serverNum;
    @DBColumnName(value = "server_create_time")
    private Timestamp serverCreateTime;
    @DBColumnName(value = "server_new_open_time")
    private Timestamp serverNewOpenTime;
    @DBColumnName(value = "vote_in")
    private int voteIn;
    @DBColumnName(value = "vote_out")
    private int voteOut;
    @DBColumnName(value = "privilege")
    private int privilege;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Timestamp refreshDate) {
        this.refreshDate = refreshDate;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getServerNum() {
        return serverNum;
    }

    public void setServerNum(int serverNum) {
        this.serverNum = serverNum;
    }

    public Timestamp getServerCreateTime() {
        return serverCreateTime;
    }

    public void setServerCreateTime(Timestamp serverCreateTime) {
        this.serverCreateTime = serverCreateTime;
    }

    public Timestamp getServerNewOpenTime() {
        return serverNewOpenTime;
    }

    public void setServerNewOpenTime(Timestamp serverNewOpenTime) {
        this.serverNewOpenTime = serverNewOpenTime;
    }

    public int getVoteIn() {
        return voteIn;
    }

    public void setVoteIn(int voteIn) {
        this.voteIn = voteIn;
    }

    public int getVoteOut() {
        return voteOut;
    }

    public void setVoteOut(int voteOut) {
        this.voteOut = voteOut;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }
}
