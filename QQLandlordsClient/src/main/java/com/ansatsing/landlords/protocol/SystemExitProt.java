package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class SystemExitProt  extends AbstractProtocol implements Serializable {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void handleProt() {

    }
    @Override
    public void sendMsg() {
        super.sendMsg(JSON.toJSONString(this));
    }
}
