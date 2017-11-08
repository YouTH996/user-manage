package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.net.Socket;

/**
 * 每次出牌信息协议
 */
public class PlayCardProt  extends AbstractProtocol implements Serializable {
    private String cards;//出的什么牌
    private int nextSeatNum = -1;//下个出牌的位置；-1代表游戏结束
    private boolean isLandlord;//出牌人是否是地主；以便判断己方是否赢了

    public int getNextSeatNum() {
        return nextSeatNum;
    }

    public void setNextSeatNum(int nextSeatNum) {
        this.nextSeatNum = nextSeatNum;
    }

    public boolean isLandlord() {
        return isLandlord;
    }

    public void setLandlord(boolean landlord) {
        isLandlord = landlord;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public PlayCardProt() {
    }

    public PlayCardProt(boolean isLandlord,int nextSeatNum,String cards, Socket socket) {
        this.cards = cards;
        super.socket = socket;
        this.isLandlord = isLandlord;
        this.nextSeatNum = nextSeatNum;
    }

    @Override
    public void handleProt() {
        landlordsRoomWindow.showCardAndPlayCard(cards,nextSeatNum,isLandlord);
    }

    @Override
    public void sendMsg() {
        super.sendMsg(this.getClass().getName()+ JSON.toJSONString(this));
    }
}
