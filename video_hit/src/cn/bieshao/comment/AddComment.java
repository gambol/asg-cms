package cn.bieshao.comment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.bieshao.common.PageResult;
import cn.bieshao.proxy.ProxyHandler;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Comment;
import com.bieshao.model.FetchListUrl;
import com.bieshao.model.Proxy;
import com.bieshao.model.WebSiteAccount;
import com.bieshao.web.dao.CommentDao;
import com.bieshao.web.dao.FetchListUrlDao;
import com.bieshao.web.dao.WebAccountDao;

/**
 * 从数据库读取帐号 和 评论内容，并添加comments
 * 
 * @author zhenbao.zhou
 * 
 */
public class AddComment implements Runnable {
    private int commentInterval; // 两次评论之间的间隔
    private String webName;
    private Class clazz;
    private final static Logger logger = Logger.getLogger("comment");
    private int urlCounter = 0;

    public AddComment(int commentInterval, String webName, Class clazz) {
        this.commentInterval = commentInterval;
        this.webName = webName;
        this.clazz = clazz;
    }

    @Override
    public void run() {
        List<Comment> commentList = CommentDao.queryAllComment();
        List<FetchListUrl> listUrlList = FetchListUrlDao.queryList(webName);
        List<WebSiteAccount> accountList = WebAccountDao.queryAccount(webName);

        if (listUrlList.size() == 0 || accountList.size() == 0 || commentList.size() == 0) {
            logger.error("error in get data from db. commentList accountList listUrlList is null. webName is:"
                    + webName);
            return;
        }

        // 随机用一个帐号
        for (FetchListUrl fetchListUrl : listUrlList) {
            String url = fetchListUrl.getUrl();

            try {
                doComment(accountList, url, commentList, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info("add comment. totally add comment for  " + urlCounter + "urls");
    }

    /**
     * 去视频上评论，go吧
     * 
     * @param user
     * @param password
     * @param listUrl
     * @param commentList
     * @throws Exception
     */
    public void doComment(List<WebSiteAccount> accountList, String listUrl, List<Comment> commentList, Class clazz)
            throws Exception {
        if (commentList == null || commentList.size() == 0) {
            return;
        }

        CommentService cs = (CommentService) clazz.newInstance();

        WebSiteAccount account = accountList.get(new Random().nextInt(accountList.size()));
        // cs.login(account.getUsername().trim(), account.getPassword().trim());
        Set<String> urlList = cs.getVideoUrlSet(listUrl);

        Random rand = new Random();

        for (String url : urlList) {
            
            try {
                account = accountList.get(rand.nextInt(accountList.size()));
                cs = (CommentService) clazz.newInstance();
 //               cs.setProxy(ProxyHandler.getInstance().getRandomProxy());
                cs.login(account.getUsername().trim(), account.getPassword().trim());
                Comment comment = commentList.get(rand.nextInt(commentList.size()));
                String newComment = cs.generateNewComment(comment.getComment());
                cs.doComment(url, newComment);
                Thread.sleep(commentInterval);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
//            System.out.println(url);
            urlCounter++;

        }
    }

    public static void main(String[] args) {
        AddComment atc = new AddComment(30000, "56", WuliuCommentService.class);
        atc.run();
    }
}
