package com.ansatsing.landlords.entity.client;

/**
 * 抢地主环节发出的信号实体类
 */
public class RoleMsg {
    private int seatNum;//哪个位置
    private String userName;
    private int roleFlag;//什么角色？1代表农民；2代表地主
    private int nextSeatNum;//下个抢地主的位置

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRoleFlag() {
        return roleFlag;
    }

    public void setRoleFlag(int roleFlag) {
        this.roleFlag = roleFlag;
    }

    public int getNextSeatNum() {
        return nextSeatNum;
    }

    public void setNextSeatNum(int nextSeatNum) {
        this.nextSeatNum = nextSeatNum;
    }
}
