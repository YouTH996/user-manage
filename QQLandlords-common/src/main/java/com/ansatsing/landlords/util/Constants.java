package com.ansatsing.landlords.util;

public class Constants {
	public  final static String SYSTEM_EXIT_MSG_FLAG = "qQ-landLord@exit%ByE";//客户端退出时发送这样的消息从而告诉服务器要把相应的线程停止。
	public final static String USER_NAME_MSG_FLAG = "name-@";//用户名信息标志
	public final static String ENTER_SEAT_MSG_FLAG = "enTeR@rOoM*MSg]";//霸占座位信息标志
	public final static String EXIT_SEAT_MSG_FLAG = "e4TeR@rmoM*MSg{";//空出座位信息标志
	public final static String INIT_SEAT_MSG_FLAG = "@4TeR@r$oM*MS5{";//初始化座位信息标志
	public final static String ENTER_ROOM_MSG_FLAG = "{enTeR@5jhs*MSg]";//进入房间信息标志
	public final static String EXIT_ROOM_MSG_FLAG = "^e4TcR@rmdM*MSg{";//退出斗地主房间信息标志
	public final static String ROOM_SEND_ALL_MSG_FLAG = "{en#D%G^H&JGBSg]";//房间群发信息标志
	public final static String ROOM_SEND_ONE_MSG_FLAG = "^e4TcR@)*U%F^J{";//房间私聊信息标志
	public final static String ROOM_REMOVE_SOCKET_FLAG = "(e)TcR@)*U+F^J{";//牌友退出房间时移除牌友socket信息标志
	public final static String ROOM_ADD_SOCKET_FLAG = "(eb%TcR@)*F+F^J{";//牌友进入房间时添加牌友socket信息标志
	
}
