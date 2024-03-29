package org.j2cms.web.action.manage.article;

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
import org.apache.struts2.convention.annotation.ExceptionMappings;
import org.apache.struts2.convention.annotation.ExceptionMapping;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.j2cms.model.CheckState;
import org.j2cms.model.PageView;
import org.j2cms.model.QueryResult;
import org.j2cms.model.article.Article;
import org.j2cms.model.channel.Channel;
import org.j2cms.model.group.Group;
import org.j2cms.service.ArticleService;
import org.j2cms.service.ChannelService;
import org.j2cms.service.GroupService;
import org.j2cms.utils.Struts2Utils;
import org.j2cms.web.action.EntityAction;

import org.j2cms.utils.CreateHtml;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import freemarker.template.TemplateException;

//@ResultPath("/Article")
//@Namespace("/article") 
@Results({
		@Result(name = "add", location = "add.jsp"),
		@Result(name = "edit", location = "edit.jsp"),
		@Result(name = "toList", location = "toList.jsp"),
		@Result(name = "succ", type = "freemarker", location = "/WEB-INF/content/util/succ.ftl"),
		@Result(name = "error", type = "freemarker", location = "/WEB-INF/content/util/error.ftl"),
		@Result(name = "errorPage", type = "freemarker", location = "/WEB-INF/content/util/errorPage.ftl"),
		@Result(name = "ok", type = "freemarker", location = "/template/biemian/article.html"),
		@Result(name = "urlRedirect", type = "freemarker", location = "/WEB-INF/content/util/urlRedirect.ftl") })
@ExceptionMappings({ @ExceptionMapping(exception = "java.sql.SQLException", result = "error", params = {
		"message", "操作数据库失败！" }) })
@SuppressWarnings("unused")
public class ArticleAction extends EntityAction<Article> {
	private static final long serialVersionUID = 2265855246102250818L;
	@Resource
	private ChannelService channelService;
	@Resource
	private GroupService groupService;

	@Resource
	private ArticleService articleService;

	private Article selector = new Article();
	private String sortType = "id";// 排序类型
	private String sortord = "desc";
	private List<Group> groups = new ArrayList<Group>(); // 会员组
	private List<Channel> channels = new ArrayList<Channel>();// 栏目组

	public String getSortord() {
		return sortord;
	}

	public void setSortord(String sortord) {
		this.sortord = sortord;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public PageView<Article> getPageView() {
		return pageView;
	}

	public void setPageView(PageView<Article> pageView) {
		this.pageView = pageView;
	}

	public Article getSelector() {
		return selector;
	}

	public void setSelector(Article selector) {
		this.selector = selector;
	}

	public List<Group> getGroups() {
		return groupService.getScrollData().getResultlist();
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Channel> getChannels() {
		return channelService.getScrollData().getResultlist();
	}

	public void setChannels(List<Channel> channels) {
		this.channels = channels;
	}

	@Actions({ @Action("/article") })
	public String execute() throws Exception {// o.father.id=1 改成这样简单
		Map<String, Object> map = new HashMap<String, Object>();
		List<Article> latestArticles = new ArrayList<Article>();
		List<Article> mostVisitArticles = new ArrayList<Article>(); // visit
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
	//	LinkedHashMap<String, String> orderbyVisitTotal = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		//orderbyVisitTotal.put("visitTotal", "desc");

		try {
			latestArticles = articleService.getScrollData(0, 30,
					"o.checkState=?1", new Object[] { CheckState.pass },
					orderby).getResultlist();
			/*
			mostVisitArticles = articleService.getScrollData(0, 15,
					"o.checkState=?1", new Object[] { CheckState.pass },
					orderbyVisitTotal).getResultlist();
			*/
			map.put("latestArticles", latestArticles);
//			map.put("mostVisitArticles", mostVisitArticles);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (entity != null && entity.getCheckState() == CheckState.pass) {
			entity.setVisitTotal(entity.getVisitTotal() + 1);// 点击数加1
			try {
				
				map.put("entity", entity);
				//new CreateHtml().init("article.html", id + ".html", map,
					//	"Article/");
				new CreateHtml().output("article.html", map, ServletActionContext.getResponse().getWriter());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}// 生成静态HTML
			Struts2Utils.setAttribute("url", "/Article/" + id + ".html");
			//return "urlRedirect";
			return "ok";
		} else {
			Struts2Utils.setAttribute("message", "该信息不存在或未通过审核！");
			return "errorPage";
		}

	}

	private boolean checkEntityForm() {
		if (entity.getTitle() == null || entity.getTitle().equals("")) {
			Struts2Utils.setAttribute("message1", "标题不能为空！");
			return false;
		}
		if (entity.getAuthor() == null || entity.getAuthor().equals("")) {
			Struts2Utils.setAttribute("message2", "作者不能为空！");
			return false;
		}
		if (entity.getReleaseDate() == null
				|| entity.getReleaseDate().equals("")) {
			Struts2Utils.setAttribute("message3", "发布时间不能为空！");
			return false;
		}

		return true;
	}

	@Actions({ @Action("update") })
	public String update() {
		try {
			if (!checkEntityForm())
				return "edit";
			super.update();
			return "toList";
		} catch (Exception e) {
			return ERROR;
		}

	}

	@Actions({ @Action("save") })
	public String save() {
		try {
			if (!checkEntityForm())
				return "add";
			super.save();
			return "toList";
		} catch (Exception e) {
			return ERROR;
		}

	}

	@Actions({ @Action("list") })
	public String list() {
		QueryResult<Article> qr;
		StringBuffer jpql = new StringBuffer("");// "1=1"?
		List<Object> params = new ArrayList<Object>();

		if (selector != null && selector.getId() != null) {
			jpql.append(" o.id=?").append((params.size() + 1));
			params.add(selector.getId());
			qr = entityService.getScrollData(0, 1, jpql.toString(),
					params.toArray());
		} else {
			int firstindex = (pageView.getCurrentpage() - 1)
					* pageView.getMaxresult();
			LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
			orderby.put(sortType == null ? "id" : sortType, sortord);
			if (selector.getChannel() != null
					&& selector.getChannel().getId() != null
					&& selector.getChannel().getId() != 1) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.channel=?").append((params.size() + 1));
				params.add(selector.getChannel());
			}
			if (selector.getCheckState() != null
					&& !"".equals(selector.getCheckState())) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.checkState=?").append((params.size() + 1));
				params.add(selector.getCheckState());
			}
			if (selector.getRecommendLevel() != null
					&& !"".equals(selector.getRecommendLevel())) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.recommendLevel=?").append((params.size() + 1));
				params.add(selector.getRecommendLevel());
			}
			if (selector.getReleaseDate() != null
					&& !"".equals(selector.getReleaseDate())) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.releaseDate=?").append((params.size() + 1));
				params.add(selector.getReleaseDate());
			}
			if (selector.getUser() != null
					&& !"".equals(selector.getUser().getUsername())) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.user.username=?").append((params.size() + 1));
				params.add(selector.getUser().getUsername());
			}
			if (selector.getTitle() != null && !"".equals(selector.getTitle())) {
				if (params.size() > 0)
					jpql.append(" and ");
				jpql.append(" o.title like ?").append((params.size() + 1));
				params.add("%" + selector.getTitle() + "%");
			}
			qr = entityService.getScrollData(firstindex,
					pageView.getMaxresult(), jpql.toString(), params.toArray(),
					orderby);
		}
		pageView.setQueryResult(qr);

		return SUCCESS;
	}

	@Action(value = "makeHtml")
	public String makeHtml() {
		Map<String, Object> map = new HashMap<String, Object>();
		CreateHtml createHtml = new CreateHtml();
		String ftl = "article.html";
		String relaPath = "Article/";
		int count = 0;
		List<Article> latestArticles = new ArrayList<Article>();
		List<Article> mostVisitArticles = new ArrayList<Article>(); // visit
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyVisitTotal = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		orderbyVisitTotal.put("visitTotal", "desc");
		latestArticles = articleService.getScrollData(0, 8, "o.checkState=?1",
				new Object[] { CheckState.pass }, orderby).getResultlist();
		mostVisitArticles = articleService.getScrollData(0, 10,
				"o.checkState=?1", new Object[] { CheckState.pass },
				orderbyVisitTotal).getResultlist();
		map.put("latestArticles", latestArticles);
		map.put("mostVisitArticles", mostVisitArticles);

		try {
			for (Integer id : ids) {
				String htmlName = id + ".html";
				Article entity = entityService.find(id);
				if (entity != null && entity.getCheckState() == CheckState.pass) {
					map.put("entity", entity);
					createHtml.init(ftl, htmlName, map, relaPath);
					count++;
				}
			}
			Struts2Utils.setAttribute("message", "已在" + relaPath + "目录生成个"
					+ count + "个html文件");
			return "succ";
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return ERROR;
	}

	/**
	 * 自动脚本里， 如果有没有生成html的文章，则重新生成
	 * 
	 * @param context
	 * @return
	 */
	public void dealNewArticle(ServletContext context) {
		Map<String, Object> map = new HashMap<String, Object>();
		CreateHtml createHtml = new CreateHtml(context);
		String ftl = "article.html";
		String relaPath = "Article/";
		int count = 0;
		List<Article> latestArticles = new ArrayList<Article>();
		List<Article> mostVisitArticles = new ArrayList<Article>(); // visit
		LinkedHashMap<String, String> orderby = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyVisitTotal = new LinkedHashMap<String, String>();
		orderby.put("id", "desc");
		orderbyVisitTotal.put("visitTotal", "desc");

		ApplicationContext cxt = WebApplicationContextUtils
				.getWebApplicationContext(context);
		ArticleService newArtService = (ArticleService) cxt
				.getBean("articleServiceImpl");

		latestArticles = newArtService.getScrollData(0, 8, "o.checkState=?1",
				new Object[] { CheckState.pass }, orderby).getResultlist();
		mostVisitArticles = newArtService.getScrollData(0, 10,
				"o.checkState=?1", new Object[] { CheckState.pass },
				orderbyVisitTotal).getResultlist();
		map.put("latestArticles", latestArticles);
		map.put("mostVisitArticles", mostVisitArticles);

		int startRows = 0;
		int rowsPerQuery = 100;
		int rightNowQuery = startRows + rowsPerQuery;
	
		while(true) {
			QueryResult<Article> articleResult = newArtService.getScrollData(
				startRows, rightNowQuery, "o.checkState=?1",
				new Object[] { CheckState.pass }, orderby);
			try {
				
				for (Article entity : articleResult.getResultlist()) {
					String htmlName = entity.getId() + ".html";
					if (entity != null
							&& entity.getCheckState() == CheckState.pass) {
						map.put("entity", entity);
						count += createHtml.initNew(ftl, htmlName, map, relaPath, false);
					}
				}
				startRows += rowsPerQuery;
				rightNowQuery += rowsPerQuery;
				
				if (articleResult.getTotalrecord() < rowsPerQuery) {
					System.out.println("总共生成了:" + count +"篇文章");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		} 
	}

}
