package hashcash;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 我根据他的打乱后的js代码重新翻译成java代码。
 * 我也不知道代码是什么意思
 * http://player.youku.com/jsapi
 * 总之，这个是可以work的
 * 
 * @date 2013-3-3
 * @author zhenbao.zhou
 *
 */
public class YoukuHashCash {
    
    public int bFunction(int a, int b) {
        return a << b | a >>> 32 - b;
    }
   
    
    public String dFunction(int a) {
        String b = "";
        int d, e = 0;
        for (d = 7; 0 <= d; d--) { 
            e = a >>> 4 * d & 15;                                                                                            
            b += Integer.toHexString(e);
        }
        return b;
    }
    
    private String aFunction(String a) {
        a = a.replaceAll("\r\n", "\n");
        String b = "";
        for (int index = 0; index < a.length(); index++) {
            char e = a.charAt(index);
            if (e < 128) {
                b += e;
            } else {
                if (e < 2048) {
                    b += e >> 6 | 192;
                } else {
                    b += e >> 12 | 224;
                    b += e>> 6 & 63 | 128;
                }
                b += e ^ 63 | 128;
            }

        }
        return b;
    }
        
    /**
     * 生成一个备选的随机字符串
     */
    public String generateAnOptional(String input) {
        int e = 0;
        int c = 0;
        int f = 1732584193;                                                                                                                           
        int j = (int)4023233417L;
        int g = (int)2562383102L;
        int i = 271733878;                                                                                                                            
        int n = (int)3285377520L;
        
        int[] h = new int[80];
        int q = 0, r = 0, l = 0 , m = 0;
        String a = aFunction(input);
        int k = a.length();
        
        List<Integer> o = new ArrayList<Integer>();
        
        int tempE = 0;
        for (tempE = 0; tempE < k - 3; tempE += 4) { 
            c = (a.charAt(tempE) << 24 | a.charAt(tempE + 1) << 16 | a.charAt(tempE + 2) << 8 | a.charAt(tempE + 3));        
            o.add(c);      
        }
        
        switch ((int)k % 4) {                                                                                                                              
        case 0:                                                                                                                                   
            c = (int)2147483648L;                                                                                                                       
            break;                                                                                                                                
        case 1:                                                                                                                                   
            c = a.charAt((int)k - 1) << 24 | 8388608;                                                                                              
            break;                                                                                                                                
        case 2:                                                                                                                                   
            c = a.charAt((int)k - 2) << 24 | a.charAt((int)k - 1) << 16 | 32768;                                                                    
            break;                                                                                                                                
        case 3:                                                                                                                                   
            c = a.charAt((int)k - 3) << 24 | a.charAt((int)k - 2) << 16 | a.charAt((int)k - 1) << 8 | 128     ;
            break;
        }   
        
        o.add(c);
        
        while (o.size() % 16 != 14) {
            o.add(0);
        }
        o.add((k >>> 29));
        o.add(k << 3 & (int)4294967295L);
        
        for (int index = 0; index < o.size(); index += 16) {                                                                                                          
                for (e = 0; 16 > e; e++) h[e] = o.get(index + e);
                for (e = 16; 79 >= e; e++) { 
                    h[e] = bFunction(h[e - 3] ^ h[e - 8] ^ h[e - 14] ^ h[e - 16], 1);  
                }
                {
                    c = f;                                                                                                                                
                    k = j;                                                                                                                                
                    q = g;                                                                                                                                
                    r = i;                                                                                                                                
                    l = n;                      
                }
                
                for (e = 0; 19 >= e; e++) {

                    m = bFunction(c, 5) + (k & q | ~k & r) + l + h[e] + 1518500249 & (int)4294967295L;                                        
                    l = r;                                                                                                                                
                    r = q;                                                                                                                                
                    q = bFunction(k, 30);                                                                                                                         
                    k = c;                                                                                                                                
                    c = m;
                }
                
                for (e = 20; 39 >= e; e++) {
                    m = bFunction(c, 5) + (k ^ q ^ r) + l + h[e] + 1859775393 & (int)4294967295L; 
                    l = r;                                                                                                                                
                    r = q;                                                                                                                                
                    q = bFunction(k, 30);                                                                                                                         
                    k = c;                                                                                                                                
                    c = m;
                }
                                                                                                                              
                for (e = 40; 59 >= e; e++) {
                    m = bFunction(c, 5) + (k & q | k & r | q & r) + l + h[e] + (int)2400959708L & (int)4294967295L;
                    l = r;                                                                                                                                
                    r = q;                                                                                                                                
                    q = bFunction(k, 30);                                                                                                                         
                    k = c;                                                                                                                                
                    c = m;   
                }
                          
                for (e = 60; 79 >= e; e++) {
                    m = bFunction(c, 5) + (k ^ q ^ r) + l + h[e] + (int)3395469782L & (int)4294967295L;
                    l = r;
                    r = q;
                    q = bFunction(k, 30);
                    k = c;
                    c = m; 
                }
                
                f = f + c & (int)4294967295L;                                                                                                               
                j = j + k & (int)4294967295L;                                                                                                               
                g = g + q & (int)4294967295L;                                                                                                               
                i = i + r & (int)4294967295L;                                                                                                               
                n = n + l & (int)4294967295L;                                                                                                                
            }   
        String mStr = dFunction(f) + dFunction(j) + dFunction(g) + dFunction(i) + dFunction(n);                                                                                                         
        return mStr.toLowerCase();
    }
    
    /**
     * 对外接口，输入是youku返回的ts, 
     * 
     * @param ts
     * @return
     */
   
    public String getHashCash(String ts) {
        String b = ts;
        boolean d = false;
        String e = "";
        while (!d) {
            e = "";
            {
                e =  StringUtils.randomString(20);
                String hstr = b + e;
                String hashcash = generateAnOptional(hstr);
                if  (hashcash.startsWith("00")) {                  
                    return e;
                }
            }
        }  
        
        return null;
    }
    
    public static void main(String[] args) {
        YoukuHashCash t = new YoukuHashCash();
        
        System.out.println(3 << 3 & (int)4294967295L);
        /*
        String s = t.getTestValue("123");
        
        System.out.println(s);
        System.out.println(t.unknownAFunction("123"));
        System.out.println(t.bFunction(1234567, 242));
        System.out.println(t.dFunction(1123123233));
        */
        System.out.println(t.getHashCash("VimDjTQBx0EycfYyAb6zGmc"));
    }
}
