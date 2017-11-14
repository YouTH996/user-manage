package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

public class SystemExitProt  extends AbstractProtocol implements Serializable {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SystemExitProt() {
    }

    public SystemExitProt(String userName,Socket socket) {
        this.userName = userName;
        super.socket = socket;
    }
    public SystemExitProt(Socket socket) {
        super.socket = socket;
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+JSON.toJSONString(this));
    }
}
