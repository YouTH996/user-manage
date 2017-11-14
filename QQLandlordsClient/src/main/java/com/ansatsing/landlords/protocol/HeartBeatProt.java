package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;

import java.io.Serializable;

/**
 * socket心跳协议--主要检测客户端的是否还连接！
 */
public class HeartBeatProt extends AbstractProtocol implements Serializable {
    private String msg = "我还活着呢!";

    public String getMsg() {
        return msg;
    }

    public HeartBeatProt(Player player) {
        super.player = player;
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
