package com.ansatsing.landlords.util;

public class Constants {
	public final  static int RECONNECT_NUM = 5;//掉线重连次数
	public final  static int CLIENT_IDLE_TIMEOUT = 4;//客户端读写空闲时间
	public final  static int SERVER_IDLE_TIMEOUT = 10;//服务器端读写空闲时间
	public final  static String PROT_PACK_NAME = "com.ansatsing.landlords.entity.protocol";//斗地主协议包名
	public final static  int CARD_WIDTH = 105;//牌的宽度
	public final static  int CARD_HEIGHT = 150;//牌的高度
	public final static  int CARD_PADDING = 20;//牌之间的间距
	public final  static  int PLAY_CARD_TIMEOUT = 18;//每次出牌超时时间 单位秒
	public final static Integer CARDS[] = new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54};
	public  final static String SYSTEM_EXIT_MSG_FLAG = "SYSTEM_EXIT_MSG_FLAG";//客户端退出时发送这样的消息从而告诉服务器要把相应的线程停止。
	public final static String USER_NAME_MSG_FLAG = "name-@";//用户名信息标志
	public final static String ENTER_SEAT_MSG_FLAG = "ENTER_SEAT_MSG_FLAG";//霸占座位信息标志
	public final static String EXIT_SEAT_MSG_FLAG = "EXIT_SEAT_MSG_FLAG";//空出座位信息标志
	public final static String INIT_SEAT_MSG_FLAG = "INIT_SEAT_MSG_FLAG";//初始化座位信息标志
	public final static String ENTER_ROOM_MSG_FLAG = "ENTER_ROOM_MSG_FLAG";//进入房间信息标志
	public final static String EXIT_ROOM_MSG_FLAG = "EXIT_ROOM_MSG_FLAG";//退出斗地主房间信息标志
	public final static String ROOM_SEND_ALL_MSG_FLAG = "ROOM_SEND_ALL_MSG_FLAG";//房间群发信息标志
	public final static String ROOM_SEND_ONE_MSG_FLAG = "ROOM_SEND_ONE_MSG_FLAG";//房间私聊信息标志
	public final static String ROOM_REMOVE_SOCKET_FLAG = "ROOM_REMOVE_SOCKET_FLAG";//牌友退出房间时移除牌友socket信息标志
	public final static String ROOM_ADD_SOCKET_FLAG = "ROOM_ADD_SOCKET_FLAG";//牌友进入房间时添加牌友socket信息标志
	public final static String GAME_READY_MSG_FLAG = "GAME_READY_MSG_FLAG";//游戏准备信息标志
	public final static String GAME_DEAL_MSG_FLAG = "GAME_DEAL_MSG_FLAG";//游戏发牌信息标志+
	public final static String SEND_CARDS_MSG_FlAG="SEND_CARDS_MSG_FlAG";//洗过的牌发送到客户端
	public final static String GAME_ROB_MSG_FLAG = "SEND_ROB_MSG_FLAG";//("抢地主阶段发送抢地主情况给服务器的信息的标志");
	public final static String START_READY_MSG_FLAG = "START_READY_MSG_FLAG";//启动游戏准备线程的信号标志
	public final static String START_DEAL_MSG_FLAG = "START_DEAL_MSG_FLAG";//启动游戏发牌线程的信号标志
	public final static String SET_ROLE_MSG_FLAG = "SET_ROLE_MSG_FLAG";//设置斗地主角色的信号标志
	public final static String RESTART_READY_MSG_FLAG = "RESTART_READY_MSG_FLAG";//重启动游戏准备线程的信号标志
	public final static String START_PLAY_MSG_FLAG = "START_PLAY_MSG_FLAG";//启动打牌线程的信号标志
	public final static String PLAY_CARD_MSG_FLAG = "PLAY_CARD_MSG_FLAG";//游戏出牌的信号标志
	public final static String GAME_OVER_MSG_FLAG = "GAME_OVER_MSG_FLAG";//一轮游戏结束的信号标志
}
