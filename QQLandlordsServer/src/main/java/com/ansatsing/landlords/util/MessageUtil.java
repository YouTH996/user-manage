package com.ansatsing.landlords.util;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;

public class MessageUtil {
	public static Message handle(String message) {
		Message msg = null;
		if(message != null && !message.trim().equals("")) {
			msg = new Message();
			if(message.contains(Constants.SYSTEM_EXIT_MSG_FLAG)) {
				msg.setTYPE(MsgType.SYSTEM_EXIT_MSG);
				msg.setMsg(message);
				return msg;
			}else if(message.startsWith("name-@")) {//用户名信息处理
				msg.setTYPE(MsgType.USER_NAME_MSG);
				msg.setMsg(message.substring(Constants.USER_NAME_MSG_FLAG.length()));
				return msg;
			}else if(message.startsWith("@") && message.contains(":")) {//私聊信息处理
				int endIndex = message.indexOf(":");
				msg.setTYPE(MsgType.SEND_ONE_MSG);
				msg.setMsg(message.substring(endIndex + 1));
				String toWho = message.substring(1, endIndex);
				msg.setToWho(toWho);
				return msg;
			}else if(message.startsWith(Constants.ENTER_SEAT_MSG_FLAG)){//进入斗地主房间信息处理
				msg.setTYPE(MsgType.ENTER_SEAT_MSG);
				msg.setMsg(message.substring(Constants.ENTER_SEAT_MSG_FLAG.length()));
				return msg;
			}else if(message.startsWith(Constants.EXIT_SEAT_MSG_FLAG)){
				msg.setTYPE(MsgType.EXIT_SEAT_MSG);
				msg.setMsg(message.substring(Constants.EXIT_SEAT_MSG_FLAG.length()));
				return msg;
			}else {//群聊信息处理
				msg.setTYPE(MsgType.SEND_ALL_MSG);
				msg.setMsg(message);
				return msg;
			}
		}
		return null;
	}
/*	private static String doHandle(String message,MsgType msgType) {
		String tempStr = message;
		if(msgType == MsgType.USER_NAME_MSG) {
			tempStr = tempStr.substring(Constants.USER_NAME_MSG_FLAG.length());
			return tempStr;
		}
		return "";
	}*/
}
