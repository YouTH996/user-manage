package com.ansatsing.landlords.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
//import java.awt.SystemTray;
//import java.awt.TrayIcon;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.ansatsing.landlords.client.BioSocketClient;
import com.ansatsing.landlords.client.Context;
import com.ansatsing.landlords.client.IClient;
import com.ansatsing.landlords.client.NettyClient;
import com.ansatsing.landlords.client.handler.SendMessageHandler;
import com.ansatsing.landlords.client.thread.BioSocketThread;
import com.ansatsing.landlords.client.thread.HeartBeatThread;
import com.ansatsing.landlords.client.thread.NettyThread;
import com.ansatsing.landlords.entity.Player;
import com.ansatsing.landlords.protocol.AbstractProtocol;
import com.ansatsing.landlords.protocol.GameRegisterProt;
import com.ansatsing.landlords.protocol.SystemExitProt;
import com.ansatsing.landlords.util.PictureUtil;

/**
 * QQ斗地主游戏登录界面
 *
 * @author ansatsing
 * @time 2017年10月20日 下午8:18:48
 */
public class LoginWidow extends JDialog {
	private JPanel content;// 面板
	private JLabel title;// 窗口标题标签
	private JLabel userNameLable;// 网名输入提示标签
	private JTextField userNameField;// 网名输入框
	private JLabel minButton;// 最小化按钮
	private JLabel exitButton;// 退出按钮
	private JLabel loginButton;// 登录按钮
	//private SystemTray tray;//系统托盘
	//private TrayIcon icon;//系统托盘图片
	private Point point = new Point();//用于记录鼠标移动之前的位置
	private Socket socket;
	private JLabel errorTip;//提示信息
	private SendMessageHandler messageHandler;
	private BioSocketThread qqClientHandler;
	private Player player;
	private HeartBeatThread heartBeatThread;
	private volatile Context context;
	public LoginWidow( ) {
		initGUI();
		//pack();
		//initTrayIcon();
		initListener();
		setLocationRelativeTo(null);
		setVisible(true);
		//this.socket = socket;
		//this.messageHandler = new SendMessageHandler(socket);
		//this.qqClientHandler = qqClientHandler;
		//this.context = context;
		//context.setLoginWidow(this);

	}

	// 窗口初始化
	private void initGUI() {
		try {
			setSize(350, 180);
			setUndecorated(true);// 不要窗口边框
			// TODO 这个API导致输入中文白屏
			// AWTUtilities.setWindowOpaque(this, false);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//仅关闭窗体，不是退出程序

			// 主面板
			content = new JPanel(); /*
									 * { protected void paintComponent(Graphics g) { super.paintComponent(g);
									 * g.drawImage(PictureUtil.getPicture("back5.jpg").getImage(), 0, 0, null);
									 * this.setOpaque(false); } };
									 */
			content.setLayout(null);
			content.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			getContentPane().add(content, BorderLayout.CENTER);

			// 窗口标题
			title = new JLabel("QQ斗地主登录");
			content.add(title);
			title.setFont(new Font("微软雅黑", Font.BOLD, 15));
			title.setBounds(3, 1, 154, 24);

			// 请输入网名
			userNameLable = new JLabel("请输入网名:");
			content.add(userNameLable);
			userNameLable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
			userNameLable.setBounds(65, 75, 154, 30);

			// 网名
			userNameField = new JTextField();
			content.add(userNameField);
			userNameField.setBounds(128, 75, 123, 30);
			userNameField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

			// 最小化按钮
			minButton = new JLabel();
			minButton.setName("23");
			content.add(minButton);
			minButton.setBounds(290, 0, 30, 20);
			minButton.setIcon(PictureUtil.getPicture("minimize.png"));

			// 关闭按钮
			exitButton = new JLabel();
			content.add(exitButton);
			exitButton.setBounds(318, 0, 40, 20);
			exitButton.setIcon(PictureUtil.getPicture("close.png"));
			
			// 登陆按钮
			loginButton = new JLabel("登 录");
			content.add(loginButton);
			loginButton.setFont(new Font("微软雅黑", Font.BOLD, 14));
			loginButton.setBounds(260, 74, 50, 30);
			//loginButton.setIcon(PictureUtil.getPicture("login_button.png"));	
			
			//提示信息
			errorTip = new JLabel();
			content.add(errorTip);
			errorTip.setFont(new Font("微软雅黑", Font.ITALIC, 12));
			errorTip.setBounds(138, 95, 150, 30);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//win系统右下角下图标显示程序：设置托盘图标
	/*private void initTrayIcon() {
		if(SystemTray.isSupported()) {//系统托盘是否支持
			tray = SystemTray.getSystemTray();
			icon = new TrayIcon(PictureUtil.getPicture("/qq_icon.png").getImage(), "QQ游戏室");
			icon.setImageAutoSize(true);
			icon.addMouseListener(new MouseAdapter() {//针对窗体最小化后要显示窗体事件

				@Override
				public void mouseReleased(MouseEvent e) {
					if(e.getButton() == MouseEvent.BUTTON1) {//1左键 2右键 3滚动
						setVisible(true);
						requestFocus();//获取焦点
					}
				}
				
			});
			PopupMenu popupMenu = new PopupMenu();
			//乱码解决方案：Run As中的Run Configurations
			//在Arguments标签下的VM arguments中添加下面这行参数代码，然后点击应用。 
		     //-Dfile.encoding=GB18030 
			MenuItem item = new MenuItem("退出");
			popupMenu.add(item);
			//通过系统托盘图标的方式来退出程序
			item.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					tray.remove(icon);//移除托盘图标
					exit();
				}
			});
			icon.setPopupMenu(popupMenu);
			try {
				tray.add(icon);
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}*/
	//窗口监听事件
	private void initListener() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		//窗体被鼠标按下事件
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				point.x = e.getX();
				point.y = e.getY();
			}
			
		});
		//窗体被鼠标拖曳事件
		this.addMouseMotionListener(new MouseAdapter() {

			//@Override
			public void mouseDragged(MouseEvent e) {
				Point p = getLocation();
				setLocation(p.x + e.getX() - point.x, p.y + e.getY() - point.y);
			}
			
		});
		//窗体最小化事件
		minButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				setVisible(false);
			}
			
		});
		//窗体关闭事件
		exitButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				exit();
			}
			
		});
		//登录事件
		loginButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if(userNameField.getText().equals("")) {
					errorTip.setText("网名不能为空!");
				}else {

						context = new Context();
						player = new Player();
						context.setLoginWidow(LoginWidow.this);
						context.setPlayer(player);
						startConnectServer(false);
				}
			}
			
		});
	}
	public void handleLogin(boolean isConnected){
		if(!isConnected){
			errorTip.setText("服务器未启动!");
		}else{
			AbstractProtocol registerProt = new GameRegisterProt(userNameField.getText().trim());
			registerProt.setPlayer(player);
			registerProt.sendMsg();
		}
	}
	//当收到服务器返回的注册信息时 进行如下处理
	public void handleGameRegister(boolean registerSuccessful,AbstractProtocol protocol){
		if(registerSuccessful){
			 //heartBeatThread = new HeartBeatThread(player);//netty自带心跳机制
			//GameLobbyWindow qqGameWindow = new GameLobbyWindow(userNameField.getText().trim(),qqClientHandler);
			player.setUserName(userNameField.getText().trim());
			GameLobbyWindow qqGameWindow = new GameLobbyWindow(player,context);
			protocol.setGameLobbyWindow(qqGameWindow);
			context.setQqGameWindow(qqGameWindow);
			//new Thread(heartBeatThread).start();
			dispose();//仅仅关闭窗体
		}else{
			errorTip.setText("这网名有人正在使用,请更换网名!");
			userNameField.setText("");
		}
	}
	private void exit() {
		if(socket != null) {
				heartBeatThread.stop();
				SystemExitProt systemExitProt = new SystemExitProt(socket);
				systemExitProt.sendMsg();
				qqClientHandler.stop();
		}
		System.exit(0);
	}
	//启动信息接收线程
	private void startConnectServer(boolean isNetty){
		//try{
			IClient client = null;
			String host = "127.0.0.1";
			int port = 6789;
			if(!isNetty){
				client = new BioSocketClient(context,host,port);
			}else{
				client = new NettyClient(context,host,port);
			}
			if(client == null) throw new NullPointerException("客户端连接器为空!");
			client.connectServer();
	}
}
