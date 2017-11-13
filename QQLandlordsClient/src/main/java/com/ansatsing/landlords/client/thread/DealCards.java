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
public class DealCards {
	private LandlordsRoomWindow landlordsRoomWindow;
	public DealCards(LandlordsRoomWindow landlordsRoomWindow) {
		this.landlordsRoomWindow = landlordsRoomWindow;
	}
	public void run() {
		List<String> card = Splitter.on(",").splitToList(landlordsRoomWindow.getServerCards());
		for(int i=0;i<card.size();i++){
			try {
				TimeUnit.MILLISECONDS.sleep(150);
				landlordsRoomWindow.dealCard(card.get(i),i);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(landlordsRoomWindow.getSeatNum() % 3 == 0) {//从左边位置开始轮流抢地主 相对于大厅里的座位
			landlordsRoomWindow.hideAllReadyLable();
			landlordsRoomWindow.startRob(landlordsRoomWindow.getSeatNum());
		}
	}
}
