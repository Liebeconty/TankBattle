package com.zte.TBattle;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

import com.zte.engin.ObjectMgr;
//�ӵ�  
public class Bullets extends ObjectMgr{

	Direction diretion;
	private boolean notEnemy;
	private boolean live = true;
	private Window Ts;
	private static ImageIcon[] Img = new ImageIcon[4];
	private static Map<String, Image> imgs = new HashMap<String, Image>(); // ����Map��ֵ�ԣ���ͬ�����Ӧ��ͬ���ӵ�

	public Bullets(int x, int y,boolean notEnemy, Direction dir, Window window) { 
		this.x = x;
		this.y = y;
		this.diretion = dir;
		for(int i = 0;i < 4; i++) {
			Img[i] = new ImageIcon("images/bullet"+ i +".gif");

		}
		// ����Map����
		imgs.put("L", Img[0].getImage()); 
		imgs.put("U", Img[1].getImage());
		imgs.put("R", Img[2].getImage());
		imgs.put("D", Img[3].getImage());
		this.notEnemy = notEnemy;
		this.Ts = window;
	}
	public Rectangle getRect() { 
		return new Rectangle(x, y, 10, 10);
	}
	public void update() {
		switch (diretion) {
		case L:
			x -= 10; // �ӵ��������
			break;
		case U:
			y -= 10; // �ӵ��������
			break;
		case R:
			x += 10; // �ӵ��������
			break;
		case D:
			y += 10; // �ӵ��������
			break;
		case STOP:
			break;
		}
		if (x < 0 || y < 0 || x > 800 || y > 600) {//�ӵ�Խ�磬����ʧ
			live = false;
		}
	}
	public void show(Graphics g) {
		if (!live) {
			Ts.bullets.remove(this);
			return;
		}
		switch (diretion) { // ѡ��ͬ������ӵ�
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		default:
			break;
		}
		update();
	}
	public boolean hitTanks(List<EnemyTank> tanks) {// ���ӵ���̹��ʱ
		for (int i = 0; i < tanks.size(); i++) {
			if (isHit(tanks.get(i))) { // ��ÿһ��̹�ˣ�������ײ���
				return true;
			}
		}
		return false;
	}
	public boolean isHit(ObjectMgr obj) { // ���ӵ���̹����
		if(obj instanceof EnemyTank || obj instanceof Tank) {
			if (this.live && this.getRect().intersects(obj.getRect()) && obj.isLive()
					&& this.notEnemy != obj.isnotEnemy()) {
				BombTank e = new BombTank(obj.getX(), obj.getY(), Ts);
				Ts.bombTanks.add(e);
				if (obj.isnotEnemy()) {
					obj.setHP(obj.getHP() - 1); // ��һ���ӵ�����һ�Σ�4ǹ��GAMEOVER
					Ts.homeTank.setX(280);
					Ts.homeTank.setY(560);
					Ts.homeTank.setDirection(Direction.U);
					if (obj.getHP() <= 0) {
						obj.setLive(false); // ��������Ϊ0ʱ������Ϊ����״̬
						Ts.home.setLive(false);
					}
				} else {
					obj.setLive(false); 
				}
				this.live = false;
				return true; // ����ɹ�������true
			}
		}
		if(obj instanceof BrickWall) {
			if (this.live && this.getRect().intersects(obj.getRect())) {
				this.live = false;
				this.Ts.brickWall.remove(obj); // �ӵ���שǽ��ʱ,�Ƴ���ǽ
				this.Ts.homeWall.remove(obj);
				return true;
			}
		}
		if(obj instanceof MetalWall) {// �ӵ��򵽽���ǽ
			if (this.live && this.getRect().intersects(obj.getRect())) {
				this.live = false;
				return true;
			}
		}
		if(obj instanceof Home) {
			if (this.live && this.getRect().intersects(obj.getRect())) {
				this.live = false;
				obj.setLive(false); // ����(��ӥ)����һǹʱ������
				return true;
			}
		}
		return false; 
	}
}
