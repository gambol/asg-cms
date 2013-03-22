package cn.bieshao.global;

import java.net.InetAddress;

public class Config {

    public  static String LOCAL_IP;
    
    static
    {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            LOCAL_IP = addr.getHostAddress();//获得本机IP
        } catch (Exception e){
            
        }
    }
    
    public static void main(String[] args) {
        System.out.println(LOCAL_IP);
    }
}
