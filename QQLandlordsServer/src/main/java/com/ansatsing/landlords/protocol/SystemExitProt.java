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
    @Override
    public void handleProt() {
        if(player != null && player.getUserName() != null) {
            userName2Player.remove(userName);
            System.out.println(player.getUserName()+"退出游戏室！");
            AbstractProtocol chatMsgProt=new ChatMsgProt(1, player.getUserName(), player.getUserName()+"退出游戏室!", -1);
            chatMsgProt.setUserName2Player(this.userName2Player);
            chatMsgProt.setTableMap(this.tableMap);
            chatMsgProt.setPlayerMap(playerMap);
            chatMsgProt.setPlayer(player);
            chatMsgProt.handleProt();
        }else{
            System.out.println("有未注册成功的人退出游戏室！");
        }
    }
}
