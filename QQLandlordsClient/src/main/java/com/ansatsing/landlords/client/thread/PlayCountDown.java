package com.ansatsing.landlords.client.thread;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 出牌倒计时
 *
 * @author ansatsing
 * @time 2017年10月23日 下午9:09:00
 */
public class PlayCountDown/* implements Runnable*/ {
	private final static Logger LOGGER = LoggerFactory.getLogger(PlayCountDown.class);
	private LandlordsRoomWindow landlordsRoomWindow;
	private volatile int seconds;
	private volatile boolean isStop = false;
	public PlayCountDown(LandlordsRoomWindow landlordsRoomWindow, int seconds){
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.seconds = seconds;
	}
	public void run() {
		while(!this.isStop){
			setTimeLableText();
			//System.out.println("111PlayCountDownThread==secondes = "+seconds);
			if(seconds == 0){
				 break;
			 }
			 try {
	                TimeUnit.SECONDS.sleep(1);
	                
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			 seconds--;
		}
		if(seconds <= 0) {
			landlordsRoomWindow.sendPlayCardMsg(true);
		}
		if(seconds > 0 ) {
			landlordsRoomWindow.sendPlayCardMsg(false);
		}
	}
	public void stop(int sec){
		this.isStop = true;
		this.seconds = sec;
	}
	//设置倒计时标签的内容：是显示倒计时还是显示 倒计时名称
	private void setTimeLableText(){
			landlordsRoomWindow.setTime(String.valueOf(seconds));	
	}
}
