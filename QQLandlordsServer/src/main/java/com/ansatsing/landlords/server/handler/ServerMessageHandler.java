package com.ansatsing.landlords.server.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.Constants;
import com.ansatsing.landlords.util.ThreadUtil;

import ch.qos.logback.core.joran.conditional.ElseAction;

public class ServerMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerMessageHandler.class);
	private Map<String, Socket> nameToSocket;
	private Socket socket;//当前客户的socket
	private PrintWriter printWriter;
	private Player player;
	private Map<Integer, String> enterSeatMap;
	private Map<String,Socket> tripleSockets;//记住正在斗地主的3个人的socket
	public ServerMessageHandler(Map<String, Socket> nameToSocket,Player player,Socket socket,Map<Integer, String> enterSeatMap,Map<String,Socket> tripleSockets) {
		this.nameToSocket = nameToSocket;
		this.player = player;
		this.socket = socket;
		this.enterSeatMap = enterSeatMap;
		this.tripleSockets = tripleSockets;
	}

	public void handleMessage(Message message) {
		try {
			if(message.getTYPE() == MsgType.USER_NAME_MSG) {
				if(nameToSocket.keySet().contains(message.getMsg())){//处理重复网名
					/*printWriter = new PrintWriter(socket.getOutputStream(), true);
					printWriter.println("这网名有人正在使用,请更换网名!");*/
					singleSendMsg(socket,"这网名有人正在使用,请更换网名!");
				}else {
					String userName = message.getMsg();
					batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",nameToSocket);
					printWriter = new PrintWriter(this.socket.getOutputStream(), true);
					singleSendMsg(socket,"这个网名可以啦!");
					nameToSocket.put(userName, socket);
					player.setUserName(userName);
					if(player.getUserName() != null ){//设置线程名称
						Thread.currentThread().setName(player.getUserName());
					}
					if(enterSeatMap.size() > 0){
						StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append(Constants.INIT_SEAT_MSG_FLAG);
						for(Integer seatNum:enterSeatMap.keySet()){
							stringBuilder.append(seatNum+"="+enterSeatMap.get(seatNum)+",");
						}
						stringBuilder.deleteCharAt(stringBuilder.length()-1);
						//String initSeat = Constants.INIT_SEAT_MSG_FLAG+Joiner.on(",").join(enterSeatList);
						singleSendMsg(socket, stringBuilder.toString());
					}
				}
			}else if(message.getTYPE() == MsgType.SEND_ONE_MSG) {
				Socket toSocket = nameToSocket.get(message.getToWho());
				String toMsg = player.getUserName()+"悄悄对你说:"+message.getMsg();
				singleSendMsg(toSocket,toMsg);
			}else  if(message.getTYPE() == MsgType.ENTER_SEAT_MSG) {//进入斗地主房间
				int seatNum = Integer.parseInt(message.getMsg());
				player.setSeatNum(seatNum);
				enterSeatMap.put(seatNum, player.getUserName());
				batchSendMsg(message.getMsg()+Constants.ENTER_SEAT_MSG_FLAG+player.getUserName(),nameToSocket);
				//enterSeatList.add(seatNum+"="+player.getUserName());
				
				//找到当前桌的其他2个人的socket
				/**算法分析
				 * 0 1	 2
				 * 3 4	 5
				 * 6 7	 8
				 * 9 10 11
				 * 
				 * 如果seatNum能整除3,则是第一个位置；如果seatNum+1能整除3则第3个位置，否则就中间的位置
				 */
				this.tripleSockets.put(player.getUserName(), this.socket);
				if(seatNum % 3 == 0){
					if(enterSeatMap.containsKey(seatNum +1)){
						this.tripleSockets.put(enterSeatMap.get(seatNum+1), nameToSocket.get(enterSeatMap.get(seatNum+1)));
					}
					if(enterSeatMap.containsKey(seatNum +2)){
						this.tripleSockets.put(enterSeatMap.get(seatNum+2), nameToSocket.get(enterSeatMap.get(seatNum+2)));
					}
						
				}else if((seatNum+1)%3 == 0){
					if(enterSeatMap.containsKey(seatNum -1)){
						this.tripleSockets.put(enterSeatMap.get(seatNum-1), nameToSocket.get(enterSeatMap.get(seatNum-1)));
					}
					if(enterSeatMap.containsKey(seatNum -2)){
						this.tripleSockets.put(enterSeatMap.get(seatNum-2), nameToSocket.get(enterSeatMap.get(seatNum-2)));
					}
				}else{
					if(enterSeatMap.containsKey(seatNum -1)){
						this.tripleSockets.put(enterSeatMap.get(seatNum-1), nameToSocket.get(enterSeatMap.get(seatNum-1)));
					}
					if(enterSeatMap.containsKey(seatNum +1)){
						this.tripleSockets.put(enterSeatMap.get(seatNum+1), nameToSocket.get(enterSeatMap.get(seatNum+1)));
					}
				}
				//斗地主房间里座位信息在牌友间互通
				if(tripleSockets.size() > 1){
					//1将自己的信息发给同桌的比你先进的牌友
					batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+player.getUserName(), tripleSockets);
					//2将同桌的比你先进去的牌友的信息发给自己
					for(String username:tripleSockets.keySet()){
						if(username.equals(player.getUserName())) continue;
							singleSendMsg(this.socket, Constants.ENTER_ROOM_MSG_FLAG+username);
					}
				}
			}else if(message.getTYPE() == MsgType.SEND_ALL_MSG) {
				batchSendMsg(player.getUserName()+"说:"+message.getMsg(),nameToSocket);
			}else if(message.getTYPE() == MsgType.EXIT_SEAT_MSG){
				player.setSeatNum(0);
				int seatNum = Integer.parseInt(message.getMsg());
				//enterSeatList.remove(seatNum+"="+player.getUserName());
				if(enterSeatMap.containsKey(seatNum)){
					enterSeatMap.remove(seatNum);
				}
				batchSendMsg(Constants.EXIT_SEAT_MSG_FLAG+message.getMsg(),nameToSocket);
				
				//斗地主房间里座位信息在牌友间互通
				if(tripleSockets.size() > 1){
					//1将自己的退出房间的信息发给的牌友
					batchSendMsg(Constants.EXIT_ROOM_MSG_FLAG+player.getUserName(), tripleSockets);
					//2清除牌友socket
					for(String username:tripleSockets.keySet()){
						if(username.equals(player.getUserName())) continue;
						tripleSockets.remove(username);
					}
				}
			}else if(message.getTYPE() == MsgType.ROOM_SEND_ALL_MSG){
				batchSendMsg(Constants.ROOM_SEND_ALL_MSG_FLAG+player.getUserName()+"说:"+message.getMsg(),tripleSockets);
			}else if(message.getTYPE() == MsgType.ROOM_SEND_ONE_MSG){
				Socket toSocket = tripleSockets.get(message.getToWho());
				String toMsg = Constants.ROOM_SEND_ONE_MSG_FLAG+player.getUserName()+"悄悄对你说:"+message.getMsg();
				singleSendMsg(toSocket,toMsg);
			}else if(message.getTYPE() == MsgType.ROOM_REMOVE_SOCKET_MSG){
				tripleSockets.remove(message.getMsg());
			}
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
			printWriter = new PrintWriter(tempSocket.getOutputStream(), true);
			printWriter.println(sendMsg);
		}
	}
	/**
	 * 私发信息
	 * @param sendMsg
	 * @throws IOException
	 */
	private void singleSendMsg(Socket _socket,String sendMsg) throws IOException{
		if(_socket != null){
			printWriter = new PrintWriter(_socket.getOutputStream(), true);
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
		if(tripleSockets.size() > 1){
			for(String username:tripleSockets.keySet()){
				if(username.equals(player.getUserName())) continue;
				try {
					batchSendMsg(Constants.ENTER_ROOM_MSG_FLAG+username, tripleSockets);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
