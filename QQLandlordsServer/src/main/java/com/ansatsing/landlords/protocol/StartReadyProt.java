package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;
import com.ansatsing.landlords.util.LandlordsUtil;

import java.io.Serializable;

/**
 * 启动准备倒计时消息协议
 */
public class StartReadyProt  extends AbstractProtocol implements Serializable {
    private boolean isReStart;//是否重新启动准备倒计时

    public boolean isReStart() {
        return isReStart;
    }

    public void setReStart(boolean reStart) {
        isReStart = reStart;
    }

    public StartReadyProt() {
    }

    public StartReadyProt(boolean isReStart) {
        this.isReStart = isReStart;
    }

    @Override
    public void handleProt() {
        int seatNum = player.getSeatNum();
        int tableNum = LandlordsUtil.getTableNum(seatNum);
        batchSendMsg(this.getClass().getName()+ JSON.toJSONString(this), tableMap.get(tableNum).getPlayers(), false);
    }
}
