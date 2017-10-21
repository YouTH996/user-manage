package com.ansatsing.landlords.client.handler;

import java.net.Socket;
import java.util.List;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.util.Constants;
import com.google.common.base.Splitter;

public class ReceiveMessageHandler {
	private GameLobbyWindow gameLobbyWindow;
	public ReceiveMessageHandler(GameLobbyWindow gameLobbyWindow) {
		this.gameLobbyWindow = gameLobbyWindow;
	}
	public void handleMessage(String msg){
		String tempMsg = "";
		if(msg != null && !msg.trim().equals("")){
			if(msg.contains(Constants.ENTER_SEAT_MSG_FLAG)){//对号入座
				//String[] strArr = msg.split(Constants.ENTER_SEAT_MSG_FLAG);//不行，返回数组长度为1
				List<String> strList = Splitter.on(Constants.ENTER_SEAT_MSG_FLAG).splitToList(msg);
				gameLobbyWindow.setSeatName(Integer.parseInt(strList.get(0)), strList.get(1));
			}else if(msg.contains(Constants.EXIT_SEAT_MSG_FLAG)){//空出座位
				tempMsg = msg.substring(Constants.EXIT_SEAT_MSG_FLAG.length());
				gameLobbyWindow.emptySeat(Integer.parseInt(tempMsg));
			}else if(msg.startsWith(Constants.INIT_SEAT_MSG_FLAG)){
				tempMsg = msg.substring(Constants.INIT_SEAT_MSG_FLAG.length());
				System.out.println(gameLobbyWindow.getTitle()+"============"+tempMsg);
				List<String> seatNums = Splitter.on(",").trimResults().splitToList(tempMsg);
				for(int i=0;i<seatNums.size();i++){
					int spiltIdx = seatNums.get(i).indexOf("=");
					int seatNum =Integer.parseInt(seatNums.get(i).substring(0,spiltIdx));
					String userName = seatNums.get(i).substring(spiltIdx+1);
					gameLobbyWindow.setSeatName(seatNum, userName);
				}
			}else{
				gameLobbyWindow.setHistoryMsg(msg);
			}
		}
	}
}
