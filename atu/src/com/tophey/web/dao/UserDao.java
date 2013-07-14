/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.dao;

import com.tophey.dao.DBTool;
import com.tophey.model.User;
import com.tophey.utils.JDBCUtils;

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
