package com.bieshao.web.service;

import com.bieshao.web.dao.TodoJobDao;

/**
 * 对用户提交的Ｊｏｂ进行奖赏或者限制
 * @author zhenbao.zhou
 *
 */
public class JobLimitService {
    
    private final static int NEW_USER  = 1;
    private final static int FIRST_THRESHOLD = 2;
    private final static int SECOND_THRESHOLD = 10;
    
    /**
     * 如果这个用户ＩＰ提交任务次数的，则可以享受３０００次服务
     * 如果这个ＩＰ今天提交任务超过１００次，我们会默认给他降权成５０次
     * 如果超过３００次，　降权成２次
     * @param ip
     * @return
     */
    public static int getMaxNumForIp(String ip) {
        int jobNumForToday = TodoJobDao.getJobNumFormIpToday(ip);
        if (jobNumForToday < NEW_USER) {
            return 3000;
        } else if (NEW_USER <= jobNumForToday  && jobNumForToday < FIRST_THRESHOLD) {
            return 2000;
        } else if (jobNumForToday >= FIRST_THRESHOLD && jobNumForToday < SECOND_THRESHOLD) {
            return 100;
        } else {
            return 10;
        }
    }
    
    public static void main(String[] args) {
        System.out.println(getMaxNumForIp(""));
    }
}
