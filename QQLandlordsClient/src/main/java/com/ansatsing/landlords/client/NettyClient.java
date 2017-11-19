package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.thread.NettyThread;

public class NettyClient implements  IClient{
    private Context context;
    public NettyClient(Context context){
        this.context = context;
    }
    public void connectServer() {
        new Thread(new NettyThread(context)).start();

    }
}
