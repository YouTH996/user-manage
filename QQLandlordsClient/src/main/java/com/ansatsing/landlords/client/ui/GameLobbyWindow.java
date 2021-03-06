package com.ansatsing.landlords.client.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.ansatsing.landlords.client.Context;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ansatsing.landlords.client.handler.SendMessageHandler;
import com.ansatsing.landlords.client.thread.BioSocketThread;

/**
 * QQ斗地主游戏大厅
 *
 * @author ansatsing
 * @time 2017年10月20日 下午8:14:18
 */
public class GameLobbyWindow extends JFrame {
	private final static Logger LOGGER = LoggerFactory.getLogger(GameLobbyWindow.class);
	private  int WIDTH = 1500;
	private  int HEIGHT = 800;
	private JPanel childJpanel1;// 主窗体左侧
	private JPanel childJpanel2;// 主窗体右侧
	private JScrollPane historyScroll;
	/** 历史消息滚动条 */
	public JTextArea historyMsg;
	/** 历史消息区域 */
	private JPanel sendPanel;
	private JTextArea sendMsg;
	/** 输入消息区域 */
	private JButton send;
	private BioSocketThread qqClientHandler;
	private JLabel seats[]; // 斗地主座位
	// private JButton seats[]; //斗地主座位
	private final int TOTAL = 30;// 总共多少桌
	private String userName;// 网名
	private LandlordsRoomWindow currentRoom;
	private SendMessageHandler messageHandler;
	private Player player;
	private  Context context;
	/*
	 * public static void main(String[] args) { Socket socket = null;
	 * QQGameWindow qqGameWindow = new QQGameWindow(socket); }
	 */

	public GameLobbyWindow(Player player,Context context) {
		this.userName = player.getUserName();
		setTitle("QQ斗地主--" + userName);
		initGUI();
		initListener();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		/////////////////////////////////////////////////////////////////
		//this.qqClientHandler = qqClientHandler;
		//this.qqClientHandler = qqClientHandler;
		this.player = player;
		this.context = context;
	}

	private void initGUI() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		WIDTH = screenSize.width;
		HEIGHT = screenSize.height - 30;
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);// 居中显示
		setResizable(false);// 宽高度固死
		BorderLayout lay = new BorderLayout();// 东南西北中 布局

		setLayout(lay);

		childJpanel1 = new JPanel() {

			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				int _x = 0;// 记录x坐标到底怎么加
				int rowNum = 1;// 记录行数
				int colNum = 6;// 记录列数
				for (int i = 0; i < TOTAL; i++) {
					int x[] = { 30 + (i - _x) * 160, 80 + (i - _x) * 160, 130 + (i - _x) * 160 };
					int y[] = { 111 + (rowNum - 1) * 150, 30 + (rowNum - 1) * 150, 111 + (rowNum - 1) * 150 };
					Polygon polygon = new Polygon(x, y, 3);
					g.setColor(Color.orange);
					g.fillPolygon(polygon);
					if ((i + 1) % colNum == 0) {
						_x = rowNum * colNum;
						rowNum++;
					}
				}
			}

		};
		childJpanel1.setLayout(null);
		// childJpanel1.setPreferredSize(new Dimension(1000, 0));
		// childJpanel1.setBackground(Color.darkGray);

		// 位置
		seats = new JLabel[TOTAL * 3];
		// seats = new JButton[TOTAL*3];
		int _x = 0;// 记录x坐标到底怎么加
		int rowNum = 1;// 记录行数
		int colNum = 6;// 记录列数
		for (int i = 0; i < TOTAL; i++) {
			int x[] = { 30 + (i - _x) * 160, 80 + (i - _x) * 160, 130 + (i - _x) * 160 };
			int y[] = { 111 + (rowNum - 1) * 150, 30 + (rowNum - 1) * 150, 111 + (rowNum - 1) * 150 };
			for (int j = 0; j < 3; j++) {
				seats[j + i * 3] = new JLabel("空位");
				seats[j + i * 3].setBounds(x[j], y[j], 40, 20);
				childJpanel1.add(seats[j + i * 3]);
			}
			if ((i + 1) % colNum == 0) {
				_x = rowNum * colNum;
				rowNum++;
			}
		}

		childJpanel2 = new JPanel();
		childJpanel2.setPreferredSize(new Dimension(500, 0));
		childJpanel2.setBackground(Color.GRAY);

		this.getContentPane().add(childJpanel1, "Center");
		this.getContentPane().add(childJpanel2, "East");

		// 历史记录
		historyMsg = new JTextArea(35, 42);
		historyMsg.setEditable(false);
		historyScroll = new JScrollPane(historyMsg);
		childJpanel2.add(historyScroll);

		// 发送消息区
		sendPanel = new JPanel();
		sendMsg = new JTextArea(11, 35);
		sendPanel.add(sendMsg);
		send = new JButton("发送");
		send.setBounds(460, 2, 30, 50);
		sendPanel.add(send);
		childJpanel2.add(sendPanel);

		// seats[20].setText("ccdfas");
		// setSeatName(21, "kkkk");
	}

	private void initListener() {
		// 窗体即将关闭事件
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				if(currentRoom != null) {
					closeLandlordsRoom();
				}
				//messageHandler.sendSystemExitMsg();
				AbstractProtocol systemExitProt = new SystemExitProt(userName);
				systemExitProt.setPlayer(player);
				systemExitProt.sendMsg();
				//qqClientHandler.stop();// 收信息的线程停止
				if(player.getSocket() != null){
					try {
						player.getSocket().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else if(player.getChannel() != null){
					player.getChannel().closeFuture();
				}
			}
		});
		// 消息发送事件
		send.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!sendMsg.getText().trim().equals("")) {
					////////////////////////////////////////////////////////
					AbstractProtocol chatMsgProt = new ChatMsgProt(1,userName,userName+"说："+sendMsg.getText().trim());
					chatMsgProt.setPlayer(player);
					chatMsgProt.sendMsg();
					setHistoryMsg("你说:" + sendMsg.getText().trim());
					/*String msg = sendMsg.getText().trim();
					msg = msg.replaceAll("：", ":");
					messageHandler.sendAllChatMsg(msg);
					if (msg.startsWith("@")) {
						int endIdx = msg.indexOf(":");
						String name = msg.substring(1, endIdx);
						setHistoryMsg("你悄悄对" + name + "说:" + msg);
					} else {
						setHistoryMsg("你说:" + msg);
					}*/
					//////////////////////////////////////////////////
				}
			}

		});
		// 座位点击事件--进入斗地主房间
		for (int i = 0; i < TOTAL * 3; i++) {
			final JLabel jLabelTemp = seats[i];
			final int seatNum = i;
			seats[i].addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if (jLabelTemp.getText().equals("空位")) {
						if (currentRoom != null) {
							closeLandlordsRoom();
						}
						jLabelTemp.setText(userName);
						currentRoom = new LandlordsRoomWindow(jLabelTemp, seatNum,player,GameLobbyWindow.this);
						context.setLandlordsRoomWindow(currentRoom);
						///////////////////////20171107//////////////////////////////////////////
						AbstractProtocol  enterSeatProt= new EnterSeatProt(seatNum,userName);
						enterSeatProt.setPlayer(player);
						enterSeatProt.sendMsg();
						//qqClientHandler .setLandlordsRoomWindow(currentRoom);
						/*messageHandler.sendEnterSeatMsg(seatNum + "");
						qqClientHandler.getReceiveMessageHandler().setLandlordsRoomWindow(currentRoom);*/

						/////////////////////////////////////////////////////////////////////
					} else {
						JOptionPane.showMessageDialog(null, "该位置有人", "信息警告", JOptionPane.WARNING_MESSAGE);
					}
				}
			});
		}
	}

	/**
	 * 显示收到的群聊消息
	 * 
	 * @param readMsg
	 */
	public void setHistoryMsg(String readMsg) {
		this.historyMsg.append(readMsg + "\n");
	}

	/**
	 * 对号入座
	 * 
	 * @param seatNum
	 * @param userName
	 */
	public void setSeatName(int seatNum, String userName) {
		seats[seatNum].setText(userName);
	}

	/**
	 * 空出位置
	 * 
	 * @param seatNum
	 */
	public void emptySeat(int seatNum) {
		seats[seatNum].setText("空位");
	}

	/**
	 * 关闭斗地主房间
	 */
	private void closeLandlordsRoom() {
		////////////////////////////////////////////////////////////////////
			//messageHandler.sendExitSeatMsg(String.valueOf(currentRoom.getSeatNum()));
		AbstractProtocol exitSeatProt = new ExitSeatProt(currentRoom.getSeatNum(),userName);
		exitSeatProt.setPlayer(player);
		exitSeatProt.sendMsg();
			/////////////////////////////////////////////////////////////////////////////
			currentRoom.closeRoom();
	}
	public void setLandlordsRoomtoNull(){
		currentRoom = null;
	}
}
