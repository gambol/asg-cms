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
 * http://count.kandian.com/kcount.php?vid=95335874&sid=0&type=play&url=http%3A%2F%2Fvideo.sina.com.cn%2Fp%2Fmusic%2Fv%2F2013%2F0117%2F094361978135.html 
 * 
 * http://video.sina.com.cn/z/sports/nba/130221okchou/#97644084  这种类型的，对应vid是97644084
 * http://video.sina.com.cn/p/music/v/2013/0117/094361978135.html  对应的vid是页面里的  vid\s:\s'(\d+)'
 * @author zhenbao.zhou
 */
public class SinaShua extends Shua {

    private static final Logger logger = Logger.getLogger(SohuShua.class);
    // http://my.tv.sohu.com/us/15991709/52395185.shtml
    // http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=52395185
    private final static String SINA_URL_PREFIX = "http://count.kandian.com/kcount.php?sid=0&type=play&vid=";
    private final static Pattern URL_ID_PATTERN = Pattern.compile("http://video.sina.com.*/#(\\d+)");   // 从url中取出id
    private final static Pattern VID_PATTERN = Pattern.compile("\\s+vid\\s*:\\s*'(\\d+)[\'|\\|]");  // 从内容中取出id
    // 一次最多发50个请求
    private final static int EVERY_STEP = 100;
    private final static int SLEEP_TIME = 250;
    String id;
    String shuaUrl;

    public SinaShua() {
    	super();
    }
    
    public SinaShua(String url, int num) {
        this.url = url.trim();
        this.num = num;
    }

    /**
     * 生成一个基本的url
     *
     * @throws Exception
     */
    public boolean generateShuaUrl() throws Exception {
        if (!url.contains("video.sina.com")) {
            return false;
        }

        Matcher m = URL_ID_PATTERN.matcher(url);
        if (m.find()) {
            // 2个不同的地方，计数器地址页不同
            id = m.group(1);
            shuaUrl = SINA_URL_PREFIX + id + "&url=" + url;
         
        } else {
            String content = HTTPUtils.getContent(url);
            if (StringUtils.isBlank(content)) {
                return false;
            }
            // logger.info(content);
            // content = "iid:123123 iid:4231";
            m = VID_PATTERN.matcher(content);
            if (m.find()) {
                id = m.group(1);
                shuaUrl = SINA_URL_PREFIX + id + "&url=" + url;
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
        
       SinaShua s = new SinaShua("http://video.sina.com.cn/v/b/97615043-1314731975.html", 200);
      //   SinaShua s = new SinaShua("http://video.sina.com.cn/m/xnedmmqs_61193045.html", 200);
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

