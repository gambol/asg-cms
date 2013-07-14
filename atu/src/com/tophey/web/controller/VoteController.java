/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tophey.web.controller;

import com.tophey.model.ServerInfo;
import com.tophey.model.ServerSysInfo;
import com.tophey.web.dao.ServerQuerier;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.tophey.utils.DateUtil;
import com.tophey.utils.JDBCUtils;

/**
 *
 * 投票接口 目前有的限制: 同一个cookie, 同一个IP 半个小时只能投票一次
 *
 * @author zhenbao.zhou
 */

@Controller
@RequestMapping("/")
public class VoteController {

    private final static Logger logger = Logger.getLogger(VoteController.class.getName());
    public final static int VOTE_INTERVAL = 1 * 60 * 1000; // 1分钟

    @RequestMapping("/vote")
    public @ResponseBody
    String handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String strId = request.getParameter("id");
        int id = 0;
        try {
            id = Integer.parseInt(strId);
        } catch (Exception e) {
        }

        if (id == 0) {
            return "";
        }

        boolean isTrue = false;
        boolean isNewIP = false;
        try {
            Long starttime = null;
            Calendar date = new GregorianCalendar();
            String ip = request.getRemoteAddr();
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                Cookie cookie = new Cookie(ip, ((Object) date.getTimeInMillis()).toString());
                cookie.setMaxAge(VOTE_INTERVAL / 1000);
                response.addCookie(cookie);
            } else {
                for (int i = 0; i < cookies.length; i++) {

                    if (cookies[i].getName().equalsIgnoreCase(ip.toString())) {
                        starttime = Long.parseLong(cookies[i].getValue());
                        isNewIP = true;
                    }
                }
            }
            if (!isNewIP) {//判断是否投过票
                Cookie cookie = new Cookie(ip, ((Object) date.getTimeInMillis()).toString());
                cookie.setMaxAge(VOTE_INTERVAL / 1000);
                response.addCookie(cookie);
            }
            if (starttime != null) {//如果投过票判断时间是否大于
                Long endtime = date.getTimeInMillis();
                System.out.println(starttime + "--" + endtime + "--" + VOTE_INTERVAL);
                if (endtime - starttime - VOTE_INTERVAL > 0) {
                    isTrue = true;
                }
            } else {
                isTrue = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            isTrue = false;
        }

        if (isTrue)//判断是否可以投票
        {
            // 更新db
            addDBVote(id);
            return "+1";
        } else {
            return "";
        }
    }
    
    /**
     * 给DB中对应server 投票数加1
     * @param serverId
     * @return 
     */
    public int addDBVote(int serverId) {
        ServerQuerier querier = new ServerQuerier();
        ServerSysInfo serverSysInfo = querier.getServerSysInfoById(serverId);
        
        if (serverSysInfo == null) {
            ServerInfo serverInfo = querier.getServerInfoById(serverId);
            if (serverInfo == null) {
                logger.log(Level.WARNING, "serverId:" + serverId  + "是错误的ID,没有相关站点");
                return 0;
            }
            
            serverSysInfo.setCategoryId(serverInfo.getCategoryId());
            serverSysInfo.setId(serverInfo.getId());
            serverSysInfo.setName(serverInfo.getName());
            serverSysInfo.setServerCreateTime(DateUtil.getCurrentTimestamp());
            serverSysInfo.setServerNewOpenTime(DateUtil.getCurrentTimestamp());
            serverSysInfo.setVoteIn(1);
        } else {
            serverSysInfo.setVoteIn(serverSysInfo.getVoteIn() + 1);
        }
        
        return JDBCUtils.update(serverSysInfo, false);
    }
    
    
}
