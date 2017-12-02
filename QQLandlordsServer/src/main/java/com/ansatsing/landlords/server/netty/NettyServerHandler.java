package com.ansatsing.landlords.server.netty;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.entity.Table;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.ExitSeatProt;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class NettyServerHandler extends SimpleChannelInboundHandler<String> {
    private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerHandler.class);
    Map<Integer, Player> playerMap;//一个座位对应一个玩家
    Map<Integer, Table> tableMap;//一桌对应一个table实体类对象
    Map<String, Player> userName2Player;//记录全部牌友信息
    private Player player;
    private AbstractProtocol protocol;

    public NettyServerHandler(Map<Integer, Player> playerMap, Map<Integer, Table> tableMap, Map<String, Player> userName2Player) {
        if (playerMap == null) throw new NullPointerException("playerMap不能为空!");
        if (tableMap == null) throw new NullPointerException("tableMap不能为空!");
        if (userName2Player == null) throw new NullPointerException("userName2Player不能为空!");
        this.playerMap = playerMap;
        this.tableMap = tableMap;
        this.userName2Player = userName2Player;
        player = new Player();
    }

    protected void channelRead0(ChannelHandlerContext ctx, String readMsg) throws Exception {
        if(!readMsg.contains("HeartBeatProt"))
        LOGGER.info("{}向服务器发送了消息：{}", (player.getUserName() == null ? "未登陆的" : player.getUserName()), readMsg);
        try {
            int endIdx = readMsg.indexOf("{");
            String className = readMsg.substring(0, endIdx);
            String classContent = readMsg.substring(endIdx);
            if (!className.equals("com.ansatsing.landlords.protocol.HeartBeatProt")) {
                Class class1 = Class.forName(className);
                if (class1 != null) {
                    protocol = (AbstractProtocol) JSON.parseObject(classContent, class1);
                    protocol.setPlayer(player);
                    protocol.setPlayerMap(playerMap);
                    protocol.setTableMap(tableMap);
                    protocol.setUserName2Player(userName2Player);
                    protocol.handleProt();
                    if (className.equals("com.ansatsing.landlords.protocol.SystemExitProt")) {
                        ctx.channel().close();
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            LOGGER.error("非法协议类0：{}", e.getMessage());
        } catch (ClassNotFoundException e) {
            LOGGER.error("非法协议类1：{}", e.getMessage());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (player == null) throw new NullPointerException("player不能为空!");
        player.setChannel(ctx.channel());
        LOGGER.info("有位新玩家连接上服务器!其IP地址：{};当前在线人数为：{}", ctx.channel().remoteAddress(), userName2Player.size() + 1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOGGER.error("出现异常:", cause.getMessage());
        if (player != null) {
            LOGGER.info("{}异常退出系统时,player.getSeatNum():", player.getSeatNum());
            handleAfterExitOrException();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (player != null) {
            LOGGER.info("{}退出系统时,player.getSeatNum():", player.getSeatNum());
            handleAfterExitOrException();
        }
    }

    /**
     * 服务器持续指定时间没有收到读写事件就关闭连接并做相应的数据处理
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) return;
        IdleStateEvent event = (IdleStateEvent) evt;
        if (event.state() == IdleState.ALL_IDLE) {
            if (player != null) {
                handleAfterExitOrException();
            }
            ctx.close();
        }
    }

    private void handleAfterExitOrException() {
        if (player.getSeatNum() > -1) {
            if (player.getGameStatus() == 1) {//在游戏中的处理逻辑
                //todo
            }
            protocol = new ExitSeatProt(player.getSeatNum(), player.getUserName());
            protocol.setPlayer(player);
            protocol.setPlayerMap(playerMap);
            protocol.setTableMap(tableMap);
            protocol.setUserName2Player(userName2Player);
            protocol.handleProt();
        }
        if (player.getUserName() != null) {
            userName2Player.remove(player.getUserName());
            LOGGER.info("{}退出系统了", player.getUserName() + (player.isUnnormalExited() ? "异常" : "正常"));
        }
    }
}
