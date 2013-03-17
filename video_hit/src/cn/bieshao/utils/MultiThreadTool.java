package cn.bieshao.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import cn.bieshao.http.AbstractThread;
import cn.bieshao.proxy.ProxyHandler;

/**
 * 目前这个类只有Youkushua使用。考虑将来将PoolHttpGet里的东西，迁移过来
 * 
 * @author zhenbao.zhou
 * 
 */
public class MultiThreadTool {
    private final static Logger logger = Logger.getLogger("http");
    private static MultiThreadTool mul;

    // 线程池
    private static ExecutorService exe = null;
    // 线程池的容量
    private static final int POOL_SIZE = 100;

    private static final int delay = 3000;

    public static MultiThreadTool getInstance() {
        if (mul == null) {
            mul = new MultiThreadTool();
            exe = Executors.newFixedThreadPool(POOL_SIZE);
        }

        return mul;
    }

    private MultiThreadTool() {

    }

    public <T extends AbstractThread> void multiRun(int threadSize, final Class<T> clz, Object param) throws Exception {
        int nowDelay = 0;
        for (int i = 0; i < threadSize; i++) {
            T t = clz.newInstance();
            t.setParam(param);
            // exe.schedule(t, nowDelay, TimeUnit.MILLISECONDS);
            exe.execute(t);
            if (i % POOL_SIZE == POOL_SIZE - 1) {
                Thread.sleep(200);
            }
        }
    }

    public void destroy() {
        exe.shutdown();
    }
}
