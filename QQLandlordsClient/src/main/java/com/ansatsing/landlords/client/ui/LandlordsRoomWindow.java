package com.ansatsing.landlords.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.handler.SendMessageHandler;
import com.ansatsing.landlords.client.thread.PlayCountDownThread;
import com.ansatsing.landlords.client.thread.ReadyCountDownThread;
import com.ansatsing.landlords.client.thread.RobCountDownThread;
import com.ansatsing.landlords.entity.Card;
import com.ansatsing.landlords.state.GameDealState;
import com.ansatsing.landlords.state.GameOverState;
import com.ansatsing.landlords.state.GamePlayState;
import com.ansatsing.landlords.state.GameReadyState;
import com.ansatsing.landlords.state.GameRobState;
import com.ansatsing.landlords.state.GameState;
import com.ansatsing.landlords.state.GameWaitState;
import com.ansatsing.landlords.util.LandlordsUtil;
import com.ansatsing.landlords.util.PictureUtil;
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
	private PlayCountDownThread playCountDown;
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
		setResizable(true);// 宽高度固死
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
		JPanel actionPanel = new JPanel();//出牌，不要，提示，抢地主，不强等动作面板
		actionPanel.setPreferredSize(new Dimension(0, 20));
		JPanel cardPanel = new JPanel();//显示纸牌的面板
		//cardPanel.setLayout(new BoxLayout(cardPanel,  BoxLayout.X_AXIS));
		cardPanel.setLayout(null);
		cardPanel.setPreferredSize(new Dimension(0, 215));
		//cardPanel.setBackground(Color.blue);
		cards = new JLabel[20];
		for(int i=0;i<20;i++){
			JLabel newJabel = new JLabel();
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
				messageHandler.sendGameReadyMsg("1");//1代表准备好
				if(countDownThread != null)
				countDownThread.stop();
				ready.setText("已准备");
				time.setText("倒计时");
				time.setVisible(false);
			}
			
		});
		//抢地主事件
		rob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(robDownThread !=null)
				robDownThread.stop(5);
				//sendRobMsg("2");
			}
		});
		//不抢地主事件
		noRob.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(robDownThread !=null){
					System.out.println("88888888888888888  	robDownThread.stop();");
					robDownThread.stop(0);
				}
				
				//sendRobMsg("1");
			}
		});
	}
	public void closeRoom() {
		if(gameState instanceof GamePlayState || gameState instanceof GameDealState || gameState instanceof GameRobState) {
			JOptionPane.showMessageDialog(null, "斗地主中不许中途退出！！", "信息警告", JOptionPane.WARNING_MESSAGE);
		}
		messageHandler.sendExitSeatMsg(String.valueOf(seatNum));
		seat.setText("空位");
		if(countDownThread != null){
			//countDownThread.stop();//关闭准备倒计时线程
			countDownThread.notAllSitted();
		}
		//if(robDownThread != null) robDownThread.stop();//关闭抢地主线程
		dispose();
	}
	public int getSeatNum() {
		return seatNum;
	}
	/**
	 * 实现了斗地主房间位置跟游戏大厅的位置顺序一致
	 * msg    --->  ansatsing=3=readFlag
	 *   1
	 * 0   2 
	 *   4
	 * 3   5
	 * @param msg
	 */
	public void setSeatUserName(String msg){
	//	LOGGER.info(userName+"入座情况》》》》》》》》》》》》"+msg);
		//int idx = msg.indexOf("=");
		String[] str=msg.split("=");
		String userName = str[0];
		//String seat_num = str[1];
		int readFlag = Integer.parseInt(str[2]);
		//messageHandler.sendAddSocketMsg(userName);
		int _seatNum = Integer.parseInt(str[1]);
		if(_seatNum == LandlordsUtil.getLeftSeatNum(seatNum)) {
			leftUserName.setText(userName);
			if(readFlag == 1) {
				leftReady.setText("已准备");
				
				leftTime.setVisible(false);
			}else{
				leftReady.setText("请准备");
				leftTime.setVisible(true);
			}
			leftReady.setVisible(true);
		}else if(_seatNum == LandlordsUtil.getRightSeatNum(seatNum)) {
			rightUserName.setText(userName);
			if(readFlag == 1) {
				rightReady.setText("已准备");
				rightTime.setVisible(false);
			}else{
				rightReady.setText("请准备");
				rightTime.setVisible(true);
			}
			rightReady.setVisible(true);
		}
		/*if(!leftUserName.getText().equals("空位") && !rightUserName.getText().equals("空位")){
			gameState.pushGameState();
			System.out.println("setSeatUserNamesetSeatUserName   gameState  "+gameState.getClass().getName());
			gameState.handleWindow();
		}*/
	}
	public void startGameReadyThread(boolean restart){
		if(restart) {
			//界面恢复到准备状态的界面，主要是针对 都不当地主进入下一轮的情况
			playerRole.setText("角色");
			leftPlayerRole.setText("角色");
			rightPlayerRole.setText("角色");
			playerRole.setVisible(false);
			leftPlayerRole.setVisible(false);
			rightPlayerRole.setVisible(false);
			for(int i=0;i<topCards.length;i++) {
				topCards[i].setIcon(null);
			}
			for(int i=0;i<leftCards.length;i++) {
				leftCards[i].setIcon(null);
			}
			for(int i=0;i<rightCards.length;i++) {
				rightCards[i].setIcon(null);
			}
			for(int i=0;i<cards.length;i++) {
				cards[i].setIcon(null);
			}
			cardList.clear();
			time.setVisible(true);
			leftTime.setVisible(true);
			rightTime.setVisible(true);
			ready.setVisible(true);
			leftReady.setVisible(true);
			rightReady.setVisible(true);
		//把不准备的消息发送给牌友
			leftReady.setText("请准备");
			rightReady.setText("请准备");
			ready.setText("请准备");
			
		}
		
		gameState = new GameReadyState(this);
		gameState.handleWindow();
	}
	public void emptySeat(String tempMsg) {
		if(leftUserName.getText().trim().equals(tempMsg)){
		//	countDownThread.stopLeft();
			leftUserName.setText("空位");
			leftReady.setText("请准备");
			leftReady.setVisible(false);
		}else if(rightUserName.getText().trim().equals(tempMsg)){
		//	countDownThread.stopRight();
			rightUserName.setText("空位");
			rightReady.setText("请准备");
			rightReady.setVisible(false);
		}
		leftTime.setText("倒计时");
		rightTime.setText("倒计时");
		if(countDownThread != null){
			countDownThread.notAllSitted();
		}
		this.time.setText("倒计时");
		//如果在准备状态时 一人退出游戏，则留下的牌友必须状态复原到游戏等待状态
		if(gameState instanceof GameReadyState){
			gameState.pullGameState();
		}
		//如果
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
	public void setGameReady(String msg){
		LOGGER.info("setGameReady----"+userName);
		String str[] = msg.split("=");
		int seat_num = Integer.parseInt(str[0]);
		int readFlag = Integer.parseInt(str[1]);
		if(isLeftPlayer(seat_num)) {
			if(readFlag == 1) {
				if(countDownThread != null) {
					countDownThread.stopLeft();
				}
				leftTime.setText("倒计时");
				leftTime.setVisible(false);
				leftReady.setText("已准备");
				leftReady.setVisible(true);
			}else if(readFlag == 0) {
				leftTime.setText("倒计时");
				leftTime.setVisible(true);
				leftReady.setText("请准备");
				leftReady.setVisible(true);
			}
		}else {
			if(readFlag == 1) {
				if(countDownThread != null)
					countDownThread.stopRight();
				rightTime.setText("倒计时");
				rightTime.setVisible(false);
				rightReady.setText("已准备");
				rightReady.setVisible(true);
			}else if(readFlag == 0) {
				rightTime.setText("倒计时");
				rightTime.setVisible(true);
				rightReady.setText("请准备");
				rightReady.setVisible(true);
			}
		}
	}
	public void sendDealMsg() {
		messageHandler.sendGameDealMsg(userName);
	}
	//把自己的抢地主信息通知其他2位牌友:   消息格式：username=角色=(seatNUm+1)  ； (seatNUm+1)-->意味轮到这个座位号的人抢地主
	public void sendRobMsg(String msg) {
		if(msg.equals("2")){//把其他地主更新为农民
			if(leftPlayerRole.equals("地主")){
				leftPlayerRole.setText("农民");
			}
			if(rightPlayerRole.equals("地主")){
				rightPlayerRole.setText("农民");
			}
		}
		playerRole.setText(msg.equals("1")?"农民":"地主");
		playerRole.setVisible(true);
		time.setText("倒计时");
		time.setVisible(false);
		rob.setVisible(false);
		noRob.setVisible(false);
		messageHandler.sendGameRobMsg(userName+"="+msg+"="+(seatNum+1));
		LOGGER.info(userName+"========="+(seatNum+1));
/*		if(isAllFarmer()){//主要针对最后一个抢地主的界面，而且全部是农民 就重启新的一轮准备
			//gameState.pullGameState();//针对最后一个抢地主的
			restartGameReady();
		}*/
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
			//topCards[i-51].setText(String.valueOf(i));
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
	//接受的其他牌友发来的信号时，首先判断是左边牌友还是右边牌友[是相对于自己的位置]
	private boolean isLeftPlayer(int seat_num) {
		if((seatNum -1) % 3 ==0 && (seat_num+1)%3==0){
			return true;
		}else if(seatNum % 3 == 0 && (seat_num -1)%3 ==0){
			return true;
		}else if((seatNum +1)%3==0&&seat_num%3 == 0){
			return true;
		}else {
			return false;
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
			leftReady.setText("请准备");
			leftReady.setVisible(false);
			rightReady.setText("请准备");
			rightReady.setVisible(false);
			ready.setText("请准备");
			ready.setVisible(false);
			gameState.pushGameState();
			gameState.handleWindow();
		}
		//启动抢地主
		public void startRob(int seat_num){
			if(seat_num == seatNum) {
				rob();
				LOGGER.info(userName+" *****rob()*****  "+seat_num);
			}
		}
		private void rob() {
			rob.setVisible(true);
			noRob.setVisible(true);
			time.setVisible(true);
			ready.setText("请准备");
			ready.setVisible(false);
			leftReady.setText("请准备");
			leftReady.setVisible(false);
			rightReady.setText("请准备");
			rightReady.setVisible(false);
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
					leftPlayerRole.setText(role.equals("1")?"农民":"地主");
					leftPlayerRole.setVisible(true);
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(rightPlayerRole.getText().equals("地主")) {
							rightPlayerRole.setText("农民");
						}						
					}
				}else if(rightUserName.getText().equals(username)){
					rightPlayerRole.setText(role.equals("1")?"农民":"地主");
					rightPlayerRole.setVisible(true);
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(leftPlayerRole.getText().equals("地主")) {
							leftPlayerRole.setText("农民");
						}
						
					}
				}
				/*if(isAllFarmer()){
					restartGameReady();
					return ;
				}*/
				startRob(seat_num);
			}
		}
		//判断是不是全部是农民
		public boolean isAllFarmer(){
			if(playerRole.getText().equals("农民") && leftPlayerRole.getText().equals("农民") && rightPlayerRole.getText().equals("农民")){
				return true;
			}
			return false;
		}
		//全部是农民重启新一轮游戏准备
		public void restartGameReady(){
			//要把未准备的信息 告知服务器端，因为新的一轮 每个人的准备状态信息都是未准备
			messageHandler.sendGameReadyMsg("0");
			playerRole.setText("角色");
			leftPlayerRole.setText("角色");
			rightPlayerRole.setText("角色");
			playerRole.setVisible(false);
			leftPlayerRole.setVisible(false);
			rightPlayerRole.setVisible(false);
			for(int i=0;i<topCards.length;i++) {
				topCards[i].setIcon(null);
			}
			for(int i=0;i<leftCards.length;i++) {
				leftCards[i].setIcon(null);
			}
			for(int i=0;i<rightCards.length;i++) {
				rightCards[i].setIcon(null);
			}
			for(int i=0;i<cards.length;i++) {
				cards[i].setIcon(null);
			}
			cardList.clear();
			time.setVisible(true);
			leftTime.setVisible(true);
			rightTime.setVisible(true);
			ready.setVisible(true);
			leftReady.setVisible(true);
			rightReady.setVisible(true);
		//把不准备的消息发送给牌友
			leftReady.setText("请准备");
			rightReady.setText("请准备");
			ready.setText("请准备");
			if(gameState instanceof GameReadyState) {
				LOGGER.info("11GameReadyStateGameReadyStateGameReadyStateGameReadyStateGameReadyStateGa");
			}else if(gameState instanceof GameDealState) {
				LOGGER.info("11GameDealStateGameDealStateGameDealStateGameDealStateGameDealState");
			}
			gameState.pullGameState();
			gameState.pullGameState();
			if(gameState instanceof GameReadyState) {
				LOGGER.info("22GameReadyStateGameReadyStateGameReadyStateGameReadyStateGameReadyStateGa");
			}else if(gameState instanceof GameDealState) {
				LOGGER.info("22GameDealStateGameDealStateGameDealStateGameDealStateGameDealState");
			}
			//gameState.pullGameState();
			gameState.handleWindow();
		}
		//用于开启准备线程时判断牌友是否已经提前准备好了
		public boolean leftIsReady() {
			if(leftReady.getText().equals("已准备")) {
				return true;
			}else {
				return false;
			}
		}
		//用于开启准备线程时判断牌友是否已经提前准备好了
		public boolean rightIsReady() {
			if(rightReady.getText().equals("已准备")) {
				return true;
			}else {
				return false;
			}
		}
		public boolean isReady() {
			if(ready.getText().equals("已准备")) {
				return true;
			}else {
				return false;
			}
		}
		public void setRobDownThread(RobCountDownThread robDownThread) {
			this.robDownThread = robDownThread;
		}
		//启动发牌线程
		public void startGameDealThread(String cards) {
			this.saveCards = cards;
			/*leftReady.setText("请准备");
			leftReady.setVisible(false);
			rightReady.setText("请准备");
			rightReady.setVisible(false);
			ready.setText("请准备");
			ready.setVisible(false);*/
			gameState.pushGameState();
			gameState.handleWindow();
		}
		//牌发完就隐藏所有准备lable
		public void hideAllReadyLable() {
			ready.setText("请准备");
			ready.setVisible(false);
			leftReady.setText("请准备");
			rightReady.setText("请准备");
			leftReady.setVisible(false);
			rightReady.setVisible(false);
		}
		public void startGamePlayThread() {
			//if(playerRole.getText().equals("地主")){
				donot.setVisible(false);
			//}
			out.setVisible(true);
			time.setVisible(true);
		}
		public void setPlayCountDownThread(PlayCountDownThread dealCardsThread) {
			this.playCountDown = dealCardsThread;
		}
		public void sendPlayCardMsg(boolean timeoutFLag) {
			int seat_num = seatNum +1;
			if(timeoutFLag){
				//List<JLabel> ss = new ArrayList<JLabel>();
				String msg = cards[cards.length-1].getText()+"="+seat_num;
				cards[cards.length-1].setIcon(null);
				messageHandler.sendPlayCardMsg(msg);
			}else{
				
			}
		}
}
