package com.james.musicplayer.util;

import android.app.Application;
import android.util.Log;
import com.james.musicplayer.runtime.PlayerRuntime;
import com.james.musicplayer.runtime.SDCardUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * SDKIniter [V1.0.0]
 * SDK��ʼ������
 * classes : com.james.musicplayer.util.SDKIniter
 * ̷���� Create at 2014/11/3 0003 9:27
 */
public class SDKIniter extends Thread {
	private static final String TAG = "SDKIniter";
	private Application application;

	private static Set<Runnable> listeners = new HashSet<Runnable>();
	private static boolean isInited = false;

	public SDKIniter(Application application) {
		this.application = application;
	}

	@Override
	public void run() {
		Log.d(TAG, "��ʼ����sdk����");

		SDCardUtil.checkSDCard(application);
		PlayerRuntime.application = application;
		PlayerRuntime.ins(application).init();
		doListener();
		setInited(true);
	}

	/**
	 * �������,ִ�м�����
	 */
	public void doListener() {
		for (Runnable run : getListeners()) {
			run.run();
		}
		getListeners().clear();
	}

	public synchronized static boolean isInited() {
		return isInited;
	}

	public synchronized static void setInited(boolean isInited) {
		SDKIniter.isInited = isInited;
	}

	public synchronized static Set<Runnable> getListeners() {
		return listeners;
	}
}
