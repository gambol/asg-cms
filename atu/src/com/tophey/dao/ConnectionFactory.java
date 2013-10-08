/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.dao;


import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.tophey.common.PathConst;
import com.tophey.utils.ConfigUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.log4j.Logger;
 

/**
 *
 * @author xiang.fu
 */
public class ConnectionFactory {

    private static final String KEY_DB_URL = "KEY_DB_URL";
    private static final String DEFAULT_DB_URL =
            "jdbc:mysql://localhost/tophey?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&zeroDateTimeBehavior=convertToNull";
    private static final String KEY_DB_USERNAME = "KEY_DB_USERNAME";
    private static final String DEFAULT_DB_USERNAME = "gambol";
    private static final String KEY_DB_PASSWORD = "KEY_DB_PASSWORD";
    private static final String DEFAULT_DB_PASSWORD = "121212";
    private static final String KEY_DB_USE_POOL = "KEY_DB_USE_POOL";
    private static final String DEFAULT_DB_USE_POOL = "false";
    private static final String DEFAULT_POOL_NAME = "jdbc/tophey";
    private static Logger log = Logger.getLogger(ConnectionFactory.class); 
    private static final String KEY_POOL_NAME = "KEY_POOL_NAME";
    private static ConnectionFactory instance = new ConnectionFactory();
    private Properties prop;
    private String username;
    private String password;
    private boolean usePool;
    private ComboPooledDataSource ds;
    private String url;

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

        System.out.println("ur:" + url);

        if (usePool) {
            try {
                
                ds = new ComboPooledDataSource();

                ds.setDriverClass("com.mysql.jdbc.Driver");
                ds.setJdbcUrl(url);
                ds.setUser(username);
                if (password != null && password.trim().length() != 0) {
                    ds.setPassword(password);
                }

                //初始化时获取三个连接，取值应在minPoolSize与maxPoolSize之间。Default: 3 initialPoolSize
                ds.setInitialPoolSize(10);
                //连接池中保留的最大连接数。Default: 15 maxPoolSize
                ds.setMaxPoolSize(30);
                //// 连接池中保留的最小连接数。
                //ds.setMinPoolSize(1);
                //当连接池中的连接耗尽的时候c3p0一次同时获取的连接数。Default: 3 acquireIncrement
                ds.setAcquireIncrement(3);
                ds.setMaxStatements(0);
                //每60秒检查所有连接池中的空闲连接。Default: 0  idleConnectionTestPeriod
                ds.setIdleConnectionTestPeriod(60);
                //最大空闲时间,25000秒内未使用则连接被丢弃。若为0则永不丢弃。Default: 0  maxIdleTime
                ds.setMaxIdleTime(25000);
                //连接关闭时默认将所有未提交的操作回滚。Default: false autoCommitOnClose
                ds.setAutoCommitOnClose(true);

                //定义所有连接测试都执行的测试语句。在使用连接测试的情况下这个一显著提高测试速度。注意：
                //测试的表必须在初始数据源的时候就存在。Default: null  preferredTestQuery
                ds.setPreferredTestQuery("select count(1) from mysql.user");
                // 因性能消耗大请只在需要的时候使用它。如果设为true那么在每个connection提交的
                // 时候都将校验其有效性。建议使用idleConnectionTestPeriod或automaticTestTable
                // 等方法来提升连接测试的性能。Default: false testConnectionOnCheckout
                ds.setTestConnectionOnCheckout(true);
                //如果设为true那么在取得连接的同时将校验连接的有效性。Default: false  testConnectionOnCheckin
                ds.setTestConnectionOnCheckin(true);

                //定义在从数据库获取新连接失败后重复尝试的次数。Default: 30  acquireRetryAttempts
                ds.setAcquireRetryAttempts(30);
                //两次连接中间隔时间，单位毫秒。Default: 1000 acquireRetryDelay
                ds.setAcquireRetryDelay(1000);
                //获取连接失败将会引起所有等待连接池来获取连接的线程抛出异常。但是数据源仍有效
                //保留，并在下次调用getConnection()的时候继续尝试获取连接。如果设为true，那么在尝试
                //获取连接失败后该数据源将申明已断开并永久关闭。Default: false  breakAfterAcquireFailure
                ds.setBreakAfterAcquireFailure(true);                
            } catch (Exception ex) {
                log.warn("Failed to look up connection pool from context",
                        ex);
            }
        }
    }

    public Connection getConnection() throws SQLException {
        if (usePool) {
            return ds.getConnection();
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