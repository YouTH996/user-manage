package com.ansatsing.landlords.protocol;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;

import java.beans.Transient;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 协议抽象类
 */
public abstract class AbstractProtocol {
    protected LandlordsRoomWindow landlordsRoomWindow;
    protected GameLobbyWindow gameLobbyWindow;
    protected LoginWidow loginWidow;
    protected Socket socket;
    //从服务器端收到信息然后进行信息处理
    public abstract void handleProt();

    public void setLandlordsRoomWindow(LandlordsRoomWindow landlordsRoomWindow) {
        this.landlordsRoomWindow = landlordsRoomWindow;
    }

    public LandlordsRoomWindow getLandlordsRoomWindow() {
        return landlordsRoomWindow;
    }
    @Transient
    public GameLobbyWindow getGameLobbyWindow() {
        return gameLobbyWindow;
    }
    @Transient
    public LoginWidow getLoginWidow() {
        return loginWidow;
    }
    @Transient
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setGameLobbyWindow(GameLobbyWindow gameLobbyWindow) {
        this.gameLobbyWindow = gameLobbyWindow;
    }

    public void setLoginWidow(LoginWidow loginWidow) {
        this.loginWidow = loginWidow;
    }
    //发送信息到服务器端
    public  void sendMsg(){

    }
    protected void sendMsg(String msg) {
        System.out.println("客户端向服务器发送信息："+msg);
        PrintWriter printWriter= null;
        try {
            printWriter =  new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        printWriter.println(msg);
    }
}
