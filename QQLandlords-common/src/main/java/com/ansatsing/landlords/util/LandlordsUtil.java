package com.ansatsing.landlords.util;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Card;
import com.ansatsing.landlords.entity.OutCard;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;

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
	//获取所以协议类的类全名
	public static Set<String> getAllProtFullName(){
		Set<String> classNames = new LinkedHashSet<String>();
		String protDirName =Constants.PROT_PACK_NAME.replace(".","/");
		URL url =  Thread.currentThread().getContextClassLoader().getResource(protDirName);
		File file = new File(url.toString().substring(5));
		if(file.isDirectory()){
			File[] files = file.listFiles();
			for(File currentFile:files){
				if(currentFile.isFile()){
					// 如果是java类文件 去掉后面的.class 只留下类名
					String className = currentFile.getName().substring(0, currentFile.getName().length() - 6);
					String entityName = Constants.PROT_PACK_NAME + '.' + className;
					//System.out.println("检测到类名：packageName="+packageName+" >>>> "+entityName);
					classNames.add(entityName);
				}
			}
		}
		return classNames;
	}

	/**
	 * 判断出的牌是什么类型
	 * 		0代表单牌								1
	 * 		1代表对子								2
	 * 		2代表3带1								4
	 * 		3代表4张相同的牌带2张牌					6
	 * 		4代表一条龙，至少5张牌 最多12			5-12
	 * 		5代表普通炸弹，4张相同的牌				4
	 * 		6代表王炸								2
	 * @param cardList
	 * @return
	 */
	public static OutCard  generateOutCard(List<String> cardList){
		int playCardType = -1;
		OutCard outCard = new OutCard();
		if(cardList != null && cardList.size() > 0){
			int cardTotal = cardList.size();
			List<Card> cards = new ArrayList<Card>();
			for(int i=0;i<cardTotal;i++){
				cards.add(generateCard(Integer.parseInt(cardList.get(i))));
			}
			Collections.sort(cards);//排序
			if(cardTotal == 1){//单牌
				outCard.setPlayCardType(0);
				outCard.setOutCardNum(cards.get(0).getNumber());
				outCard.setCards(cardList.get(0));
				return outCard;
			}else if(cardTotal == 2){//判断对子或者王炸
				if(cards.get(0).getNumber() == cards.get(1).getNumber()){
					outCard.setPlayCardType(1);
					outCard.setOutCardNum(cards.get(0).getNumber());
					outCard.setCards(Joiner.on(",").join(cardList));
					return outCard;
				}else if(cards.get(0).getNumber() == 17 && cards.get(1).getNumber() == 16){
					outCard.setPlayCardType(6);
					outCard.setOutCardNum(18);//18最大的一张牌
					outCard.setCards(Joiner.on(",").join(cardList));
					return outCard;
				}
			}else if(cardTotal == 4){//判断3带1 以及普通炸弹
				if(cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber()){
					outCard.setPlayCardType(5);
					outCard.setOutCardNum(cards.get(0).getNumber());
					outCard.setCards(Joiner.on(",").join(cardList));
					return outCard;
				}else if((cards.get(0).getNumber() != cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() == cards.get(3).getNumber())
						||(cards.get(0).getNumber() == cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber() && cards.get(2).getNumber() != cards.get(3).getNumber())){
					outCard.setPlayCardType(2);
					outCard.setOutCardNum(cards.get(2).getNumber());
					if(cards.get(0).getNumber() != cards.get(1).getNumber()){//6555-->5556
						Card card = cards.get(0);
						cards.remove(0);
						cards.add(card);
					}
					outCard.setCards(Joiner.on(",").join(cards));
					return outCard;
				}
			}else if(cardTotal == 6){//判断4带2以及 6张牌组成的一条龙
				if(isStraightCards(cards)){//345678
					outCard.setPlayCardType(4);
					outCard.setOutCardNum(cards.get(2).getNumber());
					Collections.reverse(cards);
					outCard.setCards(Joiner.on(",").join(cards));
					return outCard;
				}else if(is4with2(cards)){//444433
					outCard.setPlayCardType(6);
					outCard.setOutCardNum(cards.get(2).getNumber());
					if(cards.get(0).getNumber() != cards.get(1).getNumber() && cards.get(1).getNumber() == cards.get(2).getNumber()){
						//755554-->555547
						Card card = cards.get(0);
						cards.remove(0);
						cards.add(card);
					}else if( cards.get(1).getNumber()!= cards.get(2).getNumber()){//875555
						Card card1 = cards.get(0);
						Card card2 = cards.get(1);
						cards.remove(0);
						cards.remove(1);
						cards.add(card1);
						cards.add(card2);
					}
					outCard.setCards(Joiner.on(",").join(cardList));
					return outCard;
				}
			}else if(cardTotal == 5 || cardTotal > 6){//456789
				if(isStraightCards(cards)){
					outCard.setPlayCardType(4);
					outCard.setOutCardNum(cards.get(2).getNumber());
					Collections.reverse(cards);
					outCard.setCards(Joiner.on(",").join(cards));
					return outCard;
				}
			}
		}
		outCard.setPlayCardType(playCardType);
		return outCard;
	}
	private static boolean isStraightCards(List<Card> cards){
		if(cards != null)
		{
			if(cards.size() <5){
				return false;
			}
			for(int i=0;i<cards.size();i++){
				if(cards.get(0).getNumber()!= (cards.get(i).getNumber()+i)){
					return  false;
				}
			}
			return true;
		}else{
			return false;
		}
	}
	private static  boolean is4with2(List<Card> cards){
		//766665
		if(cards.get(0).getNumber()!= cards.get(1).getNumber()&&cards.get(1).getNumber()== cards.get(2).getNumber()
				&&cards.get(2).getNumber()== cards.get(3).getNumber()&&cards.get(3).getNumber()== cards.get(4).getNumber()
				&&cards.get(4).getNumber()!= cards.get(5).getNumber()){
			return true;
		}
		//666654
		if(cards.get(0).getNumber()== cards.get(1).getNumber()&&cards.get(1).getNumber()== cards.get(2).getNumber()
				&&cards.get(2).getNumber()== cards.get(3).getNumber()&&cards.get(3).getNumber()!= cards.get(4).getNumber()){
			return true;
		}
		//876666
		if(cards.get(1).getNumber()!= cards.get(2).getNumber()
				&&cards.get(2).getNumber()== cards.get(3).getNumber()&&cards.get(3).getNumber()!= cards.get(4).getNumber()
				&&cards.get(4).getNumber()== cards.get(5).getNumber()){
			return true;
		}
		return false;
	}
	public static void main(String[] args) throws ClassNotFoundException {
/*		Set<String> classNames = getAllProtFullName();
		*//*for(String str:classNames){
			System.out.println(str);
		}*//*
		OutCardProt outCardProt = new OutCardProt(4,"4,3,2",5, "ansatsing", false);
		String jsonStr = outCardProt.getClass().getName()+JSON.toJSONString(outCardProt);
		//System.out.println(jsonStr);
		//System.out.println(outCardProt);
		int endIdx = jsonStr.indexOf("{");
		String className = jsonStr.substring(0,endIdx);
		String classContent = jsonStr.substring(endIdx);
		Class class1 = Class.forName(className);
		JSON.parseObject(classContent,class1);*/
	}
}
