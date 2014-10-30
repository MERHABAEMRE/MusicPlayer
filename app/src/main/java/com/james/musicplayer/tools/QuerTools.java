/**
 * QuerTools.java [V1.0.0]
 * classes : com.james.musicplayer.tools.QuerTools
 * ̷���� Create at 2014-10-17 ����11:48:58
 */
package com.james.musicplayer.tools;

import java.util.ArrayList;

import com.james.musicplayer.bean.MusicInfo;
import com.james.musicplayer.db.DBInfo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * ���ݿ��ѯ������ com.james.musicplayer.tools.QuerTools
 * 
 * @author ̷���� Create at 2014-10-17 ����11:48:58
 */
public class QuerTools {

	private ContentResolver mContentResolver;
	private ArrayList<MusicInfo> resultList = null;

	private Cursor cursor;
	private String TAG = "database";

	private int limitNum = 12;

	public QuerTools(Context context) {
		mContentResolver = context.getContentResolver();
	}

	/**
	 * @param uri
	 *            Provider·��
	 * @param order
	 *            ��ѯ����Ҫ��
	 * @param limitCount
	 *            �Ƿ�������С�б���
	 * @return ������Ϣʵ���б�
	 */
	public Cursor getMusicCursorFromDataBase() {
		try {
			cursor = mContentResolver.query(DBInfo.MUSIC_URI, new String[] { DBInfo.ID+" as _id", DBInfo.TITLE,
	                DBInfo.ARTIST, DBInfo.ALBUM,DBInfo.DURATION,DBInfo.PATH }, null, null, null);

			/*cursor.moveToFirst();
			 resultList = new ArrayList<MusicInfo>();
			
			while (!cursor.isAfterLast() && cursor.getCount() > 0) {
				MusicInfo MusicInfo = new MusicInfo();
                
				MusicInfo.setId(cursor.getLong(cursor.getColumnIndex(DBInfo.ID)));
				MusicInfo.setTitleString(cursor.getString(cursor.getColumnIndex(DBInfo.TITLE)));
				MusicInfo.setArtistString(cursor.getString(cursor.getColumnIndex(DBInfo.ARTIST)));
				MusicInfo.setAlbumString(cursor.getString(cursor.getColumnIndex(DBInfo.ALBUM)));
				MusicInfo.setDuration(cursor.getString(cursor.getColumnIndex(DBInfo.DURATION)));
				MusicInfo.setPathString(cursor.getString(cursor.getColumnIndex(DBInfo.PATH)));

				Log.i(TAG, MusicInfo.getTitleString());

				resultList.add(MusicInfo);
				if (limitCount) {
					if (resultList.size() > limitNum) {
						return resultList;
					}
				}
				cursor.moveToNext();
			}*/
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		return cursor;
	}


	/**
	 * @param song_id ����ID
	 * @param uri Provider·��
	 * @return ɾ����������
	 */
	public int removeMusicFromDatabase(long _id, Uri uri) {
		try {
			return mContentResolver.delete(uri, DBInfo.ID+"=?",
					new String[] { _id + "" });
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void insertMusicToDatabase(MusicInfo musicinfo){
	    ContentValues values=new ContentValues();
	    values.put(DBInfo.TITLE, musicinfo.getTitleString());
	    values.put(DBInfo.ARTIST, musicinfo.getArtistString());
	    values.put(DBInfo.ALBUM, musicinfo.getAlbumString());
	    values.put(DBInfo.DURATION, musicinfo.getDuration());
	    values.put(DBInfo.PATH, musicinfo.getPathString());
		mContentResolver.insert(DBInfo.MUSIC_URI, values);
	}
	
	int getLimitNum() {
		return limitNum;
	}
}
