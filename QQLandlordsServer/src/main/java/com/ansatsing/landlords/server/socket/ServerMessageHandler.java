package com.ansatsing.landlords.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.server.message.AbstractMessage;
import com.ansatsing.landlords.server.message.EnterSeatMessage;
import com.ansatsing.landlords.server.message.ExitSeatMessage;
import com.ansatsing.landlords.server.message.UserNameMessage;
import com.ansatsing.landlords.server.netty.NettyServerListener;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

import io.netty.buffer.ByteBufAllocator;

public class ServerMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);
	private Player player;
	
	////////////////////////////////////////////////
	private Map<Integer, Player> playerMap;//一个座位对应一个玩家
	private Map<Integer, Table> tableMap;//一桌对应一个table实体类对象
	private Map<String, Player> userName2Player;
	private AbstractMessage socketMessage;
	public ServerMessageHandler(Player player,Map<Integer, Player> _playerMap,Map<Integer, Table> _tableMap,Map<String, Player> userName2Player) {
		this.player = player;
		this.tableMap = _tableMap;
		this.userName2Player = userName2Player;
		this.playerMap = _playerMap;
	}

	public void handleMessage(Message message) {
		if(message.getTYPE() == MsgType.USER_NAME_MSG) {
			 socketMessage = new UserNameMessage(player,playerMap,tableMap,userName2Player);
			socketMessage.handleMsg(message);

		}else if(message.getTYPE() == MsgType.SEND_ONE_MSG) {
			Player _player = userName2Player.get(message.getToWho());
			String toMsg = player.getUserName()+"悄悄对你说:"+message.getMsg();
			singleSendMsg(_player,toMsg);
		}else  if(message.getTYPE() == MsgType.ENTER_SEAT_MSG) {//进入斗地主房间
			socketMessage = new EnterSeatMessage(player,playerMap,tableMap,userName2Player);
			socketMessage.handleMsg(message);
		}else if(message.getTYPE() == MsgType.SEND_ALL_MSG) {
			batchSendMsg(player.getUserName()+"说:"+message.getMsg(),userName2Player.values(),true);
		}else if(message.getTYPE() == MsgType.EXIT_SEAT_MSG){
			socketMessage = new ExitSeatMessage(player,playerMap,tableMap,userName2Player);
			socketMessage.handleMsg(message);
		}else if(message.getTYPE() == MsgType.ROOM_SEND_ALL_MSG){
			batchSendMsg(Constants.ROOM_SEND_ALL_MSG_FLAG+player.getUserName()+"说:"+message.getMsg(),tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(),true);
		}else if(message.getTYPE() == MsgType.ROOM_SEND_ONE_MSG){
			Player _player = null;
			for(Player p: tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()) {
				if(p.getUserName().equals(message.getToWho())) {
					_player = p;
					break;
				}
			}
			String toMsg = Constants.ROOM_SEND_ONE_MSG_FLAG+player.getUserName()+"悄悄对你说:"+message.getMsg();
			singleSendMsg(_player,toMsg);
		}else if(message.getTYPE() == MsgType.GAME_READY_MSG){
			tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setCards(LandlordsUtil.getRondomCards());//产生随机牌
			this.player.setReadFlag(Integer.parseInt(message.getMsg()));
			String sendMsg = player.getSeatNum()+"="+player.getReadFlag();
			batchSendMsg(Constants.GAME_READY_MSG_FLAG+sendMsg, tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(),true);
			
			//如果有3个人准备好 启动发牌线程
			if(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers().size() == 3){
				boolean flag = true;
				for(Player _player :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()){
					if(_player.getReadFlag() == 0){
						flag = false;
						break;
					}
				}
				if(flag){
					tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(true);
					tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
					tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
					batchSendMsg(Constants.START_DEAL_MSG_FLAG+tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getCards(), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), false);
				}
			}
		}else if(message.getTYPE() == MsgType.GAME_DEAL_MSG) {
			if(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())) != null) {
				singleSendMsg(this.player, Constants.SEND_CARDS_MSG_FlAG+tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getCards());
			}
		}else if(message.getTYPE() == MsgType.SET_ROLE_MSG) {
			if(this.player.getSeatNum() %3 == 0) {
				tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
				tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
				tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
				tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(true);
			}
			String tempStr[] = message.getMsg().split("=");
			int roleFlag = Integer.parseInt(tempStr[1]);
			this.player.setRoleFlag(roleFlag);
			if(roleFlag  == 2) {
				if((this.player.getSeatNum() -1) % 3== 0) {
					playerMap.get(this.player.getSeatNum() - 1).setRoleFlag(1);
				}else if((this.player.getSeatNum() +1) % 3== 0) {
					playerMap.get(this.player.getSeatNum() - 1).setRoleFlag(1);
					playerMap.get(this.player.getSeatNum() - 2).setRoleFlag(1);
				}
			}
			batchSendMsg(Constants.SET_ROLE_MSG_FLAG+message.getMsg(), tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(),true);
			
			//当最后一个人抢完地主---》如果三个是农民 那启动准备线程;如果一个是地主 2个是农民 那启动 出牌线程
			if((this.player.getSeatNum() + 1) % 3 == 0) {
				if(tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers().size() == 3) {
					boolean flag = true;
					for(Player _pPlayer :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()) {
						if(_pPlayer.getRoleFlag() == 2) {
							flag = false;
							break;
						}
					}
					if(flag) {//启动准备线程
						for(Player _pPlayer :tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers()) {
							_pPlayer.setReadFlag(0);
						}
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(true);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(false);
						batchSendMsg(Constants.RESTART_READY_MSG_FLAG, tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).getPlayers(), true);
						
					}else {//启动出牌线程
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setDeal(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setReady(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setWait(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setRob(false);
						tableMap.get(LandlordsUtil.getTableNum(player.getSeatNum())).setPlay(true);
						
					}
				}
			}
		}
			
	}

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
		if(userName2Player !=null) {
			if(userName2Player.containsKey(LandlordsUtil.getLeftSeatNum(seatNum))) {
				if(userName2Player.get(LandlordsUtil.getLeftSeatNum(seatNum)).getUserName().equals(userName))
				{
					return LandlordsUtil.getLeftSeatNum(seatNum);
				}
			}
			if(userName2Player.containsKey(LandlordsUtil.getRightSeatNum(seatNum))) {
				if(userName2Player.get(LandlordsUtil.getRightSeatNum(seatNum)).getUserName().equals(userName))
				{
					return LandlordsUtil.getRightSeatNum(seatNum);
				}			
			}
		}
		return seatNUm;
	}	
	public static void main(String[] args) {
		int tableNum = 0;
		int seatNum = 6;
		if(seatNum % 3 == 0){
			tableNum = seatNum / 3;
		}else if((seatNum+1)%3 == 0) {
			tableNum = (seatNum +1) / 3 -1;
		}else {
			tableNum = (seatNum -1) / 3;
		}
		System.out.println(tableNum);
	}
}
