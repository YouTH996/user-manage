package com.ansatsing.landlords.protocol;

import java.io.Serializable;

/**
 * 游戏准备协议
 */
public class GameReadyProt extends AbstractProtocol implements Serializable{
    private int readyFlag;//0未准备;1准备

    public int getReadyFlag() {
        return readyFlag;
    }

    public void setReadyFlag(int readyFlag) {
        this.readyFlag = readyFlag;
    }

    public void handleProt() {

    }

    protected void sendMsg() {

    }
}
