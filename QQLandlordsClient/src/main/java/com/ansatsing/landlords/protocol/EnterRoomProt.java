package com.ansatsing.landlords.protocol;

import java.io.Serializable;

/**
 *  进入斗地主房间信息协议
 */
public class EnterRoomProt extends AbstractProtocol implements Serializable {
    private int seatNum;//位置编号
    private String userName;//登录名
    private int readyFlag;//准备情况

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getReadyFlag() {
        return readyFlag;
    }

    public void setReadyFlag(int readyFlag) {
        this.readyFlag = readyFlag;
    }

    public EnterRoomProt() {
    }

    public EnterRoomProt(int seatNum, String userName, int readyFlag) {
        this.seatNum = seatNum;
        this.userName = userName;
        this.readyFlag = readyFlag;
    }

    @Override
    public void handleProt() {
        landlordsRoomWindow.setRoomSeat(seatNum,userName,readyFlag);
    }
}
