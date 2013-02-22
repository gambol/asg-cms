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

import com.bieshao.model.Proxy;

import cn.bieshao.common.HTTPConst;
import cn.bieshao.proxy.ProxyHandler;

/**
 * 多线程去请求
 *
 * @author zhenbao.zhou
 *
 */
public class MultiHttpGet {

    /**
     * @param args
     * @throws IOReactorException
     * @throws InterruptedException
     */
    private List<String> urls;

    public MultiHttpGet(List<String> list) {
        urls = list;

    }

    public Map<String, String> asynGet() throws IOReactorException, InterruptedException {
        HttpParams params = new BasicHttpParams();

        final HttpAsyncClient httpclient = new DefaultHttpAsyncClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, HTTPConst.USER_AGENT);

        httpclient.start();
        int urlLength = urls.size();
        GetEntity[] entities = new GetEntity[urlLength];
        int i = 0;
        for (String url : urls) {
            entities[i] = new GetEntity(url, true);
            i++;
        }

        final CountDownLatch latch = new CountDownLatch(urlLength);
        final Map<String, String> responseMap = new HashMap<String, String>();
        try {
            for (final GetEntity entity : entities) {
                try {
                    httpclient.execute(entity.getGet(), new FutureCallback<HttpResponse>() {
                        public void completed(final HttpResponse response) {
                            latch.countDown();
                            ProxyHandler.getInstance().succAProxy(entity.getProxy());
                            // responseMap.put(request.getURI().toString(),
                            // response.getStatusLine().toString());

                            try {
                                System.out.println(entity.getGet().getRequestLine()
                                        + "->"
                                        + response.getStatusLine()
                                        //   + readInputStream(response.getEntity().getContent())
                                        );
                            }  catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        public void failed(final Exception ex) {
                            latch.countDown();
                            ex.printStackTrace();
                            ProxyHandler.getInstance().failAProxy(entity.getProxy());

                        }

                        public void cancelled() {
                            latch.countDown();
                        }
                    });
                } catch (Exception e) {
                    ProxyHandler.getInstance().failAProxy(entity.getProxy());
                }
            }

            System.out.println("Doing...");
        } finally {
            latch.await();
            httpclient.shutdown();
        }
        System.out.println("Done");
        return responseMap;
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

    /**
     * Test
     *
     * @param args
     */
    public static void main(String[] args) {
        List<String> urls = new ArrayList<String>();
       // urls.add("http://v0.stat.ku6.com/dostatv.do?method=setVideoPlayCount&o=5438544&c=138000&v=mgUkHRUrxHeI9_KEXuURrQ..&rnd=0.414234123");
        for (int i = 0; i < 10; i++) {
            //urls.add("http://count.vrs.sohu.com/count/stat.do?videoId=972998&t=1361489935902");
            urls.add("http://v0.stat.ku6.com/dostatv.do?method=setVideoPlayCount&o=5438544&c=138000&v=3U_MDV_bXQGsTiCi0iDySw..&rnd=0.19318498158827424");
        }
        //  urls.add("http://www.atugame.com/");
        // urls.add("http://www.google.com");
        //urls.add("http://istat.tudou.com/play.srv?162998227&noCatch=549123");

        System.out.println(urls.size());
        MultiHttpGet client = new MultiHttpGet(urls);
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

class GetEntity {

    public HttpGet get;
    public Proxy proxy;

    public GetEntity(String url, boolean needProxy) {
        get = new HttpGet(url);
        get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);
        if (needProxy) {
            proxy = ProxyHandler.getInstance().getRandomProxy();
            if (proxy != null) {
                HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
                get.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
            }
        }

    }

    public GetEntity(String url) {
        get = new HttpGet(url);
        get.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
        get.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 1000);

        proxy = ProxyHandler.getInstance().getRandomProxy();
        if (proxy != null) {
            HttpHost host = new HttpHost(proxy.getIp(), proxy.getPort());
            get.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, host);
        }

    }

    public HttpGet getGet() {
        return get;
    }

    public void setGet(HttpGet get) {
        this.get = get;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
}