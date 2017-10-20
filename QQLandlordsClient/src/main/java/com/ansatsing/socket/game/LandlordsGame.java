package com.ansatsing.socket.game;

import java.net.Socket;
import java.util.Map;

public class LandlordsGame implements Runnable {
	private Map<Integer, Socket> game3Sockets;
	public LandlordsGame(Map<Integer, Socket> game3Sockets) {
		this.game3Sockets = game3Sockets;
	}
	public void run() {
		while(true) {
			
		}
	}

}
