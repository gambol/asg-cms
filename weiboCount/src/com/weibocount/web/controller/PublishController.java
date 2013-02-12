/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import com.weibocount.model.ServerInfo;
import com.weibocount.model.ServerSysInfo;
import com.weibocount.utils.DateUtil;
import com.weibocount.web.common.PublishBean;
import com.weibocount.web.common.SessionConst;
import com.weibocount.web.dao.ServerDao;

/**
 * 用户主动发布站点
 *
 * @author zhenbao.zhou
 */
@Controller
@RequestMapping("/user/publish")
@SessionAttributes("publisBean")
public class PublishController {
    final private Logger logger = Logger.getLogger(this.getClass().getName());

    public static HashMap NETWORK_MAP = new HashMap();
    
    static {
        NETWORK_MAP.put(0, "网通");
        NETWORK_MAP.put(1, "电信");
        NETWORK_MAP.put(2, "双线");
        NETWORK_MAP.put(3, "联通");
        NETWORK_MAP.put(4, "铁通");
        
        NETWORK_MAP.put("网通", 0);
        NETWORK_MAP.put("电信", 1);
        NETWORK_MAP.put("双线", 2);
        NETWORK_MAP.put("联通", 3);
        NETWORK_MAP.put("铁通", 4);
    }
    // Invoked initially to create the "form" attribute
    // Once created the "form" attribute comes from the HTTP session (see @SessionAttributes)

    @ModelAttribute("publishBean")
    public PublishBean createFormBean() {
        return new PublishBean();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String form(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        int userId = 0;
        int serverId = 0;
        String username = (String)request.getSession().getAttribute(SessionConst.USERNAME);
        System.out.println("username:" + username);
        PublishBean pb = new PublishBean();
     
        try {
            userId = (Integer)request.getSession().getAttribute(SessionConst.USERID);
        } catch(Exception e) {
            e.printStackTrace();
            return "redirect:/user/login.htm";
        }
        
        try {
            
            String strId = request.getParameter("id");
            if (strId != null)
                serverId = Integer.parseInt(request.getParameter("id"));
        } catch(Exception e) {
            e.printStackTrace();
            pb.setNetwork("双线");
            model.addAttribute("publishBean", pb);
            return "publish";
        }
        
        // 查询db
        ServerInfo si = ServerDao.getServerInfoById(serverId);
        if (si == null) {
            pb.setNetwork("双线");
            model.addAttribute("publishBean", pb);
            return "publish";
        }
        
        pb.setServer_name(si.getName());
        pb.setBanner(si.getBannerUrl());
        pb.setCategory(si.getCategoryId());
        pb.setNetwork(si.getLine());

        pb.setDesc(si.getDescription());
        pb.setUrl(si.getUrl());
        pb.setId(si.getId());
        
        model.addAttribute("publishBean", pb);
        return "publish";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processSubmit(@Valid PublishBean publishBean, BindingResult result,
    HttpServletRequest request, HttpServletResponse response, 
            Model model) {

        if (result.hasErrors()) {
            System.err.println(result.toString() + "   " + (result.getAllErrors()).get(0).toString());
            return null;
        }
        
        int userId = 0;
        try {
            userId = (Integer)request.getSession().getAttribute(SessionConst.USERID);
        } catch(Exception e) {
            e.printStackTrace();
            return "redirect:/user/login.htm";
        }
        
        if (userId == 0) {
            logger.log(Level.WARNING, "user not logined");
            return "redirect:/user/login.htm";
        }

        ServerInfo server = new ServerInfo();
 //       server.setTitle(publishBean.getServer_name());
        server.setBannerUrl(publishBean.getBanner());
        server.setCategoryId(publishBean.getCategory());

        server.setLine(publishBean.getNetwork());
        server.setName(publishBean.getServer_name());
        server.setUrl(publishBean.getUrl());
        
        server.setSiteFrom("atu");
        server.setCreateDate(DateUtil.getCurrentTimestamp());
        server.setDescription(publishBean.getDesc());
        server.setUserId(userId);
        
        int sid  = -1;
        try {
            sid = publishBean.getId();
        } catch(Exception ex) {
            
        }
        
        if (sid <= 0) {
            ServerDao.insert(server);            
        } else {
 //           server.setId(sid);
            if (server.getUserId() != userId) {
                logger.log(Level.WARNING, "server对应的userId 和 新的UserId对应不上. old userId:" + server.getUserId() + " new UserId:" + userId);
                model.addAttribute("message", "session过期");
                return null;
            }
            
            ServerDao.update(server);
        }
        
        
        
        String message = "私服发布成功, 私服url:" + server.getUrl();
        // Success response handling

        // prepare model for rendering success message in this request
  //      model.addAttribute("message", message);
        // 确认这个页面之后，需要跳转到哪个页面?
        return "redirect:/user/sitemanage.htm";

    }
}
