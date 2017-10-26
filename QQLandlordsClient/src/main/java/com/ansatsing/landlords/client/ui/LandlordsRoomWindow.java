package com.ansatsing.landlords.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.handler.SendMessageHandler;
import com.ansatsing.landlords.client.thread.ReadyCountDownThread;
import com.ansatsing.landlords.client.thread.RobCountDownThread;
import com.ansatsing.landlords.entity.Card;
import com.ansatsing.landlords.entity.CardComparator;
import com.ansatsing.landlords.state.GameState;
import com.ansatsing.landlords.state.GameWaitState;
import com.ansatsing.landlords.util.LandlordsUtil;
import com.ansatsing.landlords.util.PictureUtil;

import ch.qos.logback.core.joran.conditional.ElseAction;
/**
 * 斗地主房间
 * @author sunyq
 *
 */
public class LandlordsRoomWindow extends JFrame {
	private final static Logger LOGGER = LoggerFactory.getLogger(LandlordsRoomWindow.class);
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
	private String userName;
	private SendMessageHandler messageHandler;
	private JLabel time;//当前牌友的倒计时
	private JLabel rob ;
	private JLabel noRob;
	private JLabel donot;
	private JLabel tip;
	private JLabel out;
	private JLabel ready;
	private ReadyCountDownThread countDownThread;
	private RobCountDownThread robDownThread;
	private JLabel rightTime;
	private JLabel leftTime;
	private JLabel leftReady;
	private JLabel rightReady;
	private GameState gameState = new GameWaitState(this);//游戏状态;初始化状态为游戏等待状态
	private String saveCards;//存放服务器发来的随机牌
	private List<Card> cardList = new ArrayList<Card>();//主要功能用于牌排序
	private JLabel playerRole;//农民还是地主--->自己
	private JLabel leftPlayerRole;//农民还是地主---》自己的左边牌友
	private JLabel rightPlayerRole;//农民还是地主--->自己的右边牌友
	public static void main(String[] args) {
		//new LandlordsRoomWindow();
	}
	public LandlordsRoomWindow(JLabel seat,int seatNum,String userName,Socket socket) {
		this.userName = userName;
		initGUI();
		initListener();
		this.seatNum = seatNum;
		this.seat = seat;
		this.messageHandler = new SendMessageHandler(socket);
	}
	private void initGUI() {
		setTitle("开房斗地主---" +userName);
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
		cards = new JLabel[20];
		for(int i=0;i<20;i++){
			JLabel newJabel = new JLabel();
			//newJabel.setIcon(PictureUtil.getPicture("cards/"+(i+1)+".jpg"));
			newJabel.setBounds(385+(18)*i, 50, 105, 150);
			cards[i] = newJabel;
			
		}
		for(int i=16;i>=0;i--){//不这步，牌的字母或者数字会被遮着
			cardPanel.add(cards[i]);
		}
		playerRole = new JLabel("角色");
		playerRole.setVisible(false);
		 rob = new JLabel("抢地主");
		 noRob = new JLabel("不抢");
		 donot = new JLabel("不要");
		 tip = new JLabel("提示");
		 out = new JLabel("出牌");
		time = new JLabel("倒计时");
		 ready = new JLabel("请准备");
		 rob.setVisible(false);
		 noRob.setVisible(false);
		 donot.setVisible(false);
		 tip.setVisible(false);
		 out.setVisible(false);
		 actionPanel.add(playerRole);
		actionPanel.add(rob);
		actionPanel.add(noRob);
		actionPanel.add(donot);
		actionPanel.add(tip);
		actionPanel.add(out);
		actionPanel.add(time);
		actionPanel.add(ready);
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
		 leftTime = new JLabel("倒计时");
		leftTime.setBounds(10, 288, 40, 20);
		leftUserName = new JLabel("空位");
		leftUserName.setBounds(10, 258, 40, 20);
		 leftReady = new JLabel("请准备");
		leftReady.setBounds(10, 318, 40, 20);
		leftActionPanel.add(leftUserName);
		leftActionPanel.add(leftTime);
		leftActionPanel.add(leftReady);
		leftPlayerRole = new JLabel("角色");
		leftPlayerRole.setVisible(false);
		leftPlayerRole.setBounds(10, 348, 40, 20);
		leftActionPanel.add(leftPlayerRole);
		leftCards = new JLabel[20];
		for(int i=0;i<20;i++){
			JLabel newJabel = new JLabel();
			//newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			newJabel.setBounds(10, 20+(15)*i, 84, 113);
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
		 rightTime = new JLabel("倒计时");
		rightTime.setBounds(10, 288, 40, 20);
		rightUserName = new JLabel("空位");
		rightUserName.setBounds(10, 258, 40, 20);
		 rightReady = new JLabel("请准备");
		rightReady.setBounds(10, 318, 40, 20);
		rightActionPanel.add(rightUserName);
		rightActionPanel.add(rightTime);
		rightActionPanel.add(rightReady);
		//rightActionPanel.setBackground(Color.red);
		//rightCardPanel.setBackground(Color.BLACK);
		rightPlayerRole = new JLabel("角色");
		rightPlayerRole.setBounds(10, 348, 40, 20);
		rightPlayerRole.setVisible(false);
		rightActionPanel.add(rightPlayerRole);
		rightCards = new JLabel[20];
		for(int i=0;i<20;i++){
			JLabel newJabel = new JLabel();
			//newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			newJabel.setBounds(10, 20+(15)*i, 84, 113);
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
			//newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
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
				closeRoom();
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
		//房间发送信息鼠标单击事件
		send.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!sendMsg.getText().trim().equals("")) {
					String msg = sendMsg.getText().trim();
					msg = msg.replaceAll("：", ":");
					//messageHandler.sendr(msg);
					if (msg.startsWith("@")) {
						messageHandler.sendRoomPrivateChatMsg(msg);
						int endIdx = msg.indexOf(":");
						String name = msg.substring(1, endIdx);
						setHistoryMsg("你悄悄对" + name + "说:" + msg);
					} else {
						messageHandler.sendRoomAllChatMsg(msg);
						setHistoryMsg("你说:" + msg);
					}
				}
			}
			
		});
		//准备事件
		ready.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				messageHandler.sendGameReadyMsg(userName);
				countDownThread.stop();
				ready.setVisible(false);
				time.setText("倒计时");
			}
			
		});
		//抢地主事件
		rob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendRobMsg("地主");
			}
		});
		//不抢地主事件
		noRob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendRobMsg("农民");
			}
		});
	}
	public void closeRoom() {
		messageHandler.sendExitSeatMsg(String.valueOf(seatNum));
		seat.setText("空位");
		dispose();
	}
	public int getSeatNum() {
		return seatNum;
	}
	/**
	 * 实现了斗地主房间位置跟游戏大厅的位置顺序一致
	 * msg    --->  ansatsing=3
	 *   1
	 * 0   2 
	 *   4
	 * 3   5
	 * @param msg
	 */
	public void setSeatUserName(String msg){
	//	LOGGER.info(userName+"入座情况》》》》》》》》》》》》"+msg);
		int idx = msg.indexOf("=");
		String userName = msg.substring(0,idx);
		messageHandler.sendAddSocketMsg(userName);
		int _seatNum = Integer.parseInt(msg.substring(idx+1));
		if(_seatNum == LandlordsUtil.getLeftSeatNum(seatNum)) {
			leftUserName.setText(userName);
		}else if(_seatNum == LandlordsUtil.getRightSeatNum(seatNum)) {
			rightUserName.setText(userName);
		}
		if(!leftUserName.getText().equals("空位") && !rightUserName.getText().equals("空位")){
			gameState.pushGameState();
			gameState.handleWindow();
			/*this.countDownThread = new CountDownThread(this, 30);
			Thread thread = new Thread(countDownThread);
			thread.start();*/
		}
	}
	public void emptySeat(String tempMsg) {
		//messageHandler.sendRemoveSocketMsg(tempMsg);
		if(leftUserName.getText().trim().equals(tempMsg)){
			leftUserName.setText("空位");
		}else if(rightUserName.getText().trim().equals(tempMsg)){
			rightUserName.setText("空位");
		}
		if(countDownThread != null){
			countDownThread.stop();
			this.time.setText("倒计时");
		}
	}
	public void setCountDownThread(ReadyCountDownThread countDownThread) {
		this.countDownThread = countDownThread;
	}
	/**
	 * 显示收到的群聊消息
	 * 
	 * @param readMsg
	 */
	public void setHistoryMsg(String readMsg) {
		this.historyMsg.append(readMsg + "\n");
	}
	//设置自己的倒计时时间
	public void setTime(String time){
		this.time.setText(time);
	}
	//设置左边牌友的倒计时时间
	public void setLeftTime(String time){
		this.leftTime.setText(time);
	}
	//设置右边牌友的倒计时时间
	public void setRightTime(String time){
		this.rightTime.setText(time);
	}
	
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public GameState getGameState() {
		return gameState;
	}
	//设置牌友的准备情况
	public void setGameReady(String userName){
		LOGGER.info("setGameReady----"+userName);
		if(leftUserName.getText().equals(userName)){
			countDownThread.stopLeft();
			leftTime.setText("倒计时");
			leftTime.setVisible(false);
			leftReady.setVisible(false);
		}else if(rightUserName.getText().equals(userName)){
			countDownThread.stopRight();
			rightTime.setText("倒计时");
			rightTime.setVisible(false);
			rightReady.setVisible(false);
		}
	}
	public void sendDealMsg() {
		messageHandler.sendGameDealMsg(userName);
	}
	//把自己的抢地主信息通知其他2位牌友:   消息格式：username=角色=(seatNUm+1)  ； (seatNUm+1)-->意味轮到这个座位号的人抢地主
	public void sendRobMsg(String msg) {
		playerRole.setText(msg);
		playerRole.setVisible(true);
		time.setText("倒计时");
		time.setVisible(false);
		rob.setVisible(false);
		noRob.setVisible(false);
		messageHandler.sendGameRobMsg(userName+"="+msg+"="+(seatNum+1));
		if(isAllFarmer()){//主要针对最后一个抢地主的界面，而且全部是农民 就重启新的一轮准备
			restartGameReady();
		}
	}
	//发牌
	/**
	 * 0 1 2
	 * 3 4 5
	 * 6 7 8
	 * @param str
	 */
	public void dealCard(String str,int i) {
		if(i>50){//发底牌
			topCards[i-51].setText(String.valueOf(i));
			topCards[i-51].setIcon(PictureUtil.getPicture("cards/back.png"));
		}else{
			if(i%3==0){//从左边第一个位置开始发牌，相对于游戏大厅里的位置！
				if(seatNum%3 ==0){
					//getLeftPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
				}else{
					getLeftPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}else if((i-1)%3==0){//接下来发三角 顶上那个位置的人牌
				if((seatNum-1)%3 ==0){
					//getTopPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
				}else{
					getTopPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}else{//接下来发三角形右边那个人的牌
				if((seatNum+1)%3 ==0){
					//getRightPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
				}else{
					getRightPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}
		}
		//显示自己的牌
		Collections.sort(cardList);
		for(int j=0;j<cardList.size();j++) {
			cards[j].setIcon(PictureUtil.getPicture("cards/"+cardList.get(j).getImage()+".jpg"));
		}
	}
	//相对于大厅位置-找到三角形顶端那个人
	private JLabel[] getTopPlayer(){
		if((seatNum -1) % 3 ==0){
			return cards;
		}else if(seatNum % 3 == 0){
			return leftCards;
		}else{
			return rightCards;
		}
	}
	//相对于大厅位置--找到三角形左边那个人
	private JLabel[] getLeftPlayer(){
		if((seatNum -1) % 3 ==0){
			return rightCards;
		}else if(seatNum % 3 == 0){
			return cards;
		}else{
			return leftCards;
		}
	}
	//相对于大厅位置--找到三角形右边那个人
		private JLabel[] getRightPlayer(){
			if((seatNum -1) % 3 ==0){
				return leftCards;
			}else if(seatNum % 3 == 0){
				return rightCards;
			}else{
				return cards;
			}
		}
		public String getServerCards(){
			return this.saveCards;
		}
		//启动发牌
		public void startDealCards(String cards){
			this.saveCards = cards;
			gameState.pushGameState();
			gameState.handleWindow();
		}
		//启动抢地主
		public void startRob(int seat_num){
			if( seat_num == 0) {//因为抢地主是从左边第一个位置来的
				rob();
			}else if(seat_num == seatNum) {
				rob();
			}else if(seat_num == seatNum) {
				rob();
			}
		}
		private void rob() {
			rob.setVisible(true);
			noRob.setVisible(true);
			time.setVisible(true);
			gameState.pushGameState();
			gameState.handleWindow();
		}
		//设置左右边牌友的角色
		public void setOtherPlayerRole(String msg){
			if(msg != null) {
				String str[] = msg.split("=");
				String username =str[0];
				String role =str[1];
				int seat_num = Integer.valueOf(str[2]);
				if(leftUserName.getText().equals(username)) {
					leftPlayerRole.setText(role);
					leftPlayerRole.setVisible(true);
				}else if(rightUserName.getText().equals(username)){
					rightPlayerRole.setText(role);
					rightPlayerRole.setVisible(true);
				}
				if(isAllFarmer()){
					restartGameReady();
					return ;
				}
				startRob(seat_num);
			}
		}
		//判断是不是全部是农民
		private boolean isAllFarmer(){
			if(playerRole.getText().equals("农民") && leftPlayerRole.getText().equals("农民") && rightPlayerRole.getText().equals("农民")){
				return true;
			}
			return false;
		}
		//全部是农民重启新一轮游戏准备
		private void restartGameReady(){
			playerRole.setText("角色");
			leftPlayerRole.setText("角色");
			rightPlayerRole.setText("角色");
			playerRole.setVisible(false);
			leftPlayerRole.setVisible(false);
			rightPlayerRole.setVisible(false);
			gameState.pullGameState();
			gameState.pullGameState();
			gameState.pullGameState();
			gameState.handleWindow();
		}
}
