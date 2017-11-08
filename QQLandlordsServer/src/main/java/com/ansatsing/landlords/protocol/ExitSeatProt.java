package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

public class ExitSeatProt extends AbstractProtocol implements Serializable {
    private int seatNum;//座位号
    private String userName;//谁退座

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
    @Override
    public void handleProt() {
        if(playerMap.containsKey(seatNum)){
            playerMap.remove(seatNum);
        }
        //游戏大厅座位信息清除
        batchSendMsg(this.getClass().getName()+JSON.toJSONString(this),userName2Player.values(),true);

        //2 清除自己
        tableMap.get(LandlordsUtil.getTableNum(seatNum)).getPlayers().remove(player);
        player.setSeatNum(-1);
        player.setReadFlag(0);
    }
}
