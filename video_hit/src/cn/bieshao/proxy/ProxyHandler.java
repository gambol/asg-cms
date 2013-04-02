package cn.bieshao.proxy;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cn.bieshao.utils.DateUtil;
import cn.bieshao.utils.JDBCUtils;

import com.bieshao.model.Proxy;
import com.bieshao.web.dao.ProxyDao;

public class ProxyHandler {

	private final static Logger logger = Logger.getLogger(ProxyHandler.class);
	private final static int ERROR_TIME = 6;
	private  static ProxyHandler instance;
	// comment 1:copyonwriteArray 特别慢。 所以我在这里选用了vector， 也是线程安全的。 这里可能会有bug
	// 			各个collection 的性能对比请参见 http://blog.csdn.net/inkfish/article/details/5185320
	// comment 2: shit. 发现vector 的东西，删除起来很麻烦， 还是用concurrentHashMap吧
	
	private static ConcurrentHashMap<Proxy, Integer> proxyMap;
	private ProxyHandler() {};
	private static Random rand;
	
	public static ProxyHandler getInstance() {
		if (instance == null) {
			instance = new ProxyHandler();
			int seed = (int)DateUtil.getCurrentTimestamp().getTime() % 12345; //随便一个种子
			rand = new Random(seed);
		}
		return instance;
	}
	
	/**
	 * 从db中重新读取所有有效的proxy
	 */
	public synchronized static void reloadProxy() {
		List<Proxy> proxyList = ProxyDao.selectAvailableProxy(false);
		ConcurrentHashMap newProxyMap = new ConcurrentHashMap<Proxy, Integer>();
		for(Proxy p : proxyList) {
			newProxyMap.put(p, 0);
		}
		
		proxyMap = newProxyMap;
	}
	
	/**
	 * 随机得到得到一个proxy
	 * 如果得到null，则不使用任何proxy
	 * @return
	 */
	public Proxy getRandomProxy() {
		if (proxyMap == null) {
			List<Proxy> proxyList = ProxyDao.selectAvailableProxy(false);
			proxyMap = new ConcurrentHashMap<Proxy, Integer>();
			for(Proxy p : proxyList) {
				proxyMap.put(p, 0);
			}
		}
		if (proxyMap == null) {
			return null;
		}
		
		int i = 0;
		while (true && i++ < ERROR_TIME) {
			try {
				int size = proxyMap.keySet().size();
				int index = rand.nextInt(size);
				Proxy p = (Proxy)proxyMap.keySet().toArray()[index];
				return p;
			} catch (Exception e){
				e.printStackTrace();
			}
		} 
		return null;

	}
	
	   /**
     * 随机得到得到一个匿名proxy
     * 如果得到null，则不使用任何proxy
     * @return
     */
    public Proxy getNimingProxy() {
        Proxy p = getRandomProxy();
        int i = 1000;
        while (p.getProxyType() != 2) {
            p = getRandomProxy();
            i--;
            if (i == 0) {
                return p;
            }
        }
        return p;
    }
	
	/*
	 * 如果失败了，更新map里的failedTime
	 */
	public void failAProxy(Proxy p) {
		if (proxyMap == null || !proxyMap.containsKey(p)){
			return;
		}
		
		logger.info("fail proxy:" + p.getIp() + " port:" + p.getPort());
		
		int failedTime = proxyMap.get(p) + 1;
		if (failedTime >= ERROR_TIME) {
			// 连续失败10次，删除这个proxy
			proxyMap.remove(p);
			// 从db里删除
			p.setDisabled(true);
			p.setDisable_time(DateUtil.getCurrentTimestamp());
			JDBCUtils.update(p, false);
		}
		proxyMap.put(p, failedTime);
	}
	
	/**
	 * 如果成功了，将proxyMap里的failedTime 置为0
	 */
	
	public void succAProxy(Proxy p){
		if (proxyMap == null || !proxyMap.contains(p)) {
			return;
		}
		
		proxyMap.put(p, 0);
	}
	
	public static void main(String[] args) {
		ProxyHandler ph = ProxyHandler.getInstance();
		Proxy p = ph.getRandomProxy();
		ph.failAProxy(p);
		ph.succAProxy(p);
	}
}


