package com.ansatsing.landlords.server.message;

import java.util.Map;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.LandlordsUtil;

public abstract class  AbstractMessage<T> {
	protected Map<Integer, String> enterSeatMap;
	protected Player player;
	//private T socket;
	public abstract void handleMsg(Message message);
	/**
	 * 群发消息
	 * @param sendMsg
	 * @throws IOException
	 */
	public void batchSendMsg(String sendMsg,Map<String, T> nameToSocket) {

	}
	/**
	 * 私发信息
	 * @param sendMsg
	 * @throws IOException
	 */
	protected void singleSendMsg(T _socket,String sendMsg){

	}
	/**
	 * 找同桌的其他牌友的座位号
	 * @param seatNum
	 * @param userName
	 * @return
	 */
	protected Integer getSeatNumByUserName(String userName) {
		int seatNUm = -1;
		int seatNum = player.getSeatNum();
		if(enterSeatMap !=null) {
			if(enterSeatMap.containsKey(LandlordsUtil.getLeftSeatNum(seatNum))) {
				if(enterSeatMap.get(LandlordsUtil.getLeftSeatNum(seatNum)).equals(userName))
				{
					return LandlordsUtil.getLeftSeatNum(seatNum);
				}
			}
			if(enterSeatMap.containsKey(LandlordsUtil.getRightSeatNum(seatNum))) {
				if(enterSeatMap.get(LandlordsUtil.getRightSeatNum(seatNum)).equals(userName))
				{
					return LandlordsUtil.getRightSeatNum(seatNum);
				}			
			}
		}
		return seatNUm;
	}	
}
