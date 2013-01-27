package com.biemian.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HTTPGet {
	public static String get(String URL) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet request = new HttpGet(URL);
		try {
			HttpResponse httpResponse = httpClient.execute(request);
			String res = EntityUtils.toString(httpResponse.getEntity());
			httpClient.getConnectionManager().shutdown();
			return (res);
		} catch (IOException e) {
			return (e.toString());
		} catch (ParseException e) {
			return (e.toString());
		}
	}
}
