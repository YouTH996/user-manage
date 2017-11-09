package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.thread.PlayCountDownThread;
import com.ansatsing.landlords.client.thread.RobCountDownThread;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 游戏出牌状态：
 * @author sunyq
 *
 */
public class GamePlayState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GamePlayState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameOverState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameRobState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameOverState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameRobState(this.landlordsRoomWindow));
	}

	@Override
	public void handleWindow() {
		PlayCountDownThread dealCardsThread = new PlayCountDownThread(landlordsRoomWindow,25);
		landlordsRoomWindow.setPlayCountDownThread(dealCardsThread);
		dealCardsThread.run();
		/*Thread thread = new Thread(dealCardsThread);
		thread.start();*/
	}

}
