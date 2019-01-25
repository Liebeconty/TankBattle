package com.zte.TBattle;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;
import javax.swing.ImageIcon;
import com.zte.engin.ObjectMgr;
//�з�̹��
public class EnemyTank extends ObjectMgr{
	public static int count = 0;
	private Direction direction = Direction.STOP; // ��ʼ��״̬Ϊ��ֹ
	private Direction Kdirection = Direction.U; // ��ʼ������Ϊ����
	Window Ts;
	private boolean notEnemy; //���ֵ���̹��
	private boolean live = true; // ��ʼ��Ϊ����
	private static Random r = new Random();
	private int step = r.nextInt(10)+5 ; // ����һ�������,���ģ��̹�˵��ƶ�·��

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
				Ts.tanks.remove(this); // ɾ����Ч��̹��
			}
			return;
		}
		switch (Kdirection) {
		//���ݷ���ѡ��̹�˵�ͼƬ
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
		this.oldX = x;//��ȡ��ǰ����
		this.oldY = y;
		switch (direction) {  //ѡ���ƶ�����
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
		if (y < 40)      //��ֹ�߳��涨����
			y = 40;
		if (x > 770)  //����������ָ����߽�
			x = 770;
		if (y > 570)
			y = 570;
		Direction[] directons = Direction.values();
		if (step == 0) {                  
			step = r.nextInt(12) + 3;  //�������·��
			int rn = r.nextInt(directons.length);
			direction = directons[rn];      //�����������
		}
		step--;
		if (r.nextInt(40) > 38)//��������������Ƶ��˿���Ƶ��
			this.fire();
	}
	void changToOldDir() {  
		x = oldX;
		y = oldY;
	}
	public Bullets fire() {  //���𷽷�
		if (!live)
			return null;
		int x = this.x + 10;  //����λ��
		int y = this.y + 10;
		Bullets m = new Bullets(x, y + 2, notEnemy, Kdirection, this.Ts);  //û�и�������ʱ����ԭ���ķ��򷢻�
		Ts.bullets.add(m);                                                
		return m;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isHit(ObjectMgr obj) {  
		if (this.live && this.getRect().intersects(obj.getRect())) {
			this.changToOldDir();    //ת����ԭ���ķ�����ȥ
			return true;
		}
		return false;
	}
	public boolean isHitTanks(java.util.List<EnemyTank> tanks) {//ײ��̹��ʱ
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
	public Rectangle getRect() { // ����ָ�������ĳ�����ʵ��
		return new Rectangle(x, y, 35, 35);
	}
}