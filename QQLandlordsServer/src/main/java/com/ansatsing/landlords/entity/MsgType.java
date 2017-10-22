package com.ansatsing.landlords.entity;

public enum MsgType {
	USER_NAME_MSG("网名消息"),
	SEND_ALL_MSG("大厅群发消息"),
	SEND_ONE_MSG("大厅私聊消息"),
	SYSTEM_EXIT_MSG("系统退出消息"),
	ENTER_SEAT_MSG("霸占座位的信息"),
	EXIT_SEAT_MSG("空出座位的信息"),
	ENTER_ROOM_MSG("进入斗地主房间信息"),
	EXIT_ROOM_MSG("退出斗地主房间信息"),
	ROOM_SEND_ALL_MSG("房间群发消息"),
	ROOM_SEND_ONE_MSG("房间私聊消息"),
	ROOM_REMOVE_SOCKET_MSG("移除同桌牌友socket信息");
	private String description;
	MsgType(String description) {
		this.description =description;
	}
}
