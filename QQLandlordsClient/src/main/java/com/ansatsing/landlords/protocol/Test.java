package com.ansatsing.landlords.protocol;

import com.alibaba.fastjson.JSON;

public class Test {
    public static void main(String[] args) throws ClassNotFoundException  {
       AbstractProtocol outCardProt = new OutCardProt(4,"4,3,2",5, "ansatsing", false);
        //outCardProt.sendMsg();
        String jsonStr = outCardProt.getClass().getName()+ JSON.toJSONString(outCardProt);
        //System.out.println(jsonStr);
        //System.out.println(outCardProt);
        int endIdx = jsonStr.indexOf("{");
        String className = jsonStr.substring(0,endIdx);
        String classContent = jsonStr.substring(endIdx);
        Class class1 = Class.forName(className);
        AbstractProtocol protocol = (AbstractProtocol)JSON.parseObject(classContent,class1);
       // protocol.setGameLobbyWindow();
        protocol.handleProt();
    }
}
