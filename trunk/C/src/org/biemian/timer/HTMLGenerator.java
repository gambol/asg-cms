package org.biemian.timer;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.j2cms.web.action.JIndexAction;
import org.j2cms.web.action.manage.article.ArticleAction;
import org.j2cms.web.action.manage.channel.ChannelAction;

public class HTMLGenerator implements Runnable{
	private final static Logger logger = Logger.getLogger(HTMLGenerator.class);
	
	private ServletContext context;
	
	public HTMLGenerator(ServletContext context) {
		this.context = context; 
	}
	
	@Override
	public void run() {
//		System.out.println("hehe html");
		try {
			new JIndexAction().makeHtmlFromContext(context);
			new ChannelAction().allChannelToHTML(context);
			new ArticleAction().dealNewArticle(context);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
