package com.zte.TBattle;

import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;

import com.zte.engin.ObjectMgr;

//����_���ӣ�  �ؽ���ӥ�ķ���Ϊ����ǽ
public class PropShovel extends ObjectMgr{
	Random r =new Random();
	Window Ts;
	boolean live = true;
	ImageIcon img = new ImageIcon("images/Metal.png");

	public PropShovel() {
		this.x = 50+r.nextInt(600);
		this.y = 50+r.nextInt(400);
	}
	public boolean isHit(Tank obj){
		if(this.live&&this.getRect().intersects(obj.getRect())){
			this.live = false;
			return true;
		}
		return false;
	}
	public void show(Graphics g) {
		if(!live) {
			return;
		}else {
			g.drawImage(img.getImage(),x,y,null);
		}

	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub

	}
}
