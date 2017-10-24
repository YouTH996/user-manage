package com.ansatsing.landlords.entity;
/**
 * 消息实体类：网络IO流数据
 * @author sunyq
 *
 */
public class Message {
	private MsgType TYPE;//消息类型
	private String msg;//要转发或者处理的消息
	private String toWho;//消息发给谁
	public MsgType getTYPE() {
		return TYPE;
	}
	public void setTYPE(MsgType tYPE) {
		TYPE = tYPE;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getToWho() {
		return toWho;
	}
	public void setToWho(String toWho) {
		this.toWho = toWho;
	}
	
	
}
