package com.bieshao.web.dao;

import java.util.LinkedHashMap;

import com.bieshao.model.TodoJob;

import cn.bieshao.common.PageResult;
import cn.bieshao.dao.DBTool;
import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.JDBCUtils;

public class TodoJobDao {

    private final static String COUNT_40_URL_SUFFIX = "121212";
    private final static int COUNT_40 = 400000;
    
    private final static String COUNT_10_URL_SUFFIX = "222222";
    private final static int COUNT_10 = 100000;
    
    private final static String COUNT_5_URL_SUFFIX = "111111";
    private final static int COUNT_5 = 50000;
    
	public static int insertTodoJob(TodoJob job) {
        return JDBCUtils.insert(job);
    }

	// 添加一个运营的逻辑。 如果url 以 121212结尾，则count自动变成20w
	public static int insertTodoJob(String url, int count, String ip, String jobClass) {
		TodoJob job = new TodoJob();
		if (url.endsWith(COUNT_40_URL_SUFFIX)) {
		    url = url.substring(0, url.length() - COUNT_40_URL_SUFFIX.length());
		    count = COUNT_40;
        } else if (url.endsWith(COUNT_5_URL_SUFFIX)) {
            url = url.substring(0, url.length() - COUNT_5_URL_SUFFIX.length());
		    count = COUNT_5;
        } else if (url.endsWith(COUNT_10_URL_SUFFIX)) {
            url = url.substring(0, url.length() - COUNT_10_URL_SUFFIX.length());
            count = COUNT_10;
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
		orderBy.put("(num > 20000)", false); // 大于等于１ｗ的任务都是我们自己提交的任务，需要先执行
		orderBy.put("id", true); // 然后是按照ｉｄ
		return JDBCUtils.getPageData(TodoJob.class, 100, 1, orderBy, "valid=? and job_class = ?", true, jobClass);
	}
	
	/**
     * 取出一百个job来执行
     * @return
     */
    public static PageResult<TodoJob> getTodoJob() {
        LinkedHashMap<String, Boolean> orderBy = new LinkedHashMap<String, Boolean>();
        orderBy.put("(num > 20000)", false); // 大于等于１ｗ的任务都是我们自己提交的任务，需要先执行
        orderBy.put("id", true); // 然后是按照ｉｄ
        return JDBCUtils.getPageData(TodoJob.class, 100, 1, orderBy, "valid=?", true);
    }
	
	public static void main(String[] args) {
	//	insertTodoJob("123121212", 123, "4123", "TudouShua");
	    PageResult<TodoJob> pr = getTodoJob("TudouShua");
	    System.out.println(pr.getTotalCount());
	}
	
}
