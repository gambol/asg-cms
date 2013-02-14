package cn.bieshao.utils;

 
 import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
 
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.nio.client.DefaultHttpAsyncClient;
import org.apache.http.nio.client.HttpAsyncClient;
import org.apache.http.nio.reactor.IOReactorException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import cn.bieshao.common.HTTPConst;
 
 /**
  * 多线程去请求
  * 
  * @author zhenbao.zhou
  *
  */
 public class MultiHttpGet{
     /**
      * @param args
      * @throws IOReactorException
      * @throws InterruptedException
      */
     private List<String> urls;

     public MultiHttpGet(List<String> list){
         urls=list;
         
     }
     public Map<String,String> asynGet() throws IOReactorException,
             InterruptedException {
    	 HttpParams params =new BasicHttpParams();
    	 
         final HttpAsyncClient httpclient = new DefaultHttpAsyncClient();
         httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);
        
         httpclient.start();
         int urlLength=urls.size();
         HttpGet[] requests = new HttpGet[urlLength];
         int i=0;
         for(String url : urls){
        	 HttpGet get = new HttpGet(url);
        	 //202.38.95.75:80
        	 //TODO
        	 // 每次都从proxy数据库中随机去取
        	 HttpHost proxy = new HttpHost("124.160.133.2", 8080);
        	 get.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        	 get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        	 get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
             requests[i] = get;
             i++;
         }
         final CountDownLatch latch = new CountDownLatch(requests.length);
         final Map<String, String> responseMap=new HashMap<String, String>();
         try {
             for (final HttpGet request : requests) {
            	
                 httpclient.execute(request, new FutureCallback<HttpResponse>() {
                	 
                     public void completed(final HttpResponse response) {
                         latch.countDown();
                         
                         //responseMap.put(request.getURI().toString(), response.getStatusLine().toString());
                         
                         try {
                             System.out.println(request.getRequestLine() + "->"
                                     + response.getStatusLine()+"->");
                             //+readInputStream(response.getEntity().getContent())
                           
                     }  catch (IllegalStateException e) {

                             e.printStackTrace();
                         } catch (Exception e) {
 
                             e.printStackTrace();
                         }
                           
                     }
 
                     public void failed(final Exception ex) {
                         latch.countDown();
                         ex.printStackTrace();

                     }
                     public void cancelled() {
                         latch.countDown();
                     }
                  });
             }
             System.out.println("Doing...");
         } finally {
             latch.await();
             httpclient.shutdown();
         }
         System.out.println("Done");
         return responseMap;
     }
     
     private String readInputStream(InputStream input) throws IOException{
         byte[] buffer = new byte[128];
         int len = 0;
         ByteArrayOutputStream bytes = new ByteArrayOutputStream();
         while((len = input.read(buffer)) >= 0) {
             bytes.write(buffer, 0, len);
         }
         return bytes.toString();
     }
     /**
      * Test
      * @param args
      */
     public static void main(String[] args) {
         List<String> urls=new ArrayList<String>();
   //      urls.add("http://www.baidu.com");
   //      urls.add("http://www.google.com");
         urls.add("http://istat.tudou.com/play.srv?162998227&noCatch=549123");
         /*
         for(int i=0;i<5;i++){
             urls.addAll(urls);
         }
         */
         System.out.println(urls.size());
         MultiHttpGet client=new MultiHttpGet(urls);
         try {
             client.asynGet();
         } catch (IOReactorException e) {
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         System.out.println("done");
     }
 }