package com.zte.TBattle;
import java.awt.Rectangle;
import com.zte.engin.ObjectMgr;
//שǽ
public class BrickWall extends ObjectMgr{
	public BrickWall(int x, int y,String img) { 
		init(x,y,img);
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}
	public Rectangle getRect() { 
		return new Rectangle(x, y, 22, 22);
	}
}
