package com.ansatsing.landlords.client.handler;

import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.util.Constants;
/*
 * 消息包装工具
 */
public class SendMessagePack {
	public static String packMsg(MsgType msgType,String msg){
		String tempMsg = "";
		if(msgType == MsgType.ENTER_SEAT_MSG){
			return Constants.ENTER_SEAT_MSG_FLAG+msg;
		}else if(msgType == MsgType.SYSTEM_EXIT_MSG){
			return Constants.SYSTEM_EXIT_MSG_FLAG;
		}else if(msgType == MsgType.USER_NAME_MSG){
			return Constants.USER_NAME_MSG_FLAG+msg;
		}else if(msgType == MsgType.SEND_ONE_MSG){
			return msg;
		}else if(msgType == MsgType.SEND_ALL_MSG){
			return msg;
		}else if(msgType == MsgType.EXIT_SEAT_MSG){
			return Constants.EXIT_SEAT_MSG_FLAG+msg;
		}
		return tempMsg;
	}
}
