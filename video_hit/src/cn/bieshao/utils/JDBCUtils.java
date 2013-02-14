/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.bieshao.utils;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;
import java.sql.Timestamp;

import cn.bieshao.common.CallBack;
import cn.bieshao.common.PageResult;
import cn.bieshao.common.QueryResult;
import cn.bieshao.dao.DBTool;

import com.bieshao.model.ServerSysInfo;

/**
 *
 * @author root
 */
public class JDBCUtils {

    /**
     * @param obj 保存的实体
     * @return 影响的行数
     */
    public static int insert(Object obj) {
        Class<?> clazz = obj.getClass();
        StringBuffer sbSql = new StringBuffer();//就是字符频繁累加的高效率的一个类 
        sbSql.append("insert into ").append(DBTool.getTableName(clazz)).append("(");
        Field[] fields = clazz.getDeclaredFields();
        StringBuffer param = new StringBuffer();
        for (Field f : fields) {
            String o = getSqlFields(f, obj);//取得对就字段的值 
            if (o != null) {//不插入null值 
                sbSql.append(DBTool.getColumnName(f)).append(",");
                param.append(o).append(",");
            }
        }
        sbSql.deleteCharAt(sbSql.length() - 1);//删除最后一个逗号 
        param.deleteCharAt(param.length() - 1);//删除最后一个逗号 
        sbSql.append(") values(");
        sbSql.append(param);
        sbSql.append(")");
        System.out.println("sql==" + sbSql.toString());
        return DBTool.execute(sbSql.toString());
    }

    /**
     * 更新,约定主健字段均为id
     *
     * @param obj 要保存的实体
     * @param isSavaNull true表示保存 [参数entity] 中的null值,false不保存null值(如自动时间).
     * @return 影响的行数
     */
    public static int update(Object obj, boolean isSavaNull) {
        Class<?> clazz = obj.getClass();
        StringBuffer sbSql = new StringBuffer();
        String idSql = "";//更新主键 
        sbSql.append("update ").append(DBTool.getTableName(clazz)).append(" set ");
        Field[] fields = clazz.getDeclaredFields();

        for (Field f : fields) {
            String o = getSqlFields(f, obj);//取得对就字段的值 
            if (f.getName().equalsIgnoreCase("id")) {
                //主键 
                if (o == null) {
                    throw new RuntimeException("主键不能为空!");
                }
                idSql = " where id=" + o;
            } else {
                //不是主键 
                if (isSavaNull || o != null) {//保存null值 || 保存非null值 
                    sbSql.append(DBTool.getColumnName(f)).append("=").append(o).append(",");
                }
            }
        }
        sbSql.deleteCharAt(sbSql.length() - 1);//删除最后一个逗号 

        sbSql.append(idSql);
        System.out.println("sql==" + sbSql.toString());
        return DBTool.execute(sbSql.toString());
    }

    /**
     * 删除
     *
     * @param clazz 删除实体的类,方法内部根据此类来获取删除的表
     * @param entityIds 删除的id
     * @return 影响的行数
     */
    public static <T> int delete(T... objs) {
        if (objs.length == 0) {//不作任何处理 
            return 0;
        }
        Class<? extends Object> clazz = objs[0].getClass();
        Field idFileld = null;//主键 
        try {
            idFileld = clazz.getDeclaredField("id");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        StringBuffer sbSql = new StringBuffer();
        //access 必须加 from 
        sbSql.append("delete from ").append(DBTool.getTableName(clazz)).append(" where id in(");
        for (int i = 0; i < objs.length; i++) {
            String o = getSqlFields(idFileld, objs[i]);//取得对就字段的值 
            sbSql.append(o).append(",");
        }
        sbSql.deleteCharAt(sbSql.length() - 1);
        sbSql.append(")");
        System.out.println("sql==" + sbSql.toString());
        return DBTool.execute(sbSql.toString());
    }

    /**
     * 取得记录个数
     *
     * @param clazz
     * @param where
     * @param params
     * @return
     */
    public static int getCount(Class<? extends Object> clazz, String where, Object... params) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(*) from ").append(DBTool.getTableName(clazz)).append(" ");
        if (where != null && !where.equals("")) {
            sb.append(" where ").append(where).append(" ");
        }
        final List<Integer> list = new ArrayList<Integer>();
        DBTool.query(new CallBack() {

            @Override
            public void callback(ResultSet rs) throws SQLException {
                if (rs.next()) {
                    list.add(rs.getInt(1));
                }
            }
        }, sb.toString(), params);
        return list.get(0);
    }

    /**
     * 取得记录个数
     *
     * @param clazz
     * @return
     */
    public static long getCount(Class<? extends Object> clazz) {
        return getCount(clazz, null);
    }

    /**
     * 查询记录
     *
     * @param firstResult 起始位置 小于等于0表示从0开始
     * @param maxResult 最大个数 负数和0 表示查询所有
     * @param orderBy 排序 LinkedHashMap <String,
     * Boolean>:Key为排序属性,Value控制升降序,为true时,表示升序(asc),反之为降序(desc)
     * @param where 查询条件
     * @param params 参数 access失效
     * @return
     */
    public static <T> QueryResult<T> getScrollData(Class<T> clazz, int firstResult, int maxResult,
            LinkedHashMap<String, Boolean> orderBy,
            String where, Object... params) {

        firstResult = firstResult >= 0 ? firstResult : Integer.MAX_VALUE;
        maxResult = maxResult >= 0 ? maxResult : Integer.MAX_VALUE;//最大显示数,如果小于0则显示全部 

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("select  * from ").append(DBTool.getTableName(clazz)).append(" ");
        if (where != null && !where.equals("")) {
            sbSql.append("where ").append(where).append(" ");
        }
        sbSql.append(buildOrderBy(orderBy));
        if (firstResult != -1 && maxResult != -1) {
            sbSql.append(" limit ").append(firstResult).append(",").append(maxResult);
        }

  //      System.out.println(sbSql.toString());
        List<T> list = DBTool.queryList(clazz, sbSql.toString(), params);
        QueryResult<T> qr = new QueryResult<T>();
        qr.setResultlist(list);
        qr.setRecordtotal(getCount(clazz, where, params));
        return qr;
    }

    /**
     * asc 升序 desc 降序
     *
     * @param orderBy LinkedHashMap <String, Boolean>
     * Key为属性,Value为true时,表示升序(asc),反之为降序(desc)
     * @return
     */
    private static String buildOrderBy(LinkedHashMap<String, Boolean> orderBy) {
        StringBuilder sb = new StringBuilder();
        if (orderBy != null && !orderBy.isEmpty()) {
            sb.append(" order by ");
            for (Map.Entry<String, Boolean> entry : orderBy.entrySet()) {
                sb.append(entry.getKey());
                if (entry.getValue()) {
                    //升序  可以省略 
                    sb.append(" asc");
                } else {
                    //降序 
                    sb.append(" desc");
                }
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 提供重载
     *
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> QueryResult<T> getScrollData(Class<T> clazz) {
        return getScrollData(clazz, -1, -1, (LinkedHashMap<String, Boolean>) null, (String) null);
    }

    public static <T> QueryResult<T> getScrollData(Class<T> clazz, LinkedHashMap<String, Boolean> orderBy) {
        return getScrollData(clazz, -1, -1, orderBy, (String) null);
    }

    public static <T> QueryResult<T> getScrollData(Class<T> clazz, int firstResult, int maxResult) {
        return getScrollData(clazz, firstResult, maxResult, (LinkedHashMap<String, Boolean>) null, (String) null);
    }

    public static <T> QueryResult<T> getScrollData(Class<T> clazz, int firstResult, int maxResult,
            LinkedHashMap<String, Boolean> orderBy) {
        return getScrollData(clazz, firstResult, maxResult, orderBy, (String) null);
    }

    public static <T> QueryResult<T> getScrollData(Class<T> clazz, int firstResult, int maxResult,
            String where, Object... params) {
        return getScrollData(clazz, firstResult, maxResult, (LinkedHashMap<String, Boolean>) null, where, params);
    }

    public static <T> QueryResult<T> getScrollData(Class<T> clazz, LinkedHashMap<String, Boolean> orderBy, String where, Object... params) {
        return getScrollData(clazz, -1, -1, orderBy, where, params);
    }

    /**
     *
     * @param <T>
     * @param clazz 显示类
     * @param maxresult 每页显示数
     * @param currentpage 当前页
     * @param orderBy 排序
     * @param where 条件
     * @param params 参数 access失效
     * @return
     */
    public static <T extends Serializable> PageResult getPageData(Class<T> clazz, int pagesize, int currentpage,
            LinkedHashMap<String, Boolean> orderBy,
            String where, Object... params) {
        PageResult<T> pageResult = new PageResult<T>(currentpage, pagesize);
        pageResult.setQueryResult(getScrollData(clazz, pageResult.getFirstResult(), pageResult.getPageSize(), orderBy, where, params));
        return pageResult;
    }

    public static <T extends Serializable> PageResult<T> getPageData(Class<T> clazz, int pagesize, int currentpage,
            LinkedHashMap<String, Boolean> orderBy) {
        return getPageData(clazz, pagesize, currentpage, orderBy, null);
    }

    public static <T extends Serializable> PageResult<T> getPageData(Class<T> clazz, int pagesize, int currentpage,
            String where, Object... params) {
        return getPageData(clazz, pagesize, currentpage, (LinkedHashMap<String, Boolean>) null, where, params);
    }

    /**
     * 生成sql对应的值 如 '字符串','2012-01-01 00:00::00',数字
     *
     * @param f 字段
     * @param obj 取值的对象
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getSqlFields(Field f, Object obj) {
        f.setAccessible(true);
        String retVal = null;
        try {
            Object o = f.get(obj);//取得对就字段的值 
            if (o == null) {//不理会  null值 
                return null;
            }
            if (f.getType() == String.class) {
                //字符串,两边加单引号,还要解决SQL注入,就是内容是一个单引号替换为两个单引号 
                retVal = (String) o;
                retVal = "'" + retVal.replace("'", "''") + "'";
            } else if (f.getType() == Date.class) {
                //日期,转换为'2012-01-01 00:00::00' 的形式 
                Date date = (Date) o;
                retVal = "'" + date.toLocaleString() + "'";//SimpleDateFormat 
            } else if (f.getType() == Timestamp.class) {
                Timestamp ts = (Timestamp) o;
                retVal = "'" + ts.toLocaleString() + "'";
            } else {
                //其它如int float等,直接写就OK 
                retVal = o + "";
            }
            return retVal;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
