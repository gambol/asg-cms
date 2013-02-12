package weibo4j.examples.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import weibo4j.Oauth;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

public class OAuth4Code {
	public static void main(String [] args) throws WeiboException, IOException{
		Oauth oauth = new Oauth();
		args = new String[]{"zhouzhenbao@gmail.com", "wodeweibo121212"};
		
		BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
		System.out.println(oauth.authorize("code"));
		System.out.print("Hit enter when it's done.[Enter]:");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();
		
		//String code = "aa466c4feb9f147c03f55befe6b7e5b4";
		Log.logInfo("code: " + code);
		try{
			System.out.println(oauth.getAccessTokenByCode(code));
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				Log.logInfo("Unable to get the access token.");
			}else{
				e.printStackTrace();
			}
		}
		//http://iozz.cn/?code=8ae5bc6848fb9b84d073cff6a3043740
	}

	

}
