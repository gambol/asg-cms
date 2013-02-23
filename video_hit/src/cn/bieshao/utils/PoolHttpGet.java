package cn.bieshao.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bieshao.model.Proxy;

import cn.bieshao.common.HTTPConst;
import cn.bieshao.proxy.ProxyHandler;

/**
 * copied from http://www.cnblogs.com/wasp520/archive/2012/06/28/2568897.html
 * @author zhenbao.zhou
 *
 */
public class PoolHttpGet {
	private final static Logger logger = Logger.getLogger("http");
	
    // 线程池
    private ExecutorService exe = null;
    // 线程池的容量
    private static final int POOL_SIZE = 200;
    
    List urls;
    public PoolHttpGet(List urls){
        this.urls=urls;
    }
    
    public void multiGet() throws Exception {
        exe = Executors.newFixedThreadPool(POOL_SIZE);
        
        // URIs to perform GETs on
        final List urisToGet = urls;
        /* 有多少url创建多少线程，url多时机子撑不住
        // create a thread for each URI
        GetThread[] threads = new GetThread[urisToGet.length];
        for (int i = 0; i < threads.length; i++) {
            HttpGet httpget = new HttpGet(urisToGet[i]);
            threads[i] = new GetThread(httpClient, httpget);            
        }
        // start the threads
        for (int j = 0; j < threads.length; j++) {
            threads[j].start();
        }

        // join the threads，等待所有请求完成
        for (int j = 0; j < threads.length; j++) {
            threads[j].join();
        }
        使用线程池*/
        for (int i = 0; i < urisToGet.size(); i++) {
            final int j=i;
            HttpGet httpget = new HttpGet((String)urisToGet.get(i));
            exe.execute(new GetThread(HttpConnPool.getHttpClient(), httpget));
        }
        
        
        //创建线程池，每次调用POOL_SIZE
        /*
        for (int i = 0; i < urisToGet.length; i++) {
            final int j=i;
            System.out.println(j);
            exe.execute(new Thread() {
                @Override
                public void run() {
                    this.setName("threadsPoolClient"+j);
                    
                        try {
                            this.sleep(100);
                            System.out.println(j);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        
                        HttpGet httpget = new HttpGet(urisToGet[j]);
                        new GetThread(httpClient, httpget).get();
                    }
                    
                    
                
            });
        }
        
        */
        exe.shutdown();
        System.out.println("Done");
    }
    
    static class GetThread extends Thread{
        
        private final HttpClient httpClient;
        private final HttpContext context;
        private final HttpGet httpget;
        private final Proxy proxy;
        
        public GetThread(HttpClient httpClient, HttpGet httpget) {
            this.httpClient = httpClient;
            this.context = new BasicHttpContext();
            this.httpget = httpget;
            proxy = ProxyHandler.getInstance().getRandomProxy();
            if (proxy != null) {
                HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
                httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
            }
        }
        @Override
        public void run(){
            this.setName("threadsPoolClient");
            get();
        }
        
        public void get() {
            try {
                HttpResponse response = this.httpClient.execute(this.httpget, this.context);
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    logger.info(this.httpget.getURI()+" status:"+response.getStatusLine().toString());
                }
                // ensure the connection gets released to the manager
                // EntityUtils.consume(entity);
            } catch (Exception ex) {
            	logger.info("error in get url:" + this.httpget.getURI() + " errmsg:" + ex.getMessage() + " proxy:" + proxy.toString());
                this.httpget.abort();
                ex.printStackTrace();
            } finally {
                httpget.releaseConnection();
            }
        }
    }
    
    public static void main(String[] args) {
    	int size = 50;
		List<String> urls = new ArrayList<String>();
		for(int i = 0; i < size; i++) {
			urls.add("http://vstat.v.blog.sohu.com/dostat.do?method=setVideoPlayCount&v=59362768&c=131&o=15991709&type=my&vc=131109&act=&st=&ar=0&ye=0&ag=&r=http://my.tv.sohu.com/user/detail/59362768.shtml");
		}
		PoolHttpGet phg = new PoolHttpGet(urls);
		try {
			phg.multiGet();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("we are done");
	}
}
