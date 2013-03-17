package com.bieshao.web.dao;

import java.util.LinkedHashMap;
import java.util.List;

import cn.bieshao.common.PageResult;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.FetchListUrl;

public class FetchListUrlDao {

    public static List<FetchListUrl> queryTudouList() {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        PageResult<FetchListUrl> pr = JDBCUtils.getPageData(FetchListUrl.class, 10000, 1, orderBy, " web = 'tudou' and disabled = false");
        return pr.getPageList();
    }
    
    public static List<FetchListUrl> queryList(String webName) {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        PageResult<FetchListUrl> pr = JDBCUtils.getPageData(FetchListUrl.class, 10000, 1, orderBy, " web = '" + webName.trim()
                +  "' and disabled = false");
        return pr.getPageList();
    }
}
