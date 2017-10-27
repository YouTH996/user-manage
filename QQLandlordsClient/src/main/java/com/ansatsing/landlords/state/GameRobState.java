package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.thread.RobCountDownThread;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 游戏抢地主状态：
 * @author sunyq
 *
 */
public class GameRobState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameRobState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GamePlayState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameDealState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GamePlayState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameDealState(this.landlordsRoomWindow));
	}
	@Override
	public void handleWindow() {
		RobCountDownThread dealCardsThread = new RobCountDownThread(landlordsRoomWindow,10);
		landlordsRoomWindow.setRobDownThread(dealCardsThread);
		Thread thread = new Thread(dealCardsThread);
		thread.start();
	}

}
