package cn.bieshao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
	// 线程池的容量
    private static final int POOL_SIZE = 30;
    
    // 线程池
    private static ExecutorService exe =  Executors.newFixedThreadPool(POOL_SIZE);
  
    List urls;
    public PoolHttpGet(List urls){
        this.urls=urls;
    }
    
    public synchronized void multiGet() throws Exception {
        // URIs to perform GETs on
        final List urisToGet = urls;

        for (int i = 0; i < urisToGet.size(); i++) {
            final int j=i;
            HttpGet httpget = new HttpGet((String)urisToGet.get(i));
            exe.execute(new GetThread(HttpConnPool.getHttpClient(), httpget));
        }
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
//            proxy = new Proxy();
            
            proxy = ProxyHandler.getInstance().getNimingProxy();
            if (proxy != null) {
              HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
              httpget.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
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
                    logger.info(this.httpget.getURI()+" status:"+response.getStatusLine().toString() + "-> " 
                            + readInputStream(response.getEntity().getContent()) +" proxy:" + proxy);
                }
                // ensure the connection gets released to the manager
                // EntityUtils.consume(entity);
            } catch (Exception ex) {
            	logger.info("error in get url:" + this.httpget.getURI() + " errmsg:" + ex.getMessage() + " proxy:" + proxy.toString());
                this.httpget.abort();
               // ex.printStackTrace();
            } finally {
                httpget.releaseConnection();
            }
        }
        
        private String readInputStream(InputStream input) throws IOException {
            byte[] buffer = new byte[128];
            int len = 0;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            while ((len = input.read(buffer)) >= 0) {
                bytes.write(buffer, 0, len);
            }
            return bytes.toString();
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
		//	e.printStackTrace();
		}
		
		System.out.println("we are done");
	}
}
