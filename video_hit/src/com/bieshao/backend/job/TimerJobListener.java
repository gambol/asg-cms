package com.bieshao.backend.job;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerJobListener implements ServletContextListener {
	private ScheduledExecutorService proxyCleanJob =  Executors.newScheduledThreadPool(1);
	
	public void contextInitialized(ServletContextEvent context) {
		// testService.scheduleWithFixedDelay(new TestTimer(), 1, 10,  TimeUnit.SECONDS);
		// indexHtml.scheduleWithFixedDelay(new HTMLGenerator(context.getServletContext()), 1, 4000,  TimeUnit.SECONDS);
		proxyCleanJob.scheduleWithFixedDelay(new ProxyCleaner(), 1, 1440,  TimeUnit.MINUTES);
	}
	
	public void contextDestroyed(ServletContextEvent context) {
		proxyCleanJob.shutdown();
	}
}
