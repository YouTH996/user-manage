package com.ansatsing.landlords.client.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.ansatsing.landlords.client.handler.ReceiveMessageHandler;
import com.ansatsing.landlords.client.ui.GameLobbyWindow;

public class ClientReceiveThread implements Runnable {
	private Socket socket;
	private boolean isStop = false;
	private GameLobbyWindow qqGameWindow;
	private ReceiveMessageHandler receiveMessageHandler;
	public ClientReceiveThread(Socket socket,GameLobbyWindow qqGameWindow) {
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
						//System.out.println(readMsg);
						receiveMessageHandler.handleMessage(readMsg);
					}
				}
			}
			//System.out.println("您退出了聊天室！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
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
