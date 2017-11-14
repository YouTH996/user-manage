package com.ansatsing.landlords.server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.server.IServer;
import com.ansatsing.landlords.server.thread.HeartBeatMonitor;
import com.ansatsing.landlords.server.thread.ServerHandlerTransferThread;

public class SocketServer implements IServer {

	public void startServer(int port) {
		ServerSocket serverSocket = null;
		//////////////////////////////////////////////////
		 Map<Integer, Player> playerMap =new ConcurrentHashMap<Integer, Player>();//一个座位对应一个玩家
		 Map<Integer, Table> tableMap = new ConcurrentHashMap<Integer, Table>();//一桌对应一个table实体类对象
		 Map<String, Player> userName2Player = new ConcurrentHashMap<String, Player>();//记录全部牌友信息
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(6789);
			System.out.println("===============QQ斗地主服务器已经开启=================");
			HeartBeatMonitor heartBeatMonitor = new HeartBeatMonitor(playerMap);
			new Thread(heartBeatMonitor).start();
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println(">>>>>>>一位网友连接成功！当前在线人数为："+(userName2Player.size()+1)+"当前在座人数："+playerMap.size());
				new Thread(new ServerHandlerTransferThread(socket,playerMap,tableMap,userName2Player)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
