package weibo4j.examples.friendships;

import weibo4j.Friendships;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;

public class GetFollowersIds {

	public static void main(String[] args) {
		String access_token = "2.00YKXbaBrYt4GCe0790265aaPt1uDB";
		String uid = "1457026570";
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		try {
			String[] ids = fm.getFollowersIdsById(uid);
			for(String u : ids){
				Log.logInfo(u.toString());
			}
			Log.logInfo("total" + ids.length);
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
