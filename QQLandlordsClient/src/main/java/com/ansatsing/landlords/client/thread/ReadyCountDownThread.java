package com.ansatsing.landlords.client.thread;

import java.util.concurrent.TimeUnit;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 准备倒计时
 *
 * @author ansatsing
 * @time 2017年10月23日 下午9:09:00
 */
public class ReadyCountDownThread implements Runnable {
	private LandlordsRoomWindow landlordsRoomWindow;
	private int seconds;
	private volatile boolean isStop = false;//自己
	private volatile boolean rightStop = false;
	private volatile boolean leftStop = false;
	public ReadyCountDownThread(LandlordsRoomWindow landlordsRoomWindow,int seconds){
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.seconds = seconds;
	}
	public void run() {
		while(!isStop || !rightStop || !leftStop){//或者关系
			setTimeLableText();
			 try {
	                TimeUnit.SECONDS.sleep(1);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
			 seconds--;
			 if(seconds == 0){
				 break;
			 }
		}
		if(seconds == 0){
			if(!isStop)
			landlordsRoomWindow.closeRoom();
		}
		if(seconds > 0){//说明3人都准备好了，那就向服务器发送请求发牌的信号
			
		}
	}
	public void stop(){
		isStop = true;
	}
	public void stopRight(){
		rightStop = true;
	}
	public void stopLeft(){
		leftStop = true;
	}
	//设置倒计时标签的内容：是显示倒计时还是显示 倒计时名称
	private void setTimeLableText(){
		if(!isStop){
			landlordsRoomWindow.setTime(String.valueOf(seconds));
		}
		if(!rightStop){
			landlordsRoomWindow.setRightTime(String.valueOf(seconds)+rightStop);
		}
		if(!leftStop){
			landlordsRoomWindow.setLeftTime(String.valueOf(seconds)+leftStop);
		}
		
	}
}
