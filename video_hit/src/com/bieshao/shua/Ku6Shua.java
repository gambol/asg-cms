/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bieshao.shua;

import cn.bieshao.utils.MultiHttpGet;
import cn.bieshao.utils.PoolHttpGet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;

/**
 * 关键是取vid
 * http://www.56.com/p83/v_MTIwOTAzMDA4.htm
 * http://stat.56.com/stat/flv.php?id=MTIwOTAzMDA4&pct=3
 * @author zhenbao.zhou
 */
public class Ku6Shua extends Shua {

    private static final Logger logger = Logger.getLogger(Ku6Shua.class);
    // http://v.ku6.com/special/show_6557552/mgUkHRUrxHeI9_KEXuURrQ...html
    // http://v0.stat.ku6.com/dostatv.do?method=setVideoPlayCount&o=5438544&c=138000&v=mgUkHRUrxHeI9_KEXuURrQ..&rnd=0.7482359842397273
    private final static String URL_PREFIX = "http://v0.stat.ku6.com/dostatv.do?method=setVideoPlayCount&v=";
    private final static Pattern URL_ID_PATTERN = Pattern.compile("v.ku6.com/.*/(.*)\\.html");   // 从url中取出id
    // 一次最多发50个请求
    private final static int EVERY_STEP = 30;
    private final static int SLEEP_TIME = 100;
    private String id;
    private String shuaUrl;

    public Ku6Shua(String url, int num) {
        this.url = url.trim();
        this.num = num;
    }

    public Ku6Shua() {
    	super();
    }
    
    /**
     * 生成一个基本的url
     *
     * @throws Exception
     */
    public boolean generateShuaUrl() throws Exception {
        if (!url.contains("v.ku6.com")) {
            return false;
        }

        Matcher m = URL_ID_PATTERN.matcher(url);
        if (m.find()) {
            // 2个不同的地方，计数器地址页不同
            id = m.group(1);
            shuaUrl = URL_PREFIX + id;
        }         

        // logger.info(content);
        // content = "iid:123123 iid:4231";
        return (shuaUrl != null);
    }

    /**
     * 请求 的url
     */
    public void shua() throws Exception {
        int i = 0;
        logger.info("can on. shua!. url:" + shuaUrl);
        while (i <= num) {
            List<String> urls = new ArrayList<String>();
            for (int j = 0; j < EVERY_STEP; j++) {
                urls.add(shuaUrl);
            }

            i += EVERY_STEP;
            PoolHttpGet client = new PoolHttpGet(urls);
            try {
                client.multiGet();
            } catch (IOReactorException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread.sleep(SLEEP_TIME);
        }
    }

    @Override
    public void doJob() {
        try {
            if (generateShuaUrl()) {
                shua();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
      // SinaShua s = new SinaShua("http://video.sina.com.cn/v/b/97615043-1314731975.html", 20);
//         WuliuShua s = new WuliuShua("http://www.56.com/u44/v_ODY2NTgxMjE.html", 10);
            Ku6Shua s = new Ku6Shua("http://v.ku6.com/special/show_6579983/SSct4cmjXbESK-kfK9Ptqg...html", 200);
       //  SinaShua s = new SinaShua("http://video.sina.com.cn/p/music/v/2013/0117/094361978135.html", 20);
        try {
             if (s.generateShuaUrl()) {
                 s.shua();
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
        }
        
        System.out.println("hehe");
    }
}

