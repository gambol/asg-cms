/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import java.io.IOException;
import java.net.Inet4Address;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.weibocount.common.PageResult;
import com.weibocount.model.ServerInfo;
import com.weibocount.utils.JDBCUtils;
import com.weibocount.web.common.InterfaceReturnValue;
import com.weibocount.web.common.PublishBean;
import com.weibocount.web.common.SessionConst;
import com.weibocount.web.dao.ServerDao;
import com.weibocount.web.dao.ServerQuerier;

/**
 *
 * @author zhenbao.zhou
 */

@Controller
@RequestMapping("/user/")
public class SiteManageController {
    
    @RequestMapping(value="sitemanage", method = RequestMethod.GET)
    public String form(HttpServletRequest request, HttpServletResponse response, 
    Model model) throws ServletException, IOException {
        int userId = 0;
        try {
            userId = (Integer)request.getSession().getAttribute(SessionConst.USERID);
        } catch(Exception e) {
            e.printStackTrace();
            return "redirect:/user/login.htm";
        }
        
       // int currentPage = offset/20 + 1;
        int currentPage = 1;
        String offset = request.getParameter("pager.offset");
        if (offset != null) {
            try {
                currentPage = Integer.parseInt(offset) / 20 + 1;
            } catch (Exception e) {
                
            }
        }
        // 查询db
        PageResult<ServerInfo> serverInfoResults = new ServerQuerier().getServerInfoByUserId(userId, currentPage);
        
        model.addAttribute("serverInfos", serverInfoResults);
        return "sitemanage";
    }
    
    @RequestMapping("/sitemanage/changeStatus")
    public @ResponseBody InterfaceReturnValue changeDisplayStatus(HttpServletRequest request, HttpServletResponse response,
    Model model) throws ServletException, IOException {
        int userId = 0;
        int serverId = 0;
        InterfaceReturnValue returnValue = new InterfaceReturnValue();
        try {
             userId = (Integer)request.getSession().getAttribute(SessionConst.USERID);
            serverId = Integer.parseInt((String)request.getParameter("id"));
        } catch(Exception e) {
            e.printStackTrace();
            return returnValue;
        }
        
        int updatedRows = ServerDao.changeDisplayStatus(serverId, userId);
       
        returnValue.setRet(updatedRows == 1);
        returnValue.setData(updatedRows);
        return returnValue;
    }
}
