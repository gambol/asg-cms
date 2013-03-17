/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bieshao.shua;

import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.MultiHttpGet;
import cn.bieshao.utils.PoolHttpGet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

/**
 * 关键是取vid
 * http://www.56.com/p83/v_MTIwOTAzMDA4.htm
 * http://stat.56.com/stat/flv.php?id=MTIwOTAzMDA4&pct=3
 * @author zhenbao.zhou
 */
public class WuliuShua extends Shua {

    private static final Logger logger = Logger.getLogger(SohuShua.class);
    // http://my.tv.sohu.com/us/15991709/52395185.shtml
    // http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=52395185
    private final static String URL_PREFIX = "http://stat.56.com/stat/flv.php?";
    private final static Pattern URL_ID_PATTERN = Pattern.compile("http://www.56.com/.*/.*[_|-](.+).html");   // 从url中取出id
    // 一次最多发50个请求
    private final static int EVERY_STEP = 5;
    private final static int SLEEP_TIME = 200;
    private String id;
    private String shuaUrl;
    private String pct;

    public WuliuShua() {
    	super();
    }
    
    public WuliuShua(String url, int num) {
        this.url = url.trim();
        this.num = num;
    }

    /**
     * 生成一个基本的url
     *
     * @throws Exception
     */
    public boolean generateShuaUrl() throws Exception {
        if (!url.contains("www.56.com")) {
            return false;
        }

        Matcher m = URL_ID_PATTERN.matcher(url);
        if (m.find()) {
            // 2个不同的地方，计数器地址页不同
            id = m.group(1);
            
            // 不知道原理，目前只会从url中判断
            if (url.contains("com/p")) {
                pct = "3";
            } else {
                pct = "1";
            }
            
            shuaUrl = URL_PREFIX + "id=" + id + "&pct=" + pct;
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
            WuliuShua s = new WuliuShua("http://www.56.com/u43/v_ODg2ODU4MTY.html", 500);
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

