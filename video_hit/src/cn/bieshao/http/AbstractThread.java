package cn.bieshao.http;

public class AbstractThread extends Thread{
    
    public Object param;

    public Object getParam() {
        return param;
    }

    public void setParam(Object params) {
        this.param = param;
    }
    
    
}
