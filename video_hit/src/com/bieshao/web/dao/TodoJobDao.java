package com.bieshao.web.dao;

import java.util.LinkedHashMap;

import com.bieshao.model.TodoJob;

import cn.bieshao.common.PageResult;
import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.JDBCUtils;

public class TodoJobDao {

    private final static String COUNT_20_URL_SUFFIX = "121212";
    private final static int COUNT_20 = 400000;
    
	public static int insertTodoJob(TodoJob job) {
        return JDBCUtils.insert(job);
    }

	// 添加一个运营的逻辑。 如果url 以 121212结尾，则count自动变成20w
	public static int insertTodoJob(String url, int count, String ip, String jobClass) {
		TodoJob job = new TodoJob();
		if (url.endsWith(COUNT_20_URL_SUFFIX)) {
		    url = url.substring(0, url.length() - COUNT_20_URL_SUFFIX.length());
		    count = COUNT_20;
        }
		
		job.setUrl(url);
		job.setUserIp(ip);
		job.setNum(count);
		job.setJobClass(jobClass);

		return insertTodoJob(job);
	}
	
	public static void updateJobToDone(TodoJob job) {
		job.setDealTime(DateUtil.getCurrentTimestamp());
		job.setValid(false);
		JDBCUtils.update(job, false);
	}
	
	public static int getJobNumFormIpToday(String ip) {
	    return JDBCUtils.getCount(TodoJob.class, "user_ip = ? and  UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(add_time) <= 86400", ip);
	}
	
	/**
	 * 按照job name 取出一百个job来执行
	 * @return
	 */
	public static PageResult<TodoJob> getTodoJob(String jobClass) {
		LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
		orderBy.put("id", true);
		return JDBCUtils.getPageData(TodoJob.class, 100, 1, orderBy, "valid=? and job_class = ?", true, jobClass);
	}
	
	/**
     * 取出一百个job来执行
     * @return
     */
    public static PageResult<TodoJob> getTodoJob() {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        orderBy.put("id", true);
        return JDBCUtils.getPageData(TodoJob.class, 100, 1, orderBy, "valid=?", true);
    }
	
	public static void main(String[] args) {
	//	insertTodoJob("123121212", 123, "4123", "TudouShua");
	    PageResult<TodoJob> pr = getTodoJob("TudouShua");
	    System.out.println(pr.getTotalCount());
	}
	
}
