package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.Constants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 初始化大厅座位信息协议
 */
public class InitSeatProt  extends AbstractProtocol implements Serializable {
    private Map<Integer,String> seatMap = new HashMap<Integer,String>();

    public Map<Integer, String> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(Map<Integer, String> seatMap) {
        this.seatMap = seatMap;
    }
    @Override
    public void handleProt() {
        if(playerMap.size() > 0){//初始化游戏大厅座位情况
            for(Integer seatNum:playerMap.keySet()){
               seatMap.put(seatNum,playerMap.get(seatNum).getUserName());
            }
            singleSendMsg(player,this.getClass().getName()+ JSON.toJSONString(this));
        }
    }
}
