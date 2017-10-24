package com.ansatsing.landlords.entity;

public class Player {
	private String userName;
	private int seatNum;//座位号
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
}
