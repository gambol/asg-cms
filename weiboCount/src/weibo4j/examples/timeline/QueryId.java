package weibo4j.examples.timeline;

import weibo4j.Timeline;
import weibo4j.examples.oauth2.Log;
import weibo4j.model.WeiboException;
import weibo4j.org.json.JSONObject;

public class QueryId {

	public static void main(String[] args) {
		String access_token = "2.00YKXbaBrYt4GCe0790265aaPt1uDB";
		String mid =  "zgt5Hv7CJ";
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			JSONObject id = tm.QueryId( mid, 1,1);
				Log.logInfo(String.valueOf(id));			
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
