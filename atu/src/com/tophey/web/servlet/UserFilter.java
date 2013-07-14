/*
 * Copyright(c) 2007-2010 by Yingzhi Tech.
 * All Rights Reserved
 */
package com.tophey.web.servlet;


import com.tophey.web.common.SessionConst;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author xiang.fu
 */
public class UserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        String username = (String) session.getAttribute(SessionConst.USERNAME);
        if(username == null){
            res.sendRedirect("/user/login.htm"); 
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
