package com.ansatsing.landlords.server;

import com.ansatsing.landlords.server.socket.SocketServer;

/**
 * qq斗地主服务器端入口类
 * @author sunyq
 *
 */
public class QQServerApp1 {

	public static void main(String[] args) {
		int port = 6789;
		IServer server = new SocketServer();
		server.startServer(port);
	}

}
