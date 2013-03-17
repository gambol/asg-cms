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
import cn.bieshao.proxy.ProxyHandler;
import cn.bieshao.utils.HTTPUtils;

/**
 * 找一些列表页，然后从中找到url
 * 
 * @author zhenbao.zhou
 */
public class WuliuCommentService extends CommentService {

    private final static Pattern VIDEO_ID_PATTERN = Pattern.compile("'videoId':\\s*\"(\\d+)\"");
    private final static Pattern USER_ID_PATTERN = Pattern.compile("'videoUserId':\\s*\"(.*)\"");
    private final static Pattern LOGIN_URL_PATTERN = Pattern.compile("<a href=\"(.*)\"");
    private final static Pattern URL_ID_PATTERN = Pattern.compile("http://www.56.com/.*/.*[_|-](.+).html");
    private final static Pattern VIDEO_PATTERN = Pattern.compile("\"(http://www.56.com/.*/v[_|-].+.html)\"");
    private final Logger logger = Logger.getLogger("comment");

    public WuliuCommentService() {
       super();
    }

    
    /**
     * 使用proxy
     * 
     * @param proxy
     */
    public WuliuCommentService(Proxy proxy) {
        client = new DefaultHttpClient();
     
    }

    public static void main(String[] args) throws IOException {
        WuliuCommentService wc = new WuliuCommentService();

        wc.login("shuakua58", "121212");
        String[] idArray = wc.getIds("http://www.56.com/u33/v_ODg3MDQ3MjY.html");
        wc.postComment(idArray[0], idArray[1], "21shipin 推广@亮增长@，@ shuakua");
    }

    public Set<String> getVideoUrlSet(String listUrl) throws IOException {
        HashSet<String> urlSet = new HashSet<String>();
        String content = HTTPUtils.getContent(listUrl);
        if (StringUtils.isBlank(content)) {
            return urlSet;
        }

        Matcher m = VIDEO_PATTERN.matcher(content);
        while (m.find()) {
            String url = m.group(1);
            urlSet.add(url.trim());
        }

        return urlSet;
    }

    /**
     * 抓取网页内容, 取出videoid 和 userid
     * 
     * @param url
     * @throws IOException
     */
    public String[] getIds(String url) throws IOException {
        logger.info("url:" + url);
        String content = HTTPUtils.getContent(url);

        String result[] = new String[2];
        String videoId = null;
        if (StringUtils.isBlank(content)) {
            return null;
        }
        // logger.info(content);
        // content = "iid:123123 iid:4231";
        Matcher m = VIDEO_ID_PATTERN.matcher(content);
        if (m.find()) {
            videoId = m.group(1);
        }

        if (videoId == null) {
            m = URL_ID_PATTERN.matcher(url);
            if (m.find()) {
                videoId = m.group(1);
            }
        }

        String userId = null;
        m = USER_ID_PATTERN.matcher(content);
        if (m.find()) {
            userId = m.group(1);
        }

        result[0] = videoId;
        result[1] = userId;
        return result;
    }

    /**
     * 给某个页面添加评论
     * @param videoId
     * @param userId
     * @param content
     * @throws IOException
     */
    public void doComment(String url, String content) throws IOException{
        logger.info("56 comment. url:" + url + " comment:" + content);
        String[] idArray = getIds(url);
        if (idArray != null) {
            postComment(idArray[0], idArray[1], content);
        } else {
            logger.error("56 url:" + url + "不能获取id");
        }
    }
    
    /**
     * 执行post过程
     * 
     * @param user
     * @param pwd
     * @param debughttp://www.56.com/u33/v_ODg3MDQ3MjY.html 
     * @throws IOException
     */
    public void postComment(String videoId, String userId, String content) throws IOException {
        HttpPost post = new HttpPost("http://comment.56.com/trickle/api/commentApi.php");
        post.setHeader("User-Agent", HTTPConst.USER_AGENT);
        /*
        Proxy proxy = ProxyHandler.getInstance().getRandomProxy();
        if (proxy != null) {
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            post.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }
         */
        // 登录表单的信息
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        qparams.add(new BasicNameValuePair("a", "insert"));
        qparams.add(new BasicNameValuePair("auth_img_input", "输入验证码"));
        qparams.add(new BasicNameValuePair("cmtContent", content));
        qparams.add(new BasicNameValuePair("content", content));
        qparams.add(new BasicNameValuePair("pct", "1"));
        if (userId != null) {
            // userId = "jcxw56";
            qparams.add(new BasicNameValuePair("v_userid", userId));
        }

        qparams.add(new BasicNameValuePair("vid", videoId));

        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "UTF-8");
        post.setEntity(params);

        // Execute the request
        HttpResponse response = client.execute(post);

        try {
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            logger.info("56 post returns: " + dump(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityUtils.consume(response.getEntity());
    }

    /**
     * 执行登录过程
     * 
     * @param user
     * @param pwd
     * @param debug
     * @throws IOException
     */
    public void login(String user, String pwd) throws IOException {
        HttpPost post = new HttpPost("http://www.renren.com/t-signin2");
        post.setHeader("User-Agent", HTTPConst.USER_AGENT);

        client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BEST_MATCH);

        // 登录表单的信息
        List<NameValuePair> qparams = new ArrayList<NameValuePair>();

        qparams.add(new BasicNameValuePair("e", user));
        qparams.add(new BasicNameValuePair("p", pwd));
        qparams.add(new BasicNameValuePair("autoLogin", "on"));
        qparams.add(new BasicNameValuePair("captcha_type", "56_login"));
        qparams.add(new BasicNameValuePair("d", "56.com"));
        qparams.add(new BasicNameValuePair("icode", ""));
        qparams
                .add(new BasicNameValuePair(
                        "u",
                        "http://www.56.com/js/login/login_box_success_v2.html|http://www.56.com/js/login/login_box_error_v_3.html|http://video.56.com/"));

        qparams.add(new BasicNameValuePair("icode", ""));

        UrlEncodedFormEntity params = new UrlEncodedFormEntity(qparams, "UTF-8");
        post.setEntity(params);

        // https://passport.sohu.com/sso/login.jsp?userid=gambol85%40sohu.com&password=93279e3308bdbbeed946fc965017f67a&appid=1074&persistentcookie=1&s=1363192090578&b=2&w=1366&pwdtype=1&v=26

        // Execute the request
        HttpResponse response = client.execute(post);
        logger.info("loginName:" + user + " password:" + pwd);
        String returnStr = "";
        try {
            // Examine the response status
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            returnStr = dump(entity);
            logger.info("56 login returns:" + returnStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityUtils.consume(response.getEntity());

        // 解释str， 并且得到正式的url
        String loginUrl = null;
        Matcher m = LOGIN_URL_PATTERN.matcher(returnStr);
        if (m.find()) {
            loginUrl = m.group(1);
        }

        logger.info("loginUrl:" + loginUrl);

        HttpGet get = new HttpGet(loginUrl);
        get.setHeader("User-Agent", HTTPConst.USER_AGENT);

        response = client.execute(get);
        try {
            // Examine the response status
            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            returnStr = dump(entity);
            logger.info("final login returns:" + returnStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
