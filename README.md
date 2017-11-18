### 为什么要搞这样一个项目 ###

1. 满足自己的java网络多线程编程的欲望！因为之前一直都是搞web开发，服务器和客户端数据交流人家web服务器早就给你搞好了，比如tomcat,jetty...等等，其实之前脑子里就有想过--是否可以自己开发一个简单的类似tomcat的web服务器，这样做了的话，至少知道其实web开发是建立在网络多线程web服务器之上否则web开发无从谈起！
2. 市面上的书籍和互联网上的电子教材以及什么培训学校的培训内容都没有类似像qq斗地主这样的真实网络多线程demo--既要处理多线程并发问题又要处理网络上各个节点网络通讯问题以及各个节点数据同步问题。
3. 如果项目胜利完成就架设一个服务器，让更多java爱好者知道有这样一个qq斗地主的服务器，然后下载客户端源码，运行程序连接服务器，越多java爱好者玩这个demo就意味着并发度越高，线程就越多，项目的问题就暴露的越多，然后大家一起来找原因解决bug，然后大家的java境界就越高尤其是网络多线程编程境界--因为这是一个真实的属于java程序员自己控制的网络多线程环境而不是书本上那些简单的demo!

先写到这样，还有要补充！

如果您对我的想法感兴趣或者demo感兴趣，请加qq群：**624454322**

**要项目源码的人请加qq群！**

### 产生qq斗地主游戏大厅界面的算法 ###

这下面就是qq斗地主大厅截图

首先说产生5行6列橙色正三角块的算法，先上代码：

```java
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
```


然后产生5*6*3= 90空位的算法，其实跟上面的差不多，先上源码：

```java
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
```

源码分析下次或者自己看，其实把握好各个临界点，就很好实现

java,qq斗地主,斗地主,模拟qq斗地主,java开发qq斗地主,java斗地主源码,java多线程,java并发编程,netty

### 斗地主状态切换分析 ###
等待人齐--->是否准备--->发牌---->抢地主---->出牌--->本次斗完
wait	ready	  deal	  rob	   play	  over