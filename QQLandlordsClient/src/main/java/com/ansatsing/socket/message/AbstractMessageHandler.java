package com.ansatsing.socket.message;

import java.net.Socket;

public abstract class AbstractMessageHandler {
	public abstract String messageHandle(String message,Socket socket);
}
