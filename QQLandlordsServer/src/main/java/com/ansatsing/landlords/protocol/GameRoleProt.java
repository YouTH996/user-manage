package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;
import java.net.Socket;

/**
 * 抢地主消息协议
 */
public class GameRoleProt extends AbstractProtocol implements Serializable {
    private int nextSeatNum;
    private String userName;
    private int roleFlag;

    public GameRoleProt() {
    }

    @Override
    public void handleProt() {
        if(this.player.getSeatNum() %3 == 0) {
            tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
            tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
            tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
            tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(true);
        }
        this.player.setRoleFlag(roleFlag);
        if(roleFlag  == 2) {
            if((this.player.getSeatNum() -1) % 3== 0) {
                playerMap.get(this.player.getSeatNum() - 1).setRoleFlag(1);
            }else if((this.player.getSeatNum() +1) % 3== 0) {
                playerMap.get(this.player.getSeatNum() - 1).setRoleFlag(1);
                playerMap.get(this.player.getSeatNum() - 2).setRoleFlag(1);
            }
            tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setLandlord(this.player);;
        }
        batchSendMsg(this.getClass().getName()+JSON.toJSONString(this), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(),true);

        //当最后一个人抢完地主---》如果三个是农民 那启动准备线程;如果一个是地主 2个是农民 那启动 出牌线程
        if((this.player.getSeatNum() + 1) % 3 == 0) {
            if(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers().size() == 3) {
                boolean flag = true;
                for(Player _pPlayer :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()) {
                    if(_pPlayer.getRoleFlag() == 2) {
                        flag = false;
                        break;
                    }
                }
                if(flag) {//启动准备线程
                    for(Player _pPlayer :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()) {
                        _pPlayer.setReadFlag(0);
                    }
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(true);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(false);
                    //tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).set
                    AbstractProtocol startReadyProt = new StartReadyProt(true);
                    startReadyProt.setPlayer(player);
                    startReadyProt.setTableMap(tableMap);
                    startReadyProt.handleProt();
                   // batchSendMsg(Constants.RESTART_READY_MSG_FLAG, tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), false);

                }else {//启动出牌线程
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(false);
                    tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setPlay(true);
                    int seat_num = tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getLandlord().getSeatNum();
                   // batchSendMsg(Constants.START_PLAY_MSG_FLAG+seat_num, tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), false);
                    //singleSendMsg(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getLandlord(), Constants.START_PLAY_MSG_FLAG+"START");
                    AbstractProtocol abstractProtocol = new StartPlayProt(seat_num);
                    abstractProtocol.setPlayer(player);
                    abstractProtocol.setPlayerMap(playerMap);
                    abstractProtocol.setTableMap(tableMap);
                    abstractProtocol.setUserName2Player(userName2Player);
                    abstractProtocol.handleProt();
                }
            }
        }
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

    public int getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(int roleFlag) {
        this.roleFlag = roleFlag;
    }

}
