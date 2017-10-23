package com.ansatsing.landlords.client;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * @file 倒计时
 * @author qzw
 *
 */
public class Counter {
 
    private JFrame frame;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;
 
    public static void main(String[] args) {
 
        new Counter().getTime();
 
    }
 
    /* 倒计时的主要代码块 */
    private void getTime() {
        long time = 1 * 36000; // 自定义倒计时时间
        long hour = 0;
        long minute = 0;
        long seconds = 0;
 
        while (time >= 0) {
            hour = time / 3600;
            minute = (time - hour * 3600) / 60;
            seconds = time - hour * 3600 - minute * 60;
            jl1.setText(hour + "时");
            jl2.setText(minute + "分");
            jl3.setText(seconds + "秒");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time--;
        }
 
    }
 
    /* 构造 实现界面的开发 GUI */
    public Counter() {
        frame = new JFrame("倒计时");
        jl1 = new JLabel();
        jl2 = new JLabel();
        jl3 = new JLabel();
 
        init();
 
    }
 
    /* 组件的装配 */
    private void init() {
        JPanel jp = new JPanel();
        jp.add(jl1);
        jp.add(jl2);
        jp.add(jl3);
 
        frame.add(jp);
 
        frame.setVisible(true);
        frame.setLocation(300, 400);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
 
}
