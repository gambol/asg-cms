package cn.bieshao.comment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import cn.bieshao.common.PageResult;
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
public class AddTudouComment implements Runnable {
    private final static int COMMENT_INTERVAL = 60000; // 两次评论之间的间隔
    private final static int REFRESH_LIST_INTERVAL = COMMENT_INTERVAL * 20; // 每次刷新列表页的时间
    private final static Logger logger = Logger.getLogger("comment");

    @Override
    public void run() {

        List<Comment> commentList = CommentDao.queryAllComment();
        List<FetchListUrl> listUrlList = FetchListUrlDao.queryTudouList();
        List<WebSiteAccount> accountList = WebAccountDao.queryTudouAccount();

        if (listUrlList.size() == 0 || accountList.size() == 0 || commentList.size() == 0) {
            logger.error("error in get data from db. commentList accountList listUrlList is null");
            return;
        }

        // 随机用一个帐号
        for (FetchListUrl fetchListUrl : listUrlList) {
            String url = fetchListUrl.getUrl();
            WebSiteAccount account = accountList.get(new Random().nextInt(accountList.size()));
            try {
                doTudouComment(account.getUsername(), account.getPassword(), url, commentList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        logger.info("tudou comment. sleep " + REFRESH_LIST_INTERVAL + " mill seconds");

    }

    /**
     * 去tudou视频上评论，go吧
     * 
     * @param user
     * @param password
     * @param listUrl
     * @param commentList
     * @throws Exception
     */
    public void doTudouComment(String user, String password, String listUrl, List<Comment> commentList)
            throws Exception {
        TudouComment tc = new TudouComment();

        if (commentList == null || commentList.size() == 0) {
            return;
        }

        tc.login(user.trim(), password.trim(), true);
        Set<String> urlList = tc.getVideoUrlSet(listUrl);

        for (String url : urlList) {
            String iid = tc.getIid(url.trim());
            if (iid == null) {
                logger.error("error in get iid. url:" + url);
                return;
            }

            Comment comment = commentList.get(new Random().nextInt(commentList.size()));
            String newComment = generateNewComment(comment.getComment());
            tc.post(iid, newComment, true);
            try {
                Thread.sleep(COMMENT_INTERVAL);
            } catch (Exception e) {
            }
        }

    }

    public String generateNewComment(String comment) {
        int commentLength = comment.length();
        int randomPart = new Random().nextInt(commentLength);

        String newComment = randomPart + comment;

        return newComment;
    }

    public static void main(String[] args) {
        AddTudouComment atc = new AddTudouComment();
        atc.run();
    }
}
