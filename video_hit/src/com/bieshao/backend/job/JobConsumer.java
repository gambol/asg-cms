package com.bieshao.backend.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.bieshao.model.TodoJob;
import com.bieshao.shua.Shua;
import com.bieshao.shua.TudouShua;
import com.bieshao.web.dao.TodoJobDao;

import cn.bieshao.common.PageResult;

/**
 * 实际处理TodoJob的类
 * 
 * @author zhenbao.zhou
 * 
 */
public class JobConsumer {
	private final static int SLEEP_TIME = 1000;
	private final static Logger logger = Logger.getLogger(JobConsumer.class);
	private final static String SHUA_PACKAGE = "com.bieshao.shua.";
	private final static double ERROR_TRY_TIME  = 1.5; //系统进程有代理服务器异常，所以我们给他一个大于1的系数。。
	
	private String jobName;
	
	public JobConsumer(String jobName) {
	    this.jobName = jobName; 
	}
	
	public void go() {
		logger.info("start job Consumer");
		while (true) {
			PageResult<TodoJob> jobPR = TodoJobDao.getTodoJob(jobName);
			for (TodoJob job : jobPR.getPageList()) {
				doJob(job);
				TodoJobDao.updateJobToDone(job);
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
				logger.debug("job consumer sleep for " + SLEEP_TIME + "millseconds");
			} catch (Exception e) {
				
			}
		}
	}

	private void doJob(TodoJob job) {
		if (job == null)
			return;
		logger.info("do job. jobUrl:" + job.getUrl() + " count:" + job.getNum() + " ip:" + job.getUserIp() + " job class:" + job.getJobClass());
		try {
			Class clazz = Class.forName(SHUA_PACKAGE + job.getJobClass());
			Shua ts = (Shua)clazz.newInstance();
			
			// 由于经常有代理服务器异常，所以我自己给他乘以一个系数好了
			ts.setNum((int)(job.getNum() * ERROR_TRY_TIME));
			//ts.setNum(job.getNum());
			
			ts.setUrl(job.getUrl());
			ts.doJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("do job done. jobUrl:" + job.getUrl() + " count:" + job.getNum() + " ip:" + job.getUserIp() + " job class:" + job.getJobClass());
	}
	
	public static void main(String[] args) throws Exception {

		JobConsumer jc = new JobConsumer("" + TudouShua.class);;
		jc.go();

		
	//	Class c = Class.forName("com.bieshao.shua.TudouShua");
	//	Shua s = (Shua)c.newInstance();
	}
}
