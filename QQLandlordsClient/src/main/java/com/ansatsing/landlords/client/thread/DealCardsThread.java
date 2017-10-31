package com.ansatsing.landlords.client.thread;



import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ansatsing.landlords.client.ui.LandlordsRoomWindow;
import com.google.common.base.Splitter;

/**
 * 发牌线程
 *
 * @author ansatsing
 * @time 2017年10月25日 下午9:18:06
 */
public class DealCardsThread implements Runnable {
	private LandlordsRoomWindow landlordsRoomWindow;
	public DealCardsThread(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	public void run() {
		if(landlordsRoomWindow.getServerCards() == null) {
			System.out.println("DealCardsThreadDealCardsThreadDealCardsThreadDealCardsThreadnulllllllllllllllllll");
		}
		List<String> card = Splitter.on(",").splitToList(landlordsRoomWindow.getServerCards());
		for(int i=0;i<card.size();i++){
			try {
				landlordsRoomWindow.dealCard(card.get(i),i);
				TimeUnit.MILLISECONDS.sleep(150);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(landlordsRoomWindow.getSeatNum() % 3 == 0) {//从左边位置开始轮流抢地主
			landlordsRoomWindow.hideAllReadyLable();
			landlordsRoomWindow.startRob(landlordsRoomWindow.getSeatNum());
		}
	}
}
