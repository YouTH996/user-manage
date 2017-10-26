package com.ansatsing.landlords.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ansatsing.landlords.util.LandlordsUtil;
import com.google.common.base.Splitter;

public class Card implements Comparable<Card>{
	private int number;//3,4,5,.....,14,15,16,17  对应牌3,4,5,6,7,8,9,10,J,Q,K,A,2,小王,大王
	private String image;
	private int colorFlag;//3-红桃，2-黑桃，1-棉花,0代表没有花色
	
	public Card(int number, String image, int colorFlag) {
		this.number = number;
		this.image = image;
		this.colorFlag = colorFlag;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getColorFlag() {
		return colorFlag;
	}
	public void setColorFlag(int colorFlag) {
		this.colorFlag = colorFlag;
	}
	public int compareTo(Card o) {//注意我们这是从大到小排序 所以 大于的时候返回 -1  小于的时候返回1
		if(this.number > o.getNumber()) {
			return -1;
		}else if(this.number == o.getNumber()) {
			if(this.colorFlag > o.getColorFlag()) {
				return -1;
			}else if(this.colorFlag == o.getColorFlag()){
				return 0;
			}else {
				return 1;
			}
		}else {
			return 1;
		}
	}
	public static void main(String[] args) {
		String cards = LandlordsUtil.getRondomCards();//产生一副随机牌，以图片的地址里的数字作为值，产生格式如：2,4,1,....
		List<String> cardList = Splitter.on(",").splitToList(cards);
		List<Card> list = new ArrayList<Card>();
		Card card = null;
		for(String cString:cardList) {
			card = LandlordsUtil.generateCard(Integer.valueOf(cString));
			list.add(card);
		}
		for(Card card2 : list) {
			System.out.print(card2.getImage()+"-");
		}
		Collections.sort(list/*,new CardComparator()*/);
		System.out.println("=================排序后=====================");
		for(Card card2 : list) {
			System.out.print(card2.getImage()+"-");
		}
	}
}
