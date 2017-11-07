package com.ansatsing.landlords.exception;

public class LandlordsException extends  RuntimeException{
    private String message;//异常信息

    public LandlordsException(){

    }

    public LandlordsException(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
