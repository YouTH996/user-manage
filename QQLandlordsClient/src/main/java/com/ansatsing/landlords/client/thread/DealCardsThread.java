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
		List<String> card = Splitter.on(",").splitToList(landlordsRoomWindow.getServerCards());
		for(int i=0;i<card.size();i++){
			try {
				landlordsRoomWindow.dealCard(card.get(i),i);
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		landlordsRoomWindow.startRob();
	}
}
