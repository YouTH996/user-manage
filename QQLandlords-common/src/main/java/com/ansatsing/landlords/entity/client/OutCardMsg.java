package com.ansatsing.landlords.entity.client;

/**
 * 出牌时发出的信号实体类
 */
public class OutCardMsg {
    private int nextSeatNum;//下个哪个位置出牌;如果值为-1代表游戏结束
    private String cards;//出的什么牌
    private boolean isLandlord;//是否是地主出牌

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public boolean isLandlord() {
        return isLandlord;
    }

    public void setLandlord(boolean landlord) {
        isLandlord = landlord;
    }

    public int getNextSeatNum() {
        return nextSeatNum;
    }

    public void setNextSeatNum(int nextSeatNum) {
        this.nextSeatNum = nextSeatNum;
    }

    @Override
    public String toString() {
        return "nextSeatNum=" + nextSeatNum +
                ", cards='" + cards + '\'' +
                ", isLandlord=" + isLandlord ;
    }
}
