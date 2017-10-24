package com.ansatsing.landlords.server.netty;

import java.nio.charset.Charset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Message;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.MessageUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

public class NettyServerHandler  implements ChannelInboundHandler {
	private Map<String, Channel> nameToSocket;
	private Map<Integer, String> enterSeatMap;
	private Map<Integer, Map<String, Channel>> gameGroups;
	private Player player;
	private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
	public NettyServerHandler(Map<String, Channel> nameToSocket, Map<Integer, String> enterSeatMap, Map<Integer, Map<String, Channel>> gameGroups) {
		player = new Player();
		this.nameToSocket = nameToSocket;
		this.enterSeatMap =enterSeatMap;
		this.gameGroups = gameGroups;
	}
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelActive(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelInactive(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		LOGGER.info("接收的消息：" + ((ByteBuf) msg).toString(Charset.defaultCharset()));
		handleMessage(ctx.channel(), msg);
	}
	public void channelReadComplete(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelRegistered(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelUnregistered(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelWritabilityChanged(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void exceptionCaught(ChannelHandlerContext arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void userEventTriggered(ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	private void handleMessage(Channel channel, Object msg) {
		Message message  = MessageUtil.handle(msg.toString());
	}
}
