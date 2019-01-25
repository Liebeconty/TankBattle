package com.zte.engin;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


/*......对象管理......*/
public abstract class ObjectMgr {
	protected int x;   //
	protected int y;
	protected int oldX;// 获取碰撞前位置，碰撞后复位
	protected int oldY;
	private boolean live = true;//默认活着
	public static final int width = 44, length = 44;
	protected ImageIcon img;
	protected InputMgr inpt = InputMgr.getInput();
	//更新坐标
	public abstract void update();
	//碰撞检测
	public boolean isHit(ObjectMgr i) {
		return false;
	}
	//通过检测
	public boolean isPass(ObjectMgr i) {
		return false;
	}

	//初始化数据
	public  void init(int x,int y,String image) {
		this.x = x;
		this.y = y;
		this.img = new ImageIcon(image);	
	}
	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, width, length);
	}
	//显示图片
	public  void show(Graphics g) {
		g.drawImage(img.getImage(), x, y, null);
	}
	// 判读是否还活着
	public boolean isLive() { 
		return live;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public ImageIcon getImg() {
		return img;
	}
	public void setImg(ImageIcon img) {
		this.img = img;
	}
	public boolean isGood() {
		return false;
	}
	public int getHP() {
		return 0;
	}
	public void setHP(int i) {
			
	}
	public void setLive(boolean b) {
		this.live = b;
	}
	public boolean isnotEnemy() {
		// TODO Auto-generated method stub
		return false;
	}

}
