package cn.bieshao.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cn.bieshao.proxy.ProxyHandler;

public class HTTPUtils {

	private final static Logger logger = Logger.getLogger(HTTPUtils.class);

	private final static String VERIFY_URL = "http://www.baidu.com";

	/**
	 * @param args
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String getContent(String url) throws ClientProtocolException, IOException {
		// 创建HttpClient实例
		HttpClient httpclient = new DefaultHttpClient();
		// 创建Get方法实例
		httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		HttpGet httpgets = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpgets);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String contentCharset = EntityUtils.getContentCharSet(entity);

			byte[] contentData = EntityUtils.toByteArray(entity);
			String str = null; //
			if (contentCharset != null) {
				str = new String(contentData, contentCharset);
			} else {
				str = new String(contentData);
			}
			logger.debug(url + " : " + str);
			// Do not need the rest
			httpgets.abort();
			return str;
		}
		return null;
	}

	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/**
	 * 验证proxy是否可以正常工作
	 * 
	 * @param proxy
	 * @return
	 */
	public static boolean verifyProxy(String ip, int port) {
		// 创建HttpClient实例
		HttpClient httpclient = new DefaultHttpClient();
		// 创建Get方法实例
		httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
		httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 500);
		httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 500);

		HttpHost host = new HttpHost(ip, port);
		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);

		HttpHead header = new HttpHead(VERIFY_URL);
		try {
			HttpResponse response = httpclient.execute(header);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			
		}
		return false;

	}
	
	public static void main(String[] args) {
		System.out.println(verifyProxy("110.4.12.170", 83));
	}

}
