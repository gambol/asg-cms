package weibo.gambol;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONArray;
import weibo4j.org.json.JSONObject;

/**
 * 查看某一天微博,究竟有多少人看到了.
 * 计算方法:
 *   用户自己的粉丝  + 转发这条微博的人的粉丝
 * @author zhenbao.zhou
 *
 */
public class GetWeiboListener {
	
	private static String access_token = "2.00YKXbaBrYt4GCe0790265aaPt1uDB";
	//AccessToken [accessToken=2.00YKXbaBrYt4GCe0790265aaPt1uDB, expireIn=157679999, refreshToken=,uid=1457026570]
	//private static String access_token = "2.00YKXbaBrYt4GCe0790265aaPt1uDB";
	String uid = "1457026570";
	
	private static int maxQueryUidNum = 50;  // 最多取多少个用户进行抽样他的粉丝数目
	private static int maxQueryRepostUidNum = 3; // 每次取转发了此条微博的200个用户,最多取多少次
	private static int everyQueryRepostCount = 200; // 每次取200个.. sina定的

	/**
	 * 从weiboid 取出用户数据,并且拿到这个用户的粉丝数
	 * 请求sina接口一次
	 * @param weiboId
	 * @param tm
	 * @return
	 * @throws Exception
	 */
	public static int getFollowerNumbyWeiboId(String weiboId, Timeline tm) throws Exception{
		Status status = tm.showStatus(weiboId);
		Log.logInfo(status.getUser().getFollowersCount() + "");
		return status.getUser().getFollowersCount();
	}
	
	/**
	 * 请求sina接口最多maxQueryUid次
	 * @param widSet
	 * @param tm
	 * @return
	 * @throws Exception
	 */
	public static int getFollwerNumByWidSet(Set<String> widSet, Timeline tm) throws Exception {
		int allFollowerNum = 0;
		
		int setSize = widSet.size();
		if (setSize == 0) {
			return 0;
		}
		
		int step = (int)Math.ceil((double)setSize / maxQueryUidNum);
		
		Iterator<String> it = widSet.iterator(); 
		int index = 0;
		int getNum = 0;
		while(it.hasNext()) {
			String wid = it.next();
			if (index % step == 0) {
				getNum++;				
				allFollowerNum += getFollowerNumbyWeiboId(wid, tm);
			}
			index++;
		}
		
		return  (int)((double)(setSize * allFollowerNum) / getNum); 
	}
	
	/**
	 * 
	 * 取出这条微博的所有转发数目
	 * 请求接口1次
	 */
	public static int getRepostNumbyWid(String weiboId, Timeline tm) throws Exception{
		JSONArray json = tm.getRepostCountByIds(weiboId);
		if (json.length() > 0) {
			JSONObject m = (JSONObject)json.get(0);
			return (Integer)m.get("reposts");
		}
		
		return 0;
	}
	
	
	// 太你妈抽样了...
	/**
	 * 返回的userid 放在set里
	 * 返回值 表示 抽样的倍数
	 * 请求接口最多maxQueryRepostUidNum次
	 * 
	 * @param weiboId
	 * @param allRepostNum
	 * @param set
	 * @return
	 */
	public static double  getAllUidRepostWid(String weiboId, int allRepostNum, Set<String> set, Timeline tm) throws Exception{
		int queryTimes = allRepostNum / everyQueryRepostCount + 1;
		double step = queryTimes / maxQueryRepostUidNum; // 为了减少次数,只能这样来了
		step = Math.ceil(step); 
		
		int gotUseIdNum = 0;
		for (int i = 0; i < queryTimes; i = (int) (i + step)) {
			Paging page = new Paging();
			page.setPage(i + 1);
			page.setCount(everyQueryRepostCount);
			JSONObject json = tm.getRepostTimelineIds(weiboId, page);
			int thisTotalNum = json.getInt("total_number");
			gotUseIdNum += thisTotalNum;

			JSONArray array = json.getJSONArray("statuses");
			for (int j = 0; j < array.length(); j++) {
				String wid = array.getString(j);
				set.add(wid);
			}
			
			if (thisTotalNum < everyQueryRepostCount) {
				Log.logDebug("getTotalNum:" + thisTotalNum + " page count:" + page.getCount() + "page'spage:" + page.getPage());
				break;
			}
		}
		
		if (gotUseIdNum == 0) {
			return 0; 
		} else {
			return (double)allRepostNum / gotUseIdNum;
		}		
	}

	
	public static void main(String[] args) throws Exception {
		// 由mid 获取 id
		String mid = "zeTExnzk1";
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		String weiboId = "";
		try {
			JSONObject jid = tm.QueryId(mid, 1,1);
			weiboId = jid.getString("id");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.logInfo("weibo id:" + weiboId);
		
		// 查看这个用户本身的粉丝
		int listerCount = getFollowerNumbyWeiboId(weiboId, tm);
		
		// 查看这条微博被转发的人
		int forwardNum = getRepostNumbyWid(weiboId, tm);
		
		Log.logInfo("forward num:" + forwardNum);
		
		TreeSet<String> widSet = new TreeSet<String>(); 
		double ampTimes = getAllUidRepostWid(weiboId, forwardNum, widSet, tm);
		Log.logInfo("ampT:" + ampTimes);
		if (widSet.size() > 0) {
			Log.logInfo("wid size: " + widSet.size() +" wid(0) : " + widSet.first());
		}
		
		// 转发这条微博的用户粉丝数
		int reposterFollowerNum = getFollwerNumByWidSet(widSet, tm); 
		Log.logDebug("repostFollowerNum:" + reposterFollowerNum);
		
		int totalNum = (int)(ampTimes * reposterFollowerNum + listerCount);
		Log.logDebug("total:" + totalNum);
	}

}
