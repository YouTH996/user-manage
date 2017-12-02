package com.ansatsing.landlords.protocol;

import com.ansatsing.landlords.client.NettyServerListener;
import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;
import com.ansatsing.landlords.entity.Player;
import io.netty.buffer.ByteBufAllocator;
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
   // protected Socket socket;
    protected Player player;
    //从服务器端收到信息然后进行信息处理
    public void handleProt(){

    }

    public void setLandlordsRoomWindow(LandlordsRoomWindow landlordsRoomWindow) {
        this.landlordsRoomWindow = landlordsRoomWindow;
    }
    @Transient
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    @Transient
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
  /*  @Transient
    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
*/
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
        if(!msg.contains("HeartBeatProt"))
        LOGGER.info("客户端向服务器发送信息："+msg);
        if(player == null) throw new NullPointerException("player为空指针!");
            if(player.getSocket() == null && player.getChannel() == null) throw new NullPointerException("player的socket或者channel为空指针!");
            if(player.getSocket() != null){
                PrintWriter printWriter= null;
                try {
                    // printWriter =  new PrintWriter(socket.getOutputStream(), true);//解决中文乱码问题
                    printWriter =  new PrintWriter( new OutputStreamWriter(player.getSocket().getOutputStream(), "UTF-8"),true );
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                printWriter.println(msg);
                // printWriter.close();//不能关闭 关闭了 就彻底完蛋了
            }else if(player.getChannel()!= null){
                player.getChannel().writeAndFlush(ByteBufAllocator.DEFAULT.buffer()
                        .writeBytes((msg+System.getProperty("line.separator")).getBytes()))
                        .addListener(new NettyServerListener());
            }
    }
}
