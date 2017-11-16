package com.ansatsing.landlords.server.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class NettyServer implements IServer {
	
	public void startServer(int port) {
		final Map<Integer, Player> playerMap =new ConcurrentHashMap<Integer, Player>();//一个座位对应一个玩家
		final Map<Integer, Table> tableMap = new ConcurrentHashMap<Integer, Table>();//一桌对应一个table实体类对象
		final Map<String, Player> userName2Player = new ConcurrentHashMap<String, Player>();//记录全部牌友信息
		
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new NettyServerHandler(playerMap,tableMap,userName2Player));
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
