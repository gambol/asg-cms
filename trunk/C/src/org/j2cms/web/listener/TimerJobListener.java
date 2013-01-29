package org.j2cms.web.listener;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.biemian.timer.HTMLGenerator;
import org.biemian.timer.TestTimer;

public class TimerJobListener implements ServletContextListener {
	private ScheduledExecutorService testService =  Executors.newScheduledThreadPool(1);
	private ScheduledExecutorService indexHtml =  Executors.newScheduledThreadPool(1);
	
	public void contextInitialized(ServletContextEvent context) {
		// testService.scheduleWithFixedDelay(new TestTimer(), 1, 10,  TimeUnit.SECONDS);
		// indexHtml.scheduleWithFixedDelay(new HTMLGenerator(context.getServletContext()), 0, 300,  TimeUnit.MINUTES);
	}
	
	public void contextDestroyed(ServletContextEvent context) {
		indexHtml.shutdown();
		testService.shutdown();
	}
}
