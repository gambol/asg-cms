package com.bieshao.shua;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.nio.reactor.IOReactorException;
import org.apache.log4j.Logger;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import cn.bieshao.utils.HTTPUtils;
import cn.bieshao.utils.MultiHttpGet;

/**
 * 刷土豆的播放量
 * @author zhenbao.zhou
 *
 */
public class TudouShua extends Shua{
	
	private static final Logger logger = Logger.getLogger(TudouShua.class);
	//http://istat.tudou.com/play.srv?162960316&noCatch=25296
	//http://www.tudou.com/programs/view/lvdHyZaJkmE/
	private final static String TUDOU_URL_PREFIX = "http://istat.tudou.com/play.srv?";
	
	private final static Pattern IID_PATTERN = Pattern.compile("iid:\\s*(\\d+)");
	
	private Random rand;
	
	private String iid;
	private String shuaUrl;
	
	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public TudouShua(String url, int times) {
		this.url = url;
		this.times = times;
		rand = new Random();
	}
	
	@Override
	public void generateShuaUrl() throws Exception{
		String content = HTTPUtils.getContent(url);
		if (StringUtils.isBlank(content)) {
			return;
		}
		//logger.info(content);
		//content = "iid:123123 iid:4231";
		Matcher m = IID_PATTERN.matcher(content);
		if (m.find()) {
			iid = m.group(1);
		}
		
		shuaUrl = TUDOU_URL_PREFIX + iid + "&noCatch=";  
	}
	
	@Override
	public void shua() {
		List<String> urls = new ArrayList<String>();
		for(int i = 0; i < times; i++) {
			urls.add(shuaUrl + rand.nextInt(10000));
		}
		
		 MultiHttpGet client=new MultiHttpGet(urls);
         try {
             client.asynGet();
         } catch (IOReactorException e) {
             e.printStackTrace();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
	}
	
	public static void main(String[] args) {
		String u = "http://www.tudou.com/programs/view/lvdHyZaJkmE/";
		TudouShua ts = new TudouShua(u, 20);
		try {
			ts.generateShuaUrl();
			ts.shua();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
