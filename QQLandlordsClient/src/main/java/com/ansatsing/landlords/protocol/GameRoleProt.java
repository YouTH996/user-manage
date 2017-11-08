package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

/**
 * 抢地主消息协议
 */
public class GameRoleProt extends AbstractProtocol implements Serializable {
    private int nextSeatNum;
    private String userName;
    private int roleFlag;

    public GameRoleProt() {
    }

    public GameRoleProt(int nextSeatNum, String userName, int roleFlag, Socket socket) {
        this.nextSeatNum = nextSeatNum;//下一个抢地主位置
        this.userName = userName;//当前抢地主的用户
        this.roleFlag = roleFlag;
        super.socket = socket;
    }

    public int getNextSeatNum() {
        return nextSeatNum;
    }

    public void setNextSeatNum(int nextSeatNum) {
        this.nextSeatNum = nextSeatNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(int roleFlag) {
        this.roleFlag = roleFlag;
    }

    @Override
    public void handleProt() {
        landlordsRoomWindow.setOtherPlayerRole(userName,String.valueOf(roleFlag),nextSeatNum);
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
