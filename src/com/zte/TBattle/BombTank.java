package com.zte.TBattle;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import com.zte.engin.ObjectMgr;
//Ì¹¿Ë±¬Õ¨
public class BombTank extends ObjectMgr{

	private boolean live = true; 
	private Window Ts;
	private static ImageIcon[] imgs = new ImageIcon[10];
	int step = 0;

	public BombTank(int x, int y, Window tc) { 
		this.x = x;
		this.y = y;
		this.Ts = tc;
		for(int i = 1;i < 11; i++) {
			imgs[i-1] = new ImageIcon("images/" + i + ".gif");
		}
	}
	public void show(Graphics g) { // »­³ö±¬Õ¨Í¼Ïñ
		if (!live) { // Ì¹¿ËÏûÊ§ºóÉ¾³ý±¬Õ¨Í¼Ïñ
			Ts.bombTanks.remove(this);
			return;
		}
		if (step == imgs.length) {
			live = false;
			step = 0;
			return;
		}
		g.drawImage(imgs[step].getImage(), x + 5, y + 5, null);
		step++;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub	
	}
}
