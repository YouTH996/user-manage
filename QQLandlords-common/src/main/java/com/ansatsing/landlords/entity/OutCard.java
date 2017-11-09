package com.ansatsing.landlords.entity;

/**
 * 打出的牌
 * 		0代表单牌								1
 * 		1代表对子								2
 * 		2代表3带1								4
 * 		3代表4张相同的牌带2张牌					6
 * 		4代表一条龙，至少5张牌 最多12			5-12
 * 		5代表普通炸弹，4张相同的牌				4
 * 		6代表王炸								2
 */
public class OutCard implements Comparable<OutCard>{
    private int playCardType;//出的牌的类型
    private int outCardNum;//出的牌的点数
    private String cards;//出的牌
    private int seatNum;//出牌的位置
    public int getPlayCardType() {
        return playCardType;
    }

    public void setPlayCardType(int playCardType) {
        this.playCardType = playCardType;
    }

    public int getOutCardNum() {
        return outCardNum;
    }

    public void setOutCardNum(int outCardNum) {
        this.outCardNum = outCardNum;
    }

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public int compareTo(OutCard o) {
        if(this.playCardType == o.playCardType){
            if(this.outCardNum > o.outCardNum){
                return 1;
            }else if(this.outCardNum == o.outCardNum){
                return 0;
            }else{
                return -1;
            }
        }else if(this.playCardType == 5 && o.playCardType < 5){
            return 1;
        } else if(this.playCardType == 6 && o.playCardType < 6){
            return 1;
        }else{
            return -1;
        }
    }
}
