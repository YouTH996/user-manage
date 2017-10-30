package com.ansatsing.landlords.server.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.server.netty.NettyServerListener;
import com.ansatsing.landlords.util.LandlordsUtil;

import io.netty.buffer.ByteBufAllocator;

public abstract class  AbstractMessage {
	protected Player player;
	protected Map<String, Player> userName2Player;
	protected  Map<Integer, Player> playerMap;
	protected Map<Integer, Table> tableMap;
	public abstract void handleMsg(Message message);
	/**
	 * 群发消息
	 * @param sendMsg
	 * @throws IOException
	 */
	public void batchSendMsg(String sendMsg,Collection<Player> players,boolean excludeMyself) {
		for(Player _player:players) {
			if(excludeMyself){
				if(_player == this.player) continue;
			}
			singleSendMsg(_player, sendMsg);
		}
	}
	/**
	 * 私发信息
	 * @param sendMsg
	 * @throws IOException
	 */
	protected void singleSendMsg(Player _player,String sendMsg){
		if(_player.getSocket() != null) {
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(_player.getSocket().getOutputStream(), true);
				printWriter.println(sendMsg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(_player.getChannel() != null) {
			_player.getChannel().writeAndFlush(ByteBufAllocator.DEFAULT.buffer()
					.writeBytes(sendMsg.getBytes()))
					.addListener(new NettyServerListener());
			System.out.println("发送的消息：" + sendMsg.getBytes().length + sendMsg);
		}
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
		if(playerMap !=null) {
			if(playerMap.containsKey(LandlordsUtil.getLeftSeatNum(seatNum))) {
				if(playerMap.get(LandlordsUtil.getLeftSeatNum(seatNum)).getUserName().equals(userName))
				{
					return LandlordsUtil.getLeftSeatNum(seatNum);
				}
			}
			if(playerMap.containsKey(LandlordsUtil.getRightSeatNum(seatNum))) {
				if(playerMap.get(LandlordsUtil.getRightSeatNum(seatNum)).getUserName().equals(userName))
				{
					return LandlordsUtil.getRightSeatNum(seatNum);
				}			
			}
		}
		return seatNUm;
	}	
}
