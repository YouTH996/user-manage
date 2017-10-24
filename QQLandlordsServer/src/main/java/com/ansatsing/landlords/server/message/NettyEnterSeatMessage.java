package com.ansatsing.landlords.server.message;

import java.util.Map;

import com.ansatsing.landlords.server.netty.NettyServerListener;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;

public class NettyEnterSeatMessage extends EnterSeatMessage<Channel> {

	@Override
	public void batchSendMsg(String sendMsg, Map<String, Channel> nameToSocket) {
		for(Channel channel:nameToSocket.values()) {
			if(channel == this.socket) continue;
			singleSendMsg(channel, sendMsg);
		}
	}

	@Override
	protected void singleSendMsg(Channel channel, String sendMsg) {
		if (null != channel) {
			channel.writeAndFlush(ByteBufAllocator.DEFAULT.buffer()
					.writeBytes(sendMsg.getBytes()))
					.addListener(new NettyServerListener());
			System.out.println("发送的消息：" + sendMsg.getBytes().length + sendMsg);
		}
	}
	
}
