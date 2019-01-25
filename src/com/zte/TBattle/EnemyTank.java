package com.zte.TBattle;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import javax.swing.ImageIcon;
import com.zte.engin.ObjectMgr;
//敌方坦克
public class EnemyTank extends ObjectMgr{
	public static int count = 0;
	private Direction direction = Direction.STOP; // 初始化状态为静止
	private Direction Kdirection = Direction.U; // 初始化方向为向上
	Window Ts;
	private boolean notEnemy; //区分敌我坦克
	private boolean live = true; // 初始化为活着
	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; // 产生一个随机数,随机模拟坦克的移动路径

	private ImageIcon[] imgs = new ImageIcon[4]; 
	public EnemyTank(int x, int y, boolean notEnemy, Direction dir, Window window) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.notEnemy = notEnemy;
		this.direction = dir;
		this.Ts = window;
		for(int i = 0;i < 4; i++) {
			imgs[i]= new ImageIcon("Images/tank"+ i +".gif");
		}
	}
	public void show(Graphics g) {
		if (!live) {
			if (!notEnemy) {
				Ts.tanks.remove(this); // 删除无效的坦克
			}
			return;
		}
		switch (Kdirection) {
		//根据方向选择坦克的图片
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
		//update(); 
	}

	public void update() {
		this.oldX = x;//获取当前坐标
		this.oldY = y;
		switch (direction) {  //选择移动方向
		case L:
			x -= 6;
			break;
		case U:
			y -= 6;
			break;
		case R:
			x += 6;
			break;
		case D:
			y += 6;
			break;
		case STOP:
			break;
		}
		if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}
		if (x < 0)
			x = 0;
		if (y < 40)      //防止走出规定区域
			y = 40;
		if (x > 770)  //超过区域则恢复到边界
			x = 770;
		if (y > 570)
			y = 570;
		Direction[] directons = Direction.values();
		if (step == 0) {                  
			step = r.nextInt(12) + 3;  //产生随机路径
			int rn = r.nextInt(directons.length);
			direction = directons[rn];      //产生随机方向
		}
		step--;
		if (r.nextInt(40) > 38)//产生随机数，控制敌人开火频率
			this.fire();
	}
	void changToOldDir() {  
		x = oldX;
		y = oldY;
	}
	public Bullets fire() {  //开火方法
		if (!live)
			return null;
		int x = this.x + 10;  //开火位置
		int y = this.y + 10;
		Bullets m = new Bullets(x, y + 2, notEnemy, Kdirection, this.Ts);  //没有给定方向时，向原来的方向发火
		Ts.bullets.add(m);                                                
		return m;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isHit(ObjectMgr obj) {  
		if (this.live && this.getRect().intersects(obj.getRect())) {
			this.changToOldDir();    //转换到原来的方向上去
			return true;
		}
		return false;
	}
	public boolean isHitTanks(java.util.List<EnemyTank> tanks) {//撞到坦克时
		for (int i = 0; i < tanks.size(); i++) {
			EnemyTank t = tanks.get(i);
			if (this != t) {
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
	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, 35, 35);
	}
}