package com.ansatsing.landlords.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collections;
import java.util.Map;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.MessageUtil;
/**
 * qq斗地主服务器端网络IO处理中心
 * @author sunyq
 *
 */
class QQServerHandler implements Runnable {
	private Socket socket;
	private String name;// 网名，必须唯一
	Map<String, Socket> nameToSocket;
	private Player player;
	public QQServerHandler( Socket socket,Map<String, Socket> nameToSocket) {
		this.socket = socket;
		this.nameToSocket = nameToSocket;
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
					if(player == null) {
						System.out.println("新进网友正在为起网名发的信息："+readMsg);
					}else {
						System.out.println(player.getUserName()+"发送了消息："+readMsg);
					}
					////////////////////////////////////////////////////////
					message = MessageUtil.handle(readMsg);
					if(message.getTYPE() == MsgType.USER_NAME_MSG) {
						if(nameToSocket.keySet().contains(message.getMsg())){//处理重复网名
							printWriter = new PrintWriter(this.socket.getOutputStream(), true);
							printWriter.println("这网名有人正在使用,请更换网名!");
						}else {
							String userName = message.getMsg();
							batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！");
							printWriter = new PrintWriter(this.socket.getOutputStream(), true);
							printWriter.println("这个网名可以啦!");
							nameToSocket.put(userName, socket);
							player = new Player();
							player.setUserName(userName);
						}
					}else if(message.getTYPE() == MsgType.SEND_ONE_MSG) {
						Socket toSocket = nameToSocket.get(message.getToWho());
						printWriter = new PrintWriter(toSocket.getOutputStream(), true);
						String toMsg = player.getUserName()+"悄悄对你说:"+message.getMsg();
						printWriter.println(toMsg);
					}else if(message.getTYPE() == MsgType.SYSTEM_EXIT_MSG) {//客户退出系统
						break;
					}else if(message.getTYPE() == MsgType.ENTER_ROOM_MSG) {//进入斗地主房间
						int seatNum = Integer.parseInt(message.getMsg());
						player.setSeatNum(seatNum);
					}else if(message.getTYPE() == MsgType.SEND_ALL_MSG) {
						batchSendMsg(player.getUserName()+"说:"+message.getMsg());
					}
					//////////////////////////////////////////////////////////
					/*if (readMsg.startsWith("name-@")) {// 网名消息格式： name-@安清
						name = readMsg.substring(6);
						if(nameToSocket.keySet().contains(name)){//处理重复网名
							printWriter = new PrintWriter(this.socket.getOutputStream(), true);
							printWriter.println("这网名有人正在使用,请更换网名!");
						}else{
							batchSendMsg(this.name+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！");
							//socketMap.put(this.socketId, socket);//只有网名合法才可以Put
							printWriter = new PrintWriter(this.socket.getOutputStream(), true);
							printWriter.println("这个网名可以啦!");
							nameToSocket.put(name, socket);
						}
					} else if (readMsg.startsWith("@")) {// 私聊消息格式：@网名:范爷，你还在线吗？找你有事！
						int endIndex = readMsg.indexOf(":");
						String toName = readMsg.substring(1, endIndex);
						Socket toSocket = nameToSocket.get(toName);
						printWriter = new PrintWriter(toSocket.getOutputStream(), true);
						String toMsg = name+"悄悄对你说:"+readMsg.substring(endIndex + 1);
						printWriter.println(toMsg);
						if (toMsg.toUpperCase().contains("BYE")) {
							break;
						}
					} else {
						batchSendMsg(this.name+"说:"+readMsg);
						if (readMsg.toUpperCase().contains("BYE")){
							break;
						}
					}*/
				}
			}
			if(player != null) {
				nameToSocket.remove(player.getUserName());
				batchSendMsg(player.getUserName()+"退出聊天室了!当前聊天室人数："+nameToSocket.size());
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
	private void batchSendMsg(String sendMsg) throws IOException{
		PrintWriter printWriter = null;
		for (Socket tempSocket : nameToSocket.values()) {
			if(tempSocket == this.socket){
				continue;
			}
			printWriter = new PrintWriter(tempSocket.getOutputStream(), true);
			printWriter.println(sendMsg);
		}
	}
}