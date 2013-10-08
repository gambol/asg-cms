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
 * sohu视频包括2种， my.tv.sohu 与 tv.sohu的方法不同 my.tv.sohu的计数器在
 * http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=
 * tv.sohu计数器在
 * http://count.vrs.sohu.com/count/stat.do?t=1361461744237.7104&videoId=972998&playListId=337981884
 *
 * http://17173.tv.sohu.com/v/1/11668/150/MTUwNjAzMQ==
 * http://17173.tv.sohu.com/port/get_videoinfo.php?id=1506031&uid=5413386
 * @author zhenbao.zhou
 */
public class SohuShua extends Shua {

    private static final Logger logger = Logger.getLogger(SohuShua.class);
    // http://my.tv.sohu.com/us/15991709/52395185.shtml
    // http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=52395185
    private final static String MY_SOHU_URL_PREFIX = "http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=";
    private final static Pattern ID_PATTERN = Pattern.compile("/(\\d+).shtml");
    private final static String SOHU_URL_PREFIX = "http://count.vrs.sohu.com/count/stat.do?videoId=";
    private final static Pattern VID_PATTERN = Pattern.compile("vid=\\s*\"(\\d+)\"");
    
    private final static Pattern ID_17173_PATTERN = Pattern.compile("id:\\s*\'(\\d+)\'");
    private final static Pattern UID_17173_PATTERN = Pattern.compile("uid:\\s*\'(\\d+)\'");
    private final static String URL_17173_PREFIX = "http://17173.tv.sohu.com/port/get_videoinfo.php?";
    
    // 一次最多给sohu发50个请求
    private final static int EVERY_STEP = 100;
    private final static int SLEEP_TIME = 100;
    String id;
    String shuaUrl;

    public SohuShua() {
    	super();
    }
    
    public SohuShua(String url, int num) {
        this.url = url;
        this.num = num;
    }

    /**
     * 生成一个基本的url
     *
     * @throws Exception
     */
    public boolean generateShuaUrl() throws Exception {
        if (!url.contains("sohu.com")) {
            return false;
        }

        if (url.contains("my.tv.sohu.com")) {
            // 2个不同的地方，计数器地址页不同
            Matcher m = ID_PATTERN.matcher(url);
            if (m.find()) {
                id = m.group(1);
                shuaUrl = MY_SOHU_URL_PREFIX + id + "&c=131&o=15991709&type=my&vc=131109&act=&st=&ar=0&ye=0&ag=&r=" + url;
            } 
        } else if (url.contains("17173.tv.sohu.com")) {
            String content = HTTPUtils.getContent(url);
            if (StringUtils.isBlank(content)) {
                return false;
            }
            Matcher idMatcher = ID_17173_PATTERN.matcher(content);
            Matcher uidMatcher = UID_17173_PATTERN.matcher(content);
            long time = DateUtil.getCurrentTimestamp().getTime();
            if (idMatcher.find() && uidMatcher.find()) {
                String id = idMatcher.group(1);
                String uid = uidMatcher.group(1);
                shuaUrl = URL_17173_PREFIX + "id=" +id + "&uid=" + uid;
            }
            
        } else {
            String content = HTTPUtils.getContent(url);
            if (StringUtils.isBlank(content)) {
                return false;
            }
            // logger.info(content);
            // content = "iid:123123 iid:4231";
            Matcher m = VID_PATTERN.matcher(content);
            long time = DateUtil.getCurrentTimestamp().getTime();
            if (m.find()) {
                String vid = m.group(1);
                shuaUrl = SOHU_URL_PREFIX + vid + "&t=" + time;
            }
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
        System.out.append("hehe");
       //SohuShua s = new SohuShua("http://17173.tv.sohu.com/v/1/11668/151/MTUxNzk2OA==", 200);
    //     SohuShua s = new SohuShua("http://tv.sohu.com/20130315/n368979003.shtml", 200);
         SohuShua s = new SohuShua("http://my.tv.sohu.com/us/77216752/60585597.shtml", 200);
        try {
             if (s.generateShuaUrl()) {
                 s.shua();
            }
        } catch (Exception e) {
            logger.error("ERROR:" + e.getMessage());
        }
        System.out.println("done");
    }
}
