package cn.bieshao.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import cn.bieshao.common.HTTPConst;

public class HttpConnPool {
	private static HttpParams httpParams;  
	private static PoolingClientConnectionManager connectionManager;  
	  
	    /** 
	     * 最大连接数 
	     */  
	    public final static int MAX_TOTAL_CONNECTIONS = 30;  
	    /** 
	     * 获取连接的最大等待时间 
	     */  
	    public final static int WAIT_TIMEOUT = 50000;  
	    /** 
	     * 每个路由最大连接数 
	     */  
	    public final static int MAX_ROUTE_CONNECTIONS = 10;  
	    /** 
	     * 连接超时时间 
	     */  
	    public final static int CONNECT_TIMEOUT = 10000;  
	    /** 
	     * 读取超时时间 
	     */  
	    public final static int READ_TIMEOUT = 10000;  
	  
	    static {  
	        httpParams = new BasicHttpParams();  
	        // 设置最大连接数  

	        // 设置获取连接的最大等待时间  
	        ConnManagerParams.setTimeout(httpParams, WAIT_TIMEOUT);  
	        // 设置连接超时时间  
	        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);  
	        // 设置读取超时时间  
	        HttpConnectionParams.setSoTimeout(httpParams, READ_TIMEOUT);
	        
	        SchemeRegistry registry = new SchemeRegistry();  
	        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));  
	        registry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));  
	        httpParams.setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);
	        connectionManager = new PoolingClientConnectionManager(registry);
	        connectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
	        // 设置每个路由最大连接数  
	        connectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
	    }  
	  
	    public static HttpClient getHttpClient() {  
	         return new DefaultHttpClient(connectionManager, httpParams);
	    }  
}
