/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import com.weibocount.common.PageResult;
import com.weibocount.common.ServerInfoDetail;
import com.weibocount.model.Category;
import com.weibocount.model.ServerInfo;
import com.weibocount.model.ServerSysInfo;
import com.weibocount.web.common.RankPageQuery;
import com.weibocount.web.common.SessionConst;
import com.weibocount.web.dao.CategoryDao;
import com.weibocount.web.dao.ServerQuerier;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo.gambol.WeiboCountResult;
import weibo.gambol.WeiboCountService;
import weibo4j.Oauth;
import weibo4j.examples.oauth2.Log;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

/**
 *
 * @author zhenbao.zhou
 */
@Controller
@RequestMapping("/")
public class IndexController  {

    public final static int PAGE_SIZE = 20;
    
    public IndexController() {
      
    }
    
    @RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) {
    	System.out.println("index");
		HttpSession session = request.getSession();
		Object access = session.getAttribute(SessionConst.AT);
		if (access == null) {
			return "index";	
		} else {
			try {
				response.sendRedirect("/count.htm");
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		
		return  null;
	}
    
    
	@RequestMapping("pre")
	public String pre(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		System.out.println("pre");
		HttpSession session = request.getSession();
		Object access = session.getAttribute(SessionConst.AT);
		if (access == null) {
			String code = request.getParameter("code");
			if (code == null) {
				 response.sendRedirect("/login.htm"); 
				 return null;
			}

			Oauth oauth = new Oauth();
			Log.logInfo("code: " + code);
			AccessToken at = null;
			try {
				at = oauth.getAccessTokenByCode(code);
			} catch (WeiboException e) {
				if (401 == e.getStatusCode()) {
					Log.logInfo("Unable to get the access token.");
				} else {
					e.printStackTrace();
				}
				return "index";
			}

			//aa466c4feb9f147c03f55befe6b7e5b4 
			session.setAttribute(SessionConst.AT, at);
			
		}
		return "redirect:/count.htm";
	}
    
    @RequestMapping("login")
    public void login(HttpServletRequest request, HttpServletResponse response, Model model) {
    	Oauth oauth = new Oauth();
    	try {
    		response.sendRedirect(oauth.authorize("code"));
    	} catch (Exception e){
    		e.printStackTrace();
    	} 
    	
    	//return new ModelAndView("index");
    }
    
     @RequestMapping("count")
    public String detail(HttpServletRequest request, HttpServletResponse response, Model model){
       String url = request.getParameter("url");
       AccessToken at = (AccessToken)request.getSession().getAttribute(SessionConst.AT);
       String  wid = null;
       
       if (url == null || at == null) {
    	   return "pre";
       } else {
    	   if (url.startsWith("http")) {
    		   // url = http://weibo.com/1401880315/zizFsbX4Q
    		   String[] strs = url.split("/");
    		   if (strs.length > 1) {
    			   wid = strs[strs.length - 1];
    		   }
    	   } else {
    		   wid = url;
    	   }
    	   
    	   if (wid == null) {
    		   return "pre";
    	   }
    	   
    	   try {
    		   WeiboCountService wcs = new WeiboCountService(at); 
    		   WeiboCountResult wcr = wcs.countWeibo(wid);
    		   model.addAttribute("wcr", wcr);
    		   model.addAttribute("url", url);
    		   return "count";
    	   } catch (Exception e) {	
    		   e.printStackTrace();
    		   return "pre";
    	   }
       }
    }
    
   
    @RequestMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        session.removeAttribute(SessionConst.AT);
        return "index";
    }
    
    @RequestMapping("tos")
    public String agreement(HttpServletRequest request, HttpServletResponse response){
        return "tos";
    }
    
}
