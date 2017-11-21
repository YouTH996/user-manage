package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;
import com.ansatsing.landlords.entity.Player;

public class Context {
    private volatile GameLobbyWindow qqGameWindow;
    private volatile LoginWidow loginWidow;
    private volatile LandlordsRoomWindow landlordsRoomWindow;
    private Player player;
    private volatile boolean isConnect = false;

    public GameLobbyWindow getQqGameWindow() {
        return qqGameWindow;
    }

    public void setQqGameWindow(GameLobbyWindow qqGameWindow) {
        this.qqGameWindow = qqGameWindow;
    }

    public LoginWidow getLoginWidow() {
        return loginWidow;
    }

    public void setLoginWidow(LoginWidow loginWidow) {
        this.loginWidow = loginWidow;
    }

    public LandlordsRoomWindow getLandlordsRoomWindow() {
        return landlordsRoomWindow;
    }

    public void setLandlordsRoomWindow(LandlordsRoomWindow landlordsRoomWindow) {
        this.landlordsRoomWindow = landlordsRoomWindow;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }
}
