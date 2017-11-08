package com.ansatsing.landlords.client.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ansatsing.landlords.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.handler.SendMessageHandler;
import com.ansatsing.landlords.client.thread.PlayCountDownThread;
import com.ansatsing.landlords.client.thread.ReadyCountDownThread;
import com.ansatsing.landlords.client.thread.RobCountDownThread;
import com.ansatsing.landlords.entity.Card;
import com.ansatsing.landlords.state.GameDealState;
import com.ansatsing.landlords.state.GamePlayState;
import com.ansatsing.landlords.state.GameReadyState;
import com.ansatsing.landlords.state.GameRobState;
import com.ansatsing.landlords.state.GameState;
import com.ansatsing.landlords.state.GameWaitState;
import com.ansatsing.landlords.util.LandlordsUtil;
import com.ansatsing.landlords.util.PictureUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
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
	
	private JPanel southPanel;
	private JPanel actionPanel;
	private JPanel cardPanel;
	
	private JPanel leftPanel;
	private JPanel leftCardPanel;
	private JPanel leftActionPanel;
	
	private JPanel rightPanel;
	private JPanel rightCardPanel;
	private JPanel rightActionPanel;
	
	private JPanel topPanel;
	private JPanel centerPanel;
	
	private List<String> playCards = new ArrayList<String>();//存储一次要出的牌的图片地址
	private List<String> cardsIdx = new ArrayList<String>();//存储一次要出的牌的索引，针对cards变量，方便方法sendPlayCardMsg进行纸牌位置复原业务处理
	private int leftHaveCardNum = 17 ;//剩下牌张数
	private int rightHaveCardNum = 17;//剩下牌张数
	private boolean isOutCard = true;//默认是出牌的
	private JLabel centerCards[] = new JLabel[12];//最多一次只能出12张牌
	private JLabel playResult ;//斗地主输赢显示器
	private Socket socket;
	private GameLobbyWindow gameLobbyWindow;
	public static void main(String[] args) {
		//new LandlordsRoomWindow();
	}
	public LandlordsRoomWindow(JLabel seat,int seatNum,String userName,Socket socket,GameLobbyWindow gameLobbyWindow) {
		this.userName = userName;
		initGUI();
		initListener();
		this.seatNum = seatNum;
		this.seat = seat;
		this.messageHandler = new SendMessageHandler(socket);
		this.socket = socket;
		this.gameLobbyWindow = gameLobbyWindow;
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
		southPanel = new JPanel();
		southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
		 actionPanel = new JPanel();//出牌，不要，提示，抢地主，不强等动作面板
		actionPanel.setPreferredSize(new Dimension(0, 20));
		cardPanel = new JPanel();//显示纸牌的面板
		//cardPanel.setLayout(new BoxLayout(cardPanel,  BoxLayout.X_AXIS));
		cardPanel.setLayout(null);
		cardPanel.setPreferredSize(new Dimension(0, 215));
		//cardPanel.setBackground(Color.blue);
		cards = new JLabel[20];
		for(int i=0;i<20;i++){
			JLabel newJabel = new JLabel();
			newJabel.setBounds(385+(18)*i, 50, 105, 150);
		//	newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			cards[i] = newJabel;
		}
		for(int i=19;i>=0;i--){//不这步，牌的字母或者数字会被遮着
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
		 leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
		leftCardPanel = new JPanel();
		//leftCardPanel.setLayout(new BoxLayout(leftCardPanel, BoxLayout.Y_AXIS));
		leftCardPanel.setLayout(null);
		leftCardPanel.setPreferredSize(new Dimension(100, 0));
		 leftActionPanel = new JPanel();
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
		 rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
		 rightCardPanel = new JPanel();
		//rightCardPanel.setLayout(new BoxLayout(rightCardPanel, BoxLayout.Y_AXIS));
		rightCardPanel.setLayout(null);
		rightCardPanel.setPreferredSize(new Dimension(100, 0));
		 rightActionPanel = new JPanel();
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
		 topPanel = new JPanel();
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
		
		//中间
		centerPanel = new JPanel();
		centerPanel.setLayout(null);
		//centerPanel.setBackground(Color.red);
		//产生存放打出来的牌
		for(int i=0;i<12;i++){
			JLabel newJabel = new JLabel();
			newJabel.setBounds(250+20*(i), 120, 105, 150);
		//	newJabel.setIcon(PictureUtil.getPicture("cards/back.png"));
			centerCards[i] = newJabel;
			centerPanel.add(centerCards[i]);
		}
		for(int i=11;i>=0;i--){
			centerPanel.add(centerCards[i]);
		}
		playResult = new JLabel();
		playResult.setText("打牌输赢");
		playResult.setBounds(370,45,60,40);
		playResult.setVisible(false);
		centerPanel.add(playResult);
		childJpanel1.add(centerPanel);
		
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
					if(tempLabel.getName() == null){//没有显示牌的地方无需走下面的业务逻辑
						return;
					}
					int x = tempLabel.getX();
					int y = tempLabel.getY();
				//	System.out.println("点击指派时tempLabel.getName()==="+tempLabel.getName());
					String strArr[] = tempLabel.getName().split("=");
					String cardIdx = strArr[0];
					String imageSrc = strArr[1];
					if(y < 50){
						y += 20;
						cardsIdx.remove(cardIdx);
						playCards.remove(imageSrc);
					}else{
						y -= 20;
						cardsIdx.add(cardIdx);
						playCards.add(imageSrc);
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
					////////////////////////////////////////////
					ChatMsgProt chatMsgProt = new ChatMsgProt(2,userName,sendMsg.getText().trim(),socket);
					chatMsgProt.sendMsg();
					setHistoryMsg("你说:" + sendMsg.getText().trim());
					/*String msg = sendMsg.getText().trim();
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
					}*/
					////////////////////////////////////////////////
				}
			}
			
		});
		//准备事件
		ready.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				/////////////////////////////////
				GameReadyProt gameReadyProt = new GameReadyProt(1,seatNum,socket);
				gameReadyProt.sendMsg();
				//messageHandler.sendGameReadyMsg("1");//1代表准备好
				/////////////////////////////////////
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
		//出牌事件
		out.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isOutCard = true;
				playCountDown.stop(5);
			}
		});
		//不出牌事件
		donot.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				isOutCard = false;
				playCountDown.stop(5);
			}
		});
	}
	public void closeRoom() {
		if(gameState instanceof GamePlayState || gameState instanceof GameDealState || gameState instanceof GameRobState) {
			JOptionPane.showMessageDialog(null, "斗地主中不许中途退出！！", "信息警告", JOptionPane.WARNING_MESSAGE);
			return;
		}
		///////////////////////////////////////////////////////////
		ExitSeatProt exitSeatProt = new ExitSeatProt(seatNum,userName,socket);
		exitSeatProt.sendMsg();
		gameLobbyWindow.setLandlordsRoomtoNull();
		//messageHandler.sendExitSeatMsg(String.valueOf(seatNum));
		/////////////////////////////////////////////////////////////
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
	}
	public void setRoomSeat(int _seatNum,String userName,int readFlag){
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
	public void setGameReady(int seat_num,int readFlag){
		LOGGER.info("setGameReady----"+userName);
		//String str[] = msg.split("=");
		//int seat_num = Integer.parseInt(str[0]);
		//int readFlag = Integer.parseInt(str[1]);
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
			if(leftPlayerRole.getText().equals("地主")){
				leftPlayerRole.setText("农民");
			}
			if(rightPlayerRole.getText().equals("地主")){
				rightPlayerRole.setText("农民");
			}
		}
		playerRole.setText(msg.equals("1")?"农民":"地主");
		playerRole.setVisible(true);
		time.setText("倒计时");
		time.setVisible(false);
		rob.setVisible(false);
		noRob.setVisible(false);
		///////////////////////////////////////////////////////
		GameRoleProt gameRoleProt = new  GameRoleProt( (seatNum+1),  userName,  Integer.parseInt(msg),  socket);
		gameRoleProt.sendMsg();
		//messageHandler.sendGameRobMsg(userName+"="+msg+"="+(seatNum+1));
		////////////////////////////////////////////////////////
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
			topCards[i-51].setName(String.valueOf(i));
			topCards[i-51].setIcon(PictureUtil.getPicture("cards/back.png"));
		}else{
			if(i%3==0){//从左边第一个位置开始发牌，相对于游戏大厅里的位置！
				if(seatNum%3 ==0){
					//getLeftPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
					sortCardAndShow();
				}else{
					getLeftPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}else if((i-1)%3==0){//接下来发三角 顶上那个位置的人牌
				if((seatNum-1)%3 ==0){
					//getTopPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
					sortCardAndShow();
				}else{
					getTopPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}else{//接下来发三角形右边那个人的牌
				if((seatNum+1)%3 ==0){
					//getRightPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/"+str+".jpg"));
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(str)));
					sortCardAndShow();
				}else{
					getRightPlayer()[i/3].setIcon(PictureUtil.getPicture("cards/back.png"));
				}
			}
		}
	}
	//排牌并显示牌
	private void sortCardAndShow() {
		//显示自己的牌
				Collections.sort(cardList);
			//	System.out.println("每发一张牌时：cardList.size()==="+cardList.size());
				for(int j=0;j<cardList.size();j++) {
					cards[j].setName(j+"="+cardList.get(j).getImage());//下标索引=纸牌图片地址索引，这样设计以便出牌完纸牌位置复原，主要针对方法sendPlayCardMsg
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
			//gameState.pushGameState();
			gameState = new GameRobState(this);
			gameState.handleWindow();
		}
		//设置左右边牌友的角色
		public void setOtherPlayerRole(String username,String role,int seat_num){
				if(leftUserName.getText().equals(username)) {
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(rightPlayerRole.getText().equals("地主")) {
							rightPlayerRole.setText("农民");
						}
					}
					leftPlayerRole.setText(role.equals("1")?"农民":"地主");
					leftPlayerRole.setVisible(true);
				}else if(rightUserName.getText().equals(username)){
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(leftPlayerRole.getText().equals("地主")) {
							leftPlayerRole.setText("农民");
						}

					}
					rightPlayerRole.setText(role.equals("1")?"农民":"地主");
					rightPlayerRole.setVisible(true);
				}
				/*if(isAllFarmer()){
					restartGameReady();
					return ;
				}*/
				startRob(seat_num);
		}
		public void setOtherPlayerRole(String msg){
			if(msg != null) {
				String str[] = msg.split("=");
				String username =str[0];
				String role =str[1];
				int seat_num = Integer.valueOf(str[2]);
				if(leftUserName.getText().equals(username)) {
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(rightPlayerRole.getText().equals("地主")) {
							rightPlayerRole.setText("农民");
						}						
					}
					leftPlayerRole.setText(role.equals("1")?"农民":"地主");
					leftPlayerRole.setVisible(true);
				}else if(rightUserName.getText().equals(username)){
					if(role.equals("2")) {//地主被后面的人抢了 那之前的地主角色就要变成农民
						if(playerRole.getText().equals("地主")) {
							playerRole.setText("农民");
						}
						if(leftPlayerRole.getText().equals("地主")) {
							leftPlayerRole.setText("农民");
						}
						
					}
					rightPlayerRole.setText(role.equals("1")?"农民":"地主");
					rightPlayerRole.setVisible(true);
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
				//LOGGER.info("11GameReadyStateGameReadyStateGameReadyStateGameReadyStateGameReadyStateGa");
			}else if(gameState instanceof GameDealState) {
				//LOGGER.info("11GameDealStateGameDealStateGameDealStateGameDealStateGameDealState");
			}
			gameState.pullGameState();
			gameState.pullGameState();
			if(gameState instanceof GameReadyState) {
				//LOGGER.info("22GameReadyStateGameReadyStateGameReadyStateGameReadyStateGameReadyStateGa");
			}else if(gameState instanceof GameDealState) {
				//LOGGER.info("22GameDealStateGameDealStateGameDealStateGameDealStateGameDealState");
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
			/*this.saveCards = cards;
			gameState.pushGameState();
			gameState.handleWindow();*/
			List<String> card = Splitter.on(",").splitToList(cards);
			for(int i=0;i<card.size();i++){
				try {
					dealCard(card.get(i),i);
					TimeUnit.MILLISECONDS.sleep(150);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(seatNum % 3 == 0) {//从左边位置开始轮流抢地主
				this.hideAllReadyLable();
				startRob(seatNum);
			}
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
		public void startGamePlayThread(String msg) {
			int seat_num = Integer.parseInt(msg);
			for(int i=0;i<topCards.length;i++) {
				String imageSrc = topCards[i].getName();
				topCards[i].setIcon(PictureUtil.getPicture("cards/"+imageSrc+".jpg"));
				if(seat_num == seatNum) {
					cardList.add(LandlordsUtil.generateCard(Integer.valueOf(imageSrc)));
				}
			}
			
			if(seat_num == seatNum) {
				donot.setVisible(false);
				out.setVisible(true);
				time.setVisible(true);
				sortCardAndShow();
				gameState = new GamePlayState(this);
				gameState.handleWindow();
			}else {//非地主界面 要把3张底牌加到地主位置
				if(isLeftPlayer(seat_num)) {
					leftCards[17].setIcon(PictureUtil.getPicture("cards/back.png"));
					leftCards[18].setIcon(PictureUtil.getPicture("cards/back.png"));
					leftCards[19].setIcon(PictureUtil.getPicture("cards/back.png"));
					leftHaveCardNum +=3;
				}else {
					rightCards[17].setIcon(PictureUtil.getPicture("cards/back.png"));
					rightCards[18].setIcon(PictureUtil.getPicture("cards/back.png"));
					rightCards[19].setIcon(PictureUtil.getPicture("cards/back.png"));
					rightHaveCardNum += 3;
				}
			}
		}
		public void setPlayCountDownThread(PlayCountDownThread dealCardsThread) {
			this.playCountDown = dealCardsThread;
		}
		public void sendPlayCardMsg(boolean timeoutFLag) {
			int seat_num = -1;
			if((seatNum+1)%3==0){
				seat_num = seatNum -2;
			}else{
				seat_num = seatNum +1;
			}
			String msg = null;
			if(timeoutFLag){//超时出牌，就出右手边第一张牌;因为不出牌也是通过发送时间为0来通知结束线程的，所以不出牌的逻辑也在下面
				if(!isOutCard){
					msg = "-1";//代表不出牌
				}else{
					int idex = cardList.size()-1;
					//System.out.println("时间超时自动出最右边第一张牌："+cards[idex].getName());
					String strArr[] = cards[idex].getName().split("=");
					msg = strArr[1];
					cards[idex].setIcon(null);
					cards[idex].setName("-1");//代表牌出了
					cards[idex].removeMouseListener(cards[idex].getMouseListeners()[0]);//移除绑定鼠标事件
					System.out.println("最右边第一张牌的地址数字："+msg);
					//cards[i].setIcon(PictureUtil.getPicture("cards/"+cardList.get(i).getImage()+".jpg"));
					centerCards[5].setIcon(PictureUtil.getPicture("cards/"+msg+".jpg"));
					cardList.remove(idex);
				}
			}else{//非超时出牌
				if(isOutCard){
					//System.out.println("playCards:=====>>>"+playCards.size());
					msg = Joiner.on(",").join(playCards);
				//	System.out.println("正常出牌："+msg);
					//1从cardList移除打出的牌
					/*for(int i=0;i<playCards.size();i++) {//删除要出的牌
						*//*if(cardList.contains(LandlordsUtil.generateCard(Integer.parseInt(playCards.get(i))))){//这下面的代码是不会执行，根本就没有包含这样的对象
							cardList.remove(LandlordsUtil.generateCard(Integer.parseInt(playCards.get(i))));
						}*//*
					}*/
					Iterator<Card> iterator = cardList.iterator();
					while(iterator.hasNext()){
						Card card = iterator.next();
						String imageSrc = card.getImage();
						for(int i=0;i<playCards.size();i++) {
							if(imageSrc.equals(playCards.get(i))){
								iterator.remove();
								break;
							}
						}
					}
					System.out.println("余下牌数："+cardList.size());
					System.out.println("cards.length："+cards.length);
					//2上移的牌位置复原
					for(int i=0;i<cardsIdx.size();i++) {
						int idx = Integer.parseInt(cardsIdx.get(i));
						JLabel tempLabel = cards[idx];
						int x = tempLabel.getX();
						int y = tempLabel.getY()+20;
						tempLabel.setBounds(x, y, 105, 150);
					}
					//3从显示牌界面移除已经出的牌
					for(int i=0;i<cards.length;i++){
						if(i>=cardList.size()){//针对已经出了的牌的显示背景图片设置为null
							cards[i].setName("-1");
							cards[i].setIcon(null);
							if(cards[i].getMouseListeners().length > 0){
								cards[i].removeMouseListener(cards[i].getMouseListeners()[0]);//移除绑定鼠标事件
							}
						}else{//余下的牌
							cards[i].setName(i+"="+cardList.get(i).getImage());
							cards[i].setIcon(PictureUtil.getPicture("cards/"+cardList.get(i).getImage()+".jpg"));
						}
					}
					//3中间面板显示自己出的牌
				}else{
					msg = "-1";//代表不出牌
				}
				showCenterCards();
			}
			//牌全部出完，您就赢了并且结束本轮游戏，发起本轮游戏结束信号给服务器端
			if(cardList.size() == 0){
				playResult.setText("您赢了！");
				seat_num = -1;//作为告诉其他牌友本来游戏结束的信号
			}
			////////////////////////////////////////////////////
			boolean isLandord = playerRole.equals("地主")?true:false;
			PlayCardProt playCardProt = new PlayCardProt(isLandord,seat_num,msg,socket);
			playCardProt.sendMsg();
			//messageHandler.sendPlayCardMsg(msg+"="+seat_num);
			///////////////////////////////////////////////////

			out.setVisible(false);
			donot.setVisible(false);
			time.setText("倒计时");
			time.setVisible(false);
			playCards.clear();
			cardsIdx.clear();
			if(cardList.size() == 0){
				///////////////////////////////////////////////////
				GameOverProt gameOverProt = new GameOverProt(socket);
				gameOverProt.sendMsg();
				//messageHandler.sendGameOverMsg(userName+"="+seatNum);
				//////////////////////////////////////////
				hideRole();
				playResult.setVisible(true);
				playResult.setText("您赢了！");
				startGameReadyThread(true);
			}
		}
		//显示上一个人出的牌 以及启动自己出牌线程
		public void showCardAndPlayCard(String showCard,int seat_num,boolean isLandlord) {
			//String str[] = msg.split("=");
			//String showCard = str[0];
			//int seat_num = Integer.parseInt(str[1]);
			if(!showCard.equals("-1")){//上家出牌与不出牌
				playCards.clear();
				playCards = new ArrayList<String>(Splitter.on(",").splitToList(showCard));
				//1从他自己的界面移除上家出了的牌
				if(isLeftPlayer(seat_num -1)){
					for(int i= leftHaveCardNum -1;i>=leftHaveCardNum-playCards.size();i--){
						leftCards[i].setIcon(null);
					}
					leftHaveCardNum -=playCards.size();
				}else{
					for(int i= rightHaveCardNum -1;i>=rightHaveCardNum-playCards.size();i--){
						leftCards[i].setIcon(null);
					}
					rightHaveCardNum -=playCards.size();
				}
				//2清理中间面板之前的显示
				for(int i=0;i<centerCards.length;i++){
					centerCards[i].setIcon(null);
				}
				//3在中间面板显示上家出的牌
				showCenterCards();
			}
			//3启动本家出牌线程
			if(seat_num == seatNum){
				time.setVisible(true);
				time.setText("倒计时");
				out.setVisible(true);
				donot.setVisible(true);
				gameState = new GamePlayState(this);
				gameState.handleWindow();
			}
			playCards.clear();
			//游戏结束,发送信号
			if(seat_num == -1){
				///////////////////////////////////////////////////
				GameOverProt gameOverProt = new GameOverProt(socket);
				gameOverProt.sendMsg();
				//messageHandler.sendGameOverMsg(userName+"="+seatNum);
				//////////////////////////////////////////
				boolean youAreLand = playerRole.equals("地主")?true:false;
				if(!isLandlord ){
					if(youAreLand){
						playResult.setText("您输了！");
					}else{
						playResult.setText("您赢了！");
					}
				}else{
					playResult.setText("您输了！");
				}
				playResult.setVisible(true);
				hideRole();
				startGameReadyThread(true);
			}
		}

		public void showCardAndPlayCard(String msg) {
			String str[] = msg.split("=");
			String showCard = str[0];
			int seat_num = Integer.parseInt(str[1]);
			if(!showCard.equals("-1")){//上家出牌与不出牌
				playCards.clear();
				playCards = new ArrayList<String>(Splitter.on(",").splitToList(showCard));
				//1从他自己的界面移除上家出了的牌
				if(isLeftPlayer(seat_num -1)){
					for(int i= leftHaveCardNum -1;i>=leftHaveCardNum-playCards.size();i--){
						leftCards[i].setIcon(null);
					}
					leftHaveCardNum -=playCards.size();
				}else{
					for(int i= rightHaveCardNum -1;i>=rightHaveCardNum-playCards.size();i--){
						leftCards[i].setIcon(null);
					}
					rightHaveCardNum -=playCards.size();
				}
				//2清理中间面板之前的显示
				for(int i=0;i<centerCards.length;i++){
					centerCards[i].setIcon(null);
				}
				//3在中间面板显示上家出的牌
				showCenterCards();
			}
			//3启动本家出牌线程
			if(seat_num == seatNum){
				time.setVisible(true);
				time.setText("倒计时");
				out.setVisible(true);
				donot.setVisible(true);
				gameState = new GamePlayState(this);
				gameState.handleWindow();
			}
			playCards.clear();
			//游戏结束,发送信号
			if(seat_num == -1){
				messageHandler.sendGameDealMsg(userName+"="+seatNum);
				hideRole();
				startGameReadyThread(true);
			}
		}
		//显示中间的牌
	private void showCenterCards(){
		int startIdx=(12-playCards.size())/2;//为了显示的尽量在中间
		for(int i=startIdx;i<12;i++) {
			if(i<startIdx+playCards.size())
			centerCards[i].setIcon(PictureUtil.getPicture("cards/"+playCards.get(i-startIdx)+".jpg"));
			else
				break;
		}
	}
	//隐藏角色label
	private void hideRole(){
		leftPlayerRole.setText("角色");
		leftPlayerRole.setVisible(false);
		rightPlayerRole.setText("角色");
		rightPlayerRole.setVisible(false);
		playerRole.setText("角色");
		playerRole.setVisible(false);
	}
	//显示准备界面
	private void showReadyLabel(){
		leftReady.setText("请准备");
		leftReady.setVisible(true);
		rightReady.setText("请准备");
		rightReady.setVisible(true);
		ready.setText("请准备");
		ready.setVisible(true);
		time.setText("倒计时");
		leftTime.setText("倒计时");
		rightTime.setText("倒计时");
		time.setVisible(true);
		leftTime.setVisible(true);
		rightTime.setVisible(true);
	}
}
