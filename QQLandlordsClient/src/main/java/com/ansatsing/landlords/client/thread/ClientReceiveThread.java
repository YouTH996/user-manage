package com.ansatsing.landlords.client.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.handler.ReceiveMessageHandler;
import com.ansatsing.landlords.client.ui.GameLobbyWindow;

public class ClientReceiveThread implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientReceiveThread.class);
	private Socket socket;
	private boolean isStop = false;
	private volatile GameLobbyWindow qqGameWindow;
	private ReceiveMessageHandler receiveMessageHandler;
	private volatile LoginWidow loginWidow;
	private volatile LandlordsRoomWindow landlordsRoomWindow;
	private AbstractProtocol protocol;
	private Player player;
	public ClientReceiveThread(Socket socket, Player player) {
		this.socket = socket;
		this.player = player;
		//this.qqGameWindow = qqGameWindow;
		//this.receiveMessageHandler = new ReceiveMessageHandler(qqGameWindow);
	}

	public GameLobbyWindow getQqGameWindow() {
		return qqGameWindow;
	}

	public void setQqGameWindow(GameLobbyWindow qqGameWindow) {
		this.qqGameWindow = qqGameWindow;
	}

	public LoginWidow getLoginWidow() {
		return loginWidow;
	}

	public void setLoginWidow(LoginWidow loginWidow) {
		this.loginWidow = loginWidow;
	}

	public LandlordsRoomWindow getLandlordsRoomWindow() {
		return landlordsRoomWindow;
	}

	public void setLandlordsRoomWindow(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}

	public void run() {
		BufferedReader bufferedReader;
		String readMsg = "";
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
			while(!isStop) {
				readMsg = bufferedReader.readLine();
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
						if(protocol != null){
							if(protocol.getGameLobbyWindow() != null && this.qqGameWindow == null){
								this.qqGameWindow = protocol.getGameLobbyWindow();
							}
							if(protocol.getLandlordsRoomWindow() != null && this.landlordsRoomWindow == null){
								this.landlordsRoomWindow = protocol.getLandlordsRoomWindow();
							}
						}
						protocol = (AbstractProtocol)JSON.parseObject(classContent,_class);
						protocol.setSocket(socket);
						protocol.setLoginWidow(loginWidow);
						protocol.setGameLobbyWindow(qqGameWindow);
						protocol.setLandlordsRoomWindow(landlordsRoomWindow);
						protocol.handleProt();
					}
				}
			}
		} catch (IOException e) {
			LOGGER.info("客户端信息接受线程出现异常："+e.getMessage());
		}finally {
			LOGGER.info("客户端信息接受线程关闭！");
			//掉线重新登录
			if(landlordsRoomWindow != null){
				landlordsRoomWindow.closeRoom();
			}
			if(qqGameWindow!=null){
				qqGameWindow.dispose();
			}
			loginWidow = new LoginWidow();
		}
	}
	//停止线程
	public void stop(){
		this.isStop = true;
	}
	public ReceiveMessageHandler getReceiveMessageHandler() {
		return receiveMessageHandler;
	}

}
