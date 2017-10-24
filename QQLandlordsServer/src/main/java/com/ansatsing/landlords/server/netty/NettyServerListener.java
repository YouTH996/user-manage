package com.ansatsing.landlords.server.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class NettyServerListener  implements ChannelFutureListener {
	private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerListener.class);
	public void operationComplete(ChannelFuture future) throws Exception {
		if (future.isSuccess()) {
			LOGGER.info("信息处理成功！");
		} else {
			LOGGER.info("信息处理失败！");
		}
	}

}
