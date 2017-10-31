package com.ansatsing.landlords.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 大厅里一桌的信息实体类
 * @author sunyq
 *
 */
public class Table {

	private String cards;
	private List<Player> players = new ArrayList<Player>(3);
	private boolean isWait = true;
	private boolean isReady =false;
	private boolean isRob =false;
	private boolean isDeal =false;
	private boolean isPlay =false;
	private boolean isOver = false;
	private Player landlord;//谁是地主
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
