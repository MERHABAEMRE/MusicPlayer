/**
 * MusicPlayService.java [V1.0.0]
 * classes : com.james.musicplayer.service.MusicPlayService
 * ̷���� Create at 2014-10-16 ����8:32:02
 */
package com.james.musicplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;

import javax.security.auth.callback.Callback;

/**
 * ���ֲ��ŷ����࣬�������֣�ͬʱ�������̷߳��͹㲥�ı�UI com.james.musicplayer.service.MusicPlayService
 * 
 * @author ̷���� Create at 2014-10-16 ����8:32:02
 */
public class MusicPlayService extends Service implements Callback{

	// ��ص����񷽷�
	private MusicPlayBinder mMusicPlayBinder = new MusicPlayBinder();

	public static RemoteCallbackList<ICallback> mCallbacks=new RemoteCallbackList<ICallback>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

	@Override
	public IBinder onBind(Intent intent) {
		if (mMusicPlayBinder != null) {
			return mMusicPlayBinder;
		}
		return null;
	}

}
