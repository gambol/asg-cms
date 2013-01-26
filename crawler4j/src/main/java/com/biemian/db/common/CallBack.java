/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biemian.db.common;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author zhenbao.zhou
 */
public interface CallBack {

    /**
     * 回调方法
     *
     * @throws SQLException
     */
    public void callback(ResultSet rs) throws SQLException;
}