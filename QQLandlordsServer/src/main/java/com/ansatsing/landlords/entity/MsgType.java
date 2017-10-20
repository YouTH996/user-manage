package com.ansatsing.landlords.entity;

public enum MsgType {
	USER_NAME_MSG("网名消息"),
	SEND_ALL_MSG("群发消息"),
	SEND_ONE_MSG("私聊消息"),
	SYSTEM_EXIT_MSG("系统退出消息"),
	ENTER_ROOM_MSG("进入斗地主房间消息");
	private String description;
	MsgType(String description) {
		this.description =description;
	}
}
