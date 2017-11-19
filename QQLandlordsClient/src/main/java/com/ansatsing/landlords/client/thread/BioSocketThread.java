package com.ansatsing.landlords.client.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.client.Context;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BioSocketThread implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(BioSocketThread.class);
	private boolean isStop = false;

	private AbstractProtocol protocol;
	private volatile Context context;
	public BioSocketThread(Context context) {
		String host = "localhost";
		int port = 6789;
		Socket socket = null;
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.context =context;
		this.context.getPlayer().setSocket(socket);
	}

	public void run() {

		BufferedReader bufferedReader;
		InputStream in=null;
		String readMsg = "";
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(context.getPlayer().getSocket().getInputStream(),"UTF-8"));
			//in = this.socket.getInputStream();
			while(!isStop) {
				readMsg = bufferedReader.readLine();
				/*int count = 0;
				while (count == 0) {
					count = in.available();
				}
				byte[] buf = new byte[count];
				in.read(buf);
				readMsg = new String(buf, "UTF-8");*/
				System.out.print(readMsg);
				if(readMsg != null){
					if(!readMsg.equals("")) {
						LOGGER.info("从服务器收到的消息："+readMsg);
						int endIdx = readMsg.indexOf("{");
						String className = readMsg.substring(0,endIdx);
						String classContent = readMsg.substring(endIdx);
						Class _class = null;
						try {
							_class = Class.forName(className);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						/*if(protocol != null){
							if(protocol.getGameLobbyWindow() != null && this.qqGameWindow == null){
								this.qqGameWindow = protocol.getGameLobbyWindow();
							}
							if(protocol.getLandlordsRoomWindow() != null && this.landlordsRoomWindow == null){
								this.landlordsRoomWindow = protocol.getLandlordsRoomWindow();
							}
						}*/
						protocol = (AbstractProtocol)JSON.parseObject(classContent,_class);
						//protocol.setSocket(socket);
						protocol.setLoginWidow(context.getLoginWidow());
						protocol.setGameLobbyWindow(context.getQqGameWindow());
						protocol.setLandlordsRoomWindow(context.getLandlordsRoomWindow());
						protocol.handleProt();
					}
				}
			}
		} catch (IOException e) {
			LOGGER.info("客户端信息接受线程出现异常："+e.getMessage());
		}finally {
			LOGGER.info("客户端信息接受线程关闭！");
			//掉线重新登录
			if(context.getLandlordsRoomWindow() != null){
				context.getLandlordsRoomWindow().closeRoom();
			}
			if(context.getQqGameWindow()!=null){
				context.getQqGameWindow().dispose();
			}
			//loginWidow = new LoginWidow();
		}
	}
	//停止线程
	public void stop(){
		this.isStop = true;
	}
	/*public ReceiveMessageHandler getReceiveMessageHandler() {
		return receiveMessageHandler;
	}*/

}
