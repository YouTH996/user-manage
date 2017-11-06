package com.ansatsing.landlords.client.thread;

import com.ansatsing.landlords.client.handler.ReceiveMessageHandler;
import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
//为了无限信息扩展而备份
public class ClientReceiveThread_20171106 implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ClientReceiveThread_20171106.class);
	private Socket socket;
	private boolean isStop = false;
	private GameLobbyWindow qqGameWindow;
	private ReceiveMessageHandler receiveMessageHandler;
	public ClientReceiveThread_20171106(Socket socket, GameLobbyWindow qqGameWindow) {
		this.socket = socket;
		this.qqGameWindow = qqGameWindow;
		this.receiveMessageHandler = new ReceiveMessageHandler(qqGameWindow);
	}
	public void run() {
		BufferedReader bufferedReader;
		String readMsg = "";
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			while(!isStop) {
				readMsg = bufferedReader.readLine();
				if(readMsg != null){
					if(!readMsg.equals("")) {
						LOGGER.info("从服务器收到的消息："+readMsg);
						receiveMessageHandler.handleMessage(readMsg);
					}
				}
			}
			//System.out.println("您退出了聊天室！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			/*if(socket != null) {//只要没有收到退出信号，就不应该有关闭套接字的代码
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
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
