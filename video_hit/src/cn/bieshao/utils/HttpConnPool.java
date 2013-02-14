package cn.bieshao.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpParams;

public class HttpConnPool {
	// 线程池
	private ExecutorService exe = null;
	// 线程池的容量
	private static final int POOL_SIZE = 200;
	private static final int maxpool = 100;

	private SchemeRegistry schemeRegistry;
	private PoolingClientConnectionManager cm;
	
	private static HttpConnPool instance;

	private HttpConnPool() {
		exe = Executors.newFixedThreadPool(POOL_SIZE);
		schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory
				.getSocketFactory()));
		cm = new PoolingClientConnectionManager(schemeRegistry);
		cm.setMaxTotal(10);
		// ClientConnectionManager cm = new
		// PoolingClientConnectionManager(schemeRegistry);
	}
	
	public static HttpConnPool getInstance() {
		if (instance == null) {
			instance = new HttpConnPool();
		}
		return instance;
	}
	
	public HttpClient getConnection(HttpParams params) {
		return new DefaultHttpClient(cm, params);
	}
}
