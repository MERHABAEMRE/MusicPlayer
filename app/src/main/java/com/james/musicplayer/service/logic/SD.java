package com.james.musicplayer.service.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.james.musicplayer.runtime.PlayerRuntime;
import com.james.musicplayer.service.MusicPlayService;

/**
 * SD [V1.0.0]
 * ҵ���߼�������
 * classes : com.james.musicplayer.service.logic.SD
 * ̷���� Create at 2014/11/3 0003 10:52
 */
public class SD {

	/**
	 * The m sd.
	 */
	private static SD mSD;

	/**
	 * The m context.
	 */
	private Context mContext;

	/**
	 * The is init logic.
	 */
	private boolean isInitLogic = false;

	/** ������. */
	private MusicPlayer mMusicPlayer;

	/**
	 * Instantiates a new sd.
	 */
	private SD() {
	}

	/**
	 * Gets the single instance of SD.
	 *
	 * @return single instance of SD
	 */
	public static SD getInstance() {
		synchronized (SD.class) {

			if (mSD == null) {
				mSD = new SD();
			}
			return mSD;
		}
	}

	/**
	 * ��ʼ��.
	 *
	 * @param context the context
	 * @throws Exception the exception
	 */
	public void init(Context context) throws Exception {
		if (context == null) {
			throw new Exception("dammit context is null !!! ");
		}
		mContext = context.getApplicationContext();
		synchronized (this) {
			if (!isInitLogic) {
				isInitLogic = true;
				RegReceivers();
				initPlayer();
				startMusicPlayService();
				this.notifyAll();

			}
		}
	}


	/**
	 * ��ʼ��������.
	 */
	private void initPlayer() {
		mMusicPlayer = MusicPlayer.ins(PlayerRuntime.application);
	}

	private void startMusicPlayService() {
		Intent intent = new Intent();
//		intent.setAction(AppConstant.MUSIC_PALY_SERVICE_ACTION);
		intent.setClass(mContext, MusicPlayService.class);
		mContext.startService(intent);
	}

	public void stopMusicPlayService() {
		Intent intent = new Intent();
//		intent.setAction(AppConstant.MUSIC_PALY_SERVICE_ACTION);
		intent.setClass(mContext, MusicPlayService.class);
		mContext.stopService(intent);
	}

	private DMReceiver mReceiver;

	private IntentFilter mFilter;

	public static boolean isPhoneClose;

	private static boolean networkRegister = true;

	private void RegReceivers() {
		networkRegister = true;
		// ע��
		if (mReceiver == null) {
			mFilter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
			mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			mFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
			mFilter.addAction(Intent.ACTION_SHUTDOWN);
			mFilter.addAction("android.intent.action.PHONE_STATE");
			mFilter.addAction(Intent.ACTION_HEADSET_PLUG);
			mFilter.setPriority(Integer.MAX_VALUE);
			mReceiver = new DMReceiver();
			mContext.registerReceiver(mReceiver, mFilter);
		}

	}

	public static class DMReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			String action = intent.getAction();
			Log.d("onReceive", "PHONE_STATE:>>>>>>>>>>>>>>>>>>>>>>>>" + action);
			if (action.equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
				// ���û����²��ż���ʼ����绰ʱ������ֹͣ����

				// ֹͣ���ֲ���
				if (PlayerRuntime.sSD.getDmPlayer().isPlaying()) {
					PlayerRuntime.sSD.getDmPlayer().pause();
					isPhoneClose = true;
				}
			} else if (action.equals("android.intent.action.PHONE_STATE")) {
				/*
				 * ���bug����������״̬�£�sim2���磬���ֲ�ֹͣ��ԭ���Ǵ�ʱͨ��TelephonyManager��getCallState
				 * ()�õ���״̬Ϊidle
				 */
				String state = intent
						.getStringExtra(TelephonyManager.EXTRA_STATE);
				if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)) {
					if (isPhoneClose) {
						// �����û���ͣ��
						PlayerRuntime.sSD.getDmPlayer().playMusic();
						// RT.sDM.getDmPlayer().startVolume(true);
						isPhoneClose = false;
					}
				} else if (state
						.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

				} else if (state
						.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)) {
					// ֹͣ���ֲ���
					if (PlayerRuntime.sSD.getDmPlayer().isPlaying()) {
						Log.d("onReceive",
								"PHONE_STATE:cause pause>>>>>>>>>>>>>>>>>>>>>>>>"
										+ action);
						PlayerRuntime.sSD.getDmPlayer().pause();
						isPhoneClose = true;
					}
				}

			} else if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
				if (intent.getIntExtra("state", 0) == 1) {
					/* �������� */
//					if (PlayerRuntime.Setting.ActiveHeadsetControl) {
//						DmMediaButtonRecv.regMediaBtnEventReceiver();
//					}
				}
			} else {
				if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent
						.getAction())) {
					// �ζ���
					PlayerRuntime.sSD.getDmPlayer().pause();
				} else {
					if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
						// �ػ�ʱ����һ��������ͣ����
						PlayerRuntime.sSD.getDmPlayer().pause();
					}
				}
			}

		}

	}

	public MusicPlayer getDmPlayer() {
		synchronized (this) {
			check(mMusicPlayer);
		}
		return mMusicPlayer;
	}


	/**
	 * Check Object .
	 *
	 * @param lg
	 *            the lg
	 */
	private void check(Object lg) {

		if (lg == null || !isInitLogic) {
			try {
				wait();
				if (!isInitLogic)
					throw new RuntimeException(
							"Error SD class is not initialized!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
