package com.ansatsing.landlords.server.netty;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.ExitSeatProt;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServerHandler  implements ChannelInboundHandler {
	Map<Integer, Player> playerMap =new ConcurrentHashMap<Integer, Player>();//一个座位对应一个玩家
	Map<Integer, Table> tableMap = new ConcurrentHashMap<Integer, Table>();//一桌对应一个table实体类对象
	Map<String, Player> userName2Player = new ConcurrentHashMap<String, Player>();//记录全部牌友信息
	private Player player;
	private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
	private AbstractProtocol protocol;

	public NettyServerHandler(Map<Integer, Player> playerMap, Map<Integer, Table> tableMap, Map<String, Player> userName2Player) {
		player = new Player();
		//player.setChannel();
		this.playerMap = playerMap;
		this.tableMap =tableMap;
		this.userName2Player = userName2Player;
	}
	public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info(ctx.channel().remoteAddress() + "连接成功！");
		player.setChannel(ctx.channel());
	}

	public void channelInactive(ChannelHandlerContext arg0) throws Exception {
		if(player!=null){
			LOGGER.info("退出系统时,player.getSeatNum():"+player.getSeatNum());
			if(player.getSeatNum() > -1){
				if(player.getGameStatus() == 1){//在游戏中的处理逻辑
					//todo
				}
				protocol = new ExitSeatProt(player.getSeatNum(),player.getUserName());
				protocol.setPlayer(player);
				protocol.setPlayerMap(playerMap);
				protocol.setTableMap(tableMap);
				protocol.setUserName2Player(userName2Player);
				protocol.handleProt();
			}
			if(player.getUserName() != null){
				userName2Player.remove(player.getUserName());
				LOGGER.info(player.getUserName()+(player.isUnnormalExited()?"异常":"正常")+"退出系统了");
			}
		}
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
		LOGGER.info("netty服务器端出异常了："+arg1.getMessage());
	}

	public void userEventTriggered(ChannelHandlerContext arg0, Object arg1) throws Exception {

	}

	private void handleMessage(Channel channel, Object msg) {
		try {
			String readMsg = "";
				readMsg = (String)msg;
				//这个位置应该加一个协议过滤器，非法协议直接跳过
				if (!readMsg.equals("")) {
					if(!readMsg.contains("HeartBeatProt"))
						LOGGER.info((player.getUserName() == null ? "未登陆的":player.getUserName())+"发送了消息："+readMsg);
					int endIdx = readMsg.indexOf("{");
					try {
						String className = readMsg.substring(0, endIdx);
						String classContent = readMsg.substring(endIdx);
						player.setLastReveHeatTime(System.currentTimeMillis());
						Class class1 = null;
						try {
							class1 = Class.forName(className);
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
						if (class1 != null) {
							protocol = (AbstractProtocol) JSON.parseObject(classContent, class1);
							protocol.setPlayer(player);
							protocol.setPlayerMap(playerMap);
							protocol.setTableMap(tableMap);
							protocol.setUserName2Player(userName2Player);
							protocol.handleProt();
							if (className.equals("com.ansatsing.landlords.protocol.SystemExitProt")) {
								channel.close();
							}
						}
					}catch (Exception e1){
						LOGGER.info("异常信息："+e1.getMessage());
					}
				}
		} catch (Exception e1) {
			LOGGER.info("SocketException异常错误："+e1.getMessage());
			if(player != null && player.getUserName() != null)
				LOGGER.info(player.getUserName()+"异常掉线了");
		} finally {


		}
	}
}
