package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

/**
 * 群聊发送消息协议:暂时不搞私聊了
 */
public class ChatMsgProt  extends AbstractProtocol implements Serializable {
    private int chatFlag;//1游戏大厅群聊 2斗地主房间群聊
    private String userName;
    private String msg;
    private int seatNum;
    public ChatMsgProt(int chatFlag, String userName, String msg,int seatNum, Socket socket) {
        this.chatFlag = chatFlag;
        this.userName = userName;
        this.msg = msg;
        super.socket = socket;
        this.seatNum = seatNum;
    }
    public ChatMsgProt(int chatFlag, String userName, String msg, Socket socket) {
        this.chatFlag = chatFlag;
        this.userName = userName;
        this.msg = msg;
        super.socket = socket;
    }
    public ChatMsgProt() {

    }


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
            gameLobbyWindow.setHistoryMsg(userName+"说:"+msg);
        }else  if(chatFlag == 2){
            landlordsRoomWindow.setHistoryMsg(userName+"说:"+msg);
        }
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
