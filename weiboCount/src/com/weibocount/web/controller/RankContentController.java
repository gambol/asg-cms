/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.web.controller;

import com.weibocount.common.PageResult;
import com.weibocount.common.ServerInfoDetail;
import com.weibocount.web.common.RankPageQuery;
import com.weibocount.web.dao.ServerQuerier;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author xiang.fu
 */
@Controller
@RequestMapping("/rank/")
public class RankContentController {

    @RequestMapping(value = "rankPage.htm")
     public String getRankPage(Map<String,Object> model) {                
//     public void getRankPage(@RequestBody RankPageQuery rpq, HttpServletRequest request, HttpServletResponse response) {                
        ServerQuerier sq = new ServerQuerier();
//        int totalCount = sq.getServerCountByCategory(rpq.getCategoryId());
        int totalCount = sq.getServerCountByCategory("1");
//        List<ServerInfoDetail> retList = sq.getServerInfoDetailPageByCategory(rpq.getCategoryId(), rpq.getStart(),rpq.getSize());
        List<ServerInfoDetail> retList = sq.getServerInfoDetailPageByCategory("1", 0,50);
        int curPage = (0 +1 ) / 50;
        PageResult pr = new PageResult<ServerInfoDetail>(totalCount, curPage,50, retList);
        model.put("pageResult", pr);
        return "index";
        
        
//        return new PageResult<ServerInfoDetail>(totalCount, curPage, rpq.getSize(), retList);
        
    }
//    public @ResponseBody
//    PageResult<ServerInfoDetail> getRankPage(@RequestBody RankPageQuery rpq, HttpServletRequest request, HttpServletResponse response) {                
//        ServerQuerier sq = new ServerQuerier();
//        int totalCount = sq.getServerCountByCategory(rpq.getCategoryId());
//        List<ServerInfoDetail> retList = sq.getServerInfoDetailPageByCategory(rpq.getCategoryId(), rpq.getStart(),rpq.getSize());
//        int curPage = (rpq.getStart() +1 ) / rpq.getSize();
//        return new PageResult<ServerInfoDetail>(totalCount, curPage, rpq.getSize(), retList);
//        
//    }
}
