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
	
}
