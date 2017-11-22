package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.thread.BioSocketThread;
import com.ansatsing.landlords.entity.Player;

import java.io.IOException;
import java.net.*;

public class BioSocketClient implements  IClient {
    private Context context;
    private  final String host;
    private  final  int port;
    public BioSocketClient(Context context,String host,int port){
        this.context = context;
        this.host = host;
        this.port = port;
    }
    public void connectServer(){
        Socket socket = null;
        try {
            socket = new Socket(host, port);
            this.context.getPlayer().setSocket(socket);
            new Thread(new BioSocketThread(context)).start();
            context.getLoginWidow().handleLogin(true);
        } catch (IOException e) {
            System.out.println("连接服务器异常："+e.getMessage());
            context.getLoginWidow().handleLogin(false);
        }
    }
}
