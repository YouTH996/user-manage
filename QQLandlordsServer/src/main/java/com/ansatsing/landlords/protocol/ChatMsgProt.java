package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;
import java.net.Socket;

/**
 * 群聊发送消息协议:暂时不搞私聊了
 */
public class ChatMsgProt extends AbstractProtocol implements Serializable {
    private int chatFlag;//1游戏大厅群聊 2斗地主房间群聊
    private String userName;
    private String msg;
    private int seatNum;

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(int chatFlag) {
        this.chatFlag = chatFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void handleProt() {
        if(chatFlag == 1){
            super.batchSendMsg(this.getClass().getName()+JSON.toJSONString(this),userName2Player.values(),true);
        }else if(chatFlag == 2){
            super.batchSendMsg(this.getClass().getName()+JSON.toJSONString(this),tableMap.get(LandlordsUtil.getTableNum(seatNum)).getPlayers(),true);
        }
    }
}
