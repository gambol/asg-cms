package com.biemian.db.domain;

import java.io.Serializable;
import java.util.Date;

import com.biemian.db.common.DBColumnName;
import com.biemian.db.common.DBTableName;

@DBTableName(value = "j2_article")
public class Article implements Serializable {
	
	@DBColumnName(value = "id")
	private Integer id;// 文章id
	@DBColumnName(value = "title")
	private String title; // 文章标题
	@DBColumnName(value = "title_img")
	private String titleImg; // 文章标题缩略图
	@DBColumnName(value="content_img")
	private String contentImg; // 文章内容缩略图
	@DBColumnName(value="author")
	private String author; // 作者
	@DBColumnName(value="origin")
	private String origin; // 来源
	@DBColumnName(value="tags")
	private String tags; // Tag标签
	@DBColumnName(value="summary")
	private String summary; // 文章摘要
	@DBColumnName(value="content")
	private String content; // 文章内容
	@DBColumnName(value="sys_date")
	private Date sysDate = new Date(); //（系统日期）
	@DBColumnName(value="release_date")
	private String releaseDate; // 发表日期（可以人为设置）
	@DBColumnName(value="check_date")
	private Date checkDate; // 审核通过日期
	@DBColumnName(value="disable_date")
	private Date disableDate; // 禁用日期
	@DBColumnName(value="visit_total")
	private Integer visitTotal=0; // 总共访问次数
	@DBColumnName(value="recommend_level")
	private Integer recommendLevel; // 推荐级别
	@DBColumnName(value="comment_count")
	private Integer commentCount=0; // 评论数量
	@DBColumnName(value="related_id")
	private String relatedID; // 相关文章ID
	@DBColumnName(value="comment_state")
	private Boolean commentState ; // 是否允许评论
	@DBColumnName(value="check_state")
	private int checkState; //状态
	@DBColumnName(value = "group_id")
	private int group_id;// 允许浏览会员组
	@DBColumnName(value = "channel_id")
	private int channelId; // 栏目ID
	@DBColumnName(value = "user_id")
	private int userId; // 发布者
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleImg() {
		return titleImg;
	}
	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}
	public String getContentImg() {
		return contentImg;
	}
	public void setContentImg(String contentImg) {
		this.contentImg = contentImg;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getSysDate() {
		return sysDate;
	}
	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public Date getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	public Date getDisableDate() {
		return disableDate;
	}
	public void setDisableDate(Date disableDate) {
		this.disableDate = disableDate;
	}
	public Integer getVisitTotal() {
		return visitTotal;
	}
	public void setVisitTotal(Integer visitTotal) {
		this.visitTotal = visitTotal;
	}
	public Integer getRecommendLevel() {
		return recommendLevel;
	}
	public void setRecommendLevel(Integer recommendLevel) {
		this.recommendLevel = recommendLevel;
	}
	public Integer getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}
	public String getRelatedID() {
		return relatedID;
	}
	public void setRelatedID(String relatedID) {
		this.relatedID = relatedID;
	}
	public Boolean getCommentState() {
		return commentState;
	}
	public void setCommentState(Boolean commentState) {
		this.commentState = commentState;
	}
	public int getCheckState() {
		return checkState;
	}
	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}
	public int getGroup_id() {
		return group_id;
	}
	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	
}
