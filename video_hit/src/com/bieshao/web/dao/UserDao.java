/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.dao;

import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;

/**
 *
 * @author xiang.fu
 */
public class UserDao {
    
    public Proxy getUserByEmail(String userEmail){
        String sql = "select * from " + DBTool.getTableName(Proxy.class) + " where username=?";
        return DBTool.queryEntity(Proxy.class, sql, userEmail);
    }
    
    
    public void addUser(Proxy user){
        JDBCUtils.insert(user);
    }
    
    public void udpateUser(Proxy user){
        JDBCUtils.update(user, true);
    }
    
    
}
