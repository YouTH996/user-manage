package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.thread.CountDownThread;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

public class GameReadyState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameReadyState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameDealState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameWaitState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameDealState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameWaitState(this.landlordsRoomWindow));
	}
	@Override
	public void handleWindow() {
		CountDownThread countDownThread = new CountDownThread(this.landlordsRoomWindow, 30);
		this.landlordsRoomWindow.setCountDownThread(countDownThread);
		Thread thread = new Thread(countDownThread);
		thread.start();
	}

}
