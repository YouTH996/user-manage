package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

/**
 * 游戏发牌信息协议
 */
public class GameDealProt extends AbstractProtocol implements Serializable {
    private String cards;

    public String getCards() {
        return cards;
    }

    public void setCards(String cards) {
        this.cards = cards;
    }

    @Override
    public void handleProt() {
        this.setCards(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getCards());
        batchSendMsg(this.getClass().getName()+ JSON.toJSONString(this), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), false);
    }
}
