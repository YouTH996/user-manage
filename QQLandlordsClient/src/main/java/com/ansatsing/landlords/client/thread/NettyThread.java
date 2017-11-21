package com.ansatsing.landlords.client.thread;

import com.ansatsing.landlords.client.Context;
import com.ansatsing.landlords.client.NettyClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.ConnectException;
import java.nio.charset.Charset;

public class NettyThread implements Runnable{
    private volatile Context context;
    public NettyThread(Context context){
        this.context = context;
    }


    public void  run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            ByteBuf delimiter = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                            pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                            pipeline.addLast(new NettyClientHandler(context));
                        }
                    });
            ChannelFuture future = null;
            try {
                future = bootstrap.connect("127.0.0.1",6789).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(future.isSuccess()){
                        context.setConnect(true);
                    }else{
                        context.setConnect(false);
                    }
                }
            });
            context.getPlayer().setChannel(future.channel());
            try {
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }catch (Exception e){
            System.out.println("异常："+e.getMessage());
        }  finally{
            group.shutdownGracefully();
        }
    }
}
