package com.ansatsing.landlords.server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.ExitSeatProt;
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
	private AbstractProtocol protocol;
	public ServerHandlerTransferThread( Socket socket,Map<Integer, Player> playerMap,Map<Integer, Table> tableMap,Map<String, Player> _userName2Player) {
		this.socket = socket;
		this.player = new Player();
		this.player.setSocket(socket);
		this.userName2Player = _userName2Player;
		this.tableMap = tableMap;
		this.playerMap = playerMap;
		messageHandler = new ServerMessageHandler(this.player,playerMap,tableMap,_userName2Player);
	}

	public void run() {//消息处理以及中转
		// TODO Auto-generated method stub
		BufferedReader bufferedReader = null;
		PrintWriter printWriter = null;
		Message message = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream(),"UTF-8"));
			String readMsg = "";
			while (true) {
				readMsg = bufferedReader.readLine();
				//这个位置应该加一个协议过滤器，非法协议直接跳过
				if (!readMsg.equals("")) {
					LOGGER.info((player.getUserName() == null ? "未登陆的":player.getUserName())+"发送了消息："+readMsg);
					int endIdx = readMsg.indexOf("{");
					String className = readMsg.substring(0,endIdx);
					String classContent = readMsg.substring(endIdx);
					Class class1 = null;
					try{
						class1 = Class.forName(className);
					}catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					if(class1 != null){
						protocol = (AbstractProtocol) JSON.parseObject(classContent,class1);
						protocol.setPlayer(player);
						protocol.setPlayerMap(playerMap);
						protocol.setTableMap(tableMap);
						protocol.setUserName2Player(userName2Player);
						protocol.handleProt();
						if(className.equals("com.ansatsing.landlords.protocol.SystemExitProt")){
							break;
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(player!=null){
				LOGGER.info("退出系统时,player.getSeatNum():"+player.getSeatNum());
				if(player.getSeatNum() > -1){
					protocol = new ExitSeatProt(player.getSeatNum(),player.getUserName());
					protocol.setPlayer(player);
					protocol.setPlayerMap(playerMap);
					protocol.setTableMap(tableMap);
					protocol.setUserName2Player(userName2Player);
					protocol.handleProt();
				}
				if(player.getUserName() != null){
					userName2Player.remove(player.getUserName());
					LOGGER.info(player.getUserName()+"退出系统了");
				}
			}
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