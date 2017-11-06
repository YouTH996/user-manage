package com.ansatsing.landlords.protocol;



import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.util.Constants;

import java.io.Serializable;

/**
 * 游戏注册协议
 */
public class GameRegisterProt extends AbstractProtocol implements Serializable {
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
        if(checkDuplicatedUserName(this.userName)){//处理重复网名
            this.successful = false;
            this.responseMsg = "这网名有人正在使用,请更换网名!";
            singleSendMsg(player, this.getClass().getName()+ JSON.toJSONString(this));
        }else {
            String userName = this.userName;
            player.setUserName(userName);
            singleSendMsg(player,"这个网名可以啦!");
            batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",userName2Player.values(),true);
            userName2Player.put(userName, player);
            if(playerMap.size() > 0){//初始化游戏大厅座位情况
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Constants.INIT_SEAT_MSG_FLAG);
                for(Integer seatNum:playerMap.keySet()){
                    stringBuilder.append(seatNum+"="+playerMap.get(seatNum).getUserName()+",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                //String initSeat = Constants.INIT_SEAT_MSG_FLAG+Joiner.on(",").join(enterSeatList);
                singleSendMsg(player, stringBuilder.toString());
            }
        }
    }
    //检查重复网名
    private boolean checkDuplicatedUserName(String userName) {
        boolean flag = false;
        for(Player _player : userName2Player.values()) {
            if(_player.getUserName().equals(userName)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
