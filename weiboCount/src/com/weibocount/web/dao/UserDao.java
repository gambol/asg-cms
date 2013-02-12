/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.dao;

import com.weibocount.dao.DBTool;
import com.weibocount.model.User;
import com.weibocount.utils.JDBCUtils;

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
