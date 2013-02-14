/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.controller;

import cn.bieshao.common.PageResult;

import com.bieshao.model.Category;
import com.bieshao.model.ServerInfo;
import com.bieshao.model.ServerSysInfo;
import com.bieshao.web.common.RankPageQuery;
import com.bieshao.web.common.SessionConst;
import com.bieshao.web.dao.CategoryDao;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;



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
