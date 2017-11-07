package com.ansatsing.landlords.exception;

public class NotSupportProtocolException extends LandlordsException {
    public NotSupportProtocolException(){
        super.setMessage("非法请求消息！");
    }
}
