package com.ansatsing.landlords.server.message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

public class EnterSeatMessage<T> extends AbstractMessage<T> {
	private final static Logger LOGGER = LoggerFactory.getLogger(EnterSeatMessage.class);
	protected Map<Integer, Map<String, T>> gameGroups;
	protected T socket;
	protected Map<String, T> nameToSocket;
	@Override
	public void handleMsg(Message message) {
		int seatNum = Integer.parseInt(message.getMsg());
		player.setSeatNum(seatNum);
		enterSeatMap.put(seatNum, player.getUserName());
		batchSendMsg(message.getMsg()+Constants.ENTER_SEAT_MSG_FLAG+player.getUserName(),nameToSocket);
		//enterSeatList.add(seatNum+"="+player.getUserName());
		
		//确定是哪一桌？
		int tableNum = LandlordsUtil.getTableNum(seatNum);
		if(gameGroups.containsKey(tableNum)) {
			//tripleSockets = gameGroups.get(tableNum);
			gameGroups.get(tableNum).put(player.getUserName(), socket);
		}else {
			Map<String,T> tripleSockets = new ConcurrentHashMap<String, T>();
			tripleSockets.put(player.getUserName(), socket);
			gameGroups.put(tableNum, tripleSockets);
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
		if(gameGroups.get(tableNum).size() > 1){
			//1将自己的信息发给同桌的比你先进的牌友
			batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+player.getUserName()+"="+seatNum, gameGroups.get(tableNum));
			//2将同桌的比你先进去的牌友的信息发给自己
			for(String username:gameGroups.get(tableNum).keySet()){
				if(username.equals(player.getUserName())) continue;
					singleSendMsg(this.socket, Constants.ENTER_ROOM_MSG_FLAG+username+"="+getSeatNumByUserName(username));
			}
		}
	}
}
