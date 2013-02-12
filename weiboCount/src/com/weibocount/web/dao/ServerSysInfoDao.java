/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.dao;

import com.weibocount.utils.JDBCUtils;

/**
 *
 * @author zhenbao.zhou
 */
public class ServerSysInfoDao {
    public static int insertServerSysInfo(ServerSysInfoDao serverSysInfo) {
        return JDBCUtils.insert(serverSysInfo);
    }
}
