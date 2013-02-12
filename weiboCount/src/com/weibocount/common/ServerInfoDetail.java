/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.weibocount.common;

import com.weibocount.model.ServerInfo;
import com.weibocount.model.ServerSysInfo;

import java.io.Serializable;

/**
 *
 * @author xiang.fu
 */
public class ServerInfoDetail implements Serializable{

    private ServerInfo serverInfo = new ServerInfo();
    private ServerSysInfo serverSysInfo = new ServerSysInfo();

    public ServerInfoDetail(ServerInfo si,ServerSysInfo ssi){
        this.serverInfo = si;
        this.serverSysInfo = ssi;
    }

        
    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public ServerSysInfo getServerSysInfo() {
        return serverSysInfo;
    }

    public void setServerSysInfo(ServerSysInfo serverSysInfo) {
        this.serverSysInfo = serverSysInfo;
    }
}
