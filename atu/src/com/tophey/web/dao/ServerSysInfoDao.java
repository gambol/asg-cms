/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.dao;

import com.tophey.utils.JDBCUtils;

/**
 *
 * @author zhenbao.zhou
 */
public class ServerSysInfoDao {
    public static int insertServerSysInfo(ServerSysInfoDao serverSysInfo) {
        return JDBCUtils.insert(serverSysInfo);
    }
}
