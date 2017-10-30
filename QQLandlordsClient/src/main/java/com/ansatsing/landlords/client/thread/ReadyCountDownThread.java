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
	private volatile boolean isAllSitted = true;
	public ReadyCountDownThread(LandlordsRoomWindow landlordsRoomWindow,int seconds){
		System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<ReadyCountDownThread()>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.leftStop = landlordsRoomWindow.leftIsReady();
		this.rightStop = landlordsRoomWindow.rightIsReady();
		this.isStop = landlordsRoomWindow.isReady();
		this.seconds = seconds;
		System.out.println("111+++++++leftStop=="+leftStop+"         rightStop==="+rightStop+"    istop==="+isStop);
	}
	public void run() {
		System.out.println("222+++++++leftStop=="+leftStop+"         rightStop==="+rightStop+"    istop==="+isStop);
		while((!isStop || !rightStop || !leftStop) && isAllSitted){//3人有一人未准备 同时 3个位置都有人
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
		if(seconds > 0 && landlordsRoomWindow.isReady() && landlordsRoomWindow.leftIsReady()&&landlordsRoomWindow.rightIsReady()){//说明3人都准备好了，那就向服务器发送请求发牌的信号
			landlordsRoomWindow.sendDealMsg();
		}else if(seconds == 0) {
			if(!isStop) {
				System.out.println("ReadyCountDownThreadReadyCountDownThrea    closeRoom");
				landlordsRoomWindow.closeRoom();
			}
		}
	}
	public void stop(){
		System.out.println("ReadyCountDownThreadReadyCountDownThrea    stop()");
		isStop = true;
	}
	public void stopRight(){
		rightStop = true;
	}
	public void stopLeft(){
		leftStop = true;
	}
	public void notAllSitted() {
		this.isAllSitted = false;
	}
	//设置倒计时标签的内容：是显示倒计时还是显示 倒计时名称
	private void setTimeLableText(){
		if(!isStop){
			landlordsRoomWindow.setTime(String.valueOf(seconds));
		}
		if(!rightStop){
			System.out.println("RightTimeRightTimeRightTime="+seconds);
			landlordsRoomWindow.setRightTime(String.valueOf(seconds));
		}
		if(!leftStop){
			System.out.println("LeftTimeLeftTimeLeftTime="+seconds);
			landlordsRoomWindow.setLeftTime(String.valueOf(seconds));
		}
		
	}
}
