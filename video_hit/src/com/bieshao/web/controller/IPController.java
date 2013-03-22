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
import com.bieshao.web.service.JobLimitService;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 访问这个url，返回你的ip
 * 主要用来判断一个代理是否是匿名代码
 * @author zhenbao.zhou
 *
 */
@Controller
@RequestMapping("/ip/")
public class IPController {
    
    @RequestMapping("realIp")
    public @ResponseBody String index(HttpServletRequest request) {
        return IPUtils.getUserIPString(request);
    }
    
}
