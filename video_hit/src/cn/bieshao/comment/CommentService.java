package cn.bieshao.comment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.bieshao.model.Proxy;

import cn.bieshao.common.HTTPConst;
import cn.bieshao.utils.HTTPUtils;
public class CommentService {
    protected HttpClient client;
    private final Logger logger = Logger.getLogger("comment");
      
    public CommentService() {
        client = new DefaultHttpClient();
    }
    
    /**
     * 提交comment
     * @param videoId
     * @param userId
     * @param content
     * @throws IOException
     */
    public void doComment(String url, String content) throws IOException { 
        
    }
    
    /*
     * 使用代理
     */
    public void setProxy(Proxy proxy) {
        if (proxy != null) {
            logger.info("use proxy:" + proxy);
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }
    }

    /**
     * 从list页获取需要评论的url
     * @param listUrl
     * @return
     * @throws IOException
     */
    public Set<String> getVideoUrlSet(String listUrl) throws IOException {
         HashSet<String> urlSet = new HashSet<String>();
        return urlSet;
    }
    
    /**
     * 登录
     * @param user
     * @param pwd
     * @throws IOException
     */
    public void login(String user, String pwd) throws IOException {
        
    }
    
     /**
     * 打印页面
     * 
     * @param entity
     * @throws IOException
     */
    public String dump(HttpEntity entity) throws IOException {
        InputStream is = entity.getContent();

        String a = HTTPUtils.convertStreamToString(is);
        return a;
    }
    
    /**
     * 随机生成一个新的评论
     */
    public String generateNewComment(String comment) {
        int commentLength = comment.length();
        int randomPart = new Random().nextInt(commentLength);

        String newComment = randomPart + comment;

        return newComment;
    }

}
