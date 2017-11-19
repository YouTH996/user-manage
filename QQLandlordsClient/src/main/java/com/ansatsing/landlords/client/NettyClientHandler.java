package com.ansatsing.landlords.client;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.client.ui.GameLobbyWindow;
import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.ansatsing.landlords.client.ui.LoginWidow;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyClientHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientHandler.class);
    private boolean isStop = false;
   /* private volatile GameLobbyWindow qqGameWindow;
    private volatile LoginWidow loginWidow;
    private volatile LandlordsRoomWindow landlordsRoomWindow;
    private AbstractProtocol protocol;
    private Player player;*/
    private volatile Context context;
    private AbstractProtocol protocol;
    public NettyClientHandler(Context context){
       /* this.player = context.getPlayer();
        this.landlordsRoomWindow = context.getLandlordsRoomWindow();
        this.loginWidow = context.getLoginWidow();
        this.qqGameWindow = context.getQqGameWindow();*/
        this.context =context;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String readMsg) throws Exception {
        LOGGER.info("从服务器收到的消息：{}", readMsg);
        int endIdx = readMsg.indexOf("{");
        String className = readMsg.substring(0, endIdx);
        String classContent = readMsg.substring(endIdx);
        Class _class = null;
        try {
            _class = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*if (protocol != null) {
            if (protocol.getGameLobbyWindow() != null && context.getQqGameWindow() == null) {
                this.qqGameWindow = protocol.getGameLobbyWindow();
            }
            if (protocol.getLandlordsRoomWindow() != null && this.landlordsRoomWindow == null) {
                this.landlordsRoomWindow = protocol.getLandlordsRoomWindow();
            }
        }*/
        protocol = (AbstractProtocol) JSON.parseObject(classContent, _class);
        protocol.setLoginWidow(context.getLoginWidow());
        protocol.setGameLobbyWindow(context.getQqGameWindow());
        protocol.setLandlordsRoomWindow(context.getLandlordsRoomWindow());
        protocol.handleProt();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("客户端信息接受线程出现异常：" + cause.getMessage());
        ctx.channel().close();
        if (context.getLandlordsRoomWindow() != null) {
            context.getLandlordsRoomWindow().closeRoom();
        }
        if (context.getQqGameWindow() != null) {
            context.getQqGameWindow().dispose();
        }
       // loginWidow = new LoginWidow();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if(context.getPlayer() == null) throw new NullPointerException("player为空!");
        context.getPlayer().setChannel(ctx.channel());
        System.out.println("channelActivechannelActive");
    }

    //停止线程
    public void stop() {
        this.isStop = true;
    }
}
