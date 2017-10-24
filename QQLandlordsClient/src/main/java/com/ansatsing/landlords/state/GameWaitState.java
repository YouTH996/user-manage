package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 游戏等待状态：等待游戏人数凑齐，只有3人入座才开启游戏
 * @author sunyq
 *
 */
public class GameWaitState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameWaitState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameReadyState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameOverState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameReadyState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameOverState(this.landlordsRoomWindow));
	}
	@Override
	public void handleWindow() {
		// TODO Auto-generated method stub
		
	}

}
