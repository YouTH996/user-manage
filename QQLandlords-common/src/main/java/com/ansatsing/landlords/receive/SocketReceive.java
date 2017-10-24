package com.ansatsing.landlords.receive;

import java.net.Socket;

public class SocketReceive implements IReceive{
	private Socket socket;
	public SocketReceive(Socket socket) {
		this.socket = socket;
	}
	public String receiveMsg() {
		// TODO Auto-generated method stub
		return null;
	}

}
