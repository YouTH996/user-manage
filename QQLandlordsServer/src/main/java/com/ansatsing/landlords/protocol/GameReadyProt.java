package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

/**
 * 游戏准备协议:点击准备 按钮
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
    @Override
    public void handleProt() {
        tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setCards(LandlordsUtil.getRondomCards());//产生随机牌
        this.player.setReadFlag(readyFlag);
       // String sendMsg = player.getSeatNum()+"="+player.getReadFlag();
        batchSendMsg(this.getClass().getName()+ JSON.toJSONString(this), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(),true);

        //如果有3个人准备好 启动发牌线程
       if(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers().size() == 3){
            boolean flag = true;
            for(Player _player :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()){
                if(_player.getReadFlag() == 0){
                    flag = false;
                    break;
                }
            }
            if(flag){
                tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(true);
                tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
                tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
                AbstractProtocol gameDealProt = new GameDealProt();
               gameDealProt.setPlayer(player);
               gameDealProt.setTableMap(tableMap);
               gameDealProt.setUserName2Player(userName2Player);
               gameDealProt.setPlayerMap(playerMap);
               gameDealProt.handleProt();
               // batchSendMsg(Constants.START_DEAL_MSG_FLAG+tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getCards(), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), false);
            }
        }
    }
}
