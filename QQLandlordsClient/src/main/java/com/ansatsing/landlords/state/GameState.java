package com.ansatsing.landlords.state;

public abstract class GameState {
	public abstract void pushGameState(GameStateManager gameStateManager);
	public abstract void pullGameState(GameStateManager gameStateManager);
	public abstract void pushGameState();
	public abstract void pullGameState();
	public abstract void handleWindow();
}
