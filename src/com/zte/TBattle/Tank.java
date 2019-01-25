package com.zte.TBattle;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import com.zte.engin.ObjectMgr;
//己方坦克
public class Tank extends ObjectMgr{
	public static int count = 0;
	private Direction direction = Direction.STOP; // 初始化状态为静止
	private Direction Kdirection = Direction.U; // 初始化方向为向上
	Window Ts;
	private boolean notEnemy; // 区分敌我坦克
	private boolean live = true; // 初始化为活着
	private int HP; // 初始生命
	private ImageIcon[] imgs = new ImageIcon[4]; 
	public Tank(int x, int y, boolean notEnemy, Direction dir, Window Ts,int hp) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.HP = hp;
		this.notEnemy = notEnemy;
		this.direction = dir;
		this.Ts = Ts;
		for(int i = 0;i < 4; i++) {
			imgs[i]= new ImageIcon("Images/MT"+ i +".png");
		}
	}
	public void show(Graphics g) {
		if (!live) {
			return;
		}
		switch (Kdirection) {//根据方向选择坦克的图片
		case D:
			g.drawImage(imgs[0].getImage(), x, y, null);
			break;
		case U:
			g.drawImage(imgs[1].getImage(), x, y, null);
			break;
		case L:
			g.drawImage(imgs[2].getImage(), x, y, null);
			break;
		case R:
			g.drawImage(imgs[3].getImage(), x, y, null);
			break;
		default:
			break;
		}
		update();
	}
	public void update() {
		this.oldX = x;
		this.oldY = y;
		if(inpt.getKeyStatus(KeyEvent.VK_RIGHT)) {//右键响应
			x += 6;y = oldY;
			direction = Direction.R;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_LEFT)) {//左键响应
			x -= 6;y = oldY;
			direction = Direction.L;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_UP)) {//上键响应
			y -= 6;x = oldX;
			direction = Direction.U;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_DOWN)) {//下键响应
			y += 6;x = oldX;
			direction = Direction.D;
		}if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}
		if (x < 0)//防止越界
			x = 0;
		if (y < 40)      
			y = 40;
		if (x > 770)  
			x = 770;
		if (y > 570)
			y = 570;//
	}
	private void changToOldDir() {  //记忆碰撞前位置
		x = oldX;
		y = oldY;
	}
	public void keyPressed(KeyEvent e) {  //接受键盘事件
		int key = e.getKeyCode();
		switch (key) {
		/*case KeyEvent.VK_M:
			Ts.audioPlayWave.stop();//M键关闭BGM,貌似有BUG   QAQ
			break;*/
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_Q:
			Ts.gameStart=true;
		case KeyEvent.VK_P://P键暂停(按着暂停，松开50ms继续......)*_V..
			try {
				Thread.sleep(50);
			} catch (Exception o) {
				o.printStackTrace();
			}break;
			//window.thread.wait();
		case KeyEvent.VK_R:  //当按下R时，重新开始游戏 ,重置参数
			Ts.PpLive.clear();
			Ts.PpTime.clear();
			Ts.PpShovel.clear();
			Ts.tanks.clear();  
			Ts.bullets.clear();
			Ts.trees.clear();
			Ts.brickWall.clear();
			Ts.homeWall.clear();
			Ts.metalWall.clear();
			Ts.homeTank.setLive(false);
			Ts.rst = false;Ts.Wrst = false;Ts.Wall = false;Ts.timer = false;
			Ts.homeTank = new Tank(300, 560, true, Direction.STOP, Ts,4);//重置自己出现的位置
			if (!Ts.home.isLive())  //重置home生命
				Ts.home.setLive(true);
			Window.setGoOn(1);
			//new Window(); //重新创建面板
			break;
		case KeyEvent.VK_T:  //当按下R时，重新开始游戏 
			Ts.PpLive.clear();
			Ts.PpTime.clear();
			Ts.PpShovel.clear();
			Ts.tanks.clear();  
			Ts.bullets.clear();
			Ts.trees.clear();
			Ts.brickWall.clear();
			Ts.homeWall.clear();
			Ts.metalWall.clear();
			Ts.homeTank.setLive(false);
			Ts.homeTank = new Tank(300, 560, true, Direction.STOP, Ts,4);//重置自己出现的位置
			if (!Ts.home.isLive())  //重置home生命
				Ts.home.setLive(true);
			Window.setGoOn(Window.getGoOn());
			//new Window(); //重新创建面板
			break;
		}
		/*case KeyEvent.VK_G: //无法实现 #-#/...QAQ
			Ts.giveLive = true;
			Ts.homeTank.setHP(200);
			Ts.homeTank.setLive(true);
			if (!Ts.home.isLive())  //重置home生命
				Ts.home.setLive(true);
			Window.setGoOn(Window.getGoOn());
			break;*/
	}
	public void keyReleased(KeyEvent e) {}
	public Bullets fire() {  //开火方法
		if (!live)
			return null;
		int x = this.x + 12;  //开火位置
		int y = this.y + 12;
		Bullets m = new Bullets(x, y + 2, notEnemy, Kdirection, this.Ts);  //没有给定方向时，向原来的方向发火
		Ts.bullets.add(m);                                                
		return m;
	}
	public boolean isHit(ObjectMgr obj) {
		if (this.live && this.getRect().intersects(obj.getRect())) {
			this.changToOldDir();    //转换到原来的方向上去
			return true;
		}
		return false;
	}
	@SuppressWarnings("unlikely-arg-type")
	public boolean isHitTanks(java.util.List<EnemyTank> tanks) {//撞到坦克时
		for (int i = 0; i < tanks.size(); i++) {
			EnemyTank t = tanks.get(i);
			if (!this.equals(t)) {
				if (this.live && t.isLive()
						&& this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}

	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isnotEnemy() {
		return notEnemy;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}
	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, 35, 35);
	}
	public void addKeyListener(KeyAdapter keyAdapter) {
	}
	public void requestFocus() {
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}