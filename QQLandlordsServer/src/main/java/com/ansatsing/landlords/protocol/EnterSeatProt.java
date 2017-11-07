package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

public class EnterSeatProt extends AbstractProtocol implements Serializable {
    private int seatNum;//座位号
    private String userName;//谁入座

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

    public void handleProt() {
        player.setSeatNum(seatNum);
        playerMap.put(seatNum, player);
        batchSendMsg(this.getClass().getName()+JSON.toJSONString(this),userName2Player.values(),true);

        //确定是哪一桌？
        int tableNum = LandlordsUtil.getTableNum(seatNum);
        if(tableMap.containsKey(tableNum)) {
            tableMap.get(tableNum).getPlayers().add(player);
        }else {
            Table table = new Table();
            table.getPlayers().add(player);
            tableMap.put(tableNum, table);
        }
		/*
		//找到当前桌的其他2个人的socket
		//**算法分析
		 * 0 1	 2
		 * 3 4	 5
		 * 6 7	 8
		 * 9 10 11
		 *
		 * 如果seatNum能整除3,则是第一个位置；如果seatNum+1能整除3则第3个位置，否则就中间的位置
		 */
        //斗地主房间里座位信息在牌友间互通
        /*if(tableMap.get(tableNum).getPlayers().size() > 1){
            //1将自己的信息发给同桌的比你先进的牌友
            batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+player.getUserName()+"="+seatNum+"="+player.getReadFlag(), tableMap.get(tableNum).getPlayers(),true);
            //2将同桌的比你先进去的牌友的信息发给自己
            for(Player temp:tableMap.get(tableNum).getPlayers()){
                if(temp == this.player) continue;
                singleSendMsg(this.player, Constants.ENTER_ROOM_MSG_FLAG+temp.getUserName()+"="+getSeatNumByUserName(temp.getUserName())+"="+temp.getReadFlag());
            }
            //如果有3个人了就启动游戏准备线程
            if(tableMap.get(tableNum).getPlayers().size() == 3){
                batchSendMsg(Constants.START_READY_MSG_FLAG, tableMap.get(tableNum).getPlayers(), false);
                tableMap.get(tableNum).setWait(false);
                tableMap.get(tableNum).setReady(true);
            }
        }*/
    }
}
