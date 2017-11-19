package com.ansatsing.landlords.server;

import com.ansatsing.landlords.server.netty.NettyServer;

/**
 * qq斗地主服务器端入口类
 * @author sunyq
 *
 */
public class QQServerApp {

	public static void main(String[] args) {
		int port = 6789;

		//bio socket 方式
		//IServer server = new SocketServer();

		/////////////////////////////////////////////

		//netty方式
		IServer server = new NettyServer();

		server.startServer(port);
	}

}
