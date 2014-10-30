/**
 * DBInfo.java [V1.0.0]
 * classes : com.james.musicplayer.bean.DBInfo
 * ̷���� Create at 2014-10-16 ����6:03:10
 */
package com.james.musicplayer.db;

import com.james.musicplayer.config.AppConstant;

import android.net.Uri;

/**
 * ���ݿ������Ϣ
 * com.james.musicplayer.bean.DBInfo
 * @author ̷���� 
 * Create at 2014-10-16 ����6:03:10
 */
public class DBInfo {
	
	public static final int VERSION=1;//���ݿ�汾

	public static final String DBNAME="musics.db";//���ݿ���
	

	//���ֱ�����ֵ
	public static String TABLE_NAME="musicinfo";
//    public static String ID = "_id";
    public static String ID = "rowid";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String DURATION = "duration";
    public static final String  PATH= "path";
    
    
    //provider�Ĳ�ͬ��ַ��Ӧ�ķ���ֵ
    public static final int ITEMS = 1;
    public static final int ITEM_ID = 2;
     
    
    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.james.db";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.james.db";
     
    //���ֱ�provider uri
    public static final Uri MUSIC_URI = Uri.parse("content://" + AppConstant.MUSIC_PROVIDER_AUTOHORITY + "/"+TABLE_NAME);
    
}
