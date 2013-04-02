package cn.bieshao.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bieshao.model.Proxy;

import cn.bieshao.common.HTTPConst;
import cn.bieshao.global.Config;

public class HTTPUtils {

    private final static Logger logger = Logger.getLogger("http");

    private final static String VERIFY_URL = "http://www.shuakua.com/ip/realIp.htm";
    //private final static String VERIFY_URL = "http://www.baidu.com/";
    
    public final static int ERROR_PROXY = 0;
    public final static int WORKING_PROXY = 1;
    public final static int ANONYMOUS_PROXY = 2; 
    public final static int MOVE_AWAY_PROXY = 3;

    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String getContent(String url) throws ClientProtocolException, IOException {
       return getContentWithProxy(url, null, 0);
    }

    public static String readInputStream(InputStream input) throws IOException {
        byte[] buffer = new byte[128];
        int len = 0;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        while ((len = input.read(buffer)) >= 0) {
            bytes.write(buffer, 0, len);
        }
        return bytes.toString();
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 由某个代理，来取得内容
     * @param url
     * @param ip
     * @param port
     * @return
     * @throws Exception
     */
    public static String getContentWithProxy(String url, String ip, int port)  throws ClientProtocolException, IOException {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);
        HttpGet httpgets = new HttpGet(url);
        if (port != 0 && ip != null) {
            HttpHost host = new HttpHost(ip, port);
            httpgets.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }
        
        HttpResponse response = httpclient.execute(httpgets);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            String contentCharset = EntityUtils.getContentCharSet(entity);

            byte[] contentData = EntityUtils.toByteArray(entity);
            String str = null; //
            if (contentCharset != null) {
                str = new String(contentData, contentCharset);
            } else {
                str = new String(contentData);
            }
            logger.debug(url + " : " + str);
            // Do not need the rest
            httpgets.abort();
            return str;
        }
        return null;
    }
    
    /**
     * 验证proxy是否可以正常工作,
     * 如果不能走带里，返回0
     * 如果可以正常工作，返回1.
     * 如果这个代理是匿名代理，返回2
     * 
     * 
     * @param proxy
     * @return
     */
    public static int verifyProxy(String ip, int port) {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);

        HttpHost host = new HttpHost(ip, port);
        HttpGet httpgets = new HttpGet(VERIFY_URL);
        httpgets.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);

        try {
            HttpResponse response = httpclient.execute(httpgets);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return ERROR_PROXY;
            }
            
            HttpEntity entity = response.getEntity();
            String str = null;
            if (entity != null) {
                // ensure the connection gets released to the manager
                // EntityUtils.consume(entity);
                str = HTTPUtils.readInputStream(entity.getContent());
                // Do not need the rest
                httpgets.abort();
            }
            
            if (ip.equals(str)){
                return ANONYMOUS_PROXY;
            } else {
                // 如果长度过长，肯定是因为丫转到其他网站去了
                if (str.length() < 50) {
                    return WORKING_PROXY;
                } else {
                    return MOVE_AWAY_PROXY;
                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
            return ERROR_PROXY;
        }
    }

    public static String get(HttpClient httpClient, Proxy proxy, String url) {
        String content = null;
        HttpGet httpget = new HttpGet(url);
        if (proxy != null) {
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            httpget.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }
        
        try {
            HttpResponse response = httpClient.execute(httpget);
 //           HttpHost host = (HttpHost)httpget.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
 //           System.out.println("host:" + host.getHostName() + ":" + host.getPort());
//            System.out.println("httpclient:" + httpClient);
            HttpEntity entity = response.getEntity();
            // ensure the connection gets released to the manager
            // EntityUtils.consume(entity);
            content = HTTPUtils.readInputStream(entity.getContent());
        } catch (Exception ex) {
            logger.info("error in get url:" + httpget.getURI() + " errmsg:" + ex.getMessage() + " proxy:" + proxy);
            httpget.abort();
        } finally {
            httpget.releaseConnection();
        }

        return content;
    }

    public static void post(HttpClient httpClient, Proxy proxy, String url, Map dataMap) {
        httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.IPAD_USER_AGENT);
        // 创建POST方法的 实例
        HttpPost httppost = new HttpPost(url);
      
        // 填入各个表单域的 值
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        
        Iterator it = dataMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            data.add(new BasicNameValuePair(key.toString(), value.toString()));
        }

        logger.debug("do post: param:" + dataMap.toString());
        try {
            // 执行postMethod
            HttpEntity requestHttpEntity = new UrlEncodedFormEntity(data);
            httppost.setEntity(requestHttpEntity);
            if (proxy != null) {
                HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
                httppost.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
            }
        
            HttpResponse response = httpClient.execute(httppost);
            String msg = "ok in post url:" + httppost.getURI();
            if (dataMap.get("url") != null) {
                // 方便我在logger里看究竟是哪个url
                msg += " youkuUrl: " + dataMap.get("url").toString();
            }
            
            logger.info(msg);
        } catch (Exception ex) {
            logger.info("error in post url:" + httppost.getURI() + " errmsg:" + ex.getMessage() + " proxy:" + proxy);
            httppost.abort();
      //       ex.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
    }
    /*
     * 0 2.135.243.138:9090
0 2.135.243.154:9090
0 2.135.243.162:9090
0 27.50.81.73:3128
0 42.121.18.69:9088
0 58.215.75.170:80
0 59.65.233.132:80
0 60.160.112.67:808
2 61.145.121.124:88
2 61.145.121.124:89
2 203.92.47.202:8082
     */

    public static void main(String[] args) {
        System.out.println(verifyProxy("203.92.47.202", 8082));
    }

}
