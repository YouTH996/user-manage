package com.ansatsing.landlords.client.thread;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 针对牌友位置的出牌倒计时；主要方便自己得到当前是谁在出牌的一个信息
 *
 * @author ansatsing
 * @time 2017年10月23日 下午9:09:00
 */
public class PlayCountDownThread implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(PlayCountDownThread.class);
	private LandlordsRoomWindow landlordsRoomWindow;
	private  int seconds;
	private volatile boolean isStop = false;
	private boolean isLeftPlayer;
	public PlayCountDownThread(LandlordsRoomWindow landlordsRoomWindow, int seconds,boolean isLeftPlayer){
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.seconds = seconds;
		this.isLeftPlayer =isLeftPlayer;
	}
	public void run() {
		while(!this.isStop){
			setTimeLableText();
			 try {
	                TimeUnit.SECONDS.sleep(1);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			 seconds--;
		}
	}

	public boolean isLeftPlayer() {
		return isLeftPlayer;
	}

	public void stop(){
		this.isStop = true;
	}
	//设置倒计时标签的内容：是显示倒计时还是显示 倒计时名称
	private void setTimeLableText(){
			landlordsRoomWindow.setRightOrLeftTime(seconds, isLeftPlayer);
	}
}
