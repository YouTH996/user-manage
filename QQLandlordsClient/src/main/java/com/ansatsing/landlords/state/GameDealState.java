package com.ansatsing.landlords.state;

import com.ansatsing.landlords.client.thread.DealCards;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 游戏发牌状态
 * @author sunyq
 *
 */
public class GameDealState extends GameState {
	private LandlordsRoomWindow landlordsRoomWindow;
	public GameDealState(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	@Override
	public void pushGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GamePlayState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState(GameStateManager gameStateManager) {
		gameStateManager.setGameSate(new GameReadyState(this.landlordsRoomWindow));
	}

	@Override
	public void pushGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameRobState(this.landlordsRoomWindow));
	}

	@Override
	public void pullGameState() {
		// TODO Auto-generated method stub
		landlordsRoomWindow.setGameState( new GameReadyState(this.landlordsRoomWindow));
	}

	@Override
	public void handleWindow() {
		DealCards dealCardsThread = new DealCards(landlordsRoomWindow);
		dealCardsThread.run();
		/*Thread thread = new Thread(dealCardsThread);
		thread.start();*/
	}

}
