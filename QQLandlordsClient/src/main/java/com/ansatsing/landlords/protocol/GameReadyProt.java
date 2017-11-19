package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

/**
 * 游戏准备协议
 */
public class GameReadyProt extends AbstractProtocol implements Serializable{
    private int readyFlag;//0未准备;1准备
    private int seatNum;
    public int getReadyFlag() {
        return readyFlag;
    }

    public void setReadyFlag(int readyFlag) {
        this.readyFlag = readyFlag;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public GameReadyProt(int readyFlag, int seatNum, Socket socket) {
        this.readyFlag = readyFlag;
        this.seatNum = seatNum;
      //  super.socket = socket;
    }
    public GameReadyProt(int readyFlag, int seatNum) {
        this.readyFlag = readyFlag;
        this.seatNum = seatNum;
        //  super.socket = socket;
    }
    public GameReadyProt() {
    }
    @Override
    public void handleProt() {
        landlordsRoomWindow.setGameReady(seatNum,readyFlag);
    }
    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
