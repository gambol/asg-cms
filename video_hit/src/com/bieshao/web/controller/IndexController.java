/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.controller;

import cn.bieshao.common.Cryptor;
import cn.bieshao.common.PageResult;
import cn.bieshao.utils.IPUtils;

import com.bieshao.model.ServerInfo;
import com.bieshao.model.ServerSysInfo;
import com.bieshao.web.common.RankPageQuery;
import com.bieshao.web.common.SessionConst;
import com.bieshao.web.dao.CategoryDao;
import com.bieshao.web.dao.TodoJobDao;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
      
	private final static int DEFAULT_NUM = 500;
	private final static int MAX_NUM = 3000;
	
    public IndexController() {
      
    }
    
    @RequestMapping("index")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		return  "index";
	}
    
  
    @RequestMapping(value="tudou",method = RequestMethod.GET)
	public String tudouIndex(HttpServletRequest request,
			HttpServletResponse response, Model model) {
    	return getCommonFunction(request, response, model, "/tudou.htm", "土豆视频");
	}
	
    @RequestMapping(value="tudou",method = RequestMethod.POST)
	public String tudouPost(HttpServletRequest request,
			HttpServletResponse response, Model model) {
    	return postCommonFunction(request, response, model, "/tudou.htm", "TudouShua", "土豆视频");
	}
     
    @RequestMapping(value="sina",method = RequestMethod.GET)
 	public String sinaIndex(HttpServletRequest request,
 			HttpServletResponse response, Model model) {
    	return getCommonFunction(request, response, model, "/sina.htm", "新浪视频");
 	}
 	
    @RequestMapping(value="sina",method = RequestMethod.POST)
 	public String sinaPost(HttpServletRequest request,
 			HttpServletResponse response, Model model) {   	
    	return postCommonFunction(request, response, model, "/sina.htm", "SinaShua", "新浪视频");
 	}
    
    @RequestMapping(value="sohu",method = RequestMethod.GET)
   	public String suhoIndex(HttpServletRequest request,
   			HttpServletResponse response, Model model) {
      	return getCommonFunction(request, response, model, "/sohu.htm", "搜狐视频");
   	}
   	
    @RequestMapping(value="sohu",method = RequestMethod.POST)
   	public String sohuPost(HttpServletRequest request,
   			HttpServletResponse response, Model model) {   	
      	return postCommonFunction(request, response, model, "/sina.htm", "SohuShua", "搜狐视频");
   	}
    
    @RequestMapping(value="56",method = RequestMethod.GET)
   	public String wuliuIndex(HttpServletRequest request,
   			HttpServletResponse response, Model model) {
      	return getCommonFunction(request, response, model, "/56.htm", "56视频");
   	}
   	
    @RequestMapping(value="56",method = RequestMethod.POST)
   	public String wuliuPost(HttpServletRequest request,
   			HttpServletResponse response, Model model) {   	
      	return postCommonFunction(request, response, model, "/56.htm", "WuliuShua", "56视频");
   	}
    
    @RequestMapping(value="ku6",method = RequestMethod.GET)
   	public String ku6Index(HttpServletRequest request,
   			HttpServletResponse response, Model model) {
      	return getCommonFunction(request, response, model, "/ku6.htm", "酷6视频");
   	}
   	
    @RequestMapping(value="ku6",method = RequestMethod.POST)
   	public String ku6Post(HttpServletRequest request,
   			HttpServletResponse response, Model model) {   	
      	return postCommonFunction(request, response, model, "/ku6.htm", "Ku6Shua", "酷6视频");
   	}
    
    /**
     * 展示各个页面的函数
     * @param className
     * @param stepName
     */
    private String getCommonFunction(HttpServletRequest request,
 			HttpServletResponse response, Model model, String url, String stepName) {
    	  	HttpSession session = request.getSession();
     	String newNumStr = request.getParameter("n2");
     	
     	int originNum = DEFAULT_NUM;
     	try {
     		originNum = (Integer)session.getAttribute("num");
     	} catch(Exception e){
      	}
     	
     	int addNum = Cryptor.decryptAsInt(newNumStr);
     	
     	if (Math.abs(addNum) == DEFAULT_NUM) {
     		originNum += addNum;
     	}
     	if (originNum >= MAX_NUM) {
     		originNum = MAX_NUM;
     	} else if (originNum <= 0) {
     		originNum = DEFAULT_NUM;
     	}
     	
     	session.setAttribute(SessionConst.NUM, originNum);   	
     	model.addAttribute(SessionConst.NUM, originNum);
     	model.addAttribute(SessionConst.POST_URL, url);
     	model.addAttribute(SessionConst.LAST_STEP_NAME, stepName);

     	model.addAttribute("deleteUrl", url + "n2=" + Cryptor.encryptedM500);
     	model.addAttribute("addUrl", url + "?n2=" + Cryptor.encrypted500);
 		return  "url_form";
    }
    
    /**
     * 处理post的方法
     * @param request
     * @param response
     * @param model
     * @return
     */
    private String postCommonFunction(HttpServletRequest request,
 			HttpServletResponse response, Model model, String returnUrl,
 			String className, String stepName) {
     	HttpSession session = request.getSession();
     	String url = request.getParameter("url");
     	if (url == null) {
     		return "url_form";
     	}
     	model.addAttribute("url", url);
     	
     	int num = DEFAULT_NUM;
     	try {
     		num = (Integer)session.getAttribute(SessionConst.NUM);
     	} catch(Exception e){
     		e.printStackTrace();
     	}
     	
     	TodoJobDao.insertTodoJob(url, num, IPUtils.getUserIPString(request), className);    	
     	
     	session.setAttribute(SessionConst.NUM, DEFAULT_NUM);
     	model.addAttribute(SessionConst.LAST_STEP_URL, returnUrl);
     	model.addAttribute(SessionConst.LAST_STEP_NAME,  stepName);
 		return  "commit_succ";
 	}
    
}
