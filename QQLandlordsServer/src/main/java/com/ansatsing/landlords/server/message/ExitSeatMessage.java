package com.ansatsing.landlords.server.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;
/**
 * 大厅私聊信息
 * @author sunyq
 *
 * @param <T>
 */
public class ExitSeatMessage<T> extends AbstractMessage<T> {
	
	protected Map<Integer, Map<String, T>> gameGroups;
	protected T socket;
	protected Map<String, T> nameToSocket;
	@Override
	public void handleMsg(Message message) {
		int seatNum = Integer.parseInt(message.getMsg());
		//enterSeatList.remove(seatNum+"="+player.getUserName());
		if(enterSeatMap.containsKey(seatNum)){
			enterSeatMap.remove(seatNum);
		}
		//游戏大厅座位信息清除
		batchSendMsg(Constants.EXIT_SEAT_MSG_FLAG+message.getMsg(),nameToSocket);
		
		//斗地主房间里座位信息在牌友间互通
		if(gameGroups.get(LandlordsUtil.getTableNum(seatNum)).size() > 1){
			//1将自己的退出房间的信息发给的牌友
			batchSendMsg(Constants.EXIT_ROOM_MSG_FLAG+player.getUserName(), gameGroups.get(LandlordsUtil.getTableNum(seatNum)));

			//2 清除自己
			gameGroups.get(LandlordsUtil.getTableNum(seatNum)).remove(player.getUserName());
		}
		player.setSeatNum(-1);
	}
}
