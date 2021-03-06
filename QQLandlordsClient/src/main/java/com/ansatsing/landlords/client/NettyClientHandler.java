package com.ansatsing.landlords.client;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.HeartBeatProt;
import com.ansatsing.landlords.util.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

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
    private Bootstrap bootstrap;
    private static int reconnetNums = 0;
    public NettyClientHandler(Context context,Bootstrap bootstrap){
       /* this.player = context.getPlayer();
        this.landlordsRoomWindow = context.getLandlordsRoomWindow();
        this.loginWidow = context.getLoginWidow();
        this.qqGameWindow = context.getQqGameWindow();*/
        this.bootstrap = bootstrap;
        this.context =context;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String readMsg) throws Exception {
        LOGGER.info("从服务器收到的消息1：{}", readMsg);
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
        LOGGER.info("从服务器收到的消息2：{}", readMsg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
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
        reconnetNums = 0;
        if(context.getPlayer() == null) throw new NullPointerException("player为空!");
        context.getPlayer().setChannel(ctx.channel());
        System.out.println("channelActivechannelActive");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        if(context.isLoginSuccess()){
            if(reconnetNums >= Constants.RECONNECT_NUM){
                System.out.printf("已经连续重连服务器%d次啦，哎呀算了不玩这游戏了....\n",reconnetNums);
                bootstrap.group().shutdownGracefully();
                return;
            }
            reconnetNums++;
            System.out.printf("掉线了，3秒后将进行第%d次重连服务器....\n",reconnetNums);
            ctx.channel().eventLoop().schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        bootstrap.connect().addListener(new ChannelFutureListener() {
                            @Override
                            public void operationComplete(ChannelFuture future) throws Exception {
                                if(future.cause() != null){
                                    System.out.printf("重连出现异常：%s,可能是服务器没有开启！\n",future.cause().getMessage());
                                }else {
                                    System.out.printf("第%d次重连成功!",reconnetNums);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },2, TimeUnit.SECONDS);
        }
    }

    //停止线程
    public void stop() {
        this.isStop = true;
    }

    /**
     *持续没有读写事件多少秒后就触发发送心跳包到服务器
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(!(evt instanceof IdleStateEvent)) return;
        IdleStateEvent event = (IdleStateEvent)evt;
        if(event.state() == IdleState.ALL_IDLE){
            AbstractProtocol heartBeatProt = new HeartBeatProt(context.getPlayer());
            heartBeatProt.sendMsg();
        }
    }
}
