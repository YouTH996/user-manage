package com.ansatsing.landlords.protocol;

import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.Transient;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 协议抽象类
 */
public abstract class AbstractProtocol {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractProtocol.class);
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
        LOGGER.info("客户端向服务器发送信息："+msg);
        PrintWriter printWriter= null;
        try {
           // printWriter =  new PrintWriter(socket.getOutputStream(), true);//解决中文乱码问题
            printWriter =  new PrintWriter( new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true );
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        printWriter.println(msg);
       // printWriter.close();//不能关闭 关闭了 就彻底完蛋了
    }
}
