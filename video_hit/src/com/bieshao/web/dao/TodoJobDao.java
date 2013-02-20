package com.bieshao.web.dao;

import java.util.LinkedHashMap;

import com.bieshao.model.TodoJob;

import cn.bieshao.common.PageResult;
import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.JDBCUtils;

public class TodoJobDao {

	public static int insertTodoJob(TodoJob job) {
        return JDBCUtils.insert(job);
    }
	
	public static int insertTodoJob(String url, int count, String ip, String jobClass) {
		TodoJob job = new TodoJob();
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
		insertTodoJob("123", 123, "4123", "TudouShua");
	}
	
}
