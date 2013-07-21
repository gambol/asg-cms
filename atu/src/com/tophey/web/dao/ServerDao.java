/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.dao;

import com.tophey.dao.DBTool;
import com.tophey.model.ServerInfo;
import java.util.List;
import com.tophey.utils.DateUtil;
import com.tophey.utils.JDBCUtils;

/**
 * ServerInfo相关的更新和插入
 * @author root
 */
public class ServerDao {
    
    public static int insert(ServerInfo si) {
        return JDBCUtils.insert(si);
    }
    
    public static int delete(ServerInfo si) {
        return JDBCUtils.delete(si);
    }
    
    public static ServerInfo getServerInfoById(int id) {
        return new ServerQuerier().getServerInfoById(id);
    }
    
    public static int update(ServerInfo si) {
        return JDBCUtils.update(si, false);
    }
    
    /*
     * 更改某个server的显示/隐藏状态
     * 如果是online 则修改成offline
     * 如果是offline 则修改成online
     * 
     * 如果是其他状态,不改变
     */
    public static int changeDisplayStatus(int serverId) {
        String sql = "update server_info set status = (case status  when  'online' then "
                + "'hidden' when 'hidden' then 'online' else status end) where id = ?";
        return DBTool.execute(sql, serverId);
    }
    
    /*
     * 更改某个server的显示/隐藏状态
     * 如果是online 则修改成offline
     * 如果是offline 则修改成online
     * 
     * 如果是其他状态,不改变
     */
    public static int changeDisplayStatus(int serverId, int userId) {
        String sql = "update server_info set status = (case status  when  'online' then "
                + "'offline' when 'offline' then 'online' else status end) where id = ? and user_id = ?";
        return DBTool.execute(sql, serverId, userId);
    }
    
    public static void main(String[] args) {
        ServerInfo si = new ServerInfo();
        si.setName("gambol test");
        si.setLine("双线");
        si.setCategoryId(1);
        si.setDescription("哈哈 这是我的小测试");
        si.setUrl("http://www.atugame.com");
        si.setCreateDate(DateUtil.getCurrentTimestamp());
        si.setUpdateDate(DateUtil.getCurrentTimestamp());
        si.setUserId(1);
        System.out.println(ServerDao.insert(si));
        
        System.out.println(ServerDao.delete(si));
    }
}
