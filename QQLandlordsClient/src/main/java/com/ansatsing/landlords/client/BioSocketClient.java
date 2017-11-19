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
    public void connectServer() throws IOException{
        Socket socket = null;
        socket = new Socket(host, port);
        this.context.getPlayer().setSocket(socket);
        new Thread(new BioSocketThread(context)).start();

    }
}
