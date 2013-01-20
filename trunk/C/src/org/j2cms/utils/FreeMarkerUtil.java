package org.j2cms.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateModelException;

/**
 * 
 * <p>Title: FreeMarkerUtil.java</p>
 * 
 * <p>Description: freemarker工具类</p>
 * 
 * <p>Date: Mar 9, 2012</p>
 * 
 * <p>Time: 4:00:48 PM</p>
 * 
 * <p>Copyright: 2012</p>
 * 
 * <p>Company: freeteam</p>
 * 
 * @author freeteam
 * @version 1.0
 * 
 * <p>============================================</p>
 * <p>Modification History
 * <p>Mender: </p>
 * <p>Date: </p>
 * <p>Reason: </p>
 * <p>============================================</p>
 */

@Deprecated
public class FreeMarkerUtil {

	 /**     
	  * 生成静态页面主方法     默认编码为UTF-8
	  * @param context ServletContext     
	  * @param data 一个Map的数据结果集     
	  * @param templatePath ftl模版路径     
	  * @param htmlPath 生成静态页面的路径   
	 * @throws TemplateModelException 
	  */    
	public static void createHTML(ServletContext context,Map<String,Object> data,String templatePath,String htmlPath) throws TemplateModelException{
		createHTML(context, data, "UTF-8", templatePath, "UTF-8", htmlPath);
	}
	 /**     
	  * 生成静态页面主方法     
	  * @param context ServletContext     
	  * @param data 一个Map的数据结果集     
	  * @param templatePath ftl模版路径     
	  * @param templateEncode ftl模版编码     
	  * @param htmlPath 生成静态页面的路径   
	  * @param htmlEncode 生成静态页面的编码     
	 * @throws TemplateModelException 
	  */    
	public static void createHTML(ServletContext context,Map<String,Object> data,String templetEncode,String templatePath,String htmlEncode,String htmlPath) throws TemplateModelException{
		
		Configuration freemarkerCfg=initCfg(context, templetEncode);
		
		try {            
			//指定模版路径            
			Template template = freemarkerCfg.getTemplate(templatePath,templetEncode);            
			template.setEncoding(templetEncode);            
			//静态页面路径                      
			File htmlFile = new File(htmlPath);            
			Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), htmlEncode));            
			//处理模版              
			template.process(data, writer);            
			writer.flush();            
			writer.close();        
		} catch (Exception e) {            
			e.printStackTrace();        
		}
	}
	/**
	 * 处理页面后，装处理结果放入指定Out
	 * @param context
	 * @param data
	 * @param templatePath
	 * @throws TemplateModelException 
	 */
	public static void createWriter(ServletContext context,Map<String,Object> data,String templatePath,Writer writer) throws TemplateModelException{
		createWriter(context, data, "UTF-8", templatePath, "UTF-8",writer);
	}
	public static void createWriter(ServletContext context,Map<String,Object> data,String templetEncode,String templatePath,String htmlEncode,Writer writer) throws TemplateModelException{

		Configuration freemarkerCfg=initCfg(context, templetEncode);
		
		try {            
			//指定模版路径            
			Template template = freemarkerCfg.getTemplate(templatePath,templetEncode);            
			template.setEncoding(templetEncode);            
			//处理模版              
			template.process(data, writer);        
		} catch (Exception e) {            
			e.printStackTrace();        
		}
	}
	/**
	 * 初始化freemarker配置
	 * @return
	 * @throws TemplateModelException 
	 */
	public static Configuration initCfg(ServletContext context,String templetEncode) throws TemplateModelException{

		Configuration freemarkerCfg=null;
		//判断context中是否有freemarkerCfg属性
		if (context.getAttribute("freemarkerCfg")!=null) {
			freemarkerCfg=(Configuration)context.getAttribute("freemarkerCfg");
		}else {
			freemarkerCfg = new Configuration();        
			//加载模版        
			freemarkerCfg.setServletContextForTemplateLoading(context, "/");        
			freemarkerCfg.setEncoding(Locale.getDefault(), templetEncode);    
			
			//加载自定义标签
			//栏目类
			/*
			freemarkerCfg.setSharedVariable("channel", new ChannelDirective());
			freemarkerCfg.setSharedVariable("channelList", new ChannelListDirective());
			//信息类
			freemarkerCfg.setSharedVariable("infoList", new InfoListDirective());
			freemarkerCfg.setSharedVariable("infoPage", new InfoPageDirective());
			freemarkerCfg.setSharedVariable("infoAttchs", new InfoAttchsDirective());
			freemarkerCfg.setSharedVariable("infoSearch", new InfoSearchDirective());
			//链接类
			freemarkerCfg.setSharedVariable("linkClass", new LinkClassDirective());
			freemarkerCfg.setSharedVariable("link", new LinkDirective());
			//Ajax类
			freemarkerCfg.setSharedVariable("ajaxInfoClick", new AjaxInfoClickDirective());
			*/
		}
		return freemarkerCfg;
	}
}