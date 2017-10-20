package com.ansatsing.socket.client.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class QQClientApp {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 6789;
		Socket socket = null;
		PrintWriter printWriter = null;
		try {
			socket = new Socket(host, port);
			LoginWidow loginWidow = new LoginWidow(socket);
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
