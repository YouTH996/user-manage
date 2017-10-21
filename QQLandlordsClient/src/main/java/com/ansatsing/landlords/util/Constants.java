package com.ansatsing.landlords.util;

public class Constants {
	public  final static String SYSTEM_EXIT_MSG_FLAG = "qQ-landLord@exit%ByE";//客户端退出时发送这样的消息从而告诉服务器要把相应的线程停止。
	public final static String USER_NAME_MSG_FLAG = "name-@";//用户名信息标志
	public final static String ENTER_SEAT_MSG_FLAG = "enTeR@rOoM*MSg]";//霸占座位信息标志
	public final static String EXIT_SEAT_MSG_FLAG = "e4TeR@rmoM*MSg{"; //空出座位信息标志
	public final static String INIT_SEAT_MSG_FLAG = "@4TeR@r$oM*MS5{";//初始化座位信息标志
}
