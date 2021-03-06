package com.ansatsing.landlords.client.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.util.Constants;
import com.google.common.base.Splitter;

public class ReceiveMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReceiveMessageHandler.class);
	private GameLobbyWindow gameLobbyWindow;
	private LandlordsRoomWindow landlordsRoomWindow;
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
				List<String> seatNums = Splitter.on(",").trimResults().splitToList(tempMsg);
				for(int i=0;i<seatNums.size();i++){
					int spiltIdx = seatNums.get(i).indexOf("=");
					int seatNum =Integer.parseInt(seatNums.get(i).substring(0,spiltIdx));
					String userName = seatNums.get(i).substring(spiltIdx+1);
					gameLobbyWindow.setSeatName(seatNum, userName);
				}
			}else if(msg.startsWith(Constants.ENTER_ROOM_MSG_FLAG)){
				if(landlordsRoomWindow != null){
					landlordsRoomWindow.setSeatUserName(msg.substring(Constants.ENTER_ROOM_MSG_FLAG.length()));
				}
			}else if(msg.startsWith(Constants.EXIT_ROOM_MSG_FLAG)){
				if(landlordsRoomWindow != null){
					landlordsRoomWindow.emptySeat(msg.substring(Constants.EXIT_ROOM_MSG_FLAG.length()));
				}
			}else if(msg.startsWith(Constants.ROOM_SEND_ALL_MSG_FLAG)){
				if(landlordsRoomWindow!=null){
					landlordsRoomWindow.setHistoryMsg(msg.substring(Constants.ROOM_SEND_ALL_MSG_FLAG.length()));
				}
			}else if(msg.startsWith(Constants.ROOM_SEND_ONE_MSG_FLAG)){
				if(landlordsRoomWindow!=null){
					landlordsRoomWindow.setHistoryMsg(msg.substring(Constants.ROOM_SEND_ONE_MSG_FLAG.length()));
				}
			}else if(msg.startsWith(Constants.GAME_READY_MSG_FLAG)){
				if(landlordsRoomWindow!=null){
					landlordsRoomWindow.setGameReady(msg.substring(Constants.GAME_READY_MSG_FLAG.length()));
				}
			}else if(msg.startsWith(Constants.SEND_CARDS_MSG_FlAG)){
				landlordsRoomWindow.startDealCards(msg.substring(Constants.SEND_CARDS_MSG_FlAG.length()));
			}else if(msg.startsWith(Constants.GAME_ROB_MSG_FLAG)) {
				landlordsRoomWindow.setOtherPlayerRole(msg.substring(Constants.GAME_ROB_MSG_FLAG.length()));
			}else if(msg.startsWith(Constants.START_READY_MSG_FLAG)){
				landlordsRoomWindow.startGameReadyThread(false);
			}else if(msg.startsWith(Constants.RESTART_READY_MSG_FLAG)){
				landlordsRoomWindow.startGameReadyThread(true);
			}else if(msg.startsWith(Constants.START_DEAL_MSG_FLAG)){
				landlordsRoomWindow.startGameDealThread(msg.substring(Constants.START_DEAL_MSG_FLAG.length()));
			}else if(msg.startsWith(Constants.SET_ROLE_MSG_FLAG)) {
				landlordsRoomWindow.setOtherPlayerRole(msg.substring(Constants.SET_ROLE_MSG_FLAG.length()));
			}else if(msg.startsWith(Constants.START_PLAY_MSG_FLAG)){
				landlordsRoomWindow.startGamePlayThread(msg.substring(Constants.START_PLAY_MSG_FLAG.length()));
			}else if(msg.startsWith(Constants.PLAY_CARD_MSG_FLAG)){
				//landlordsRoomWindow.showCardAndPlayCard(msg.substring(Constants.PLAY_CARD_MSG_FLAG.length()));
			}else{
				gameLobbyWindow.setHistoryMsg(msg);
			}
		}
	}
	public void setLandlordsRoomWindow(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	
}
