package com.ansatsing.landlords.client;

import com.ansatsing.landlords.entity.Player;

import java.io.IOException;
import java.net.*;

public class SocketClient implements  IClient {
    private Player player;
    public SocketClient(Player player) {
        this.player = player;
    }

    public void connectServer() throws UnknownHostException,SocketException,ConnectException ,IOException{
       String host = "39.108.166.35";
        //String host = "localhost";
        int port = 6789;
        Socket socket = null;
        socket = new Socket(host, port);
        player.setSocket(socket);

    }
}
