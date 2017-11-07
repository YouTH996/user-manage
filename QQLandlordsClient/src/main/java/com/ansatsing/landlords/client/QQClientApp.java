package com.ansatsing.landlords.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ansatsing.landlords.client.thread.ClientReceiveThread;
import com.ansatsing.landlords.client.ui.LoginWidow;

public class QQClientApp {

	public static void main(String[] args) {
		String host = "127.0.0.1";
		int port = 6789;
		Socket socket = null;
		try {
			socket = new Socket(host, port);
			ClientReceiveThread qqClientHandler = new ClientReceiveThread(socket);
			LoginWidow loginWidow = new LoginWidow(socket,qqClientHandler);
			qqClientHandler.setLoginWidow(loginWidow);
			Thread thread = new Thread(qqClientHandler);
			thread.start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
