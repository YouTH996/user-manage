package com.ansatsing.landlords.server.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;
/**
 * 大厅私聊信息
 * @author sunyq
 *
 * @param <T>
 */
public class ExitSeatMessage extends AbstractMessage {
	
	public ExitSeatMessage(Player player,Map<Integer, Player> playerMap,Map<Integer, Table> tableMap,Map<String, Player> userName2Player) {
		this.player = player;
		this.userName2Player = userName2Player;
		this.playerMap = playerMap;
		this.tableMap = tableMap;
	}
	@Override
	public void handleMsg(Message message) {
		int seatNum = Integer.parseInt(message.getMsg());
		//enterSeatList.remove(seatNum+"="+player.getUserName());
		if(playerMap.containsKey(seatNum)){
			playerMap.remove(seatNum);
		}
		//游戏大厅座位信息清除
		batchSendMsg(Constants.EXIT_SEAT_MSG_FLAG+message.getMsg(),userName2Player.values());
		
		//斗地主房间里座位信息在牌友间互通
		if(tableMap.get(LandlordsUtil.getTableNum(seatNum)).getPlayers().size() > 1){
			//1将自己的退出房间的信息发给的牌友
			batchSendMsg(Constants.EXIT_ROOM_MSG_FLAG+player.getUserName(), tableMap.get(LandlordsUtil.getTableNum(seatNum)).getPlayers());

			//2 清除自己
			tableMap.get(LandlordsUtil.getTableNum(seatNum)).getPlayers().remove(player);
		}
		player.setSeatNum(-1);
	}
}
