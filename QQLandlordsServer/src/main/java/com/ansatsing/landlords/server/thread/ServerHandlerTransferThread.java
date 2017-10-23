package com.ansatsing.landlords.server.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.server.handler.ServerMessageHandler;
import com.ansatsing.landlords.util.MessageUtil;
/**
 * qq斗地主服务器端网络IO处理中心
 * @author sunyq
 *
 */
public class ServerHandlerTransferThread implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandlerTransferThread.class);
	private Socket socket;
	Map<String, Socket> nameToSocket;
	private Player player;
	private ServerMessageHandler messageHandler;
	//private Map<String,Socket> tripleSockets;//记住正在斗地主的3个人的socket
	//private Map<Integer, Map<String, Socket>> gameGroups;
	public ServerHandlerTransferThread( Socket socket,Map<String, Socket> nameToSocket,Map<Integer, String> enterSeatMap,Map<Integer, Map<String, Socket>> gameGroups) {
		this.socket = socket;
		this.nameToSocket = nameToSocket;
		player = new Player();
		messageHandler = new ServerMessageHandler(nameToSocket, player,socket,enterSeatMap,gameGroups);
		//this.gameGroups = gameGroups;
	}

	public void run() {//消息处理以及中转
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		Message message = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			String readMsg = "";
			while (true) {
				readMsg = bufferedReader.readLine();
				if (!readMsg.equals("")) {
					System.out.println((player.getUserName() == null ? "未登陆的":player.getUserName())+"发送了消息："+readMsg);
					message = MessageUtil.handle(readMsg);
					if(message != null){
						if(message.getTYPE() == MsgType.SYSTEM_EXIT_MSG) {//客户退出系统
							if(player == null || player.getUserName() == null) {
								System.out.println("一位牌友正试图进入QQ斗地主游戏厅，但最后还是走了。。。");
							}else {
								System.out.println(player.getUserName()+"牌友下线走了。。。。");
							}
							break;
						}else{
							messageHandler.handleMessage(message);
						}
					}else{
						LOGGER.info("message是空指针");
					}
				}
			}
			if(player != null && player.getUserName() != null) {
				nameToSocket.remove(player.getUserName());
				messageHandler.batchSendMsg(player.getUserName()+"退出聊天室了!当前聊天室人数："+nameToSocket.size(),nameToSocket);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(printWriter != null) {
				printWriter.close();
			}
			if(this.socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 移除退出房间的同桌牌友的socket
	 * @param userName
	 */
	/*public void removePlayer(String userName){
		tripleSockets.remove(userName);
	}*/
}