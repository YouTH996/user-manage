package com.ansatsing.landlords.server.socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.server.message.AbstractMessage;
import com.ansatsing.landlords.server.message.EnterSeatMessage;
import com.ansatsing.landlords.server.message.SocketEnterSeatMessage;
import com.ansatsing.landlords.server.message.SocketExitSeatMessage;
import com.ansatsing.landlords.server.message.SocketUserNameMessage;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.LandlordsUtil;

public class ServerMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);
	private Map<String, Socket> nameToSocket;
	private Socket socket;//当前客户的socket
	private Player player;
	private Map<Integer, String> enterSeatMap;
	//private Map<String,Socket> tripleSockets;//记住正在斗地主的3个人的socket gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum()))
	private Map<Integer, Map<String, Socket>> gameGroups; 
	public ServerMessageHandler(Map<String, Socket> nameToSocket,Player player,Socket socket,Map<Integer, String> enterSeatMap,Map<Integer, Map<String, Socket>> gameGroups) {
		this.nameToSocket = nameToSocket;
		this.player = player;
		this.socket = socket;
		this.enterSeatMap = enterSeatMap;
		this.gameGroups = gameGroups;
	}

	public void handleMessage(Message message) {
		try {
			if(message.getTYPE() == MsgType.USER_NAME_MSG) {
				AbstractMessage<Socket> socketMessage = new SocketUserNameMessage(player, socket, enterSeatMap, gameGroups, nameToSocket);
				socketMessage.handleMsg(message);
				/*if(nameToSocket.keySet().contains(message.getMsg())){//处理重复网名
					singleSendMsg(socket,"这网名有人正在使用,请更换网名!");
				}else {
					String userName = message.getMsg();
					batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",nameToSocket);
					singleSendMsg(socket,"这个网名可以啦!");
					nameToSocket.put(userName, socket);
					player.setUserName(userName);
					if(enterSeatMap.size() > 0){//初始化游戏大厅座位情况
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(Constants.INIT_SEAT_MSG_FLAG);
						for(Integer seatNum:enterSeatMap.keySet()){
							stringBuilder.append(seatNum+"="+enterSeatMap.get(seatNum)+",");
						}
						stringBuilder.deleteCharAt(stringBuilder.length()-1);
						//String initSeat = Constants.INIT_SEAT_MSG_FLAG+Joiner.on(",").join(enterSeatList);
						singleSendMsg(socket, stringBuilder.toString());
					}
				}*/
			}else if(message.getTYPE() == MsgType.SEND_ONE_MSG) {
				Socket toSocket = nameToSocket.get(message.getToWho());
				String toMsg = player.getUserName()+"悄悄对你说:"+message.getMsg();
				singleSendMsg(toSocket,toMsg);
			}else  if(message.getTYPE() == MsgType.ENTER_SEAT_MSG) {//进入斗地主房间
				SocketEnterSeatMessage seatMessage = new SocketEnterSeatMessage(player, socket, enterSeatMap, gameGroups, nameToSocket);
				seatMessage.handleMsg(message);
				/*int seatNum = Integer.parseInt(message.getMsg());
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
					if(tripleSockets == null) {
						tripleSockets = new ConcurrentHashMap<String, Socket>();
					}
					Map<String,Socket> tripleSockets = new ConcurrentHashMap<String, Socket>();
					tripleSockets.put(player.getUserName(), socket);
					gameGroups.put(tableNum, tripleSockets);
				}
				
				//找到当前桌的其他2个人的socket
				*//**算法分析
				 * 0 1	 2
				 * 3 4	 5
				 * 6 7	 8
				 * 9 10 11
				 * 
				 * 如果seatNum能整除3,则是第一个位置；如果seatNum+1能整除3则第3个位置，否则就中间的位置
				 *//*
				//斗地主房间里座位信息在牌友间互通
				if(gameGroups.get(tableNum).size() > 1){
					//1将自己的信息发给同桌的比你先进的牌友
					batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+player.getUserName()+"="+seatNum, gameGroups.get(tableNum));
					//2将同桌的比你先进去的牌友的信息发给自己
					for(String username:gameGroups.get(tableNum).keySet()){
						if(username.equals(player.getUserName())) continue;
							singleSendMsg(this.socket, Constants.ENTER_ROOM_MSG_FLAG+username+"="+getSeatNumByUserName(username));
					}
				}*/
			}else if(message.getTYPE() == MsgType.SEND_ALL_MSG) {
				batchSendMsg(player.getUserName()+"说:"+message.getMsg(),nameToSocket);
			}else if(message.getTYPE() == MsgType.EXIT_SEAT_MSG){
				AbstractMessage<Socket> socketMessage = new SocketExitSeatMessage(player, socket, enterSeatMap, gameGroups, nameToSocket);
				socketMessage.handleMsg(message);
				/*int seatNum = Integer.parseInt(message.getMsg());
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
				player.setSeatNum(-1);*/
			}else if(message.getTYPE() == MsgType.ROOM_SEND_ALL_MSG){
				batchSendMsg(Constants.ROOM_SEND_ALL_MSG_FLAG+player.getUserName()+"说:"+message.getMsg(),gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())));
			}else if(message.getTYPE() == MsgType.ROOM_SEND_ONE_MSG){
				Socket toSocket = gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())).get(message.getToWho());
				String toMsg = Constants.ROOM_SEND_ONE_MSG_FLAG+player.getUserName()+"悄悄对你说:"+message.getMsg();
				singleSendMsg(toSocket,toMsg);
			}else if(message.getTYPE() == MsgType.GAME_READY_MSG){
				batchSendMsg(Constants.GAME_READY_MSG_FLAG+message.getMsg(), gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())));
			}
			
			
			/*else if(message.getTYPE() == MsgType.ROOM_REMOVE_SOCKET_MSG){
				gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())).remove(message.getMsg());
				//tripleSockets = null;
			}else if(message.getTYPE() == MsgType.ROOM_ADD_SOCKET_MSG){
				gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())).put(message.getMsg(), nameToSocket.get(message.getMsg()));
			}*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	/**
	 * 群发消息
	 * @param sendMsg
	 * @throws IOException
	 */
	public void batchSendMsg(String sendMsg,Map<String,Socket> sockets) throws IOException{
		for (Socket tempSocket : sockets.values()) {
			if(tempSocket == this.socket){
				continue;
			}
			singleSendMsg(tempSocket, sendMsg);
		}
	}
	/**
	 * 私发信息
	 * @param sendMsg
	 * @throws IOException
	 */
	private void singleSendMsg(Socket _socket,String sendMsg) throws IOException{
		if(_socket != null){
			PrintWriter printWriter = new PrintWriter(_socket.getOutputStream(), true);
			printWriter.println(sendMsg);
		}else{
			LOGGER.info("私聊对象的socket为空！所以私聊信息发送失败");
		}
		
	}
	/**
	 * 发送比你先入桌的牌友信息
	 * @throws  
	 */
	private void sendBeforeYouMsg(){
		if(gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())).size() > 1){
			for(String username:gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())).keySet()){
				if(username.equals(player.getUserName())) continue;
				try {
					batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+username, gameGroups.get(LandlordsUtil.getTableNum(player.getSeatNum())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 找同桌的其他牌友的座位号
	 * @param seatNum
	 * @param userName
	 * @return
	 */
	private Integer getSeatNumByUserName(String userName) {
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
