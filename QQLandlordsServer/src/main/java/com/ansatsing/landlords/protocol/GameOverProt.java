package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;
import java.net.Socket;

/**
 * 一轮游戏结束信息协议
 */
public class GameOverProt extends AbstractProtocol implements Serializable{

    @Override
    public void handleProt() {
        player.setReadFlag(0);
        player.setRoleFlag(0);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(true);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setOver(false);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setPlay(false);
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setLandlord(null);
    }
}
