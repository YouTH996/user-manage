package com.ansatsing.landlords.entity;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

	public int compare(Card o1, Card o2) {
		// TODO Auto-generated method stub
		//return o1.compareTo(o2);//升序
		return o2.compareTo(o1);//降序
	}

}
