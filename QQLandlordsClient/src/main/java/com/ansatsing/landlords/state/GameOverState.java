package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 游戏结束状态
 * @author sunyq
 *
 */
public class GameOverState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameOverState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GamePlayState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameWaitState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GamePlayState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameWaitState(this.landlordsRoomWindow));
	}
	@Override
	public void handleWindow() {
		// TODO Auto-generated method stub
		
	}

}
