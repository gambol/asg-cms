package com.tophey.model;

import com.tophey.common.DBColumnName;
import com.tophey.common.DBTableName;
import java.io.Serializable;
import java.sql.Timestamp;

import com.tophey.utils.DateUtil;
  
@DBTableName(value = "server_info")
public class ServerInfo implements Serializable{

    /**
     * 增加一些默认值，免得填错
     */
    public ServerInfo() {
        this.updateDate = DateUtil.getCurrentTimestamp();
        this.publishTime = DateUtil.getCurrentTimestamp();
        this.createDate = DateUtil.getCurrentTimestamp();
        this.addTime = DateUtil.getCurrentTimestamp();
        this.checkResult = 1;  // 暂时不需要人工审核
    }
    
    @DBColumnName(value = "id")
    private int id;
    @DBColumnName(value = "name")
    private String name;
    @DBColumnName(value = "line")
    private String line;
    @DBColumnName(value = "description")
    private String description;
    
    @DBColumnName(value = "summary")
    private String summary;
    
    @DBColumnName(value = "url")
    private String url;
    
    /**
     * 字段和name冲突,暂时废弃这个字段
     * @deprecated 
     **/
    @DBColumnName(value = "title")
    private String title;
    @DBColumnName(value = "banner_url")
    private String bannerUrl;
    @DBColumnName(value = "category_id")
    private int categoryId;
    @DBColumnName(value = "create_date")
    private Timestamp createDate;
    @DBColumnName(value = "update_date")
    private Timestamp updateDate;
    @DBColumnName(value = "is_disabled")
    private int isDisabled;
    @DBColumnName(value = "disable_date")
    private Timestamp disableDate;
    @DBColumnName(value = "disable_reason")
    private String disableReason;
    @DBColumnName(value = "user_id")
    private int userId;
    @DBColumnName(value = "add_time")
    private Timestamp addTime;
    // 审核结果。
    @DBColumnName(value = "check_result")
    private int checkResult;
    // 审核时间
    @DBColumnName(value = "check_time")
    private Timestamp checkTime;
    @DBColumnName(value = "publish_time")
    private Timestamp publishTime;

    @DBColumnName(value = "site_from")
    private String siteFrom;

    @DBColumnName(value = "status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getSiteFrom() {
        return siteFrom;
    }

    public void setSiteFrom(String siteFrom) {
        this.siteFrom = siteFrom;
    }
      
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public int getIsDisable() {
        return isDisabled;
    }

    public void setIsDisable(int isDisable) {
        this.isDisabled = isDisable;
    }

    public Timestamp getDisableDate() {
        return disableDate;
    }

    public void setDisableDate(Timestamp disableDate) {
        this.disableDate = disableDate;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    public int getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(int checkResult) {
        this.checkResult = checkResult;
    }

    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    public int getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(int isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
        this.publishTime = publishTime;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    
}
