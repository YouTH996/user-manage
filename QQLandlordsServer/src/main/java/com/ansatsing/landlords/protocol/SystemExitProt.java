package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class SystemExitProt extends AbstractProtocol implements Serializable {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void handleProt() {
        if(player != null && player.getUserName() != null) {
            userName2Player.remove(userName);
            // batchSendMsg(player.getUserName()+"退出聊天室了!当前聊天室人数："+userName2Player.size(),userName2Player.values(),true);
        }
    }
}
