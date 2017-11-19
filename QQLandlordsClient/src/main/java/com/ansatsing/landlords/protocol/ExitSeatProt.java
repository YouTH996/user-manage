package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

public class ExitSeatProt extends AbstractProtocol implements Serializable {
    private int seatNum;//座位号
    private String userName;//谁退座

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
    @Override
    public void handleProt() {
        gameLobbyWindow.emptySeat(seatNum);
        if(landlordsRoomWindow != null)
        landlordsRoomWindow.emptySeat(userName);
    }

    public ExitSeatProt() {
    }
    public ExitSeatProt(int seatNum, String userName, Socket socket) {
        this.seatNum = seatNum;
        this.userName = userName;
       // super.socket = socket;
    }
    public ExitSeatProt(int seatNum, String userName) {
        this.seatNum = seatNum;
        this.userName = userName;
       // super.socket = socket;
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
        landlordsRoomWindow = null;
    }
}
