package com.ansatsing.socket.client.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class QQClientHandler implements Runnable {
	private Socket socket;
	private boolean isStop = false;
	private QQGameWindow qqGameWindow;
	public QQClientHandler(Socket socket,QQGameWindow qqGameWindow) {
		this.socket = socket;
		this.qqGameWindow = qqGameWindow;
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
						qqGameWindow.setHistoryMsg(readMsg);
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
}
