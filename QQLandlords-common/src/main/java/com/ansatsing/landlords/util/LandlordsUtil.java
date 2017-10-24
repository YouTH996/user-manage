package com.ansatsing.landlords.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LandlordsUtil {
	private final static Logger LOGGER = LoggerFactory.getLogger(LandlordsUtil.class);
	/**
	 * 通过座位号找到桌位号
	 * @param seatNum
	 * @return
	 */
	public static int getTableNum(int seatNum) {
		if(seatNum % 3 == 0){
			return seatNum / 3;
		}else if((seatNum+1)%3 == 0) {
			return (seatNum +1) / 3 -1;
		}else {
			return (seatNum -1) / 3;
		}
	}
	/**
	 * 获取左边位置的座位号
	 * @return
	 */
	public static int getLeftSeatNum(int seatNum ) {
		if(seatNum % 3 == 0){//左边  --参考游戏大厅方位
			return seatNum +1;
		}else if((seatNum+1)%3 == 0){//右边--参考游戏大厅方位
			return seatNum - 2;
		}else{//顶上--参考游戏大厅方位
			return seatNum + 1;
		}
	}
	/**
	 * 获取右边边位置的座位号
	 * @return
	 */
	public static int getRightSeatNum(int seatNum) {
		if(seatNum % 3 == 0){//左边  --参考游戏大厅方位
			return seatNum +2;
		}else if((seatNum+1)%3 == 0){//右边--参考游戏大厅方位
			return seatNum - 1;
		}else{//顶上--参考游戏大厅方位
			return seatNum - 1;
		}
	}	
}
