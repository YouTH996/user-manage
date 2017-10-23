package com.ansatsing.landlords.client.thread;

import java.util.concurrent.TimeUnit;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 倒计时线程
 *
 * @author ansatsing
 * @time 2017年10月23日 下午9:09:00
 */
public class CountDownThread implements Runnable {
	private LandlordsRoomWindow landlordsRoomWindow;
	private int seconds;
	private volatile boolean isStop = false;
	public CountDownThread(LandlordsRoomWindow landlordsRoomWindow,int seconds){
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.seconds = seconds;
	}
	public void run() {
		while(!isStop){
			landlordsRoomWindow.setTime(String.valueOf(seconds));
			 try {
	                TimeUnit.SECONDS.sleep(1);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			 if(seconds == 0){
				 break;
			 }
			 seconds--;
		}
		if(seconds == 0){
			landlordsRoomWindow.closeRoom();
		}
	}
	public void stop(){
		isStop = true;
	}
}
