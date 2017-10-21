package com.ansatsing.landlords.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ansatsing.landlords.util.PictureUtil;
/**
 * 斗地主房间
 * @author sunyq
 *
 */
public class LandlordsRoomWindow extends JFrame {
	private JLabel seat;
	private int seatNum;
	private final int WIDTH = 1500;
	private final int HEIGHT = 800;
	private JPanel childJpanel1;//主窗体左侧
	private JPanel childJpanel2;//主窗体右侧	
	private JScrollPane historyScroll;/** 历史消息滚动条 */
	public JTextArea historyMsg;/** 历史消息区域 */
	private JPanel sendPanel;
	private JTextArea sendMsg;/** 输入消息区域 */
	private JButton send;	
	private JLabel[] cards;//存放纸牌
	private JLabel[] leftCards;//存放左边的牌
	private JLabel[] rightCards;//存放右边的牌
	private JLabel leftUserName;
	private JLabel rightUserName;
	private JLabel[] topCards;
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
		setSize(WIDTH, HEIGHT);
		setResizable(false);// 宽高度固死
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		
		BorderLayout lay = new BorderLayout();// 东南西北中 布局
		
		setLayout(lay);
		childJpanel1 = new JPanel();
		childJpanel1.setBackground(Color.cyan);
		childJpanel1.setLayout(new BorderLayout());
		
		//显示自己方向的面板
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
		//southPanel.setBackground(Color.red);
		JPanel actionPanel = new JPanel();//出牌，不要，提示，抢地主，不强等动作面板
		actionPanel.setPreferredSize(new Dimension(0, 20));
		//actionPanel.setSize(200, 30);
		//actionPanel.sets
		//actionPanel.setBackground(Color.BLACK);
		JPanel cardPanel = new JPanel();//显示纸牌的面板
		//cardPanel.setLayout(new BoxLayout(cardPanel,  BoxLayout.X_AXIS));
		cardPanel.setLayout(null);
		cardPanel.setPreferredSize(new Dimension(0, 215));
		//cardPanel.setBackground(Color.blue);
		cards = new JLabel[17];
		for(int i=0;i<17;i++){
			JLabel newJabel = new JLabel();
			newJabel.setIcon(PictureUtil.getPicture("cards/"+(i+1)+".jpg"));
			newJabel.setBounds(385+(18)*i, 50, 105, 150);
			cards[i] = newJabel;
			
		}
		for(int i=16;i>=0;i--){//不这步，牌的字母或者数字会被遮着
			cardPanel.add(cards[i]);
		}
		JLabel rob = new JLabel("抢地主");
		JLabel noRob = new JLabel("不抢");
		JLabel donot = new JLabel("不要");
		JLabel tip = new JLabel("提示");
		JLabel out = new JLabel("出牌");
		JLabel time = new JLabel("倒计时");
		actionPanel.add(rob);
		actionPanel.add(noRob);
		actionPanel.add(donot);
		actionPanel.add(tip);
		actionPanel.add(out);
		actionPanel.add(time);
		southPanel.add(actionPanel);
		southPanel.add(cardPanel);
		childJpanel1.add(southPanel, "South");
		
		//左边牌友
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		JPanel leftCardPanel = new JPanel();
		//leftCardPanel.setLayout(new BoxLayout(leftCardPanel, BoxLayout.Y_AXIS));
		leftCardPanel.setLayout(null);
		leftCardPanel.setPreferredSize(new Dimension(100, 0));
		JPanel leftActionPanel = new JPanel();
		leftActionPanel.setPreferredSize(new Dimension(50, 0));
		leftActionPanel.setLayout(null);
		JLabel leftTime = new JLabel("倒计时");
		leftTime.setBounds(10, 288, 40, 20);
		leftUserName = new JLabel("空位");
		leftUserName.setBounds(10, 258, 40, 20);
		leftActionPanel.add(leftUserName);
		leftActionPanel.add(leftTime);
		//leftActionPanel.setBackground(Color.red);
		//leftCardPanel.setBackground(Color.BLACK);
		leftCards = new JLabel[17];
		for(int i=0;i<17;i++){
			JLabel newJabel = new JLabel();
			newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			newJabel.setBounds(10, 80+(18)*i, 84, 113);
			leftCards[i] = newJabel;
			leftCardPanel.add(leftCards[i]);
			
		}
		leftPanel.add(leftActionPanel);
		leftPanel.add(leftCardPanel);
		
		childJpanel1.add(leftPanel,"West");
		
		//右边牌友
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		JPanel rightCardPanel = new JPanel();
		//rightCardPanel.setLayout(new BoxLayout(rightCardPanel, BoxLayout.Y_AXIS));
		rightCardPanel.setLayout(null);
		rightCardPanel.setPreferredSize(new Dimension(100, 0));
		JPanel rightActionPanel = new JPanel();
		rightActionPanel.setPreferredSize(new Dimension(50, 0));
		rightActionPanel.setLayout(null);
		JLabel rightTime = new JLabel("倒计时");
		rightTime.setBounds(10, 288, 40, 20);
		rightUserName = new JLabel("空位");
		rightUserName.setBounds(10, 258, 40, 20);
		rightActionPanel.add(rightUserName);
		rightActionPanel.add(rightTime);
		//rightActionPanel.setBackground(Color.red);
		//rightCardPanel.setBackground(Color.BLACK);
		rightCards = new JLabel[17];
		for(int i=0;i<17;i++){
			JLabel newJabel = new JLabel();
			newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			newJabel.setBounds(10, 80+(18)*i, 84, 113);
			rightCards[i] = newJabel;
			rightCardPanel.add(rightCards[i]);
			
		}
		rightPanel.add(rightCardPanel);
		rightPanel.add(rightActionPanel);
		
		
		childJpanel1.add(rightPanel,"East");
		
		//上方三张底牌
		JPanel topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setPreferredSize(new Dimension(0, 130));
		topCards = new JLabel[3];
		for(int i=0;i<3;i++){
			JLabel newJabel = new JLabel();
			newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			newJabel.setBounds(420+(100)*i, 10, 84, 113);
			topCards[i] = newJabel;
			topPanel.add(topCards[i]);
			
		}
		//topPanel.setBackground(Color.RED);
		childJpanel1.add(topPanel, "North");
		
		childJpanel2 = new JPanel();
		childJpanel2.setPreferredSize(new Dimension(350, 0));
		childJpanel2.setBackground(Color.GRAY);

		this.getContentPane().add(childJpanel1, "Center");
		this.getContentPane().add(childJpanel2, "East");

		// 历史记录
		historyMsg = new JTextArea(41,30);
		historyMsg.setEditable(false);
		historyScroll = new JScrollPane(historyMsg );
		childJpanel2.add(historyScroll);
		
		//发送消息区
		sendPanel = new JPanel();
		sendMsg = new JTextArea(5,25);
		sendPanel.add(sendMsg);
		send = new JButton("发送");
		send.setBounds(335, 2, 30, 50);
		sendPanel.add(send);
		childJpanel2.add(sendPanel);		
		setVisible(true);
	}
	private void initListener() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				seat.setText("空位");
			}
			
		});
		for(int i=0;i<cards.length;i++){
			final JLabel tempLabel = cards[i];
			tempLabel.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					int x = tempLabel.getX();
					int y = tempLabel.getY();
					if(y < 50){
						y += 20;
					}else{
						y -= 20;
					}
					tempLabel.setBounds(x, y, 105, 150);
				}
				
			});
		}
	}
	public void closeRoom() {
		seat.setText("空位");
		dispose();
	}
	public int getSeatNum() {
		return seatNum;
	}
	
}
