package com.ansatsing.landlords.client.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import com.ansatsing.landlords.entity.MsgType;
/**
 * 消息处理并发送中心:信息从客户端发送到服务器端;
 *
 * @author ansatsing
 * @time 2017年10月20日 下午9:59:35
 */
public class SendMessageHandler {
	private Socket socket;
	private PrintWriter printWriter;
	public SendMessageHandler(Socket socket) {
		this.socket = socket;
	}
	/**
	 * 发送霸占座位的信号
	 * @param msg
	 */
	public void sendEnterSeatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.ENTER_SEAT_MSG, msg));
		}
	}
	/**
	 * 发送空出座位的信号
	 * @param msg
	 */
	public void sendExitSeatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.EXIT_SEAT_MSG, msg));
		}
	}	
	/**
	 * 发送退出系统信号
	 */
	public void sendSystemExitMsg(){
		sendMsg(SendMessagePack.packMsg(MsgType.SYSTEM_EXIT_MSG, ""));
	}
	/**
	 * 发送私聊信息
	 * @param msg
	 */
	public void sendPrivateChatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.SEND_ONE_MSG, msg));
		}
	}
	/**
	 * 发送群聊消息
	 * @param msg
	 */
	public void sendAllChatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.SEND_ALL_MSG, msg));
		}
	}
	/**
	 * 发送房间私聊信息
	 * @param msg
	 */
	public void sendRoomPrivateChatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.ROOM_SEND_ONE_MSG, msg));
		}
	}
	/**
	 * 发送房间群聊消息
	 * @param msg
	 */
	public void sendRoomAllChatMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.ROOM_SEND_ALL_MSG, msg));
		}
	}	
	/**
	 * 发送用户名登记消息
	 * @param msg
	 */
	public void sendUsernameMSg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.USER_NAME_MSG, msg));
		}
	}
	/**
	 * 发送游戏准备信息
	 * @param msg
	 */
	public void sendGameReadyMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.GAME_READY_MSG, msg));
		}
	}
	/**
	 * 发送游戏发牌信号
	 * @param msg
	 */
	public void sendGameDealMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.GAME_DEAL_MSG, msg));
		}
	}	
	/**
	 * 发送游戏抢地址情况的信号
	 * @param msg
	 */
	public void sendGameRobMsg(String msg){
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.SET_ROLE_MSG, msg));
		}
	}		
	/**
	 * 发送消息
	 * @param msg
	 */
	private void sendMsg(String msg){
		if(printWriter == null){
			try {
				printWriter =  new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		printWriter.println(msg);
	}
	public void sendPlayCardMsg(String msg) {
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.PLAY_CARD_MSG, msg));
		}
	}
	//发送游戏结束信号
    public void sendGameOverMsg(String msg) {
		if(msg != null && !msg.trim().equals("")){
			sendMsg(SendMessagePack.packMsg(MsgType.GAME_OVER_MSG, msg));
		}
    }
}
