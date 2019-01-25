package com.zte.engin;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

/*......���̿���ϵͳ......*/
public class InputMgr {
	//���ϣ���˹ͼ
	private Map<Integer,Boolean> keyStatus;
	private static InputMgr inpt;
	
	//����ģʽ��ȷ��һ����ֻ��һ������
	public static InputMgr getInput() {//static:ȷ������������ֻ��һ��
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
	//���value
	public boolean getKeyStatus(int keyCode) {
		return keyStatus.get(keyCode);
	}
	//ͨ������������Ӧ��ֵ
	public void setKeyStatus(KeyEvent e, Boolean value) {
		this.keyStatus.put(e.getKeyCode(), value);
	}
}
