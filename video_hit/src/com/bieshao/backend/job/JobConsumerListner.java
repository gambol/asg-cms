package com.bieshao.backend.job;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*
 * 处理todo_job里的任务
 */
public class JobConsumerListner implements ServletContextListener {

	public void contextInitialized(ServletContextEvent e) {
		new ToDoJobThread().start();
	}

	public void contextDestroyed(ServletContextEvent e) {

	}

	class ToDoJobThread extends Thread {
		public void run() {
			new JobConsumer().go();
		}
	}
}
