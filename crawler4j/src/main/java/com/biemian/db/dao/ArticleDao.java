package com.biemian.db.dao;

import com.biemian.db.common.JDBCUtils;
import com.biemian.db.domain.Article;

public class ArticleDao {

	static int userId = 1;
	static String author = "管理员";
	static int checkState = 2;
	static String origin = "网络";
	static int groupId = 1;
	
	public static void insertArticle(Article article) {
		JDBCUtils.insert(article);
	}
	
	public static void insert(String title, String content, String summary,
			String publishTime, int channelId, String origin) {
		Article ar = new Article();
		ar.setTitle(title);
		ar.setContent(content);
		if (publishTime != null && publishTime.length() > 10) {
			publishTime = publishTime.substring(0, 9);
		}
		
		ar.setReleaseDate(publishTime);
		ar.setChannelId(channelId);
		ar.setOrigin(origin);
		ar.setGroup_id(groupId);
		ar.setUserId(userId);
		ar.setCheckState(checkState);
		ar.setAuthor(author);
		
		insertArticle(ar);
	}
	
	public static void insert(String title, String content, String summary,
			String publishTime, int channelId) {
		insert(title, content, summary, publishTime, channelId, origin);
	}
}
