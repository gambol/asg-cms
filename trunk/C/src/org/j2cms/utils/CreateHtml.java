package org.j2cms.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import org.apache.struts2.ServletActionContext;
import org.j2cms.model.config.Config;
import org.j2cms.utils.Struts2Utils;

public class CreateHtml {
	private ServletContext context;
	private Config config;
	private String templateurl;
	private Configuration cfg;

	public CreateHtml() {
		this.context = ServletActionContext.getServletContext();
		initOtherParam();
	}

	public CreateHtml(ServletContext context) {
		if (context == null) {
			this.context = ServletActionContext.getServletContext();
		} else {
			this.context = context;
		}
		initOtherParam();
	}

	private void initOtherParam() {
		config = (Config) context.getAttribute("C");
		templateurl = this.config.getTemplate().trim();
		cfg = new Configuration();
	}

	/**
	 * 如果alwaysNew为false时， 只有当文件不存在时，才生成新的
	 * 
	 * @param ftl
	 * @param htmlName
	 * @param map
	 * @param relaPath
	 * @throws IOException
	 * @throws TemplateException
	 */
	public int initNew(String ftl, String htmlName, Map<String, Object> map,
			String relaPath, boolean alwaysNew) throws IOException,
			TemplateException {
		this.cfg.setServletContextForTemplateLoading(context, "/");
		this.cfg.setEncoding(Locale.getDefault(), "utf-8");

		map.put("C", this.config);

		Template template = this.cfg.getTemplate(this.templateurl + ftl);
		template.setEncoding("utf-8");

		String path = context.getRealPath("/");
		File fileName = new File(path + relaPath + htmlName);
		if (!alwaysNew) {
			if (fileName.exists()) {
				return 0;
			}
		}
		Writer out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(fileName), "utf-8"));
		try {
			template.process(map, out);
			System.out.println("write file:" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
		return 1;
	}
	
	public void output(String ftl, Map<String, Object> map, Writer out)
			throws IOException, TemplateException {
		map.put("C", this.config);

		this.cfg.setServletContextForTemplateLoading(context, "/");
		this.cfg.setEncoding(Locale.getDefault(), "utf-8");
		Template template = this.cfg.getTemplate(this.templateurl + ftl);
		template.setEncoding("utf-8");
		try {
			template.process(map, out);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}

	}

	public void init(String ftl, String htmlName, Map<String, Object> map,
			String relaPath) throws IOException, TemplateException {
		initNew(ftl, htmlName, map, relaPath, true);
	}

	public void multiFtlCreateHtml(String[] ftls, String[] htmlNames,
			Map<String, Object> map, String relaPath) throws IOException,
			TemplateException {
		map.put("C", this.config);

		this.cfg.setServletContextForTemplateLoading(context, "/");
		this.cfg.setEncoding(Locale.getDefault(), "utf-8");

		Writer out = null;

		Template template = null;

		String path = context.getRealPath("/");
		for (short i = 0; i < ftls.length; i = (short) (i + 1)) {
			template = this.cfg.getTemplate(this.templateurl + ftls[i]);
			template.setEncoding("utf-8");

			File fileName = new File(path + relaPath + htmlNames[i]);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "utf-8"));
			template.process(map, out);
		}
		template = this.cfg.getTemplate(this.templateurl + "index.ftl");
		template.setEncoding("utf-8");

		File fileName = new File(path + relaPath + "index.html");
		out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(
				fileName), "utf-8"));
		template.process(map, out);
		System.out.println("write file:" + fileName);
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true), "utf-8"));
			out.write(context.getAttribute("index.html").toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.flush();
			out.close();
		}
	}

	public void addPoweredBy(String htmlName, String relaPath) {
		Writer out = null;
		String path = context.getRealPath("/");
		File fileName = new File(path + relaPath + htmlName);
		try {
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName, true), "utf-8"));
			out.write(Struts2Utils.getContextAttribute("index.html").toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
