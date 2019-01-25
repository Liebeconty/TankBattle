package com.zte.engin;

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;


/*......�������......*/
public abstract class ObjectMgr {
	protected int x;   //
	protected int y;
	protected int oldX;// ��ȡ��ײǰλ�ã���ײ��λ
	protected int oldY;
	private boolean live = true;//Ĭ�ϻ���
	public static final int width = 44, length = 44;
	protected ImageIcon img;
	protected InputMgr inpt = InputMgr.getInput();
	//��������
	public abstract void update();
	//��ײ���
	public boolean isHit(ObjectMgr i) {
		return false;
	}
	//ͨ�����
	public boolean isPass(ObjectMgr i) {
		return false;
	}

	//��ʼ������
	public  void init(int x,int y,String image) {
		this.x = x;
		this.y = y;
		this.img = new ImageIcon(image);	
	}
	public Rectangle getRect() { // ����ָ�������ĳ�����ʵ��
		return new Rectangle(x, y, width, length);
	}
	//��ʾͼƬ
	public  void show(Graphics g) {
		g.drawImage(img.getImage(), x, y, null);
	}
	// �ж��Ƿ񻹻���
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
