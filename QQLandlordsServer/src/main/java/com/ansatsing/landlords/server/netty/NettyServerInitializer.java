package com.ansatsing.landlords.server.netty;

import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.util.Constants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class NettyServerInitializer extends ChannelInitializer<NioSocketChannel> {
    Map<Integer, Player> playerMap =new ConcurrentHashMap<Integer, Player>();//一个座位对应一个玩家
    Map<Integer, Table> tableMap = new ConcurrentHashMap<Integer, Table>();//一桌对应一个table实体类对象
    Map<String, Player> userName2Player = new ConcurrentHashMap<String, Player>();//记录全部牌友信息
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer(System.getProperty("line.separator").getBytes());
        pipeline.addLast(new IdleStateHandler(0,0, Constants.SERVER_IDLE_TIMEOUT, TimeUnit.SECONDS));
        pipeline.addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
        pipeline.addLast(new NettyServerHandler(playerMap,tableMap,userName2Player));
    }
}
