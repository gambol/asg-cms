package com.bieshao.backend.job;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

/*
 * 处理todo_job里的任务
 */
public class JobConsumerListner implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(JobConsumerListner.class);
    
	public void contextInitialized(ServletContextEvent e) {
	    String[] jobNames = new String[] {"Ku6Shua", "SinaShua", 
	            "SohuShua", "TudouShua", "WuliuShua", "YoukuShua"};
	    
	    // 每个job 一种类型，可以免得某种类型太多了，影响其他类型
	    // 所以job里面，最慢的是56（56服务器比较慢，每秒只能发10个左右请求
	    //, 其次是youku(我处理需要时间)， 其他的都还比较快(大概在每秒100个请求左右)
	    for(String jobName : jobNames) {
	        new ToDoJobThread(jobName).start();
		}
	}

	public void contextDestroyed(ServletContextEvent e) {

	}

	class ToDoJobThread extends Thread {
	    private String jobName;
	    
	    public ToDoJobThread(String jobName) {
	        this.jobName = jobName;
	    }
	    
	    @Override
		public void run() {
	        logger.info("jobName:" + jobName + " go!");
			new JobConsumer(jobName).go();
		}
	}
	
	public static void main(String[] args) {
	    String[] jobNames = new String[] {"Ku6Shua", "SinaShua", 
                "SohuShua", "TudouShua", "WuliuShua", "YoukuShua"};
	    
        JobConsumerListner jc = new JobConsumerListner();
        // 每个job 一个后台线程处理
        for(String jobName : jobNames) {
            jc.new ToDoJobThread(jobName).start();
        }
    }
}
