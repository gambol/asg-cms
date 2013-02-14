package cn.bieshao.utils;

import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
  
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Header;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HTTPUtils {

	private final static Logger logger = Logger.getLogger(HTTPUtils.class);
	
	  /** 
     * @param args 
     * @throws IOException  
     * @throws ClientProtocolException  
     */  
    public static String getContent(String url) throws ClientProtocolException, IOException  
    {  
        // 创建HttpClient实例     
        HttpClient httpclient = new DefaultHttpClient();  
        // 创建Get方法实例     
        httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
        HttpGet httpgets = new HttpGet(url);    
        HttpResponse response = httpclient.execute(httpgets);    
        HttpEntity entity = response.getEntity();    
        if (entity != null) {    
        	String contentCharset = EntityUtils.getContentCharSet(entity);

        	byte[] contentData = EntityUtils.toByteArray(entity);
        	String str = null;	// 
        	if (contentCharset != null) {
        		str = new String(contentData, contentCharset);
        	} else {
        		str = new String(contentData);
        	}
            logger.debug(url + " : " + str);  
            // Do not need the rest    
            httpgets.abort();    
            return str;
        }  
        return null;
    }  
      
    public static String convertStreamToString(InputStream is) {      
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));      
        StringBuilder sb = new StringBuilder();      
       
        String line = null;      
        try {      
            while ((line = reader.readLine()) != null) {  
                sb.append(line + "\n");      
            }      
        } catch (IOException e) {      
            e.printStackTrace();      
        } finally {      
            try {      
                is.close();      
            } catch (IOException e) {      
               e.printStackTrace();      
            }      
        }      
        return sb.toString();      
    }  
    
    
    
}
