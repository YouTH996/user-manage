package com.ansatsing.landlords.entity;

import java.net.Socket;

import io.netty.channel.Channel;

public class Player {
	private String userName;
	private int seatNum;//座位号
	private Socket socket;
	private Channel channel;
	private int readFlag = 0;//0没有准备好    1准备好了
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(int seatNum) {
		this.seatNum = seatNum;
	}

	public Socket getSocket() {
		return socket;
	}
	

	public int getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(int readFlag) {
		this.readFlag = readFlag;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}
	
}
