/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.dao;

import java.util.LinkedHashMap;

import cn.bieshao.common.PageResult;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Category;
import com.bieshao.model.ServerInfo;

/**
 *
 * @author zhenbao.zhou
 */
public class CategoryDao {
    
    /**
     * 列举所有的私服分类
     * display_order 越大,排在月前面
     * 按照display 逆序, id 顺序
     * @return 
     */
      public static PageResult<Category> getAllCategory() {
       LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
       orderBy.put("display_order", true);
       orderBy.put("id", false);
       PageResult<Category> pr = JDBCUtils.getPageData(Category.class, 200, 1, orderBy, " is_disabled = 0");
       return pr;   
    }
}
