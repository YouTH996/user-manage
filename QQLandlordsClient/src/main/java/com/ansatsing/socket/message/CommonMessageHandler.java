package com.ansatsing.socket.message;

import java.io.PrintWriter;
import java.net.Socket;

public class CommonMessageHandler extends AbstractMessageHandler {
	/**
	 * 
	 */
	@Override
	public String messageHandle(String message,Socket socket) {
		String readMsg = message;
/*		if (readMsg.startsWith("name-@")) {// 网名消息格式： name-@安清
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
		return "";
	}

}
