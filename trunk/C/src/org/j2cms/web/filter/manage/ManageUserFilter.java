package org.j2cms.web.filter.manage;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.j2cms.model.user.User;
import org.j2cms.utils.WebUtil;


public class ManageUserFilter implements Filter {
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		User user = WebUtil.getUser(request);
		if (user.getGroup()==null||user.getGroup().getManageUser()!= true) {
			HttpServletResponse response = (HttpServletResponse) res;
			response.sendRedirect("/util/403");
			return;
		}
		filterChain.doFilter(req, res);
	}
	public void init(FilterConfig arg0) throws ServletException {}
	public void destroy() {}

}
