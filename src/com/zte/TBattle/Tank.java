package com.zte.TBattle;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import com.zte.engin.ObjectMgr;
//����̹��
public class Tank extends ObjectMgr{
	public static int count = 0;
	private Direction direction = Direction.STOP; // ��ʼ��״̬Ϊ��ֹ
	private Direction Kdirection = Direction.U; // ��ʼ������Ϊ����
	Window Ts;
	private boolean notEnemy; // ���ֵ���̹��
	private boolean live = true; // ��ʼ��Ϊ����
	private int HP; // ��ʼ����
	private ImageIcon[] imgs = new ImageIcon[4]; 
	public Tank(int x, int y, boolean notEnemy, Direction dir, Window Ts,int hp) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.HP = hp;
		this.notEnemy = notEnemy;
		this.direction = dir;
		this.Ts = Ts;
		for(int i = 0;i < 4; i++) {
			imgs[i]= new ImageIcon("Images/MT"+ i +".png");
		}
	}
	public void show(Graphics g) {
		if (!live) {
			return;
		}
		switch (Kdirection) {//���ݷ���ѡ��̹�˵�ͼƬ
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
		update();
	}
	public void update() {
		this.oldX = x;
		this.oldY = y;
		if(inpt.getKeyStatus(KeyEvent.VK_RIGHT)) {//�Ҽ���Ӧ
			x += 6;y = oldY;
			direction = Direction.R;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_LEFT)) {//�����Ӧ
			x -= 6;y = oldY;
			direction = Direction.L;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_UP)) {//�ϼ���Ӧ
			y -= 6;x = oldX;
			direction = Direction.U;
		}
		if(inpt.getKeyStatus(KeyEvent.VK_DOWN)) {//�¼���Ӧ
			y += 6;x = oldX;
			direction = Direction.D;
		}if (this.direction != Direction.STOP) {
			this.Kdirection = this.direction;
		}
		if (x < 0)//��ֹԽ��
			x = 0;
		if (y < 40)      
			y = 40;
		if (x > 770)  
			x = 770;
		if (y > 570)
			y = 570;//
	}
	private void changToOldDir() {  //������ײǰλ��
		x = oldX;
		y = oldY;
	}
	public void keyPressed(KeyEvent e) {  //���ܼ����¼�
		int key = e.getKeyCode();
		switch (key) {
		/*case KeyEvent.VK_M:
			Ts.audioPlayWave.stop();//M���ر�BGM,ò����BUG   QAQ
			break;*/
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_Q:
			Ts.gameStart=true;
		case KeyEvent.VK_P://P����ͣ(������ͣ���ɿ�50ms����......)*_V..
			try {
				Thread.sleep(50);
			} catch (Exception o) {
				o.printStackTrace();
			}break;
			//window.thread.wait();
		case KeyEvent.VK_R:  //������Rʱ�����¿�ʼ��Ϸ ,���ò���
			Ts.PpLive.clear();
			Ts.PpTime.clear();
			Ts.PpShovel.clear();
			Ts.tanks.clear();  
			Ts.bullets.clear();
			Ts.trees.clear();
			Ts.brickWall.clear();
			Ts.homeWall.clear();
			Ts.metalWall.clear();
			Ts.homeTank.setLive(false);
			Ts.rst = false;Ts.Wrst = false;Ts.Wall = false;Ts.timer = false;
			Ts.homeTank = new Tank(300, 560, true, Direction.STOP, Ts,4);//�����Լ����ֵ�λ��
			if (!Ts.home.isLive())  //����home����
				Ts.home.setLive(true);
			Window.setGoOn(1);
			//new Window(); //���´������
			break;
		case KeyEvent.VK_T:  //������Rʱ�����¿�ʼ��Ϸ 
			Ts.PpLive.clear();
			Ts.PpTime.clear();
			Ts.PpShovel.clear();
			Ts.tanks.clear();  
			Ts.bullets.clear();
			Ts.trees.clear();
			Ts.brickWall.clear();
			Ts.homeWall.clear();
			Ts.metalWall.clear();
			Ts.homeTank.setLive(false);
			Ts.homeTank = new Tank(300, 560, true, Direction.STOP, Ts,4);//�����Լ����ֵ�λ��
			if (!Ts.home.isLive())  //����home����
				Ts.home.setLive(true);
			Window.setGoOn(Window.getGoOn());
			//new Window(); //���´������
			break;
		}
		/*case KeyEvent.VK_G: //�޷�ʵ�� #-#/...QAQ
			Ts.giveLive = true;
			Ts.homeTank.setHP(200);
			Ts.homeTank.setLive(true);
			if (!Ts.home.isLive())  //����home����
				Ts.home.setLive(true);
			Window.setGoOn(Window.getGoOn());
			break;*/
	}
	public void keyReleased(KeyEvent e) {}
	public Bullets fire() {  //���𷽷�
		if (!live)
			return null;
		int x = this.x + 12;  //����λ��
		int y = this.y + 12;
		Bullets m = new Bullets(x, y + 2, notEnemy, Kdirection, this.Ts);  //û�и�������ʱ����ԭ���ķ��򷢻�
		Ts.bullets.add(m);                                                
		return m;
	}
	public boolean isHit(ObjectMgr obj) {
		if (this.live && this.getRect().intersects(obj.getRect())) {
			this.changToOldDir();    //ת����ԭ���ķ�����ȥ
			return true;
		}
		return false;
	}
	@SuppressWarnings("unlikely-arg-type")
	public boolean isHitTanks(java.util.List<EnemyTank> tanks) {//ײ��̹��ʱ
		for (int i = 0; i < tanks.size(); i++) {
			EnemyTank t = tanks.get(i);
			if (!this.equals(t)) {
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

	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	public boolean isnotEnemy() {
		return notEnemy;
	}
	public int getHP() {
		return HP;
	}
	public void setHP(int HP) {
		this.HP = HP;
	}
	public Rectangle getRect() { // ����ָ�������ĳ�����ʵ��
		return new Rectangle(x, y, 35, 35);
	}
	public void addKeyListener(KeyAdapter keyAdapter) {
	}
	public void requestFocus() {
	}
	public void setDirection(Direction direction) {
		this.direction = direction;
	}

}