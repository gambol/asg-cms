package com.bieshao.shua;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.MultiHttpGet;
import cn.bieshao.utils.PoolHttpGet;

/**
 * 刷土豆的播放量
 *
 * @author zhenbao.zhou
 *
 */
public class TudouShua extends Shua {

    private static final Logger logger = Logger.getLogger(TudouShua.class);
    // http://istat.tudou.com/play.srv?162960316&noCatch=25296
    // http://www.tudou.com/programs/view/lvdHyZaJkmE/
    private final static String TUDOU_URL_PREFIX = "http://istat.tudou.com/play.srv?";
    private final static Pattern IID_PATTERN = Pattern.compile("iid:\\s*(\\d+)");
    // 一次最多给土豆发50个请求
    private final static int EVERY_STEP = 100;
    private final static int SLEEP_TIME = 20;
    private Random rand;
    private String iid;
    private String shuaUrl;

    public String getIid() {
        return iid;
    }

    public void setIid(String iid) {
        this.iid = iid;
    }

    public TudouShua() {
        rand = new Random();
    }

    public TudouShua(String url, int num) {
        this.url = url;
        this.num = num;
        rand = new Random();
    }

    /**
     * 生成一个基本的url
     *
     * @throws Exception
     */
    public boolean generateShuaUrl() throws Exception {
        if (!url.contains("tudou.com")) {
            return false;
        }
        
        String content = HTTPUtils.getContent(url);
        if (StringUtils.isBlank(content)) {
            return false;
        }
        // logger.info(content);
        // content = "iid:123123 iid:4231";
        Matcher m = IID_PATTERN.matcher(content);
        if (m.find()) {
            iid = m.group(1);
            shuaUrl = TUDOU_URL_PREFIX + iid + "&noCatch=";
            return true;
        } else {
            return false;
        }

    }

    /**
     * 请求tudou 的url
     */
    public void shua() throws Exception {
        int i = 0;
        while (i <= num) {
            List<String> urls = new ArrayList<String>();
            for (int j = 0; j < EVERY_STEP; j++) {
                urls.add(shuaUrl + rand.nextInt(10000));
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
        String u = "http://www.tudou.com/programs/view/us5k530r2og/";
        TudouShua ts = new TudouShua(u, 2000);
        try {
            ts.generateShuaUrl();
            ts.shua();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
