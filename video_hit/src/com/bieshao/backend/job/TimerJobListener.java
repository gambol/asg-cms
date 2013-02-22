package com.bieshao.backend.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class TimerJobListener implements ServletContextListener {
	private ScheduledExecutorService proxyImproveJob =  Executors.newScheduledThreadPool(1);
	
	public void contextInitialized(ServletContextEvent context) {
		// testService.scheduleWithFixedDelay(new TestTimer(), 1, 10,  TimeUnit.SECONDS);
		// indexHtml.scheduleWithFixedDelay(new HTMLGenerator(context.getServletContext()), 1, 4000,  TimeUnit.SECONDS);
		proxyImproveJob.scheduleWithFixedDelay(new ProxyImprover(), 1, 1440,  TimeUnit.MINUTES);
	}
	
	public void contextDestroyed(ServletContextEvent context) {
		proxyImproveJob.shutdown();
	}
	
	
	/**
	 * 获取最开始的时间
	 * 返回值为 毫秒 (milliseconds)
	 * @param startStr
	 * @return
	 */
    private long getInitDelay(String startStr){
        SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat yyyyMMddHHmmssSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Date startDate = null;
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        String tryTime = yyyyMMddSdf.format(now) + startStr;
        try {
                Date tryDate = yyyyMMddHHmmssSdf.parse(tryTime);
                if (now.getTime() <= tryDate.getTime()) {
                         startDate = tryDate;
                } else {
                         cal.add(Calendar.DATE, 1);
                         startDate = yyyyMMddHHmmssSdf.parse( yyyyMMddSdf.format(cal.getTime()) + startStr);
                }                        
        } catch(Exception e) {
                e.printStackTrace();
        }
        return (startDate.getTime() - now.getTime());
}

}
