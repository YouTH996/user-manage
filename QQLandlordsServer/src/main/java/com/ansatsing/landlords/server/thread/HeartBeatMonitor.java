package com.ansatsing.landlords.server.thread;

import com.ansatsing.landlords.entity.Player;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 监听那些玩家掉线了，掉线了就踢掉：主要针对入座位置的玩家，没有入座的玩家不监听
 */
public class HeartBeatMonitor implements Runnable {
    private Map<Integer, Player> playerMap;;
    public HeartBeatMonitor(Map<Integer, Player> playerMap){
        this.playerMap = playerMap;
    }
    public void run() {
        while (true){
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          Iterator iterator = playerMap.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<Integer,Player> entry = (Map.Entry<Integer,Player>)iterator.next();
                Player player = entry.getValue();
                if(System.currentTimeMillis() - player.getLastReveHeatTime() > 10000 ){
                    if(player.getSocket() != null){
                        try {
                            player.setUnnormalExited(true);
                            player.getSocket().close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
