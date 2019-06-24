package com.zte.TBattle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.zte.engin.InputMgr;
import com.zte.engin.SceneMgr;

//场景 窗口
public class Window extends SceneMgr {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int GoOn = 1;//关卡从第一关开始
	myMusicPlay audioPlayWave = new myMusicPlay("music/1.wav");
	//boolean giveLive = false;//复活  失败-_-||  @_@ QAQ
	Thread thread = new Thread(new PaintThread());
	public Window() {
		setSize(800, 600); // 设置界面大小
		setLocation(280, 50); // 设置界面出现的位置	
		setResizable(false);
		setVisible(true);
		addKeyListener();
		thread.start();// 线程启动
		addWindowListener(new WindowAdapter() { // 窗口监听关闭
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//播放音乐
		audioPlayWave.start();
	}
	public static boolean printable = true;
	int delay = 0;          //时间凝滞器持续时间变量
	boolean timer = false;  //
	boolean rst = false;    //	
	boolean Wall = false;	//铲子持续时间变量
	boolean Wrst = false;
	int Wdelay = 0; 
	Tank homeTank = new Tank(280, 560, true, Direction.STOP, this,4);
	Home home = new Home(373, 540, this,"Images/home.png");
	List<River> theRiver = new ArrayList<River>();//河流
	List<EnemyTank> tanks = new ArrayList<EnemyTank>();//坦克
	List<BombTank> bombTanks = new ArrayList<BombTank>();//坦克爆炸
	List<Bullets> bullets = new ArrayList<Bullets>();//子弹
	List<Tree> trees = new ArrayList<Tree>();//树
	List<BrickWall> homeWall = new ArrayList<BrickWall>(); //砖墙-家
	List<BrickWall> brickWall = new ArrayList<BrickWall>();//砖墙
	List<MetalWall> metalWall = new ArrayList<MetalWall>();//金属墙
	List<MetalWall> homeMetal = new ArrayList<MetalWall>();//金属墙-家
	List<PropLive> PpLive = new ArrayList<PropLive>();//道具_生命
	List<PropTime> PpTime = new ArrayList<PropTime>();//道具_时间凝滞
	List<PropShovel> PpShovel = new ArrayList<PropShovel>();//道具_铲子

	public void framPaint(Graphics g) {
		if(!gameStart) {
			showBegin(g);
		}else {
			Color c = g.getColor();
			g.setColor(Color.WHITE); 
			Font f1 = g.getFont();
			g.setFont(new Font("宋体", Font.BOLD, 20));
			g.drawString("坦克大战", 340, 50);
			g.setFont(new Font("宋体", Font.BOLD, 15));
			g.drawString("第"+ (GoOn - 1) +"关", 360, 70);
			g.setFont(new Font("宋体", Font.BOLD, 15));
			g.drawString("区域内还有敌方坦克: ", 2, 70);
			g.setFont(new Font("TimesRoman", Font.ITALIC, 15));
			g.drawString("" + tanks.size(), 155, 70);
			g.setFont(new Font("宋体", Font.BOLD, 15));
			g.drawString("我方剩余生命数: ", 640, 70);
			g.setFont(new Font("TimesRoman", Font.ITALIC, 15));
			g.drawString("" + homeTank.getHP(),770, 70);
			g.setFont(f1);
			if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
				//清空链表
				theRiver.clear();tanks.clear();bombTanks.clear();metalWall.clear();
				bullets.clear();trees.clear();homeWall.clear();brickWall.clear();
				PpLive.clear();PpTime.clear();PpShovel.clear();homeMetal.clear();
				if(GoOn > 7 ) {// 判断是否赢得比赛 (所有关卡结束) && 是否开始下一关
					gameWin = true;
					showOver(g);
				}else {//开始下一关，并重置初始值
					rst = false;Wrst = false;Wall = false;timer = false;
					homeTank = new Tank(280, 560, true, Direction.STOP, this,homeTank.getHP());
					if(GoOn > 1) {
						try {
							Thread.sleep(200);//下一关开始前休眠200ms
						} catch (Exception o) {
							o.printStackTrace();
						}
					}
					init();
					GoOn += 1;
				}
			}
			g.setColor(c);
			for (int i = 0; i < theRiver.size(); i++) {
				River r = theRiver.get(i);
				homeTank.isHit(r);
				r.show(g);
			}
			home.show(g); // 画出家
			homeTank.show(g);// 画出自己家的坦克
			if(timer) {//设置时间凝滞器持续时间
				delay++;
				if(delay > 100) {
					delay = 0;
					timer = false;
					rst = true;
				}
			}
			for (int i = 0; i < bullets.size(); i++) { // 对子弹链表进行循环判断
				Bullets m = bullets.get(i);
				m.hitTanks(tanks); // 每一个子弹打到坦克上
				m.isHit(homeTank); // 每一个子弹打到自己家的坦克上时
				m.isHit(home); // 每一个子弹打到家里时
				for (int j = 0; j < metalWall.size(); j++) { // 每一个子弹打到金属墙上
					MetalWall mw = metalWall.get(j);
					m.isHit(mw);
				}
				for (int j = 0; j < brickWall.size(); j++) {// 每一个子弹打到砖墙上
					BrickWall w = brickWall.get(j);
					m.isHit(w);
				}
				for (int j = 0; j < homeWall.size(); j++) {// 每一个子弹打到家的墙上
					BrickWall cw = homeWall.get(j);
					m.isHit(cw);
				}
				for (int j = 0; j < homeMetal.size(); j++) { // 每一个子弹打到金属墙上
					MetalWall mw = homeMetal.get(j);
					mw.show(g);
					m.isHit(mw);
					homeTank.isHit(mw);
				}
				m.show(g); // 画出效果图
			}
			Random rp = new Random();
			if(rp.nextInt(50) > 48) {//随机产生道具  游戏结束后停止生成
				if(PpLive.size() == 0 && tanks.size() != 0 && home.isLive()) {//道具_生命 
					PpLive.add(new PropLive());
				}
			}
			if(rp.nextInt(50) > 48) {//随机产生道具
				if(PpShovel.size() == 0 && tanks.size() != 0 && home.isLive()) {//道具_铲子
					PpShovel.add(new PropShovel());
				}
			}
			if(rp.nextInt(50) > 48) {//随机产生道具
				if(PpTime.size() == 0 && tanks.size() != 0 && home.isLive()) {//道具_时间凝滞 
					PpTime.add(new PropTime());
				}
			}
			for (int i = 0; i < tanks.size(); i++) { //画坦克
				EnemyTank t = tanks.get(i); // 获得键值对的键
				for (int j = 0; j < homeWall.size(); j++) {
					BrickWall cw = homeWall.get(j);
					t.isHit(cw); // 每一个坦克撞到家里的墙时
				}
				for (int j = 0; j < brickWall.size(); j++) { // 每一个坦克撞到家以外的墙
					BrickWall cw = brickWall.get(j);
					t.isHit(cw);
				}
				for (int j = 0; j < metalWall.size(); j++) { // 每一个坦克撞到金属墙
					MetalWall mw = metalWall.get(j);
					t.isHit(mw);
				}
				for (int j = 0; j < theRiver.size(); j++) {
					River r = theRiver.get(j); // 每一个坦克撞到河流时
					t.isHit(r);
				}
				t.isHitTanks(tanks); // 撞到自己的人
				t.isHit(home);
				t.show(g);
				if(!timer){
					t.update();
				}
				for(int j = 0; j<PpTime.size(); j++) { //画出道具_时间凝滞
					PropTime pt = PpTime.get(j);          
					pt.show(g);
					pt.isHit(homeTank);
					if(!pt.isLive() && !rst) {//己方坦克吃到道具，敌方坦克静止一段时间
						timer = true;
					}
				}
			}
			for(int i = 0; i<PpLive.size(); i++) { //画出道具_生命
				PropLive tp = PpLive.get(i);
				tp.show(g);
				tp.isHit(homeTank);
			}
			for (int i = 0; i < trees.size(); i++) { // 画出树
				Tree tr = trees.get(i);
				tr.show(g);
			}
			for (int i = 0; i < bombTanks.size(); i++) { // 画出爆炸效果
				BombTank bt = bombTanks.get(i);
				bt.show(g);
			}
			for (int i = 0; i < brickWall.size(); i++) { // 画出土墙
				BrickWall cw = brickWall.get(i);
				cw.show(g);
			}
			for (int i = 0; i < metalWall.size(); i++) { // 画出金属墙
				MetalWall mw = metalWall.get(i);
				mw.show(g);
			}
			homeTank.isHitTanks(tanks);
			homeTank.isHit(home);
			for (int i = 0; i < metalWall.size(); i++) {// 撞到金属墙
				MetalWall w = metalWall.get(i);
				homeTank.isHit(w);
			}
			for (int i = 0; i < brickWall.size(); i++) {
				BrickWall cw = brickWall.get(i);
				homeTank.isHit(cw);
			}
			for (int i = 0; i < homeWall.size(); i++) { // 家里的坦克撞到自己家
				BrickWall w = homeWall.get(i);
				homeTank.isHit(w);
			}
		}
		if(Wall) {//设置铲子持续时间
			Wdelay++;
			if(Wdelay > 200) {
				Wdelay = 0;
				Wall = false;
				Wrst = true;
			}
		}
		for(int j = 0; j<PpShovel.size(); j++) { //画出道具_铲子
			PropShovel pm = PpShovel.get(j);          
			pm.show(g);
			pm.isHit(homeTank);
			if(!pm.isLive() && homeTank.isLive() && !Wrst) {//如果己方坦克吃到铲子道具重建雄鹰的防御为金属墙
				for (int i = 0; i < 3; i++) { 
					homeMetal.add(new MetalWall(327 + 44 * i, 516-22,"Images/metalWall.gif"));
					homeMetal.add(new MetalWall(327, 516 + 22 * i,"Images/metalWall.gif"));
					homeMetal.add(new MetalWall(417, 516 + i * 22,"Images/metalWall.gif"));
				}
				Wall = true;
			}
			if(!Wall) {
				homeMetal.clear();
			}
		}
	}
	public void init() {
		for (int i = 0; i < 7; i++) { // 家的布局
			if(i < 6)
				homeWall.add(new BrickWall(328 + 22 * i, 516-22,"Images/BrickWall.gif"));
			if (i < 4) {
				homeWall.add(new BrickWall(350, 516 + 22 * i,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(328, 516 + 22 * i,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(416, 516 + i * 22,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(438, 516 + i * 22,"Images/BrickWall.gif"));
			}else if (i < 7) 
				homeWall.add(new BrickWall(372 + 22 * (i - 4), 516,"Images/BrickWall.gif"));
		}

		//关卡一：
		if(GoOn == 1) {
			for(int i = 0;i < 10; i++) {//砖墙的布局
				for (int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(374 + 22 * i, 224 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(374 - 22 * (i-1), 224 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(374 + 22 * j, 224 + 22 * (i - 4) ,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(460 + 22 * i, 268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(308 - 22 * i, 268 + 22 * j, "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 3; i++) { // 金属墙布局
				metalWall.add(new MetalWall(2 + 80 * i, 558,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(42 + 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(762 - 80 * i, 558,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(722 - 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(374 + 44 * (i - 1), 356,"Images/metalWall.gif"));
			}
			for(int i = 0; i < 3; i++) {//树的布局
				trees.add(new Tree(2,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(46,124,"Images/tree.gif"));
				trees.add(new Tree(762,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(762 - 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(718,124,"Images/tree.gif"));
				trees.add(new Tree(2,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,350,"Images/tree.gif"));
				trees.add(new Tree(46,306,"Images/tree.gif"));
				trees.add(new Tree(762,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(762 - 44 * i,350,"Images/tree.gif"));
				trees.add(new Tree(718,306,"Images/tree.gif"));	
				if(i < 2) {
					trees.add(new Tree(330 + 88 * i,180,"Images/tree.gif"));
					trees.add(new Tree(330,180 + 88 * i,"Images/tree.gif"));
				}
				trees.add(new Tree(418,268,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(308, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river1);
			River river2 = new River();
			river2.init(396, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river2);
		}
		//关卡二：
		if(GoOn == 2) {
			for (int i = 0; i < 3; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(140+22*j,529+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(500+22*j,529+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22*j,309+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22*j,375+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(495+22*j,310+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(495+22*j,310+22*3,"Images/BrickWall.gif"));
				}		
			}
			for(int i = 0;i < 10; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(360+22+22*(i-1),375+22+22*j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22-22*i,375+22+22*j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(140+66-22*j,375+22-22*(i-3),"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(500+58-22*j,375+22-22*(i-3),"Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 2; i++) { // 金属墙布局
				metalWall.add(new MetalWall(316 + 44 * i,441,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(272 + 44 * i,309,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(272 + 176 * i,353,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(404 + 44 * i,309,"Images/metalWall.gif"));
			}

			for (int i = 0; i < 4; i++) { // 树的布局
				trees.add(new Tree(140 + 44 * i, 485,"Images/tree.gif"));
				trees.add(new Tree(500 + 44 * i, 485,"Images/tree.gif"));
				trees.add(new Tree(632, 441,"Images/tree.gif"));
				trees.add(new Tree(96, 441,"Images/tree.gif"));
				trees.add(new Tree(316-44*2,309,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(272, 309-44,"Images/river.jpg");
			theRiver.add(river1);
			River river2 = new River();
			river2.init(272+44*2, 309-44,"Images/river.jpg");
			theRiver.add(river2);
			River river3 = new River();
			river3.init(272+44*4, 309-44,"Images/river.jpg");
			theRiver.add(river3);
		}
		//关卡三：
		if(GoOn == 3) {
			for (int i = 0; i < 48; i++) {//砖墙布局
				if (i < 18) {
					brickWall.add(new BrickWall(200 + 22 * i, 300, "Images/BrickWall.gif")); // 普通墙布局
					brickWall.add(new BrickWall(200 + 22 * i, 200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(200 + 22 * i , 320, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(200 + 22 * i , 220, "Images/BrickWall.gif"));

				} else if (i < 32) {
					brickWall.add(new BrickWall(220, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(240, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(540, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(520, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
				}else if (i < 40){
					brickWall.add(new BrickWall(0 + 22 * (i - 32), 100, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(640 + 22 * (i - 32), 100, "Images/BrickWall.gif"));


				}else if (i < 48){
					brickWall.add(new BrickWall(160,  140+22 * (i - 40), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(620,  140+22 * (i - 40), "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 8; i++) { // 金属墙布局
				metalWall.add(new MetalWall(160,556, "Images/metalWall.gif"));
				metalWall.add(new MetalWall(580,556, "Images/metalWall.gif"));
				if (i < 2) {
					metalWall.add(new MetalWall(250,100+44*i, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(500,100+44*i, "Images/metalWall.gif"));
				}else if (i < 3){
					metalWall.add(new MetalWall(0+44*(i-2),450, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(760+44*(i-2),450, "Images/metalWall.gif"));
				}else if (i < 4){
					metalWall.add(new MetalWall(80+44*(i-3),500,"Images/metalWall.gif"));
					metalWall.add(new MetalWall(660+44*(i-3),500, "Images/metalWall.gif"));
				}else if (i < 8)
					metalWall.add(new MetalWall(310+44*(i-4),250, "Images/metalWall.gif"));

			}

			for (int i = 0; i < 4; i++) { // 树的布局
				if (i < 4) {
					trees.add(new Tree(0 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(210 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(429 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(648 + 44 * i, 360, "Images/tree.gif"));
					trees.add(new Tree(310 + 44 * i, 120 , "Images/tree.gif"));
					trees.add(new Tree(310 + 44 * i, 84 , "Images/tree.gif"));

				}

			}
			River river1 = new River();
			river1.init(65, 150,"Images/river.jpg");//河水的布局
			theRiver.add(river1);
			River river3 = new River();
			river3.init(65, 194,"Images/river.jpg");//河水的布局
			theRiver.add(river3);
			River river2 = new River();
			river2.init(655, 150,"Images/river.jpg");//河水的布局
			theRiver.add(river2);
			River river4 = new River();
			river4.init(655, 194,"Images/river.jpg");//河水的布局
			theRiver.add(river4);
		}
		//关卡四：
		if(GoOn == 4) {
			for(int i = 0;i < 10; i++) {//砖墙的布局
				for (int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(450 + 22 * i, 324 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(450 - 22 * (i-1), 324 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(450 + 22 * j, 324 + 22 * (i - 3) ,"Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 3; i++) { // 金属墙布局
				metalWall.add(new MetalWall(80 + 80 * i, 358,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(220 + 80 * i, 158,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(562 - 80 * i, 158,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(690- 80 * i, 358,"Images/metalWall.gif"));
			}
			for(int i = 0; i < 3; i++) {//树的布局
				trees.add(new Tree(0,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(665 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(755,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,358,"Images/tree.gif"));
				trees.add(new Tree(562,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(755 - 44 * i,358,"Images/tree.gif"));
				trees.add(new Tree(560,175,"Images/tree.gif"));	
				if(i < 2) {
					trees.add(new Tree(330 + 88 * i,180,"Images/tree.gif"));
					trees.add(new Tree(330,180 + 88 * i,"Images/tree.gif"));
				}
				trees.add(new Tree(418,268,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(215, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river1);
			River river2 = new River();
			river2.init(225 + 100, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river2);
			River river3 = new River();
			river3.init(255 + 100, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river3);
			River river4 = new River();
			river4.init(455 + 100, 400,"Images/river.jpg");//河水的布局
			theRiver.add(river4);
		}
		//关卡五：
		if(GoOn == 5) {
			for(int i = 0;i < 2; i++) {
				trees.add(new Tree(0 , 180 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(0 , 268,"Images/tree.gif"));
				trees.add(new Tree(44 , 224,"Images/tree.gif"));
				trees.add(new Tree(264 + 44 * (i + 1),312,"Images/tree.gif"));
				trees.add(new Tree(264,312 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(528,312 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(704 - 44 * i,22 + 44 * i,"Images/tree.gif"));

			}
			for(int i = 0;i < 8; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(44 + 22 * i,268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(44 + 22 * j,356 + 22 * i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(572 + 22 * i,268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(572 + 22 * j,268 + 22 * i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(616 + 22 * i,110 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(484 + 22 * j,224 + 22 * (i + 2),"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(44 + 22 * i , 180 + 22 * j,"Images/BrickWall.gif"));
					if(i < 6) {
						brickWall.add(new BrickWall(266 + 22 * j,290 - 22 * i,"Images/BrickWall.gif"));
					}
					brickWall.add(new BrickWall(310 + 22 * i,180 + 22 * j,"Images/BrickWall.gif"));
				}
				trees.add(new Tree(750,312 + 44 * i,"Images/tree.gif"));
			}
			for(int i = 0;i < 13; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(220 + 22 * i,400 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(704 + 22 * j,312 + 22 * i,"Images/BrickWall.gif"));
				}
			}
			for(int i = 0;i < 2; i++) {
				metalWall.add(new MetalWall(220,312 + 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(396 + 44 * i,312 - 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(484 + 44 * i,180- 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(352 + 44 * i,224,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(44,532 + 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(44,22 + 44 * i,"Images/metalWall.gif"));
			}
		}
		//关卡六:
		if(GoOn == 6) {
			for (int i = 0; i < 15; i++) {
				if (i < 5) {
					brickWall.add(new BrickWall(0 + 22 * i, 300, "Images/BrickWall.gif")); // 普通墙布局
					brickWall.add(new BrickWall(175 + 22 * i, 300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * i ,300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * i ,300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * i ,300, "Images/BrickWall.gif"));

				} else if (i < 10) {
					brickWall.add(new BrickWall(0 + 22 * (i-5), 200, "Images/BrickWall.gif")); // 普通墙布局
					brickWall.add(new BrickWall(175 + 22 *(i-5), 200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
				} else if (i < 15) {
					brickWall.add(new BrickWall(0 + 22 * (i-10), 400, "Images/BrickWall.gif")); // 普通墙布局
					brickWall.add(new BrickWall(175 + 22 * (i-10), 400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 5; i++) { // 金属墙布局
				if (i < 2) {
					metalWall.add(new MetalWall(55,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(186+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(361+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(536+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(700,156, "Images/metalWall.gif"));
				}else if (i < 4){
					metalWall.add(new MetalWall(80+44*(i-3),500,"Images/metalWall.gif"));
					metalWall.add(new MetalWall(660+44*(i-3),500, "Images/metalWall.gif"));
				}else if (i < 5){
					metalWall.add(new MetalWall(295+44*(i-4),500, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(450+44*(i-4),500, "Images/metalWall.gif"));
				}
			}
			for (int i = 0; i < 7; i++) { // 树的布局
				if (i < 3) {
					trees.add(new Tree(145 + 44 * i, 500 , "Images/tree.gif"));
					trees.add(new Tree(510 + 44 * i,500 , "Images/tree.gif"));
				}else if (i<7){
					trees.add(new Tree(120,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(295,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(470,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(645,100+44*i , "Images/tree.gif"));
				}
			}
		}
		//关卡七：
		if(GoOn == 7) {
			//砖墙的布局
			brickWall.add(new BrickWall(389 , 364 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(389,313,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall( 389,262 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(389,211 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(285,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,313 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,160 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,160 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,313 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(493,262, "Images/BrickWall.gif"));

			for (int i = 0; i < 3; i++) { // 金属墙布局
				metalWall.add(new MetalWall(2 + 80 * i, 558,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(290 + 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(762 - 80 * i, 558,"Images/metalWall.gif"));	
			}
			for(int i = 0; i < 8; i++) {//树的布局
				trees.add(new Tree(44,148+44*i,"Images/tree.gif"));
				trees.add(new Tree(712,148+ 44 * i,"Images/tree.gif"));
				if(i<6) {
					trees.add(new Tree(88,236+ 44 * i,"Images/tree.gif"));
					trees.add(new Tree(668,236+ 44 * i,"Images/tree.gif"));
				}
				if(i<4) {
					trees.add(new Tree(132,324+44*i,"Images/tree.gif"));
					trees.add(new Tree(624,324+44*i,"Images/tree.gif"));
				}
				if(i<2) {
					trees.add(new Tree(176,412+ 44 * i,"Images/tree.gif"));
					trees.add(new Tree(580,412+ 44 * i,"Images/tree.gif"));
				}
			}
		}
		// 初始化坦克数量 第一关为10辆，往后第 n 关 + 2(n-1)辆,并设置坦克出现的位置
		for (int i = 0; i < 10 + (GoOn - 1) * 2; i++) { 
			if (i < 9)  
				tanks.add(new EnemyTank(150 + 70 * i, 40, false, Direction.D, this));
			else if (i < 15)
				tanks.add(new EnemyTank(750, 40 + 50 * (i - 6) , false, Direction.D,
						this));
			else
				tanks.add(new EnemyTank(10, 50 * (i - 12), false, Direction.D,
						this));
		}	
	}

	private class PaintThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try {
					Thread.sleep(50);//50毫秒循环一次
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//键盘监听
	public void addKeyListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				homeTank.keyPressed(e);
				InputMgr.getInput().setKeyStatus(e, true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				homeTank.keyReleased(e);
				InputMgr.getInput().setKeyStatus(e, false);
			}
		});
		//获得键盘的焦点
		requestFocus();
	}
	public void actionPerformed(ActionEvent e) {}
	public static int getGoOn() {
		return GoOn;
	}
	public static void setGoOn(int goOn) {
		GoOn = goOn;
	}
}
//脑壳痛 @_@  写到奔溃