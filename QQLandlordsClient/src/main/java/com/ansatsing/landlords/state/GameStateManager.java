package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
/**
 * 游戏状态管理类
 * @author sunyq
 *
 */
public class GameStateManager {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameStateManager(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow =  landlordsRoomWindow;
	}
	//private GameState gameState;
	public void setGameSate(GameState gameState) {
		landlordsRoomWindow.setGameState(gameState);
	}
	
	public void pullGameState() {
		this.landlordsRoomWindow.getGameState().pullGameState(this);
	}
	
	public void pushGameState() {
		this.landlordsRoomWindow.getGameState().pushGameState(this);
	}
}
