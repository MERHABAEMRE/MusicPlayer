package com.james.musicplayer.service.logic;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.RemoteException;

import com.james.musicplayer.bean.MusicInfo;
import com.james.musicplayer.config.AppConstant;
import com.james.musicplayer.config.ConfigManager;
import com.james.musicplayer.db.DBInfo;
import com.james.musicplayer.service.MusicBrodcastCode;
import com.james.musicplayer.util.QuerTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * MusicPlayer [V1.0.0]
 * MusicPlayer�̳�MediaPlayer�࣬����дMediaPlayer���е�pause()��start()�������ڷ����и��µ�ǰ������Ϣ���������ֲ��Ž����е�������Ϣ
 * classes : com.james.musicplayer.service.logic.MusicPlayer
 * @author ̷���� Create at 2014-10-18 ����10:48:39
 */
public class MusicPlayer extends MediaPlayer implements MediaPlayer.OnCompletionListener {

	private static boolean ISPLAYING = false;// �������Ƿ����ڲ�������

	// ��ǰ����������Ϣ
	private static ArrayList<MusicInfo> NOW_PLAYING_LIST;// ��ǰ�����б�
	private static int NOW_PLAYING_INDEX = 0;// ��ǰ����λ��
	private static String NOW_PLAYING_TITLE = "";// ��ǰ�������ֱ���
	private static String NOW_PLAYING_ARTIST = "";// ��ǰ����������������
	private static String NOW_PLAYING_ALBUM = "";// ��ǰ������������ר��
	private static String NOW_PLAYING_PATH = "";// ��ǰ������������·��
	private static long NOW_PLAYING_ID = 0;// ��ǰ��������ID
	private static String NOW_DURATION = "";// ��ǰ��������ʱ��

	private static int NOW_PLAYING_TIME = -1;// ��ǰ��������ʱ��
	private static int CURRENT_PLAY_MOD = -1;// ��ǰѭ��ģʽ

	// ������
	private static MusicPlayer mMusicPlayer;

	private static ConfigManager mConfigManager;
	// ��ѯ������
	static QuerTools querTools;
	//��������
	public static Object lockObj = new Object();

	public MusicPlayer(Context context) {
		Context mContext = context;
	}


	public static MusicPlayer ins(Context context)
	{

		mConfigManager = new ConfigManager(context);
		querTools = new QuerTools(context);
		synchronized (lockObj)
		{
			if (mMusicPlayer == null)
			{
				mMusicPlayer = new MusicPlayer(context);
			}
		}
		return mMusicPlayer;
	}

	@Override
	public void pause() throws IllegalStateException {
		updateInfo();
		super.pause();
	}

	@Override
	public void start() throws IllegalStateException {
		updateInfo();
		// ��������Seek,play��ť��������Ϣ
		changeUIBrodcast(MusicBrodcastCode.CODE_SET_SEEKBAR_TXT_DURATION);
		changeUIBrodcast(MusicBrodcastCode.CODE_CHANGE_PLAY_BUTTON_BG);
		changeUIBrodcast(MusicBrodcastCode.CODE_CHANGE_MUSIC_INFO);
		super.start();
	}

	private void updateInfo() {
		NOW_PLAYING_TITLE = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX)
				.getTitleString();
		NOW_PLAYING_ARTIST = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX)
				.getArtistString();
		NOW_PLAYING_ALBUM = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX)
				.getAlbumString();
		NOW_PLAYING_ID = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX).getId();
		NOW_DURATION = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX)
				.getDuration();
	}
	/**
	 * �趨����ѭ��ģʽ
	 *
	 * @param whitch
	 *            ѭ��ģʽ����
	 * @throws android.os.RemoteException
	 */
	public void setPlayMod(int whitch) throws RemoteException {
		mConfigManager.setPlayModConfig(whitch);
	}

	public void resumeMusic() throws RemoteException {
		if (NOW_PLAYING_INDEX >= 0) {
			mMusicPlayer.start();
		}
		ISPLAYING = true;
		setPlayBtnBg();
	}

	public void playPrevious() throws RemoteException {
		CURRENT_PLAY_MOD = getPlayMod();
		switch (CURRENT_PLAY_MOD) {
			case AppConstant.PLAY_MOD_LIST:
				if (NOW_PLAYING_INDEX == 0) {
					NOW_PLAYING_INDEX = 0;
				} else {
					NOW_PLAYING_INDEX = NOW_PLAYING_INDEX - 1;
				}
				break;
			case AppConstant.PLAY_MOD_CIRCLE:
				if (NOW_PLAYING_INDEX == 0) {
					NOW_PLAYING_INDEX = NOW_PLAYING_LIST.size() - 1;
				} else {
					NOW_PLAYING_INDEX = NOW_PLAYING_INDEX - 1;
				}
				break;
			case AppConstant.PLAY_MOD_RANDOM:
				NOW_PLAYING_INDEX = new Random()
						.nextInt(NOW_PLAYING_LIST.size() - 1);
				break;
			case AppConstant.PLAY_MOD_SINGLE:
				break;
			default:
				NOW_PLAYING_INDEX = 0;
				break;
		}
		playMusic();
		changeUIBrodcast(MusicBrodcastCode.CODE_SET_SEEKBAR_MAX);
		ISPLAYING = true;
		setPlayBtnBg();
	}

	public void playNext() throws RemoteException {


		CURRENT_PLAY_MOD = getPlayMod();
		switch (CURRENT_PLAY_MOD) {
			case AppConstant.PLAY_MOD_LIST:
				if (NOW_PLAYING_INDEX == NOW_PLAYING_LIST.size() - 1) {
					mMusicPlayer.stop();
				} else {
					NOW_PLAYING_INDEX = NOW_PLAYING_INDEX + 1;
				}
				break;
			case AppConstant.PLAY_MOD_CIRCLE:
				if (NOW_PLAYING_INDEX == NOW_PLAYING_LIST.size() - 1) {
					NOW_PLAYING_INDEX = 0;
				} else {
					NOW_PLAYING_INDEX = NOW_PLAYING_INDEX + 1;
				}
				break;
			case AppConstant.PLAY_MOD_RANDOM:
				NOW_PLAYING_INDEX = new Random()
						.nextInt(NOW_PLAYING_LIST.size() - 1);
				break;
			case AppConstant.PLAY_MOD_SINGLE:
				break;
			default:
				NOW_PLAYING_INDEX = 0;
		}
		playMusic();
		changeUIBrodcast(MusicBrodcastCode.CODE_SET_SEEKBAR_MAX);
		ISPLAYING = true;
		setPlayBtnBg();
	}

	public void pauseMusic() throws RemoteException {
		mMusicPlayer.pause();
		ISPLAYING = false;
		setPlayBtnBg();
	}

	public void stopMusic() {
		mMusicPlayer.stop();
		ISPLAYING = false;
		setPlayBtnBg();
	}

	/**
	 * ��������
	 */
	public void playMusic() {
		if (NOW_PLAYING_LIST != null) {
			NOW_PLAYING_PATH = NOW_PLAYING_LIST.get(NOW_PLAYING_INDEX).getPathString();

			if (NOW_PLAYING_INDEX >= 0) {
				try {
					mMusicPlayer.reset();
					mMusicPlayer.setDataSource(NOW_PLAYING_PATH);
					mMusicPlayer.prepare();
					mMusicPlayer.start();

				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				ISPLAYING = true;
				mMusicPlayer.setOnCompletionListener(this);
				setPlayBtnBg();
			}
		}
	}

	// ÿ�����ֲ��Ž��������
	@Override
	public void onCompletion(MediaPlayer mp) {
		try {
			playNext();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	// �ı����ֲ��Ű�ť
	private void setPlayBtnBg() {
		changeUIBrodcast(MusicBrodcastCode.CODE_CHANGE_PLAY_BUTTON_BG);
	}

	public void seekTo(int where) {
		mMusicPlayer.seekTo(where);
	}

	public int getCurrentSeekTime() {
		return mMusicPlayer.getCurrentPosition();
	}

	public void setCurrentSeekTime(int position) {

		mMusicPlayer.setCurrentSeekTime(position);
	}

	public int getPlayMod() throws RemoteException {
		return mConfigManager.getPlayMod();
	}

	public String getNowDurationString() {
		return NOW_DURATION;
	}

	public int getNowDurationInt(){
		return mMusicPlayer.getDuration();
	}


	/**
	 * ���͸ı䲥�Ž���㲥��ʹ��MusicBrodcastCode�о�̬��������brodcastCode
	 *
	 * @param brodcastCode
	 *            ��ֵ���е�ֵ
	 */
	private void changeUIBrodcast(int brodcastCode) {
		Intent intent = new Intent();// ����Intent����
		intent.setAction(AppConstant.MUSIC_SERVICE_RECEIVER_ACTION);
		intent.putExtra(MusicBrodcastCode.KEY, brodcastCode);
		//sendBroadcast(intent);// ���͹㲥
	}

	public void setNowPlayingList(int index) {
		Cursor cursor =querTools.getMusicCursorFromDataBase();
		NOW_PLAYING_LIST=getNowPlayingList(cursor);
		NOW_PLAYING_INDEX=index;
	}

	public ArrayList<MusicInfo> getNowPlayingList(Cursor cursor) {
		cursor.moveToFirst();
		ArrayList<MusicInfo> resultList = new ArrayList<MusicInfo>();

		while (!cursor.isAfterLast() && cursor.getCount() > 0) {
			MusicInfo MusicInfo = new MusicInfo();
			MusicInfo.setId(cursor.getLong(cursor.getColumnIndex("_id")));
			MusicInfo.setTitleString(cursor.getString(cursor.getColumnIndex(DBInfo.TITLE)));
			MusicInfo.setArtistString(cursor.getString(cursor.getColumnIndex(DBInfo.ARTIST)));
			MusicInfo.setAlbumString(cursor.getString(cursor.getColumnIndex(DBInfo.ALBUM)));
			MusicInfo.setDuration(cursor.getString(cursor.getColumnIndex(DBInfo.DURATION)));
			MusicInfo.setPathString(cursor.getString(cursor.getColumnIndex(DBInfo.PATH)));
			resultList.add(MusicInfo);
			cursor.moveToNext();
		}
		return resultList;
	}
}
