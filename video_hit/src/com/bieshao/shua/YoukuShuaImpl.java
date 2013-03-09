package com.bieshao.shua;

import hashcash.YoukuHashCash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import cn.bieshao.common.HTTPConst;
import cn.bieshao.http.AbstractThread;
import cn.bieshao.proxy.ProxyHandler;
import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.HttpConnPool;
import cn.bieshao.utils.JsonUtil;

import com.bieshao.model.Proxy;

/**
 * youku刷的实现代码
 * @author zhenbao.zhou
 *
 */
public class YoukuShuaImpl  extends AbstractThread {
    private static final Logger logger = Logger.getLogger(YoukuShua.class);
    public static final String VIDEO_INFO_URL = "http://v.youku.com/player/getPlayList/VideoIDS/";
    public static final String POST_URL = "http://stat.youku.com/player/addPlayerStaticReport";
    
    private  HttpClient httpClient;
    private  HttpContext context;
    private  HttpGet httpget;
    private  Proxy proxy;
    private  HttpPost httppost;
    private  String youkuId;
    
    // 从外部传进来的参数, 在我们这个函数里， params是一个string
    private Object param;
    
    public YoukuShuaImpl() {
        httpClient = HttpConnPool.getHttpClient();
        context = new BasicHttpContext();
        proxy = ProxyHandler.getInstance().getRandomProxy();
    }
    
    public void setYoukuId(String id) {
        youkuId = id;
    }
    
    public void setParam(Object param) {
        youkuId = (String)param;
    }
    
    /**
     * 输入一个youku的id，  id类似于XNTIxNDEyNDA4
     * 1. 从VIDEO_INOF_URL Get出相关的值
     * 2. 用youkuHashcash生成h
     * 3, post给POST_URL
     * @param id
     * @return
     */
    public Map getPostParam() throws Exception{
        Map videoInfoMap = getVideoMapFromId(youkuId);
        if (videoInfoMap == null) {
            return null;
        }
        
        String url = "http://v.youku.com/v_show/id_" + youkuId + ".html";
        int uid = 0;
        String ts = (String) videoInfoMap.get("ts");
        String videoId = (String) videoInfoMap.get("videoid");
        String referer = "null";
        String source = "video";
        int fullflag = 0;
        String ikuflag = "m";

        Map segMap = (Map) videoInfoMap.get("segs");
        List flvList = (List) segMap.get("flv");

        int totalSec = 0;
        for (int i = 0; i < flvList.size(); i++) {
            Map flvMap = (Map) flvList.get(0);
            totalSec += Integer.parseInt((String)flvMap.get("seconds"));
        }

        int totalSeg = flvList.size();

        Map postParam = new HashMap();
        postParam.put("url", url);
        postParam.put("uid", uid);
        postParam.put("t", ts);
        postParam.put("totalsec", totalSec);
        postParam.put("totalseg", totalSeg);
        postParam.put("videoid", videoId);
        postParam.put("source", source);
        postParam.put("referer", referer);
        postParam.put("fullflag", fullflag);
        postParam.put("ikuflag", ikuflag);
        postParam.put("sid", getSid());

        try {
/*
            String hashCash = new HashCash().GenerateHashCash(ts, 4);
            String[] splitArray = hashCash.split("::");
            */
            String hashCash = new YoukuHashCash().getHashCash(ts);
            postParam.put("h", hashCash);
 //           System.out.println("t:" + ts + " : h:" + hashCash);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postParam;
    }
  
    public Map getVideoMapFromId(String id) throws Exception{
        if (id == null)
            return null;

        String infoUlr = VIDEO_INFO_URL + id;
        String content = HTTPUtils.get(httpClient, proxy, infoUlr);

        try {
            Map resultMap = JsonUtil.parserStrToObj(content);
            List dataList = (List) resultMap.get("data");
            Map data = (Map) dataList.get(0);
            return data;
        } catch (Exception e) {
            logger.debug("error in get youku data : " + e.getMessage());
            //e.printStackTrace();
        }

        // System.out.println("ts:" + ts);
        return null;
    }

    /**
     * sid 是一个根据时间的随机数
     * 
     * @return
     */
    public String getSid() {
        long rightNowMil = System.currentTimeMillis();
        int sec = (int) (rightNowMil / 1000);
        int ms = (int) (rightNowMil % 1000);

        int randNum = (int) (Math.random() * 9000 + 1000);
        StringBuffer sb = new StringBuffer();
        sb.append(sec).append(ms).append(1000 + ms).append(randNum);
        return sb.toString();
    }

    @Override
    public void run(){
        this.setName("threadPoolPost");
        try {
            Map postParams = getPostParam();
            if (postParams == null) {
                logger.debug("error in get post params. skip");
                return;
            }
            
            HTTPUtils.post(httpClient, proxy, POST_URL, postParams);
        } catch(Exception e) {
            logger.error("error in youkushua thread. msg:" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws Exception {
        YoukuShuaImpl ys = new YoukuShuaImpl();
        // ys.getTsForId("XNTIxNDA3OTA4");
        String id = "XMzUzNjIyMzUy";
        ys.setYoukuId(id);
        for (int i = 0; i < 10; i++) {
        
            ys.run();
        }
    }


}
