package cn.bieshao.comment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.bieshao.model.Proxy;

import cn.bieshao.proxy.ProxyHandler;
import cn.bieshao.utils.HTTPUtils;

/**
 * 找一些列表页，然后从中找到url
 * @author zhenbao.zhou
 */
public class TudouCommentService {

    private  HttpClient client;
    private final static Pattern IID_PATTERN = Pattern.compile("iid:\\s*(\\d+)");
    private final static Pattern VIDEO_URL_PATTERN = Pattern.compile("\"(http://www.tudou.com/programs/view/.*/)\"");
    private final Logger logger = Logger.getLogger("comment");
    
    public TudouCommentService() {
         client = new DefaultHttpClient();
    }
    
    public TudouCommentService(Proxy proxy) {
         client = new DefaultHttpClient();
         if (proxy != null) {
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }
    }
    
    public static void main(String[] args) throws IOException { 
        Proxy proxy = ProxyHandler.getInstance().getRandomProxy();
        TudouCommentService tc = new TudouCommentService();
        
        tc.login("papasky731@gmail.com","121212", true);
     //   tc.login("beckybeckyhc@gmail.com", "121212", true);
        
        /*
        List<String> urlList = getVideoUrlList("http://www.tudou.com/cate/ich10a-2b-2c-2d-2e-2f-2g-2h-2i-2j-2k-2l-2m-2n-2o-2so2pe2pa1.html");
        
        for(String url : urlList) {
            String iid = getIid(url);
            if (iid == null) {
                System.out.println("error in get iid");
                return;
            }
            
            int commentLength = COMMENT.length();
            int randomPart = new Random().nextInt(commentLength);
            
            String newComment = COMMENT.substring(0, randomPart) + randomPart + COMMENT.substring(randomPart);
            
            post(iid, newComment, true);
            try {
                Thread.sleep(30000);
            } catch (Exception e) {
            }
        }
        */
        
        String iid = tc.getIid("http://www.tudou.com/listplay/l2HlHoMs3kY.html");
        tc.post(iid,  "喜欢这个视频。做的真棒，希望更多人看到它。请试试shuakua ", true);
    }
    
    public Set<String> getVideoUrlSet(String listUrl) throws IOException {
        HashSet<String> urlSet = new HashSet<String>();
        String content = HTTPUtils.getContent(listUrl);
        if (StringUtils.isBlank(content)) {
            return urlSet;
        }
       
        Matcher m = VIDEO_URL_PATTERN.matcher(content);
        while (m.find()) {
            String url = m.group(1);
            urlSet.add(url.trim());
        }
        
        return urlSet;
    }
    
    
    /**
     * 抓取网页内容
     * @param url
     * @throws IOException
     */
    public  String getIid(String url) throws IOException {
        logger.info("url:" + url);
        String content = HTTPUtils.getContent(url);
        String iid = null;
        if (StringUtils.isBlank(content)) {
            return iid;
        }
        // logger.info(content);
        // content = "iid:123123 iid:4231";
        Matcher m = IID_PATTERN.matcher(content);
        if (m.find()) {
            iid = m.group(1);
        }
        
        return iid;
    }
    
    
    /**
     * 执行post过程
     * @param user
     * @param pwd
     * @param debug
     * @throws IOException
     */
    public void post(String iid, String content, boolean debug) throws IOException {
        HttpPost post = new HttpPost("http://www.tudou.com/comments/itemnewcomment.srv?method=add");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
        
        //登录表单的信息
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("iid", iid));
        qparams.add(new BasicNameValuePair("content", content));
        qparams.add(new BasicNameValuePair("parent", "0"));
        
        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "UTF-8");
        post.setEntity(params);

        // Execute the request
        HttpResponse response = client.execute(post);
        try{
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            logger.info("tudou post returns: " + dump(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityUtils.consume(response.getEntity());
    }
    
    /**
     * 执行登录过程
     * @param user
     * @param pwd
     * @param debug
     * @throws IOException
     */
    public void login(String user, String pwd, boolean debug) throws IOException {
        HttpPost post = new HttpPost("http://login.tudou.com/login.do");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.3 (KHTML, like Gecko) Chrome/6.0.472.63 Safari/534.3");
        
        
        
        //登录表单的信息
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("loginname", user));
        qparams.add(new BasicNameValuePair("password", pwd));
        qparams.add(new BasicNameValuePair("act", "ajaxLogin2Json"));
        qparams.add(new BasicNameValuePair("remember", "true"));
        
        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "UTF-8");
        post.setEntity(params);

        // Execute the request
        HttpResponse response = client.execute(post);
        logger.info("loginName:" + user + " password:" + pwd);  
        try {
            // Examine the response status
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            logger.info("tudou login returns:" + dump(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityUtils.consume(response.getEntity());
    }

    /**
     * 打印页面
     * @param entity
     * @throws IOException
     */
    private String dump(HttpEntity entity) throws IOException {
        InputStream is =  entity.getContent();
        
       String a = HTTPUtils.convertStreamToString(is);
       return a;
    }
    
}
