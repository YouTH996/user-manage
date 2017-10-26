package com.ansatsing.landlords.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.entity.Card;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

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
	/**
	 * 获取随机牌：以英文逗号隔开的组成的字符串形式
	 * @return
	 */
	public static String getRondomCards() {
		List<Integer> source=new ArrayList<Integer>(Arrays.asList(Constants.CARDS));  
		Collections.shuffle(source);
		return Joiner.on(",").join(source);
	}
	/**
	 * 根据图片地址产生Card实体对象，然后添加到list里,然后用来排序
	 * @param idx
	 * @return
	 */
	public static Card generateCard(int idx) {
		Card card = null;
		int number = 0;//3,4,5,.....,14,15,16,17  对应牌3,4,5,6,7,8,9,10,J,Q,K,A,2,小王,大王
		String image;
		int colorFlag = 0;//4-红桃，3-方块，2-黑桃，1-棉花  0-没有花色	
		if(idx ==1 ) {//大王
			number = 17;
			colorFlag = 0;
			image = String.valueOf(idx);
			card = new Card(number, image, colorFlag);
			return card;
		}else if(idx == 2) {// 小王
			number = 16;
			colorFlag = 0;
			image = String.valueOf(idx);
			card = new Card(number, image, colorFlag);
			return card;
		}else {
			if(idx >=3 && idx <=15) {//黑桃
				number = idx;
				colorFlag = 2;
			}else if(idx >= 16 && idx <= 28) {//红桃
				number = idx -13;
				colorFlag = 4;
			}else if(idx >= 29 && idx <= 41) {//棉花
				number = idx -26;
				colorFlag = 1;
			}else  if(idx >= 42 && idx <= 54) {//方块
				number = idx -39;
				colorFlag = 3;
			}
			image = String.valueOf(idx);
			card = new Card(number, image, colorFlag);
			return card;
		}
	}
	public static void main(String[] args) {
		for(int i=0;i<10;i++)
		System.out.println(LandlordsUtil.getRondomCards());
	}
}
