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
import org.apache.http.Header;
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
import cn.bieshao.proxy.ProxyHandler;

public class HTTPUtils {

    private final static Logger logger = Logger.getLogger("http");

    private final static String VERIFY_URL = "http://www.baidu.com";

    /**
     * @param args
     * @throws IOException
     * @throws ClientProtocolException
     */
    public static String getContent(String url) throws ClientProtocolException, IOException {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);
        HttpGet httpgets = new HttpGet(url);
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
     * 验证proxy是否可以正常工作
     * 
     * @param proxy
     * @return
     */
    public static boolean verifyProxy(String ip, int port) {
        // 创建HttpClient实例
        HttpClient httpclient = new DefaultHttpClient();
        // 创建Get方法实例
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
        httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 500);
        httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 500);

        HttpHost host = new HttpHost(ip, port);
        httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);

        HttpHead header = new HttpHead(VERIFY_URL);
        try {
            HttpResponse response = httpclient.execute(header);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;

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
            HttpHost host = (HttpHost)httpget.getParams().getParameter(ConnRoutePNames.DEFAULT_PROXY);
 //           System.out.println("host:" + host.getHostName() + ":" + host.getPort());
//            System.out.println("httpclient:" + httpClient);
            HttpEntity entity = response.getEntity();
            // ensure the connection gets released to the manager
            // EntityUtils.consume(entity);
            content = HTTPUtils.readInputStream(entity.getContent());
        } catch (Exception ex) {
            logger.debug("error in get url:" + httpget.getURI() + " errmsg:" + ex.getMessage());
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
            logger.debug("ok in post url:" + httppost.getURI());
        } catch (Exception ex) {
            logger.debug("error in post url:" + httppost.getURI() + " errmsg:" + ex.getMessage());
            httppost.abort();
      //       ex.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
    }

    public static void main(String[] args) {
        System.out.println(verifyProxy("110.4.12.170", 83));
    }

}
