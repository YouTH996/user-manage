package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.thread.NettyThread;

public class NettyClient implements  IClient{
    private Context context;
    public NettyClient(Context context){
        this.context = context;
    }
    public void connectServer() {
       NettyThread nettyThread = new NettyThread(context);
        //FutureTask<String> result = new FutureTask<String>(nettyThread);
        new Thread(nettyThread).start();

    }
}
