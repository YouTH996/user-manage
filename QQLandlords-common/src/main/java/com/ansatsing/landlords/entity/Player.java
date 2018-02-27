package com.ansatsing.landlords.entity;

import java.net.Socket;

import io.netty.channel.Channel;

public class Player {
	private String userName;
	private int seatNum = -1;//座位号
	private Socket socket;
	private Channel channel;
	private volatile int readFlag = 0;//0没有准备好    1准备好了
	private volatile int roleFlag = 0;//0无角色 1农民角色 2地主角色
	private volatile int gameStatus;//1代表斗地主游戏中；0代表不是
	private boolean unnormalExited;//true代表非正常退出，false代表正在退出
	private long lastReveHeatTime;//最后一次收到心跳包协议的时间：单位毫秒
	private int tableNum = -1;//第几桌;-1代表未入座
	public String getUserName() {
		return userName;
	}

	public int getGameStatus() {
		return gameStatus;
	}

	public boolean isUnnormalExited() {
		return unnormalExited;
	}

	public long getLastReveHeatTime() {
		return lastReveHeatTime;
	}

	public void setLastReveHeatTime(long lastReveHeatTime) {
		this.lastReveHeatTime = lastReveHeatTime;
	}

	public void setUnnormalExited(boolean unnormalExited) {
		this.unnormalExited = unnormalExited;
	}

	public void setGameStatus(int gameStatus) {
		this.gameStatus = gameStatus;
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

	public int getTableNum() {
		if(tableNum == -1 && seatNum > -1){
			tableNum = seatNum/3;
		}
		return tableNum;
	}

	public void setTableNum(int tableNum) {
		this.tableNum = tableNum;
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

	public int getRoleFlag() {
		return roleFlag;
	}

	public void setRoleFlag(int roleFlag) {
		this.roleFlag = roleFlag;
	}
	
}
