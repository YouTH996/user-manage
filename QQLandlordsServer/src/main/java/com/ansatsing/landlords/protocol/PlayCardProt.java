package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;
import java.net.Socket;

/**
 * 每次出牌信息协议
 */
public class PlayCardProt extends AbstractProtocol implements Serializable {
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


    @Override
    public void handleProt(){
        batchSendMsg(this.getClass().getName()+JSON.toJSONString(this), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), true);

    }
}
