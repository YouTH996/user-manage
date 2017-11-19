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

import java.nio.charset.Charset;

public class NettyThread implements Runnable{
    private volatile Context context;
    public NettyThread(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        System.out.println("1nettyClietn");
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        System.out.println("2nettyClietn");
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
            System.out.println("3nettyClietn");
            ChannelFuture future = bootstrap.connect("127.0.0.1",6789).sync();
            System.out.println("4nettyClietn");
            context.getPlayer().setChannel(future.channel());
            future.channel().closeFuture().sync();

        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
