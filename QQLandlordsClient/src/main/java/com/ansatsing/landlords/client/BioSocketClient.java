package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.thread.BioSocketThread;
import com.ansatsing.landlords.entity.Player;

import java.io.IOException;
import java.net.*;

public class BioSocketClient implements  IClient {
    private Context context;
    public BioSocketClient(Context context){
        this.context = context;
    }
    public void connectServer() {
        new Thread(new BioSocketThread(context)).start();

    }
}
