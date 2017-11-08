package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

/*
启动出牌状态信息协议：发出这信号代表可以地主可以开始出牌了
 */
public class StartPlayProt extends AbstractProtocol implements Serializable {
    private int seatNum; //最先出牌的位置，也就是地主的位置

    public int getSeatNum() {
        return seatNum;
    }

    public StartPlayProt(int seatNum) {
        this.seatNum = seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
    @Override
    public void handleProt() {
        int tableNum = LandlordsUtil.getTableNum(player.getSeatNum());
        batchSendMsg(this.getClass().getName()+ JSON.toJSONString(this), tableMap.get(tableNum).getPlayers(), false);
    }
}
