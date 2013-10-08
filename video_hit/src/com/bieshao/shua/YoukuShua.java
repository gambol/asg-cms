package com.bieshao.shua;

import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.JsonUtil;
import cn.bieshao.utils.PoolHttpGet;
import cn.bieshao.utils.MultiThreadTool;
import cn.bieshao.utils.PostUtil;

import hashcash.HashCashBak;
import hashcash.YoukuHashCash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;

/**
 * 具体请看YoukuShuaThread里的实现
 * @author zhenbao.zhou
 *
 */
public class YoukuShua extends Shua {
    private static final Logger logger = Logger.getLogger(YoukuShua.class);
 
    private final static Pattern URL_ID_PATTERN = Pattern.compile("http://v.youku.com/v_show/id_(.+).html");
    
    private  String id;
    
    public YoukuShua() {
        super();
    }
    
    public YoukuShua(String url, int num) {
        this.url = url.trim();
        this.num = num;
        id = null;
    }
    
    @Override
    public void doJob() {
        logger.info("do job " + num + " times. url:" + url);
        try {
            Matcher m = URL_ID_PATTERN.matcher(url);
            if (m.find()) {
                id = m.group(1);
            }             
            
            if (id == null) {
                logger.debug("error in get id");
                return;
            }
            
            MultiThreadTool pho = MultiThreadTool.getInstance();

            pho.multiRun(num, YoukuShuaImpl.class, id);
              //  Thread.sleep(SLEEP_TIME);
            //  pho.destroy();
            
            /*
            YoukuShuaImpl ys = new YoukuShuaImpl();
            ys.setYoukuId(id);
            for(int i = 0; i < num; i++) {
               ys.run();
            }
            System.out.println("succtimes:" + YoukuShuaImpl.succTimes);
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }           
    
    public static void main(String[] args) {
        YoukuShua ys = new YoukuShua("http://v.youku.com/v_show/id_XNjE1NjI2OTE2.html", 20);
        ys.doJob();
    }
}
