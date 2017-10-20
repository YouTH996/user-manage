package com.ansatsing.landlords.entity;

public enum MsgType {
	USER_NAME_MSG("网名消息"),
	SEND_ALL_MSG("群发消息"),
	SEND_ONE_MSG("私聊消息"),
	SYSTEM_EXIT_MSG("系统退出消息"),
	ENTER_SEAT_MSG("霸占座位的消息"),
	EXIT_SEAT_MSG("空出座位的消息");
	private String description;
	MsgType(String description) {
		this.description =description;
	}
}
