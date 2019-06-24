package com.zte.TBattle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.zte.engin.InputMgr;
import com.zte.engin.SceneMgr;

//���� ����
public class Window extends SceneMgr {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int GoOn = 1;//�ؿ��ӵ�һ�ؿ�ʼ
	myMusicPlay audioPlayWave = new myMusicPlay("music/1.wav");
	//boolean giveLive = false;//����  ʧ��-_-||  @_@ QAQ
	Thread thread = new Thread(new PaintThread());
	public Window() {
		setSize(800, 600); // ���ý����С
		setLocation(280, 50); // ���ý�����ֵ�λ��	
		setResizable(false);
		setVisible(true);
		addKeyListener();
		thread.start();// �߳�����
		addWindowListener(new WindowAdapter() { // ���ڼ����ر�
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//��������
		audioPlayWave.start();
	}
	public static boolean printable = true;
	int delay = 0;          //ʱ������������ʱ�����
	boolean timer = false;  //
	boolean rst = false;    //	
	boolean Wall = false;	//���ӳ���ʱ�����
	boolean Wrst = false;
	int Wdelay = 0; 
	Tank homeTank = new Tank(280, 560, true, Direction.STOP, this,4);
	Home home = new Home(373, 540, this,"Images/home.png");
	List<River> theRiver = new ArrayList<River>();//����
	List<EnemyTank> tanks = new ArrayList<EnemyTank>();//̹��
	List<BombTank> bombTanks = new ArrayList<BombTank>();//̹�˱�ը
	List<Bullets> bullets = new ArrayList<Bullets>();//�ӵ�
	List<Tree> trees = new ArrayList<Tree>();//��
	List<BrickWall> homeWall = new ArrayList<BrickWall>(); //שǽ-��
	List<BrickWall> brickWall = new ArrayList<BrickWall>();//שǽ
	List<MetalWall> metalWall = new ArrayList<MetalWall>();//����ǽ
	List<MetalWall> homeMetal = new ArrayList<MetalWall>();//����ǽ-��
	List<PropLive> PpLive = new ArrayList<PropLive>();//����_����
	List<PropTime> PpTime = new ArrayList<PropTime>();//����_ʱ������
	List<PropShovel> PpShovel = new ArrayList<PropShovel>();//����_����

	public void framPaint(Graphics g) {
		if(!gameStart) {
			showBegin(g);
		}else {
			Color c = g.getColor();
			g.setColor(Color.WHITE); 
			Font f1 = g.getFont();
			g.setFont(new Font("����", Font.BOLD, 20));
			g.drawString("̹�˴�ս", 340, 50);
			g.setFont(new Font("����", Font.BOLD, 15));
			g.drawString("��"+ (GoOn - 1) +"��", 360, 70);
			g.setFont(new Font("����", Font.BOLD, 15));
			g.drawString("�����ڻ��ез�̹��: ", 2, 70);
			g.setFont(new Font("TimesRoman", Font.ITALIC, 15));
			g.drawString("" + tanks.size(), 155, 70);
			g.setFont(new Font("����", Font.BOLD, 15));
			g.drawString("�ҷ�ʣ��������: ", 640, 70);
			g.setFont(new Font("TimesRoman", Font.ITALIC, 15));
			g.drawString("" + homeTank.getHP(),770, 70);
			g.setFont(f1);
			if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
				//�������
				theRiver.clear();tanks.clear();bombTanks.clear();metalWall.clear();
				bullets.clear();trees.clear();homeWall.clear();brickWall.clear();
				PpLive.clear();PpTime.clear();PpShovel.clear();homeMetal.clear();
				if(GoOn > 7 ) {// �ж��Ƿ�Ӯ�ñ��� (���йؿ�����) && �Ƿ�ʼ��һ��
					gameWin = true;
					showOver(g);
				}else {//��ʼ��һ�أ������ó�ʼֵ
					rst = false;Wrst = false;Wall = false;timer = false;
					homeTank = new Tank(280, 560, true, Direction.STOP, this,homeTank.getHP());
					if(GoOn > 1) {
						try {
							Thread.sleep(200);//��һ�ؿ�ʼǰ����200ms
						} catch (Exception o) {
							o.printStackTrace();
						}
					}
					init();
					GoOn += 1;
				}
			}
			g.setColor(c);
			for (int i = 0; i < theRiver.size(); i++) {
				River r = theRiver.get(i);
				homeTank.isHit(r);
				r.show(g);
			}
			home.show(g); // ������
			homeTank.show(g);// �����Լ��ҵ�̹��
			if(timer) {//����ʱ������������ʱ��
				delay++;
				if(delay > 100) {
					delay = 0;
					timer = false;
					rst = true;
				}
			}
			for (int i = 0; i < bullets.size(); i++) { // ���ӵ��������ѭ���ж�
				Bullets m = bullets.get(i);
				m.hitTanks(tanks); // ÿһ���ӵ���̹����
				m.isHit(homeTank); // ÿһ���ӵ����Լ��ҵ�̹����ʱ
				m.isHit(home); // ÿһ���ӵ��򵽼���ʱ
				for (int j = 0; j < metalWall.size(); j++) { // ÿһ���ӵ��򵽽���ǽ��
					MetalWall mw = metalWall.get(j);
					m.isHit(mw);
				}
				for (int j = 0; j < brickWall.size(); j++) {// ÿһ���ӵ���שǽ��
					BrickWall w = brickWall.get(j);
					m.isHit(w);
				}
				for (int j = 0; j < homeWall.size(); j++) {// ÿһ���ӵ��򵽼ҵ�ǽ��
					BrickWall cw = homeWall.get(j);
					m.isHit(cw);
				}
				for (int j = 0; j < homeMetal.size(); j++) { // ÿһ���ӵ��򵽽���ǽ��
					MetalWall mw = homeMetal.get(j);
					mw.show(g);
					m.isHit(mw);
					homeTank.isHit(mw);
				}
				m.show(g); // ����Ч��ͼ
			}
			Random rp = new Random();
			if(rp.nextInt(50) > 48) {//�����������  ��Ϸ������ֹͣ����
				if(PpLive.size() == 0 && tanks.size() != 0 && home.isLive()) {//����_���� 
					PpLive.add(new PropLive());
				}
			}
			if(rp.nextInt(50) > 48) {//�����������
				if(PpShovel.size() == 0 && tanks.size() != 0 && home.isLive()) {//����_����
					PpShovel.add(new PropShovel());
				}
			}
			if(rp.nextInt(50) > 48) {//�����������
				if(PpTime.size() == 0 && tanks.size() != 0 && home.isLive()) {//����_ʱ������ 
					PpTime.add(new PropTime());
				}
			}
			for (int i = 0; i < tanks.size(); i++) { //��̹��
				EnemyTank t = tanks.get(i); // ��ü�ֵ�Եļ�
				for (int j = 0; j < homeWall.size(); j++) {
					BrickWall cw = homeWall.get(j);
					t.isHit(cw); // ÿһ��̹��ײ�������ǽʱ
				}
				for (int j = 0; j < brickWall.size(); j++) { // ÿһ��̹��ײ���������ǽ
					BrickWall cw = brickWall.get(j);
					t.isHit(cw);
				}
				for (int j = 0; j < metalWall.size(); j++) { // ÿһ��̹��ײ������ǽ
					MetalWall mw = metalWall.get(j);
					t.isHit(mw);
				}
				for (int j = 0; j < theRiver.size(); j++) {
					River r = theRiver.get(j); // ÿһ��̹��ײ������ʱ
					t.isHit(r);
				}
				t.isHitTanks(tanks); // ײ���Լ�����
				t.isHit(home);
				t.show(g);
				if(!timer){
					t.update();
				}
				for(int j = 0; j<PpTime.size(); j++) { //��������_ʱ������
					PropTime pt = PpTime.get(j);          
					pt.show(g);
					pt.isHit(homeTank);
					if(!pt.isLive() && !rst) {//����̹�˳Ե����ߣ��з�̹�˾�ֹһ��ʱ��
						timer = true;
					}
				}
			}
			for(int i = 0; i<PpLive.size(); i++) { //��������_����
				PropLive tp = PpLive.get(i);
				tp.show(g);
				tp.isHit(homeTank);
			}
			for (int i = 0; i < trees.size(); i++) { // ������
				Tree tr = trees.get(i);
				tr.show(g);
			}
			for (int i = 0; i < bombTanks.size(); i++) { // ������ըЧ��
				BombTank bt = bombTanks.get(i);
				bt.show(g);
			}
			for (int i = 0; i < brickWall.size(); i++) { // ������ǽ
				BrickWall cw = brickWall.get(i);
				cw.show(g);
			}
			for (int i = 0; i < metalWall.size(); i++) { // ��������ǽ
				MetalWall mw = metalWall.get(i);
				mw.show(g);
			}
			homeTank.isHitTanks(tanks);
			homeTank.isHit(home);
			for (int i = 0; i < metalWall.size(); i++) {// ײ������ǽ
				MetalWall w = metalWall.get(i);
				homeTank.isHit(w);
			}
			for (int i = 0; i < brickWall.size(); i++) {
				BrickWall cw = brickWall.get(i);
				homeTank.isHit(cw);
			}
			for (int i = 0; i < homeWall.size(); i++) { // �����̹��ײ���Լ���
				BrickWall w = homeWall.get(i);
				homeTank.isHit(w);
			}
		}
		if(Wall) {//���ò��ӳ���ʱ��
			Wdelay++;
			if(Wdelay > 200) {
				Wdelay = 0;
				Wall = false;
				Wrst = true;
			}
		}
		for(int j = 0; j<PpShovel.size(); j++) { //��������_����
			PropShovel pm = PpShovel.get(j);          
			pm.show(g);
			pm.isHit(homeTank);
			if(!pm.isLive() && homeTank.isLive() && !Wrst) {//�������̹�˳Ե����ӵ����ؽ���ӥ�ķ���Ϊ����ǽ
				for (int i = 0; i < 3; i++) { 
					homeMetal.add(new MetalWall(327 + 44 * i, 516-22,"Images/metalWall.gif"));
					homeMetal.add(new MetalWall(327, 516 + 22 * i,"Images/metalWall.gif"));
					homeMetal.add(new MetalWall(417, 516 + i * 22,"Images/metalWall.gif"));
				}
				Wall = true;
			}
			if(!Wall) {
				homeMetal.clear();
			}
		}
	}
	public void init() {
		for (int i = 0; i < 7; i++) { // �ҵĲ���
			if(i < 6)
				homeWall.add(new BrickWall(328 + 22 * i, 516-22,"Images/BrickWall.gif"));
			if (i < 4) {
				homeWall.add(new BrickWall(350, 516 + 22 * i,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(328, 516 + 22 * i,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(416, 516 + i * 22,"Images/BrickWall.gif"));
				homeWall.add(new BrickWall(438, 516 + i * 22,"Images/BrickWall.gif"));
			}else if (i < 7) 
				homeWall.add(new BrickWall(372 + 22 * (i - 4), 516,"Images/BrickWall.gif"));
		}

		//�ؿ�һ��
		if(GoOn == 1) {
			for(int i = 0;i < 10; i++) {//שǽ�Ĳ���
				for (int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(374 + 22 * i, 224 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(374 - 22 * (i-1), 224 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(374 + 22 * j, 224 + 22 * (i - 4) ,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(460 + 22 * i, 268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(308 - 22 * i, 268 + 22 * j, "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 3; i++) { // ����ǽ����
				metalWall.add(new MetalWall(2 + 80 * i, 558,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(42 + 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(762 - 80 * i, 558,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(722 - 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(374 + 44 * (i - 1), 356,"Images/metalWall.gif"));
			}
			for(int i = 0; i < 3; i++) {//���Ĳ���
				trees.add(new Tree(2,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(46,124,"Images/tree.gif"));
				trees.add(new Tree(762,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(762 - 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(718,124,"Images/tree.gif"));
				trees.add(new Tree(2,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,350,"Images/tree.gif"));
				trees.add(new Tree(46,306,"Images/tree.gif"));
				trees.add(new Tree(762,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(762 - 44 * i,350,"Images/tree.gif"));
				trees.add(new Tree(718,306,"Images/tree.gif"));	
				if(i < 2) {
					trees.add(new Tree(330 + 88 * i,180,"Images/tree.gif"));
					trees.add(new Tree(330,180 + 88 * i,"Images/tree.gif"));
				}
				trees.add(new Tree(418,268,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(308, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river1);
			River river2 = new River();
			river2.init(396, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river2);
		}
		//�ؿ�����
		if(GoOn == 2) {
			for (int i = 0; i < 3; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(140+22*j,529+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(500+22*j,529+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22*j,309+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22*j,375+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(495+22*j,310+22*i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(495+22*j,310+22*3,"Images/BrickWall.gif"));
				}		
			}
			for(int i = 0;i < 10; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(360+22+22*(i-1),375+22+22*j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(360+22-22*i,375+22+22*j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(140+66-22*j,375+22-22*(i-3),"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(500+58-22*j,375+22-22*(i-3),"Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 2; i++) { // ����ǽ����
				metalWall.add(new MetalWall(316 + 44 * i,441,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(272 + 44 * i,309,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(272 + 176 * i,353,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(404 + 44 * i,309,"Images/metalWall.gif"));
			}

			for (int i = 0; i < 4; i++) { // ���Ĳ���
				trees.add(new Tree(140 + 44 * i, 485,"Images/tree.gif"));
				trees.add(new Tree(500 + 44 * i, 485,"Images/tree.gif"));
				trees.add(new Tree(632, 441,"Images/tree.gif"));
				trees.add(new Tree(96, 441,"Images/tree.gif"));
				trees.add(new Tree(316-44*2,309,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(272, 309-44,"Images/river.jpg");
			theRiver.add(river1);
			River river2 = new River();
			river2.init(272+44*2, 309-44,"Images/river.jpg");
			theRiver.add(river2);
			River river3 = new River();
			river3.init(272+44*4, 309-44,"Images/river.jpg");
			theRiver.add(river3);
		}
		//�ؿ�����
		if(GoOn == 3) {
			for (int i = 0; i < 48; i++) {//שǽ����
				if (i < 18) {
					brickWall.add(new BrickWall(200 + 22 * i, 300, "Images/BrickWall.gif")); // ��ͨǽ����
					brickWall.add(new BrickWall(200 + 22 * i, 200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(200 + 22 * i , 320, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(200 + 22 * i , 220, "Images/BrickWall.gif"));

				} else if (i < 32) {
					brickWall.add(new BrickWall(220, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(240, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(540, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(520, 420 + 22 * (i - 18), "Images/BrickWall.gif"));
				}else if (i < 40){
					brickWall.add(new BrickWall(0 + 22 * (i - 32), 100, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(640 + 22 * (i - 32), 100, "Images/BrickWall.gif"));


				}else if (i < 48){
					brickWall.add(new BrickWall(160,  140+22 * (i - 40), "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(620,  140+22 * (i - 40), "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 8; i++) { // ����ǽ����
				metalWall.add(new MetalWall(160,556, "Images/metalWall.gif"));
				metalWall.add(new MetalWall(580,556, "Images/metalWall.gif"));
				if (i < 2) {
					metalWall.add(new MetalWall(250,100+44*i, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(500,100+44*i, "Images/metalWall.gif"));
				}else if (i < 3){
					metalWall.add(new MetalWall(0+44*(i-2),450, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(760+44*(i-2),450, "Images/metalWall.gif"));
				}else if (i < 4){
					metalWall.add(new MetalWall(80+44*(i-3),500,"Images/metalWall.gif"));
					metalWall.add(new MetalWall(660+44*(i-3),500, "Images/metalWall.gif"));
				}else if (i < 8)
					metalWall.add(new MetalWall(310+44*(i-4),250, "Images/metalWall.gif"));

			}

			for (int i = 0; i < 4; i++) { // ���Ĳ���
				if (i < 4) {
					trees.add(new Tree(0 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(210 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(429 + 44 * i, 360 , "Images/tree.gif"));
					trees.add(new Tree(648 + 44 * i, 360, "Images/tree.gif"));
					trees.add(new Tree(310 + 44 * i, 120 , "Images/tree.gif"));
					trees.add(new Tree(310 + 44 * i, 84 , "Images/tree.gif"));

				}

			}
			River river1 = new River();
			river1.init(65, 150,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river1);
			River river3 = new River();
			river3.init(65, 194,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river3);
			River river2 = new River();
			river2.init(655, 150,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river2);
			River river4 = new River();
			river4.init(655, 194,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river4);
		}
		//�ؿ��ģ�
		if(GoOn == 4) {
			for(int i = 0;i < 10; i++) {//שǽ�Ĳ���
				for (int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(450 + 22 * i, 324 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(450 - 22 * (i-1), 324 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(450 + 22 * j, 324 + 22 * (i - 3) ,"Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 3; i++) { // ����ǽ����
				metalWall.add(new MetalWall(80 + 80 * i, 358,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(220 + 80 * i, 158,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(562 - 80 * i, 158,"Images/metalWall.gif"));	
				metalWall.add(new MetalWall(690- 80 * i, 358,"Images/metalWall.gif"));
			}
			for(int i = 0; i < 3; i++) {//���Ĳ���
				trees.add(new Tree(0,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(665 + 44 * i,80,"Images/tree.gif"));
				trees.add(new Tree(755,80 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(2 + 44 * i,358,"Images/tree.gif"));
				trees.add(new Tree(562,350 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(755 - 44 * i,358,"Images/tree.gif"));
				trees.add(new Tree(560,175,"Images/tree.gif"));	
				if(i < 2) {
					trees.add(new Tree(330 + 88 * i,180,"Images/tree.gif"));
					trees.add(new Tree(330,180 + 88 * i,"Images/tree.gif"));
				}
				trees.add(new Tree(418,268,"Images/tree.gif"));
			}
			River river1 = new River();
			river1.init(215, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river1);
			River river2 = new River();
			river2.init(225 + 100, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river2);
			River river3 = new River();
			river3.init(255 + 100, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river3);
			River river4 = new River();
			river4.init(455 + 100, 400,"Images/river.jpg");//��ˮ�Ĳ���
			theRiver.add(river4);
		}
		//�ؿ��壺
		if(GoOn == 5) {
			for(int i = 0;i < 2; i++) {
				trees.add(new Tree(0 , 180 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(0 , 268,"Images/tree.gif"));
				trees.add(new Tree(44 , 224,"Images/tree.gif"));
				trees.add(new Tree(264 + 44 * (i + 1),312,"Images/tree.gif"));
				trees.add(new Tree(264,312 + 44 * i,"Images/tree.gif"));
				trees.add(new Tree(528,312 - 44 * i,"Images/tree.gif"));
				trees.add(new Tree(704 - 44 * i,22 + 44 * i,"Images/tree.gif"));

			}
			for(int i = 0;i < 8; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(44 + 22 * i,268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(44 + 22 * j,356 + 22 * i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(572 + 22 * i,268 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(572 + 22 * j,268 + 22 * i,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(616 + 22 * i,110 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(484 + 22 * j,224 + 22 * (i + 2),"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(44 + 22 * i , 180 + 22 * j,"Images/BrickWall.gif"));
					if(i < 6) {
						brickWall.add(new BrickWall(266 + 22 * j,290 - 22 * i,"Images/BrickWall.gif"));
					}
					brickWall.add(new BrickWall(310 + 22 * i,180 + 22 * j,"Images/BrickWall.gif"));
				}
				trees.add(new Tree(750,312 + 44 * i,"Images/tree.gif"));
			}
			for(int i = 0;i < 13; i++) {
				for(int j = 0; j < 2; j++) {
					brickWall.add(new BrickWall(220 + 22 * i,400 + 22 * j,"Images/BrickWall.gif"));
					brickWall.add(new BrickWall(704 + 22 * j,312 + 22 * i,"Images/BrickWall.gif"));
				}
			}
			for(int i = 0;i < 2; i++) {
				metalWall.add(new MetalWall(220,312 + 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(396 + 44 * i,312 - 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(484 + 44 * i,180- 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(352 + 44 * i,224,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(44,532 + 44 * i,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(44,22 + 44 * i,"Images/metalWall.gif"));
			}
		}
		//�ؿ���:
		if(GoOn == 6) {
			for (int i = 0; i < 15; i++) {
				if (i < 5) {
					brickWall.add(new BrickWall(0 + 22 * i, 300, "Images/BrickWall.gif")); // ��ͨǽ����
					brickWall.add(new BrickWall(175 + 22 * i, 300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * i ,300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * i ,300, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * i ,300, "Images/BrickWall.gif"));

				} else if (i < 10) {
					brickWall.add(new BrickWall(0 + 22 * (i-5), 200, "Images/BrickWall.gif")); // ��ͨǽ����
					brickWall.add(new BrickWall(175 + 22 *(i-5), 200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * (i-5) ,200, "Images/BrickWall.gif"));
				} else if (i < 15) {
					brickWall.add(new BrickWall(0 + 22 * (i-10), 400, "Images/BrickWall.gif")); // ��ͨǽ����
					brickWall.add(new BrickWall(175 + 22 * (i-10), 400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(350 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(525 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
					brickWall.add(new BrickWall(700 + 22 * (i-10) ,400, "Images/BrickWall.gif"));
				}
			}
			for (int i = 0; i < 5; i++) { // ����ǽ����
				if (i < 2) {
					metalWall.add(new MetalWall(55,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(186+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(361+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(536+44*i,156, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(700,156, "Images/metalWall.gif"));
				}else if (i < 4){
					metalWall.add(new MetalWall(80+44*(i-3),500,"Images/metalWall.gif"));
					metalWall.add(new MetalWall(660+44*(i-3),500, "Images/metalWall.gif"));
				}else if (i < 5){
					metalWall.add(new MetalWall(295+44*(i-4),500, "Images/metalWall.gif"));
					metalWall.add(new MetalWall(450+44*(i-4),500, "Images/metalWall.gif"));
				}
			}
			for (int i = 0; i < 7; i++) { // ���Ĳ���
				if (i < 3) {
					trees.add(new Tree(145 + 44 * i, 500 , "Images/tree.gif"));
					trees.add(new Tree(510 + 44 * i,500 , "Images/tree.gif"));
				}else if (i<7){
					trees.add(new Tree(120,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(295,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(470,100+44*i , "Images/tree.gif"));
					trees.add(new Tree(645,100+44*i , "Images/tree.gif"));
				}
			}
		}
		//�ؿ��ߣ�
		if(GoOn == 7) {
			//שǽ�Ĳ���
			brickWall.add(new BrickWall(389 , 364 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(389,313,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall( 389,262 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(389,211 ,"Images/BrickWall.gif"));
			brickWall.add(new BrickWall(285,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,313 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(337,160 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,262 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,160 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(441,313 , "Images/BrickWall.gif"));
			brickWall.add(new BrickWall(493,262, "Images/BrickWall.gif"));

			for (int i = 0; i < 3; i++) { // ����ǽ����
				metalWall.add(new MetalWall(2 + 80 * i, 558,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(290 + 80 * i, 458,"Images/metalWall.gif"));
				metalWall.add(new MetalWall(762 - 80 * i, 558,"Images/metalWall.gif"));	
			}
			for(int i = 0; i < 8; i++) {//���Ĳ���
				trees.add(new Tree(44,148+44*i,"Images/tree.gif"));
				trees.add(new Tree(712,148+ 44 * i,"Images/tree.gif"));
				if(i<6) {
					trees.add(new Tree(88,236+ 44 * i,"Images/tree.gif"));
					trees.add(new Tree(668,236+ 44 * i,"Images/tree.gif"));
				}
				if(i<4) {
					trees.add(new Tree(132,324+44*i,"Images/tree.gif"));
					trees.add(new Tree(624,324+44*i,"Images/tree.gif"));
				}
				if(i<2) {
					trees.add(new Tree(176,412+ 44 * i,"Images/tree.gif"));
					trees.add(new Tree(580,412+ 44 * i,"Images/tree.gif"));
				}
			}
		}
		// ��ʼ��̹������ ��һ��Ϊ10��������� n �� + 2(n-1)��,������̹�˳��ֵ�λ��
		for (int i = 0; i < 10 + (GoOn - 1) * 2; i++) { 
			if (i < 9)  
				tanks.add(new EnemyTank(150 + 70 * i, 40, false, Direction.D, this));
			else if (i < 15)
				tanks.add(new EnemyTank(750, 40 + 50 * (i - 6) , false, Direction.D,
						this));
			else
				tanks.add(new EnemyTank(10, 50 * (i - 12), false, Direction.D,
						this));
		}	
	}

	private class PaintThread implements Runnable {
		public void run() {
			// TODO Auto-generated method stub
			while (printable) {
				repaint();
				try {
					Thread.sleep(50);//50����ѭ��һ��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	//���̼���
	public void addKeyListener() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				homeTank.keyPressed(e);
				InputMgr.getInput().setKeyStatus(e, true);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				homeTank.keyReleased(e);
				InputMgr.getInput().setKeyStatus(e, false);
			}
		});
		//��ü��̵Ľ���
		requestFocus();
	}
	public void actionPerformed(ActionEvent e) {}
	public static int getGoOn() {
		return GoOn;
	}
	public static void setGoOn(int goOn) {
		GoOn = goOn;
	}
}
//�Կ�ʹ @_@  д������