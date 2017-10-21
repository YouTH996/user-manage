package com.ansatsing.landlords.client.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 * 斗地主房间
 * @author sunyq
 *
 */
public class LandlordsRoomWindow extends JFrame {
	private JLabel seat;
	private int seatNum;
	public static void main(String[] args) {
		//new LandlordsRoomWindow();
	}
	public LandlordsRoomWindow(JLabel seat,int seatNum) {
		initGUI();
		initListener();
		this.seatNum = seatNum;
		this.seat = seat;
	}
	private void initGUI() {
		setTitle("开房斗地主");
		setSize(1200, 800);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	private void initListener() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				seat.setText("空位");
			}
			
		});
	}
	public void closeRoom() {
		seat.setText("空位");
		dispose();
	}
	public int getSeatNum() {
		return seatNum;
	}
	
}
