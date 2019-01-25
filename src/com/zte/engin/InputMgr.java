package com.zte.engin;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/*......键盘控制系统......*/
public class InputMgr {
	//集合，哈斯图
	private Map<Integer,Boolean> keyStatus;
	private static InputMgr inpt;
	
	//单例模式，确保一个类只造一个对象
	public static InputMgr getInput() {//static:确保方法、变量只有一个
		if(inpt == null) {
			inpt = new InputMgr();
			inpt.init();
		}
		return inpt;
	}
	
	public void init() {
		keyStatus = new HashMap<Integer,Boolean>();
		for(int i = 0;i < 127; i++) {
			keyStatus.put(i, false);
		}
	}
	//获得value
	public boolean getKeyStatus(int keyCode) {
		return keyStatus.get(keyCode);
	}
	//通过键，重置相应的值
	public void setKeyStatus(KeyEvent e, Boolean value) {
		this.keyStatus.put(e.getKeyCode(), value);
	}
}
