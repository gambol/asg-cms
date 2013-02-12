/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.dao;

import com.weibocount.common.PathConst;
import com.weibocount.utils.ConfigUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author xiang.fu
 */
public class ConnectionFactory {

    private static final String KEY_DB_URL = "KEY_DB_URL";
    private static final String DEFAULT_DB_URL =
            "jdbc:mysql://localhost/tophey?useUnicode=true&characterEncoding=utf-8&autoReconnect=true";
    private static final String KEY_DB_USERNAME = "KEY_DB_USERNAME";
    private static final String DEFAULT_DB_USERNAME = "gambol";
    private static final String KEY_DB_PASSWORD = "KEY_DB_PASSWORD";
    private static final String DEFAULT_DB_PASSWORD = "";
    private static final String KEY_DB_USE_POOL = "KEY_DB_USE_POOL";
    private static final String DEFAULT_DB_USE_POOL = "false";
    private static final String DEFAULT_POOL_NAME = "jdbc/tophey";
    private static Log log = LogFactory.getLog(ConnectionFactory.class);
    private static final String KEY_POOL_NAME = "KEY_POOL_NAME";
    private static ConnectionFactory instance = new ConnectionFactory();
    private Properties prop;
    private String username;
    private String password;
    private boolean usePool;
    private DataSource pool;
    private String url;
    private String poolname;

    private ConnectionFactory() {
        try {
            prop = ConfigUtils.loadProp(PathConst.DB_CONFIG_PATH);
        } catch (IOException ex) {
            ex.printStackTrace();
            log.warn("Get db config failed! try to init db.xml",ex);
            initDefaultProp();
        }

        username = prop.getProperty(KEY_DB_USERNAME);
        password = prop.getProperty(KEY_DB_PASSWORD);
        url = prop.getProperty(KEY_DB_URL);
        usePool = Boolean.parseBoolean(prop.getProperty(KEY_DB_USE_POOL));
        poolname = prop.getProperty(KEY_POOL_NAME);

        System.out.println("ur:" + url);

        if (usePool) {
            try {
                pool = (DataSource) new InitialContext().lookup(poolname);
                if (pool == null) {
                    log.warn("Connection pool is NULL");
                } else {
                    log.info("connection pool looked up");
                }
            } catch (NamingException ex) {
                log.warn("Failed to look up connection pool from context",
                        ex);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (usePool) {
            return pool.getConnection();
        } else {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                return DriverManager.getConnection(url, username, password);
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                log.warn("Cannot load driver class", ex);

                throw new SQLException("Cannot load driver class", ex);
            }
        }
    }

    public static ConnectionFactory getInstance() {
        return instance;
    }

    private void initDefaultProp() {
        prop = new Properties();
        prop.setProperty(KEY_DB_URL, DEFAULT_DB_URL);
        prop.setProperty(KEY_DB_USERNAME, DEFAULT_DB_USERNAME);
        prop.setProperty(KEY_DB_PASSWORD, DEFAULT_DB_PASSWORD);
        prop.setProperty(KEY_DB_USE_POOL, DEFAULT_DB_USE_POOL);
        prop.setProperty(KEY_POOL_NAME, DEFAULT_POOL_NAME);


        try {


            ConfigUtils.saveProp(prop,
                    PathConst.DB_CONFIG_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Cannot create db.xml", e);
        }
    }
}