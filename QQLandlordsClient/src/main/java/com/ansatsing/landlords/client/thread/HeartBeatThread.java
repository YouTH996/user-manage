package com.ansatsing.landlords.client.thread;

import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.HeartBeatProt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.rmi.runtime.Log;

import java.util.concurrent.TimeUnit;

/**
 * 心跳线程
 */
public class HeartBeatThread implements  Runnable {
    private final  static Logger LOGGER = LoggerFactory.getLogger(HeartBeatThread.class);
    private volatile boolean isStop = false;
    private Player player;

    public HeartBeatThread(Player player) {
        this.player = player;
    }
    public void stop(){
        this.isStop = true;
    }
    public void run() {
        LOGGER.info("客户端心跳线程启动");
        while(!isStop){
            AbstractProtocol heartBeatProt = new HeartBeatProt(player);
            heartBeatProt.sendMsg();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("客户端心跳线程结束");
    }
}
