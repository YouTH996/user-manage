package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

/**
 * 一轮游戏结束信息协议
 */
public class GameOverProt  extends AbstractProtocol implements Serializable{

    @Override
    public void handleProt() {

    }

    public GameOverProt(Socket socket) {
        //super.socket = socket;
    }
    public GameOverProt() {

    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
