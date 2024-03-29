package org.j2cms.web.action.manage.channel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.j2cms.model.CheckState;
import org.j2cms.model.PageView;
import org.j2cms.model.QueryResult;
import org.j2cms.model.article.Article;
import org.j2cms.model.channel.Channel;
import org.j2cms.service.ArticleService;
import org.j2cms.service.ChannelService;
import org.j2cms.service.EntityService;
import org.j2cms.utils.Struts2Utils;
import org.j2cms.utils.WebUtil;
import org.j2cms.web.action.EntityAction;

import org.j2cms.utils.CreateHtml;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import freemarker.template.TemplateException;

@Results({
		@Result(name = "toList", type = "chain", location = "list"),
		@Result(name = "succ", type = "freemarker", location = "/WEB-INF/content/util/succ.ftl"),
		@Result(name = "error", type = "freemarker", location = "/WEB-INF/content/util/error.ftl"),
		@Result(name = "errorPage", type = "freemarker", location = "/WEB-INF/content/util/errorPage.ftl"),
		@Result(name = "ok", type = "freemarker", location = "/template/biemian/channel.ftl"),
		@Result(name = "urlRedirect", type = "freemarker", location = "/WEB-INF/content/util/urlRedirect.ftl") })
@ExceptionMappings({ @ExceptionMapping(exception = "java.sql.SQLException", result = "error", params = {
		"message", "操作数据库失败！" }) })
public class ChannelAction extends EntityAction<Channel> {

	private static final long serialVersionUID = 7854600986342870142L;
	@Resource
	private ArticleService articleService;
	private Channel channel;
	private Integer fatherID;
	private String display = "all";
	private String displayInIndex = "all";
	private String single = "all";
	private List<Channel> channels = new ArrayList<Channel>();
	private List<Channel> channelsDisplay = new ArrayList<Channel>();

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Integer getFatherID() {
		return fatherID;
	}

	public void setFatherID(Integer fatherID) {
		this.fatherID = fatherID;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getDisplayInIndex() {
		return displayInIndex;
	}

	public void setDisplayInIndex(String displayInIndex) {
		this.displayInIndex = displayInIndex;
	}

	public String getSingle() {
		return single;
	}

	public void setSingle(String single) {
		this.single = single;
	}

	public List<Channel> getChannels() {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("rankid", "asc");
		channels = entityService.getScrollData(-1, -1, orderby).getResultlist();
		return channels;
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	public List<Channel> getChannelsDisplay() {
		return channelsDisplay;
	}

	public void setChannelsDisplay(List<Channel> channelsDisplay) {
		this.channelsDisplay = channelsDisplay;
	}

	@Actions({ @Action("/manage/left") })
	public String execute() {
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("rankid", "asc");
		channels = entityService.getScrollData(-1, -1, orderby).getResultlist();
		return SUCCESS;
	}

	@Override
	@Actions({ @Action("list") })
	public String list() {
		QueryResult<Channel> qr;
		StringBuffer jpql = new StringBuffer("");// "1=1"?
		List<Object> params = new ArrayList<Object>();

		int page = WebUtil.StrToInt(Struts2Utils.getParameter("page"));
		Integer id = WebUtil.StrToInt(Struts2Utils.getParameter("id"));
		if (page != 0)
			pageView.setCurrentpage(page);
		int firstindex = (pageView.getCurrentpage() - 1)
				* pageView.getMaxresult();
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("rankid", "asc");


		if (single != null && !"all".equals(single) && !"".equals(single)) {
			if (params.size() > 0)
				jpql.append(" and ");
			jpql.append(" o.single=?").append((params.size() + 1));
			params.add(new Boolean(single));
		}
		if (display != null && !"all".equals(display) && !"".equals(display)) {
			if (params.size() > 0)
				jpql.append(" and ");
			jpql.append(" o.display=?").append((params.size() + 1));
			params.add(new Boolean(display));
		}
		if (displayInIndex != null && !"all".equals(displayInIndex)
				&& !"".equals(displayInIndex)) {
			if (params.size() > 0)
				jpql.append(" and ");
			jpql.append(" o.displayInIndex=?").append((params.size() + 1));
			params.add(new Boolean(displayInIndex));
		}
		qr = entityService.getScrollData(firstindex, pageView.getMaxresult(),
				jpql.toString(), params.toArray(), orderby);
		pageView.setQueryResult(qr);
		return SUCCESS;
	}

	@Actions({
			@Action(value = "/channel"),
			@Action(value = "/search", results = { @Result(name = "search", type = "freemarker", location = "template/search.html") }), })
	public String channel() {
		Integer id = WebUtil.StrToInt(Struts2Utils.getParameter("id"));// channel
		String title = Struts2Utils.getParameter("title");
		int page = WebUtil.StrToInt(Struts2Utils.getParameter("page"));// channel.ftl传递过来的page的值
		Channel channel = entityService.find(id);
		Struts2Utils.setAttribute("entity", channel);
		Struts2Utils.setAttribute("title", title);

		Map<String, Object> map = new HashMap<String, Object>();
		String channelFTL = "channel.ftl";
		String singleFTL = "single.ftl";
		String relaPath = "";
		String htmlName = id + ".html";
		map.put("entity", channel);
		map.put("title", title);
		
		List<Article> latestArticles = new ArrayList<Article>();
		List<Article> mostVisitArticles = new ArrayList<Article>(); // visit
		LinkedHashMap<String, String> orderbyId = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyVisitTotal = new LinkedHashMap<String, String>();
		orderbyId.put("id", "desc");
		orderbyVisitTotal.put("visitTotal", "desc");
		
		latestArticles = articleService.getScrollData(0, 30, "o.checkState=?1",
				new Object[] { CheckState.pass }, orderbyId).getResultlist();
		/*
		mostVisitArticles = articleService.getScrollData(0, 15,
				"o.checkState=?1", new Object[] { CheckState.pass },
				orderbyVisitTotal).getResultlist();
		*/
		map.put("latestArticles", latestArticles);
		//		map.put("mostVisitArticles", mostVisitArticles);

		try {
			channel.setVisitTotal(channel.getVisitTotal() + 1);// 点击量加1
		} catch (Exception e) {
			return "errorPage";
		}
		if (channel.getCheckState() == CheckState.pass) {
			
			if (channel.getSingle() != null && channel.getSingle() == true) {
				// 	暂时不支持
				try {
					new CreateHtml().init(singleFTL, htmlName, map, relaPath);// 生成静态HTML
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TemplateException e) {
					e.printStackTrace();
				}
			} else {
				PageView<Article> pageView = new PageView<Article>(1);
				if (page != 0) {
					pageView.setCurrentpage(page);
					relaPath = "Channel/";
					htmlName = id + "_" + page + ".html";// 有页码的HTML的名称
				}
				int firstindex = (pageView.getCurrentpage() - 1)
						* pageView.getMaxresult();
				LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
				orderby.put("id", "desc");
				StringBuffer jpql = new StringBuffer("o.checkState=?1");
				List<Object> params = new ArrayList<Object>();
				params.add(CheckState.pass);
				if (id != null && id != 1) {
					if (params.size() > 0)
						jpql.append(" and ");
					jpql.append(" o.channel=?").append((params.size() + 1));
					params.add(new Channel(id));
				}
				if (title != null && !"".equals(title)) {
					if (params.size() > 0)
						jpql.append(" and ");
					jpql.append(" o.title like ?").append((params.size() + 1));
					params.add("%" + title + "%");
				}
				QueryResult<Article> qr;
				qr = articleService.getScrollData(firstindex,
						pageView.getMaxresult(), jpql.toString(),
						params.toArray(), orderby);
				pageView.setQueryResult(qr);
				if (title != null && !"".equals(title)) {// 如果是搜索
					Struts2Utils.setAttribute("searchPageView", pageView);
					return "search";
				} else {
					map.put("pageView", pageView);
					try {
						
						//new CreateHtml().init(channelFTL, htmlName, map,
						// relaPath);// 生成静态HTML
						new CreateHtml().output("channel.ftl", map, ServletActionContext.getResponse().getWriter());
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (TemplateException e) {
						e.printStackTrace();
					}
				}
			}
			//Struts2Utils.setAttribute("url", "/" + relaPath + htmlName);
			//Struts2Utils.setAttribute("pageView", pageView);
			//Struts2Utils.setMapAttribute(map);
			return "urlRedirect";
		} else {
			return "errorPage";
		}

	}

	/**
	 * 所有频道都重新生成html
	 *  一次取出所有的频道。 如果频道过多时，可能会oom。 注意。
	 * @return
	 */
	public void allChannelToHTML(ServletContext context) {
		QueryResult<Channel> channelList = new QueryResult<Channel>();
		try {
			if (entityService != null) {
				channelList = entityService.getScrollData();
			} else {
				ApplicationContext cxt = WebApplicationContextUtils
						.getWebApplicationContext(context);
				ChannelService channelSerivce = (ChannelService) cxt
						.getBean("channelServiceImpl");
				channelList = channelSerivce.getScrollData(-1, -1);
				// Channel c = channelSerivce.find(12);
				// System.out.println(c.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (channelList.getTotalrecord() != 0) {
			for (Channel entity : channelList.getResultlist()) {
				makeSignleChannelToHtml(entity, context);
			}
		}
	}

	@Action(value = "makeHtml")
	public String makeHtml() {
		String strid[] = Struts2Utils.getParameterValues("ids");
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		Integer intid = 0;
		int i = 0;

		try {
			for (i = 0; i < strid.length; i++) {
				intid = Integer.parseInt(strid[i]);
				makeSingleChannelToHtml(intid, null);//
			}
			Struts2Utils.setAttribute("message", "已在生成个" + i + "个栏目的html文件");
			return "succ";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	public String makeSignleChannelToHtml(Channel entity, ServletContext context) {
		if (entity == null) {
			return "error";
		}
		
		int id = entity.getId();
		
		CreateHtml creatHtml = new CreateHtml(context);
		Map<String, Object> map = new HashMap<String, Object>();
		String channelFTL = "channel.ftl";
		String singleFTL = "single.ftl";
		String relaPath = "";
		String htmlName = id + ".html";
		map.put("entity", entity);

		List<Article> latestArticles = new ArrayList<Article>();
		List<Article> mostVisitArticles = new ArrayList<Article>(); // visit
		LinkedHashMap<String, String> orderbyId = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyVisitTotal = new LinkedHashMap<String, String>();
		orderbyId.put("id", "desc");
		orderbyVisitTotal.put("visitTotal", "desc");
		
		if (articleService == null) {
			ApplicationContext cxt = WebApplicationContextUtils.getWebApplicationContext(context);
			articleService = (ArticleService) cxt.getBean("articleServiceImpl");
		}
		
		latestArticles = articleService.getScrollData(0, 15, "o.checkState=?1",
				new Object[] { CheckState.pass }, orderbyId).getResultlist();
		mostVisitArticles = articleService.getScrollData(0, 15,
				"o.checkState=?1", new Object[] { CheckState.pass },
				orderbyVisitTotal).getResultlist();
		map.put("latestArticles", latestArticles);
		map.put("mostVisitArticles", mostVisitArticles);

		if (entity.getSingle() != null && entity.getSingle() == true) {
			try {
				creatHtml.init(singleFTL, htmlName, map, relaPath);// 生成静态HTML
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			return "succ";
		} else {
			PageView<Article> pageView = new PageView<Article>(1);
			int firstindex = (pageView.getCurrentpage() - 1)
					* pageView.getMaxresult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			List<Object> params = new ArrayList<Object>();
			QueryResult<Article> qr;

			orderby.put("id", "desc");
			StringBuffer jpql = new StringBuffer("o.checkState=?1");
			params.add(CheckState.pass);
			if (id != 0 && id != 1) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.channel=?").append((params.size() + 1));
				params.add(new Channel(id));
			}
			qr = articleService.getScrollData(firstindex,
					pageView.getMaxresult(), jpql.toString(), params.toArray(),
					orderby);
			pageView.setQueryResult(qr);
			try {
				map.put("pageView", pageView);
				creatHtml.init(channelFTL, htmlName, map, relaPath);// 生成静态HTML
				relaPath = "Channel/";
				htmlName = id + "_1.html";// 在Channel目录下生成第一个页面
				creatHtml.init(channelFTL, htmlName, map, relaPath);// 生成静态HTML
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
			
			for (int page = 2; page <= pageView.getTotalpage(); page++) {
				htmlName = id + "_" + page + ".html";// 有页码的HTML的名称
				pageView.setCurrentpage(page);
				firstindex = (pageView.getCurrentpage() - 1)
						* pageView.getMaxresult();
				qr = articleService.getScrollData(firstindex,
						pageView.getMaxresult(), jpql.toString(),
						params.toArray(), orderby);
				pageView.setQueryResult(qr);
				try {
					map.put("pageView", pageView);
					new CreateHtml(context).init(channelFTL, htmlName, map,
							relaPath);// 生成静态HTML
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TemplateException e) {
					e.printStackTrace();
				}

			}

			return "succ";
		}
	}

	public String makeSingleChannelToHtml(int id, ServletContext context) {
		Channel entity = entityService.find(id);
		return makeSignleChannelToHtml(entity, context);
	}

}
