package cn.bieshao.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.params.CoreProtocolPNames;

import cn.bieshao.common.HTTPConst;

/***
 * httpClient的post请求
 * 用来测试的方法，不用于生产
 * @author panguixiang
 * 
 *
 */
public class PostUtil {
    /**
     *
     * 得到Http请求结果
     *
     * @param url请求地址
     * @param parms请求参数
     * @return
     */
    public synchronized static String getBody(String url, Map parms) {

        // byte[] body = null;
        String resultstr = null;
        // 构造HttpClient的实例
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.IPAD_USER_AGENT);

        // 创建POST方法的 实例
        PostMethod postMethod = new PostMethod(url);
        // 填入各个表单域的 值
        NameValuePair[] data = new NameValuePair[parms.keySet().size()];
        Iterator it = parms.entrySet().iterator();

        int i = 0;

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            data[i] = new NameValuePair(key.toString(), value.toString());
            i++;
        }
        // 将表单的 值放入postMethod中
        postMethod.setRequestBody(data);

        try {
            // 执行postMethod
            int statusCode = httpClient.executeMethod(postMethod);// httpclient对于要求接受后继服务的请求，等待返回
            // 象post和put等不能自动处理转发
            // 301或者302
            if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // 从 头中取出转向的地址
                Header locationHeader = postMethod
                        .getResponseHeader("location");

                String location = null;

                if (locationHeader != null) {

                    location = locationHeader.getValue();
                    System.out
                            .println("The page was redirected to:" + location);
                } else {
                    System.out.println("Location field value is null");
                }
            }
            // 第一张接收返回信息方式
            // body = postMethod.getResponseBody();
          /*  resultstr = new String(postMethod.getResponseBodyAsString()
                    .getBytes(), "UTF-8");
           */
            /**
             * 第二张接收返回信息方式 
             String newStr = new
             String(postMethod.getResponseBodyAsString().getBytes(), "UTF-8");
              System.out.println(newStr);
             */
            String newStr = new  String(postMethod.getResponseBodyAsString().getBytes(), "UTF-8");
             System.out.println(newStr);

        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            postMethod.releaseConnection();
        }

        return resultstr;

    }

   public static void main(String[] args) {
       Map m = new HashMap();
       /*
        * fullflag 0
h   gQMIizHkMGKL3DMQ
ikuflag n2
referer null
sid 136206520179517957630
source  video
t   QhkURzAXQgE4MncvAXEjKHM
totalsec    2540
totalseg    7
uid 0
url http://v.youku.com/v_show/id_XNDcwNDc0MTUy.html
videoid 117618538
        */
       
       m.put("fullflag", 0);
       m.put("h", "gQMIizHkMGKL3DMQ");
       m.put("ikuflag", "n2");
       m.put("refer", "null");
       m.put("sid", "136206520179517957630");
       m.put("source", "video");
       m.put("t", "QhkURzAXQgE4MncvAXEjKHM");
       m.put("totalsec", 2540);
       m.put("totalseq", 7);
       m.put("uid", 0);
       m.put("url", "http://v.youku.com/v_show/id_XNDcwNDc0MTUy.html");
       m.put("videoid", 117618538);
       
       for(int i = 0; i < 1000; i++)
           getBody("http://stat.youku.com/player/addPlayerStaticReport", m);
       
   }

}