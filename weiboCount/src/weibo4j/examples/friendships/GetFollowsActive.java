package weibo4j.examples.friendships;

import weibo4j.Friendships;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.User;
import weibo4j.model.UserWapper;
import weibo4j.model.WeiboException;

public class GetFollowsActive {

	public static void main(String[] args) {
		String access_token = "2.00YKXbaBrYt4GCe0790265aaPt1uDB";
		String uid = "1457026570";
		Friendships fm = new Friendships();
		fm.client.setToken(access_token);
		try {
			UserWapper users = fm.getFollowersActive(uid);
			for(User u : users.getUsers()){
				Log.logInfo(u.toString());
			}
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
