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
    private boolean loginSuccess = false;//登录是否成功，用于判断释放重连服务器
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

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
}
