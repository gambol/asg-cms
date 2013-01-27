package com.biemian.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;


public class IOUtil {
	private static Logger logger = Logger.getLogger(IOUtil.class);  

	public static void output(String filename, String text,boolean append){ 
    	File out = new File(filename);
        try{
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out, append), "UTF-8"));
        	bw.write(text);
        	
            bw.flush(); 
            bw.close();
        }catch(IOException e){
            logger.error("An I/O error occurs.",e);
        }
    }
    
    public static void outputMultiRow(String filename, List<String> multiRow,boolean append){  
    	File out = new File(filename);
        try{
        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out, append), "UTF-8"));
        	
        	for(String str : multiRow){
        		bw.write(str + "\n");
        	}
        	
            bw.flush(); 
            bw.close();
        }catch(IOException e){
            logger.error("An I/O error occurs.",e);
        }
    }
    
    public static String inputOneRow(String filename){
        String str = null;
        try{ 
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
            str = bufReader.readLine();
            bufReader.close();
        }catch(IOException e){
        	logger.error("Can't load the specified file.",e);      
        }
        return str;
    }
    
    public static List<String> inputMoreRows(String filename){
    	List<String> list = new LinkedList<String>();

    	String str = null;
    	try{ 
    		BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename),"UTF-8"));
    		while((str = bufReader.readLine()) != null){
    			list.add(str);
    		}

    		bufReader.close();
    	}catch(IOException e){
    		logger.error("Can't load the specified file.",e);      
    	}
    	return list;
    }
    
    public static String stream2String(InputStream in, String encoding) throws IOException{
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			byte[] tmpByte = new byte[1024];
			int len = 0;
			while ((len = in.read(tmpByte)) != -1) {
				baos.write(tmpByte, 0, len);
			}
		}finally {
			baos.close();
		}
		return new String(baos.toByteArray(), encoding);
    }
    
    public static List<String> getConfigData(String filename){
    	List<String>  resultList = new ArrayList<String>();
    	
    	InputStream is=IOUtil.class.getResourceAsStream("/" + filename);
    	try {
    		String str = stream2String(is, "UTF-8");
    		String[] strs = str.split("[\r\n]+");
    		if(strs != null){
    			resultList = Arrays.asList(strs);
    		}
    	}catch (Exception e) {
    		logger.error("Cann't access the " + filename,e);
    	}
    	
    	return resultList;
    }
}


