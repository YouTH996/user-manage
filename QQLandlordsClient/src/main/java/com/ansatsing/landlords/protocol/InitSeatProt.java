package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 初始化大厅座位信息协议
 */
public class InitSeatProt extends AbstractProtocol implements Serializable {
    private Map<Integer,String> seatMap = new HashMap<Integer,String>();

    public Map<Integer, String> getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(Map<Integer, String> seatMap) {
        this.seatMap = seatMap;
    }
    @Override
    public void handleProt() {
        if(seatMap.size() > 0){
            Iterator iterator = seatMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Integer,String> entry = (Map.Entry)iterator.next();
                int seatNum = entry.getKey();
                String userName = entry.getValue();
                gameLobbyWindow.setSeatName(seatNum, userName);
            }
        }
    }
}
