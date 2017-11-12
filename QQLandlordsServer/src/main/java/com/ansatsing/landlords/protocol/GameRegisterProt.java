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
    @Override
    public void handleProt() {
        if(checkDuplicatedUserName(this.userName)){//处理重复网名
            this.successful = false;
            this.responseMsg = "这网名有人正在使用,请更换网名!";
            singleSendMsg(player, this.getClass().getName()+ JSON.toJSONString(this));
        }else {
            String userName = this.userName;
            player.setUserName(userName);
            this.successful = true;
            this.responseMsg = "这个网名可以啦!";
            singleSendMsg(player,this.getClass().getName()+ JSON.toJSONString(this));
            player.setSeatNum(-1);
            //System.out.println();
            AbstractProtocol chatMsgProt=new ChatMsgProt(1, userName, userName+"骑着母猪大摇大摆溜进游戏室!", -1);
            chatMsgProt.setUserName2Player(this.userName2Player);
            chatMsgProt.setTableMap(this.tableMap);
            chatMsgProt.setPlayerMap(playerMap);
            chatMsgProt.setPlayer(player);
            chatMsgProt.handleProt();
           // batchSendMsg(userName+"骑着野母猪大摇大摆的溜进聊天室......大家给点面子欢迎欢迎！！！",userName2Player.values(),true);
            userName2Player.put(userName, player);
            if(playerMap.size() > 0) {//初始化游戏大厅座位情况
                AbstractProtocol initSeatProt = new InitSeatProt();
                initSeatProt.setUserName2Player(this.userName2Player);
                initSeatProt.setTableMap(this.tableMap);
                initSeatProt.setPlayerMap(playerMap);
                initSeatProt.setPlayer(player);
                initSeatProt.handleProt();
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
