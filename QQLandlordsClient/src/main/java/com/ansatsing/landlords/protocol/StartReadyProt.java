package com.ansatsing.landlords.protocol;

import java.io.Serializable;

/**
 * 启动准备倒计时消息协议
 */
public class StartReadyProt extends AbstractProtocol implements Serializable {
    private boolean isReStart;//是否重新启动准备倒计时

    public boolean isReStart() {
        return isReStart;
    }

    public void setReStart(boolean reStart) {
        isReStart = reStart;
    }

    @Override
    public void handleProt() {
        landlordsRoomWindow.startGameReadyThread(isReStart);
    }
}
