package com.ansatsing.landlords.protocol;

import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

/**
 * 出牌消息协议
 */
public class OutCardProt extends AbstractProtocol implements Serializable{
    private int seatNum;//哪个位置出的牌
    private String cards;//出的什么牌
    private int nextSeatNum = -1;//下个出牌的位置；-1代表游戏结束
   // private boolean isOver;//是否出完
    private String userName;//出牌人网名
    private boolean isLandlord;//出牌人是否是地主；以便判断己方是否赢了

    public OutCardProt(int seatNum, String cards, int nextSeatNum, String userName, boolean isLandlord) {
        this.seatNum = seatNum;
        this.cards = cards;
        this.nextSeatNum = nextSeatNum;
        this.userName = userName;
        this.isLandlord = isLandlord;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
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

    public boolean isLandlord() {
        return isLandlord;
    }

    public void setLandlord(boolean landlord) {
        isLandlord = landlord;
    }

    @Override
    public String toString() {
        return "OutCardProt{" +
                "seatNum=" + seatNum +
                ", cards='" + cards + '\'' +
                ", nextSeatNum=" + nextSeatNum +
                ", userName='" + userName + '\'' +
                ", isLandlord=" + isLandlord +
                '}';
    }

    public void handleProt() {
      //  batchSendMsg(message, tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), true);
    }
}
