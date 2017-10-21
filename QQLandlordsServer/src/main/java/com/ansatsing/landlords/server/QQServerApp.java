package com.ansatsing.landlords.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.net.ServerSocketFactory;

/**
 * qq斗地主服务器端入口类
 * @author sunyq
 *
 */
public class QQServerApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket serverSocket = null;
		Map<String, Socket> nameToSocket = new ConcurrentHashMap<String, Socket>();//网名对应socket
		List<Map<Integer, Socket>> gameGroups = new ArrayList<Map<Integer,Socket>>();//有几桌斗地主就存放几桌
		//List<Map<Integer, LinkedList<Integer>>> enterSeatList = new ArrayList<Map<Integer,LinkedList<Integer>>>();//目前才一个区，存放每个区的座位入座情况
		List<String> enterSeatList =new ArrayList<String>();//存放座位入座情况,格式：8=username
		try {
			serverSocket = ServerSocketFactory.getDefault().createServerSocket(6789);
			System.out.println("===============QQ斗地主服务器已经开启=================");
			Map<Integer, Socket> game3Sockets = null;//存放一组斗地主的socket
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println(">>>>>>>一位网友连接成功！当前在线人数为："+(nameToSocket.size()+1)+"当前在座人数："+enterSeatList.size());
				new Thread(new ServerHandlerTransferThread(socket,nameToSocket,enterSeatList)).start();
				if(game3Sockets == null) {
					game3Sockets = new ConcurrentHashMap<Integer, Socket>(); 
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
