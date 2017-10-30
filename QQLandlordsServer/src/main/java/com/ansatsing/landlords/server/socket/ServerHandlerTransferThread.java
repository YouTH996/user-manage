package com.ansatsing.landlords.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.MsgType;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.util.MessageUtil;
/**
 * qq斗地主服务器端网络IO处理中心
 * @author sunyq
 *
 */
public class ServerHandlerTransferThread implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandlerTransferThread.class);
	private Socket socket;
	private Player player;
	private ServerMessageHandler messageHandler;
	private Map<String, Player> userName2Player;
	private Map<Integer, Player> playerMap;//一个座位对应一个玩家
	private Map<Integer, Table> tableMap;//一桌对应一个table实体类对象
	public ServerHandlerTransferThread( Socket socket,Map<Integer, Player> playerMap,Map<Integer, Table> tableMap,Map<String, Player> _userName2Player) {
		this.socket = socket;
		this.player = new Player();
		this.player.setSocket(socket);
		this.userName2Player = _userName2Player;
		this.tableMap = tableMap;
		this.playerMap = playerMap;
		if(tableMap == null) LOGGER.info("tableMapnullllllllllllllllllllllllllllll");
		messageHandler = new ServerMessageHandler(this.player,playerMap,tableMap,_userName2Player);
	}

	public void run() {//消息处理以及中转
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		Message message = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			String readMsg = "";
			while (true) {
				readMsg = bufferedReader.readLine();
				if (!readMsg.equals("")) {
					System.out.println((player.getUserName() == null ? "未登陆的":player.getUserName())+"发送了消息："+readMsg);
					message = MessageUtil.handle(readMsg);
					if(message != null){
						if(message.getTYPE() == MsgType.SYSTEM_EXIT_MSG) {//客户退出系统
							if(player == null || player.getUserName() == null) {
								System.out.println("一位牌友正试图进入QQ斗地主游戏厅，但最后还是走了。。。");
							}else {
								System.out.println(player.getUserName()+"牌友下线走了。。。。");
							}
							break;
						}else{
							System.out.println("userName2Player  size:=======   "+userName2Player.size());
							System.out.println("playerMap  size:=======   "+playerMap.size());
							System.out.println("tableMap  size:=======   "+tableMap.size());
							messageHandler.handleMessage(message);
						}
					}else{
						LOGGER.info("message是空指针");
					}
				}
			}
			if(player != null && player.getUserName() != null) {
				userName2Player.remove(player.getUserName());
				messageHandler.batchSendMsg(player.getUserName()+"退出聊天室了!当前聊天室人数："+userName2Player.size(),userName2Player.values(),true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(printWriter != null) {
				printWriter.close();
			}
			if(this.socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}