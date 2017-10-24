package com.ansatsing.landlords.server.message;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Player;


public class SocketEnterSeatMessage extends EnterSeatMessage<Socket> {
	private final static Logger LOGGER = LoggerFactory.getLogger(SocketEnterSeatMessage.class);
	public SocketEnterSeatMessage(Player player,Socket socket,Map<Integer, String> enterSeatMap,Map<Integer, Map<String, Socket>> gameGroups,Map<String, Socket> nameToSocket) {
		this.player = player;
		this.socket = socket;
		this.enterSeatMap = enterSeatMap;
		this.gameGroups = gameGroups;
		this.nameToSocket = nameToSocket;		
	}
	/**
	 * 群发消息
	 * @param sendMsg
	 * @throws IOException
	 */
	public void batchSendMsg(String sendMsg,Map<String,Socket> sockets) {
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
	protected void singleSendMsg(Socket _socket,String sendMsg){
		if(_socket != null){
			PrintWriter printWriter;
			try {
				printWriter = new PrintWriter(_socket.getOutputStream(), true);
				printWriter.println(sendMsg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			LOGGER.info("私聊对象的socket为空！所以私聊信息发送失败");
		}
	}
}
