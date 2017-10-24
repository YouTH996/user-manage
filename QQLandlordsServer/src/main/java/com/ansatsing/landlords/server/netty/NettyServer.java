package com.ansatsing.landlords.server.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ansatsing.landlords.server.IServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

public class NettyServer implements IServer {
	
	public void startServer(int port) {
		final Map<String, Channel> nameToSocket = new ConcurrentHashMap<String, Channel>();//网名对应socket
		//List<Map<Integer, LinkedList<Integer>>> enterSeatList = new ArrayList<Map<Integer,LinkedList<Integer>>>();//目前才一个区，存放每个区的座位入座情况
		//List<String> enterSeatList =new ArrayList<String>();//存放座位入座情况,格式：8=username
		final Map<Integer, String> enterSeatMap = new ConcurrentHashMap<Integer, String>();//存放座位入座情况
		final Map<Integer, Map<String, Channel>> gameGroups = new ConcurrentHashMap<Integer, Map<String,Channel>>();//有几桌斗地主就存放几桌
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2));
					ch.pipeline().addLast(new LengthFieldPrepender(2, false));
					ch.pipeline().addLast(new NettyServerHandler(nameToSocket,enterSeatMap,gameGroups));
				}
			});
			bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture future = bootstrap.bind(port).sync();
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

}
