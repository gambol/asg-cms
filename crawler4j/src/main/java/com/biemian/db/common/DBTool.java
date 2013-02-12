/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biemian.db.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 *
 * @author xiang.fu
 */
public class DBTool {

	
	private static final Logger logger = Logger.getLogger(DBTool.class);
	
    public static String getTableName(Class clz) {

        Annotation[] as = clz.getAnnotations();
        for (Annotation a : as) {
            if (a instanceof DBTableName) {
                return ((DBTableName) a).value();
            }
        }
        return null;
    }

    public static String getColumnName(Field f) {
        Annotation[] as = f.getAnnotations();
        for (Annotation a : as) {
            if (a instanceof DBColumnName) {
                return ((DBColumnName) a).value();
            }
        }
        return null;
    }

    public static Object getObjectFromRS(ResultSet resutlSet, Class clz) throws SQLException, Exception {
        Object o = null;
        try {
            o = clz.newInstance();
        } catch (Exception ex) {
            throw ex;
        }

        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields) {
            Annotation[] anns = f.getAnnotations();
            String columnName = null;
            for (Annotation ann : anns) {
                if (ann instanceof DBColumnName) {
                    columnName = ((DBColumnName) ann).value();
                }
            }
            Object fo = null;
            if (columnName != null) {
                fo = resutlSet.getObject(columnName);
                if (fo != null) {
                    f.setAccessible(true);
                    f.set(o, fo);
                }
            }
        }
        return o;
    }

    public static void rollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
             logger.error(ex.getMessage());
        }
    }

    public static void closeStmt(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                 logger.error(ex.getMessage());
            }
        }
    }

    public static void closeRS(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

    public static void closeAll(Connection conn, Statement stmt, ResultSet rs) {
        try { // 捕捉异常
            try {
                if (rs != null) { // 当ResultSet对象的实例rs不为空时
                    rs.close(); // 关闭ResultSet对象
                }
            } finally {
                try {
                    if (stmt != null) { // 当Statement对象的实例stmt不为空时
                        stmt.close(); // 关闭Statement对象
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (conn != null) { // 当Connection对象的实例conn不为空时
                        conn.close(); // 关闭Connection对象
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); // 输出异常信息
        }
    }

    /**
     * 给PreparedStatement赋值
     *
     * @param pstmt
     * @param values
     * @throws SQLException
     */
    private static void setPstmtValues(PreparedStatement pstmt, Object... values)
            throws SQLException {
        if (pstmt != null & values != null) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
        }
    }

    /**
     * 执行SQL语句(insert,update,delete)
     *
     * @param callBack 回调函数中rs可获取[由于执行此 Statement 对象]而创建的所有自动生成的键
     * @param sql sql语句
     * @param values 对就占位符的值
     * @return 返回影响的行数
     */
    public static int execute(CallBack callBack, String sql, Object... values) {
        int retVal = 0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("[execute]sql:" + sql);
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtValues(pstmt, values);
            retVal = pstmt.executeUpdate();
            if (callBack != null) {
                rs = pstmt.getGeneratedKeys();
                callBack.callback(rs);//回调
            }
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            closeAll(conn, pstmt, rs);
        }
        
        logger.info("[execute over]sql:" + sql.substring(0, 1) + " sql length:" + sql.length());
        return retVal;
    }

    /**
     * 执行SQL语句(insert,update,delete)
     *
     * @param sql sql语句
     * @param values 对就占位符的值
     * @return 返回影响的行数
     */
    public static int execute(String sql, Object... values) {
        return execute(null, sql, values);
    }

    /**
     * 回调一次 未经过任何处理的ResultSet
     *
     * @param callBack
     * @param sql
     * @param values
     * @see JdbcUtils.query()
     */
    public static void query(CallBack callBack, String sql, Object... values) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        logger.debug("[query]sql:" + sql);
        try {
            conn = ConnectionFactory.getInstance().getConnection();
            pstmt = conn.prepareStatement(sql);
            setPstmtValues(pstmt, values);
            rs = pstmt.executeQuery();
            callBack.callback(rs);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            closeAll(conn, pstmt, rs);
        }
    }

    /**
     * 查询实体列表
     *
     * @param <T>
     * @param clazz
     * @param sql
     * @param values
     * @return
     */
    public static <T> List<T> queryList(final Class<T> clazz, String sql, Object... values) {
        final List<T> list = new ArrayList<T>();
       
        query(new CallBack() {

            @Override
            public void callback(ResultSet rs) throws SQLException {
                while (rs.next()) {
                    try {
                        T t = (T) getObjectFromRS(rs, clazz);
                        list.add(t);
                    } catch (Exception e) {
                        e.printStackTrace();;
                        throw new RuntimeException(e.getMessage());
                    }
                }
            }
        }, sql, values);
        return list;
    }

    /**
     * 查询实体
     *
     * @param <T>
     * @param clazz
     * @param sql
     * @param values
     * @return
     */
    public static <T> T queryEntity(final Class<T> clazz, String sql, Object... values) {
        List<T> list = queryList(clazz, sql, values);
        if (list.size() == 0) {
            return null;
        }
        return list.get(0);
    }
}
