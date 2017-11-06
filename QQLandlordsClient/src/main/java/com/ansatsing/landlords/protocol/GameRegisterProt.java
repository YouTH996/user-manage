package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * 游戏注册协议
 */
public class GameRegisterProt  extends AbstractProtocol implements Serializable {
    private String userName;//注册的用户名
    private boolean successful;//是否注册成功
    private String responseMsg;//服务器返回的信息

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }


    public void handleProt() {
        loginWidow.handleGameRegister(this.successful,this);
    }

    public void sendMsg() {
      super.sendMsg(JSON.toJSONString(this));
    }
}
