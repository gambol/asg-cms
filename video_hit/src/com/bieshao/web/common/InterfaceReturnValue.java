/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bieshao.web.common;

/**
 *
 * 从接口返回给页面的接口数据类型
 * 参数 	是否必须 	值类型 	说明 
 * ret 	是 	[true,false] 	表示调用是否成功，
 * errcode 	否 	数字 	出错码， 当调用出错时， 此字段是必须的
 * errmsg 	否 	字符串 	出错信息， 输出出错码对应的出错信息
 * ver 	否 	数字 	版本号，用以提供兼容的接口使用，在多个版本时出现
 * data 	是 	json 	业务数据 
 * 
 * 参照url
 * http://wiki.corp.qunar.com/pages/viewpage.action?pageId=9933661
 * 
 * @author zhenbao.zhou
 */
public class InterfaceReturnValue {
    boolean ret;
    int errcode;
    String errmsg;
    Object data;
    int ver;

    public InterfaceReturnValue () {
        ret = false;
        errcode = 0;
        errmsg = null;
        data = null;
        ver = 0;
    }
    
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public boolean isRet() {
        return ret;
    }

    public void setRet(boolean ret) {
        this.ret = ret;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }
    
    
}
