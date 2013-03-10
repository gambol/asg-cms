package com.bieshao.web.dao;

import java.util.LinkedHashMap;
import java.util.List;

import cn.bieshao.common.PageResult;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.WebSiteAccount;

public class WebAccountDao {

    public static List<WebSiteAccount> queryTudouAccount() {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        PageResult<WebSiteAccount> pr = JDBCUtils.getPageData(WebSiteAccount.class, 10000, 1, orderBy, " web = 'tudou' and disabled = false");
        return pr.getPageList();
    }
}
