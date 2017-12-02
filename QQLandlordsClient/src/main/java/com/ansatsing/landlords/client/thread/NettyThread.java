package com.ansatsing.landlords.client.thread;

import com.ansatsing.landlords.client.Context;
import com.ansatsing.landlords.client.NettyClientHandler;
import com.ansatsing.landlords.util.Constants;
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
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class NettyThread implements Runnable {
    private volatile Context context;
    private final String host;
    private final int port;

    public NettyThread(Context context, String host, int port) {
        this.context = context;
        this.host = host;
        this.port = port;
    }


    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        final  Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .remoteAddress(host,port)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            ByteBuf delimiter = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
                            pipeline.addLast(new IdleStateHandler(0,0, Constants.CLIENT_IDLE_TIMEOUT, TimeUnit.SECONDS));
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
                            pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
                            pipeline.addLast(new NettyClientHandler(context,bootstrap));
                        }
                    });
            ChannelFuture future = null;
           future = bootstrap.connect().addListener(new ChannelFutureListener() {
           // bootstrap.connect().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.cause() != null){
                        System.out.println("连接服务器出现异常：" + future.cause().getMessage());
                        context.setLoginSuccess(false);
                        context.getLoginWidow().handleLogin(false);
                    }else{
                        context.setLoginSuccess(true);
                        context.getPlayer().setChannel(future.channel());
                        context.getLoginWidow().handleLogin(future.isSuccess());
                    }
                }
            }).sync();
           // future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            context.getLoginWidow().handleLogin(false);
            System.out.println("异常：" + e.getMessage());
        } finally {
           // if(!context.isLoginSuccess())
           // group.shutdownGracefully();
        }
    }
}
