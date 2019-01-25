package com.zte.engin;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/*......场景管理......*/
public class SceneMgr extends Frame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//集合 
	protected List<ObjectMgr> list = new ArrayList<ObjectMgr>();
	public boolean gameOver = false;//游戏结束
	protected boolean gameWin = false;//游戏胜利
	public boolean gameStart = false;//游戏开始 
	protected int score = 0;
	Image screenImage = null;
	

	//回调函数
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for(ObjectMgr i : list) {
			i.show(g);
		}
		showOver(g);
		showScore(g);
		showBegin(g);
	}
	@Override
	public void update(Graphics g) {
		screenImage = this.createImage(800, 600);
		Graphics bg = screenImage.getGraphics();
		Color c = bg.getColor();
		bg.setColor(Color.BLACK);
		bg.fillRect(0, 0, 800, 600);
		bg.setColor(c);
		framPaint(bg);
		g.drawImage(screenImage, 0, 0, null);
	}
	public void framPaint(Graphics g) {
		
	}
	//碰撞检测
	public void hit() {
		for(ObjectMgr i : list) {
			for(ObjectMgr j : list) {
				if(!i.equals(j) && i.isHit(j) ) {
					gameOver = true;
				}
			}
		}
	}
	//统计分数
	public void scoring() {
		for(ObjectMgr i : list) {
			for(ObjectMgr j : list) {
				if(!i.equals(j) && i.isPass(j)) {
					score++;
				}
			}
		}
	}
	//更新坐标
	/*public void updateAll() {
		for(ObjectMgr o : list) {
			if(!gameOver && !gameStart) {
				o.update();
			}
		}
	}*/
	//显示结束界面
	public void showOver(Graphics g) {
		ImageIcon imgOver = new ImageIcon("images/gameover.png");
		ImageIcon imgWin = new ImageIcon("images/gameWin.png");
		if(gameOver) {
			g.drawImage(imgOver.getImage(),0,0,null);
		}
		if(gameWin) {
			g.drawImage(imgWin.getImage(),0,0,null);
		}
	}
	//显示开始页面
	public void showBegin(Graphics g) {
		ImageIcon img = new ImageIcon("images/GameStart.png");
		if(!gameStart) {
			g.drawImage(img.getImage(),0,0,null);
		}
	}
	public List<ObjectMgr> getList() {
		return list;
	}
	//显示分数
	public void showScore(Graphics g) {
		Font font = new Font("宋体",Font.BOLD,28);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(score + "",30, 30);
	}
}
