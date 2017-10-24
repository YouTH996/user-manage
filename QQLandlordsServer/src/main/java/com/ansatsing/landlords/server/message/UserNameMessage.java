package com.ansatsing.landlords.server.message;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.util.Constants;
/**
 * 输入用户名登录 消息
 * @author sunyq
 *
 */
public class UserNameMessage<T> extends AbstractMessage<T> {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserNameMessage.class);
	protected Map<Integer, Map<String, T>> gameGroups;
	protected T socket;
	protected Map<String, T> nameToSocket;
	@Override
	public void handleMsg(Message message) {
		if(nameToSocket.keySet().contains(message.getMsg())){//处理重复网名
			/*printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println("这网名有人正在使用,请更换网名!");*/
			singleSendMsg(socket,"这网名有人正在使用,请更换网名!");
		}else {
			String userName = message.getMsg();
			batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",nameToSocket);
			singleSendMsg(socket,"这个网名可以啦!");
			nameToSocket.put(userName, socket);
			player.setUserName(userName);
			if(enterSeatMap.size() > 0){//初始化游戏大厅座位情况
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(Constants.INIT_SEAT_MSG_FLAG);
				for(Integer seatNum:enterSeatMap.keySet()){
					stringBuilder.append(seatNum+"="+enterSeatMap.get(seatNum)+",");
				}
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				//String initSeat = Constants.INIT_SEAT_MSG_FLAG+Joiner.on(",").join(enterSeatList);
				singleSendMsg(socket, stringBuilder.toString());
			}
		}
	}

}
