package com.ansatsing.landlords.client.handler;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.util.Constants;

public class LandlordsRoomReceiveMsgHandler {
	private LandlordsRoomWindow gameLobbyWindow;
	public LandlordsRoomReceiveMsgHandler(LandlordsRoomWindow gameLobbyWindow) {
		this.gameLobbyWindow = gameLobbyWindow;
	}
	public void handleMessage(String msg){
		String tempMsg = "";
		if(msg != null && !msg.trim().equals("")){
			if(msg.contains(Constants.ENTER_ROOM_MSG_FLAG)){//对号入座
				tempMsg = msg.substring(Constants.ENTER_ROOM_MSG_FLAG.length());
				gameLobbyWindow.setSeatUserName(tempMsg);
			}else if(msg.contains(Constants.EXIT_ROOM_MSG_FLAG)){//空出座位
				tempMsg = msg.substring(Constants.EXIT_ROOM_MSG_FLAG.length());
				gameLobbyWindow.emptySeat(tempMsg);
			}else{
				gameLobbyWindow.setHistoryMsg(msg);
			}
		}
	}
}
