package org.j2cms.web.listener;

import java.io.PrintStream;
import java.net.InetAddress;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.j2cms.model.config.Config;

public class ConfigListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext servletContext = arg0.getServletContext();
		try {
			EntityManagerFactory factory = Persistence
					.createEntityManagerFactory("j2cmsSSH");
			EntityManager em = factory.createEntityManager();
			em.getTransaction().begin();
			Config config = (Config) em.find(Config.class, Integer.valueOf(1));
			em.getTransaction().commit();
			if ((config.getUuid() == null) || ("".equals(config.getUuid()))
					|| (config.getUuid().length() != 36)) {
				config.setUuid(UUID.randomUUID().toString());
			}
			InetAddress addr = InetAddress.getLocalHost();
			String ip = addr.getHostAddress().toString();
			config.setIp(ip);
			try {
				String logined = "<html><head><title>管理中心-Powered by </title><meta http-equiv=Content-Type content=text/html;charset=utf-8></head><frameset rows='64,*'  frameborder='NO' border='0' framespacing='0'><frame src='/manage/top' noresize='noresize' frameborder='NO' name='topFrame' scrolling='no' marginwidth='0' marginheight='0' target='main' /><frameset cols='200,*' id='frame'><frame src='/manage/left' name='leftFrame' noresize='noresize' marginwidth='0' marginheight='0' frameborder='0' scrolling='auto' target='main' /><frame src='http://baidu.com' name='main' marginwidth='0' marginheight='0' frameborder='0' scrolling='auto' target='_self' /></frameset></frameset><noframes><body></body></noframes></html>";
				servletContext.setAttribute("logined", logined);
				// String powerby =
				// "<div style='float:left;width:100%;text-align:center;'><center>Powered by <a href='http://www.j2cms.com'  target='_blank'>J2CMS</a></center></div>";
				servletContext.setAttribute("index.html", "");
			} catch (Exception e) {
				System.out.println("操作失败!");
				e.printStackTrace();
			}
			servletContext.setAttribute("C", config);
			em.close();
			factory.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("【监听到】初始化网站的配置Configuration失败。");
		}
		System.out.println("【监听到】初始化网站的配置Configuration成功。");
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}
}