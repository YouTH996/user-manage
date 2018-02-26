package com.ansatsing.landlords.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 大厅里一桌的信息实体类
 * @author sunyq
 *
 */
public class Table {

	private volatile String cards;
	private  List<Player> players = new ArrayList<Player>(3);
	private volatile boolean isWait = true;
	private volatile boolean isReady =false;
	private volatile boolean isRob =false;
	private volatile boolean isDeal =false;
	private volatile boolean isPlay =false;
	private volatile boolean isOver = false;
	private  Player landlord;//谁是地主
	public String getCards() {
		return cards;
	}
	public void setCards(String cards) {
		this.cards = cards;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public boolean isWait() {
		return isWait;
	}
	public void setWait(boolean isWait) {
		this.isWait = isWait;
	}
	public boolean isReady() {
		return isReady;
	}
	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}
	public boolean isRob() {
		return isRob;
	}
	public void setRob(boolean isRob) {
		this.isRob = isRob;
	}
	public boolean isDeal() {
		return isDeal;
	}
	public void setDeal(boolean isDeal) {
		this.isDeal = isDeal;
	}
	public boolean isPlay() {
		return isPlay;
	}
	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}
	public boolean isOver() {
		return isOver;
	}
	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}
	public Player getLandlord() {
		return landlord;
	}
	public void setLandlord(Player landlord) {
		this.landlord = landlord;
	}
	
}
