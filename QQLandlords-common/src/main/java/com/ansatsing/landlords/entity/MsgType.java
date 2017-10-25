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
	ROOM_REMOVE_SOCKET_MSG("移除同桌牌友socket信息"),
	ROOM_ADD_SOCKET_MSG("增加同桌牌友socket信息"),
	GAME_READY_MSG("游戏准备信息"),
	GAME_DEAL_MSG("客户端请求服务器发牌信息"),
	SEND_CARDS_MSG("服务器端发送洗过的牌到客户端以供客户端发牌动作的信息");
	private String description;
	MsgType(String description) {
		this.description =description;
	}
}
