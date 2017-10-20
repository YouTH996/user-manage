package com.ansatsing.landlords.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;

public class MessageHandler {
	private Map<String, Socket> nameToSocket;
	private Socket socket;//当前客户的socket
	private PrintWriter printWriter;
	private Player player;
	public MessageHandler(Map<String, Socket> nameToSocket,Player player) {
		this.nameToSocket = nameToSocket;
		this.player = player;
	}
	public void handleMessage(Message message) {
		try {
			if(message.getTYPE() == MsgType.USER_NAME_MSG) {
				if(nameToSocket.keySet().contains(message.getMsg())){//处理重复网名
					/*printWriter = new PrintWriter(socket.getOutputStream(), true);
					printWriter.println("这网名有人正在使用,请更换网名!");*/
					singleSendMsg("这网名有人正在使用,请更换网名!");
				}else {
					String userName = message.getMsg();
					batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！");
					printWriter = new PrintWriter(this.socket.getOutputStream(), true);
					singleSendMsg("这个网名可以啦!");
					nameToSocket.put(userName, socket);
					player = new Player();
					player.setUserName(userName);
					
				}
			}else if(message.getTYPE() == MsgType.SEND_ONE_MSG) {
				Socket toSocket = nameToSocket.get(message.getToWho());
				//printWriter = new PrintWriter(toSocket.getOutputStream(), true);
				String toMsg = player.getUserName()+"悄悄对你说:"+message.getMsg();
				//printWriter.println(toMsg);
				singleSendMsg(toMsg);
			}else  if(message.getTYPE() == MsgType.ENTER_ROOM_MSG) {//进入斗地主房间
				int seatNum = Integer.parseInt(message.getMsg());
				player.setSeatNum(seatNum);
			}else if(message.getTYPE() == MsgType.SEND_ALL_MSG) {
				batchSendMsg(player.getUserName()+"说:"+message.getMsg());
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
	private void batchSendMsg(String sendMsg) throws IOException{
		for (Socket tempSocket : nameToSocket.values()) {
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
	private void singleSendMsg(String sendMsg) throws IOException{
		printWriter = new PrintWriter(socket.getOutputStream(), true);
		printWriter.println(sendMsg);
	}
}
