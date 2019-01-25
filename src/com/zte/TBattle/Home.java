package com.zte.TBattle;

import java.awt.Graphics;

import javax.swing.ImageIcon;

import com.zte.engin.ObjectMgr;
//家
public class Home extends ObjectMgr{
	private Window Ts;
	private boolean live = true;
	private ImageIcon homeImags = new ImageIcon("Images/home.png");

	public Home(int x, int y, Window window,String path) {
		init(x,y,path);
		this.Ts=window;
	}
	public void show(Graphics g) {
		if (live) { // 如果活着，则画出home
			g.drawImage(homeImags.getImage(), x, y, null);
			for (int i = 0; i < Ts.homeWall.size(); i++) {
				BrickWall w = Ts.homeWall.get(i);
				w.show(g);
			}
		} else {
			Ts.tanks.clear();// 作清理页面工作
			Ts.PpLive.clear();
			Ts.PpTime.clear();
			Ts.PpShovel.clear();
			Ts.metalWall.clear();
			Ts.homeWall.clear();
			Ts.brickWall.clear();
			Ts.bombTanks.clear();
			Ts.theRiver.clear();
			Ts.trees.clear();
			Ts.bullets.clear();
			Ts.homeTank.setLive(false);
			Ts.gameOver = true;
			Ts.showOver(g);
		}
	}

	public boolean isLive() { // 判读是否还活着
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
