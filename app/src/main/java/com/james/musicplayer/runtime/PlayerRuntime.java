package com.james.musicplayer.runtime;

import android.content.Context;

import com.james.musicplayer.service.logic.SD;

import java.lang.ref.WeakReference;

/**
 * Runtime [V1.0.0]
 * classes : com.james.musicplayer.runtime.MusicPlayerRuntime
 * ̷���� Create at 2014/11/3 0003 9:47
 */
public class PlayerRuntime {

	/** SDK��Ĺ������The s sdk. */
	public static SDKHelper sSDK;

	/** The application. */
	public static Context application;

	/** �洢preference���� The s prefer. */
	public static PreferenceHelper sPrefer;

	/** ҵ������� The sd. */
	public static SD sSD;

	/** The sPlayerRuntime. */
	static PlayerRuntime sPlayerRuntime = null;

	/** The ref. */
	private WeakReference<Context> mReference = null;

	private PlayerRuntime(Context ctx)
	{
		mReference = new WeakReference<Context>(ctx);
	}

	public static synchronized PlayerRuntime ins(Context ctx)
	{
		if (ctx == null)
			return null;
		if (sPlayerRuntime == null)
		{
			sPlayerRuntime = new PlayerRuntime(ctx);
		}
		return sPlayerRuntime;
	}

	private static Thread sInitThread = new Thread(new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				sSD.init(sPlayerRuntime.application);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}, "sInitThread");

	// �Ƿ��ʼ����
	/** The m is init. */
	public static boolean mIsInit = false;
	/**
	 * ��ʼ�� Inits the.
	 */
	public void init()
	{
		// �������
		sPrefer = PreferenceHelper.ins(application);
		sSDK = SDKHelper.instance();
		sSD = SD.getInstance();
		synchronized (this)
		{
			if (!mIsInit)
			{
				sInitThread.start();
			}
			mIsInit = true;
		}
	}
}
