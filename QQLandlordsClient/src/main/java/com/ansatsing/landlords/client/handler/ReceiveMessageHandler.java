package com.ansatsing.landlords.client.handler;

import java.net.Socket;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.util.Constants;

public class ReceiveMessageHandler {
	private GameLobbyWindow gameLobbyWindow;
	public ReceiveMessageHandler(GameLobbyWindow gameLobbyWindow) {
		this.gameLobbyWindow = gameLobbyWindow;
	}
	public void handleMessage(String msg){
		String tempMsg = "";
		if(msg != null && !msg.trim().equals("")){
			if(msg.contains(Constants.ENTER_SEAT_MSG_FLAG)){//对号入座
				String[] strArr = msg.split(Constants.ENTER_SEAT_MSG_FLAG);
				gameLobbyWindow.setSeatName(Integer.parseInt(strArr[0]), strArr[1]);
			}else if(msg.contains(Constants.EXIT_SEAT_MSG_FLAG)){//空出座位
				tempMsg = msg.substring(Constants.EXIT_SEAT_MSG_FLAG.length());
				gameLobbyWindow.emptySeat(Integer.parseInt(tempMsg));
			}else{
				gameLobbyWindow.setHistoryMsg(msg);
			}
		}
	}
}
