package cn.bieshao.utils;

import java.util.HashMap;

import org.codehaus.jackson.map.ObjectMapper;

public class JsonUtil {

    public static HashMap parserStrToObj(String str) {
        HashMap<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            map = mapper.readValue(str, HashMap.class);
        } catch (Exception e){
            e.printStackTrace();
        }
       return map;
    }

}
