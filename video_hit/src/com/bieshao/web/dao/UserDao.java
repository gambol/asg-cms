/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.dao;

import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.User;

/**
 *
 * @author xiang.fu
 */
public class UserDao {
    
    public User getUserByEmail(String userEmail){
        String sql = "select * from " + DBTool.getTableName(User.class) + " where username=?";
        return DBTool.queryEntity(User.class, sql, userEmail);
    }
    
    
    public void addUser(User user){
        JDBCUtils.insert(user);
    }
    
    public void udpateUser(User user){
        JDBCUtils.update(user, true);
    }
    
    
}
