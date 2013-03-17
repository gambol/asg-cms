package cn.bieshao.comment;

import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;


import com.bieshao.model.Comment;
import com.bieshao.model.FetchListUrl;
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
    private final static int COMMENT_INTERVAL = 480000; // 两次评论之间的间隔
    //  private final static int REFRESH_LIST_INTERVAL = COMMENT_INTERVAL * 20; // 每次刷新列表页的时间
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
           
            try {
                doTudouComment(accountList, url, commentList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
    public void doTudouComment(List<WebSiteAccount> accountList, String listUrl, List<Comment> commentList)
            throws Exception {
        TudouCommentService tc = new TudouCommentService();

        if (commentList == null || commentList.size() == 0) {
            return;
        }
        WebSiteAccount account = accountList.get(new Random().nextInt(accountList.size()));
        tc.login(account.getUsername().trim(), account.getPassword().trim(), true);
        Set<String> urlList = tc.getVideoUrlSet(listUrl);

        Random rand = new Random();
        for (String url : urlList) {
            account = accountList.get(rand.nextInt(accountList.size()));
            TudouCommentService commentTc = new TudouCommentService();
            commentTc.login(account.getUsername().trim(), account.getPassword().trim(), true);
            String iid = commentTc.getIid(url.trim());
            if (iid == null) {
                logger.error("error in get iid. url:" + url);
                return;
            }

            Comment comment = commentList.get(rand.nextInt(commentList.size()));
            String newComment = generateNewComment(comment.getComment());
            logger.info("tudou post:" + url + " content:" + newComment);
            commentTc.post(iid, newComment, true);
            
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
