package com.ansatsing.landlords.client;

import com.ansatsing.landlords.client.thread.NettyThread;

public class NettyClient implements  IClient{
    private Context context;
    private  final String host;
    private  final  int port;
    public NettyClient(Context context,String host,int port){
        this.context = context;
        this.host = host;
        this.port = port;
    }
    public void connectServer() {
       NettyThread nettyThread = new NettyThread(context,host,port);
        //FutureTask<String> result = new FutureTask<String>(nettyThread);
        new Thread(nettyThread).start();

    }
}
