
package com.weibocount.web.servlet;


import com.weibocount.web.common.SessionConst;

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
 * @author zhenbao.zhou
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
        
        Object at =  session.getAttribute(SessionConst.AT);
        System.out.println("filter code:" + at);
        
        if(at == null){
            res.sendRedirect("/login.htm"); 
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
