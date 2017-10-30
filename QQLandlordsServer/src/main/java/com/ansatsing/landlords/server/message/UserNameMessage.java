package com.ansatsing.landlords.server.message;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.util.Constants;
/**
 * 输入用户名登录 消息
 * @author sunyq
 *
 */
public class UserNameMessage extends AbstractMessage {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserNameMessage.class);
	public UserNameMessage(Player player,Map<Integer, Player> playerMap,Map<Integer, Table> tableMap,Map<String, Player> userName2Player) {
		this.player = player;
		this.userName2Player = userName2Player;
		this.playerMap = playerMap;
		this.tableMap = tableMap;
	}
	@Override
	public void handleMsg(Message message) {
		if(checkDuplicatedUserName(message.getMsg())){//处理重复网名
			singleSendMsg(player, "这网名有人正在使用,请更换网名!");
		}else {
			String userName = message.getMsg();
			player.setUserName(userName);
			singleSendMsg(player,"这个网名可以啦!");
			batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",userName2Player.values(),true);
			userName2Player.put(userName, player);
			if(playerMap.size() > 0){//初始化游戏大厅座位情况
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(Constants.INIT_SEAT_MSG_FLAG);
				for(Integer seatNum:playerMap.keySet()){
					stringBuilder.append(seatNum+"="+playerMap.get(seatNum).getUserName()+",");
				}
				stringBuilder.deleteCharAt(stringBuilder.length()-1);
				//String initSeat = Constants.INIT_SEAT_MSG_FLAG+Joiner.on(",").join(enterSeatList);
				singleSendMsg(player, stringBuilder.toString());
			}
		}
	}
	//检查重复网名
	private boolean checkDuplicatedUserName(String userName) {
		boolean flag = false;
		for(Player _player : userName2Player.values()) {
			if(_player.getUserName().equals(userName)) {
				flag = true;
				break;
			}
		}
		return flag;
	}
}
