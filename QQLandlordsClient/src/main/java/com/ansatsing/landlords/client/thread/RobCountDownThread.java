package com.ansatsing.landlords.client.thread;

import java.util.concurrent.TimeUnit;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;

/**
 * 抢地主倒计时
 *
 * @author ansatsing
 * @time 2017年10月23日 下午9:09:00
 */
public class RobCountDownThread implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(RobCountDownThread.class);
	private LandlordsRoomWindow landlordsRoomWindow;
	private int seconds;
	private volatile boolean isStop = false;
	public RobCountDownThread(LandlordsRoomWindow landlordsRoomWindow,int seconds){
		this.landlordsRoomWindow = landlordsRoomWindow;
		this.seconds = seconds;
	}
	public void run() {
		while(!this.isStop){
			setTimeLableText();
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
		if(seconds == 0) {//如果牌友一直不点 按钮 ，就直接当农民
			landlordsRoomWindow.sendRobMsg("农民");
		}
	}
	public void stop(){
		this.isStop = true;
	}
	//设置倒计时标签的内容：是显示倒计时还是显示 倒计时名称
	private void setTimeLableText(){
			landlordsRoomWindow.setTime(String.valueOf(seconds));	
	}
}
