package com.bieshao.web.dao;

import java.util.LinkedHashMap;
import java.util.List;

import cn.bieshao.common.PageResult;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Comment;

public class CommentDao {

    public static List<Comment> queryAllComment() {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        PageResult<Comment> pr = JDBCUtils.getPageData(Comment.class, 10000, 1, orderBy, " disabled = false");
        return pr.getPageList();
    }
}
