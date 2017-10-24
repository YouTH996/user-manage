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
			}else if(message.startsWith(Constants.ENTER_ROOM_MSG_FLAG)){
				msg.setTYPE(MsgType.ENTER_ROOM_MSG);
				msg.setMsg(message.substring(Constants.ENTER_ROOM_MSG_FLAG.length()));
				return msg;
			}else if(message.startsWith(Constants.EXIT_ROOM_MSG_FLAG)){
				msg.setTYPE(MsgType.EXIT_ROOM_MSG);
				msg.setMsg(message.substring(Constants.EXIT_ROOM_MSG_FLAG.length()));
				return msg;
			}else if(message.startsWith(Constants.ROOM_SEND_ALL_MSG_FLAG)){//房间群聊
				msg.setTYPE(MsgType.ROOM_SEND_ALL_MSG);
				msg.setMsg(message.substring(Constants.ROOM_SEND_ALL_MSG_FLAG.length()));
				return msg;
			}else if(message.startsWith(Constants.ROOM_SEND_ONE_MSG_FLAG)){//房间私聊
				String toMsg = message.substring(Constants.ROOM_SEND_ONE_MSG_FLAG.length());
				if(toMsg.startsWith("@") && toMsg.contains(":")){
					int endIndex = toMsg.indexOf(":");
					msg.setTYPE(MsgType.ROOM_SEND_ONE_MSG);
					msg.setMsg(toMsg.substring(endIndex + 1));
					String toWho = toMsg.substring(1, endIndex);
					msg.setToWho(toWho);
					return msg;
				}
			}else if(message.startsWith(Constants.ROOM_REMOVE_SOCKET_FLAG)){
				msg.setTYPE(MsgType.ROOM_REMOVE_SOCKET_MSG);
				msg.setMsg(message.substring(Constants.ROOM_REMOVE_SOCKET_FLAG.length()));
				return msg;
			}else if(message.startsWith(Constants.ROOM_ADD_SOCKET_FLAG)){
				msg.setTYPE(MsgType.ROOM_ADD_SOCKET_MSG);
				msg.setMsg(message.substring(Constants.ROOM_ADD_SOCKET_FLAG.length()));
				return msg;
			}else {//大厅群聊信息处理
				msg.setTYPE(MsgType.SEND_ALL_MSG);
				msg.setMsg(message);
				return msg;
			}
		}
		return null;
	}
	public static void main(String[] args) {
		String mString = "^e4TcR@)*U%F^J{@wax:exaefe";
		String toMsg = mString.substring(Constants.ROOM_SEND_ONE_MSG_FLAG.length());
		if(toMsg.startsWith("@") && toMsg.contains(":")){
			int endIndex = toMsg.indexOf(":");
			//msg.setTYPE(MsgType.ROOM_SEND_ONE_MSG);
			//msg.setMsg(toMsg.substring(endIndex + 1));
			System.out.println(toMsg.substring(1, endIndex));
			String toWho = toMsg.substring(1, endIndex);
		//	msg.setToWho(toWho);
			//return msg;
		}
	}
}
