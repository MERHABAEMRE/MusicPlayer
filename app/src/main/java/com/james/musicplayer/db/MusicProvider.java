/**
 * MusicProvider.java [V1.0.0]
 * classes : com.james.musicplayer.db.MusicProvider
 * ̷���� Create at 2014-10-16 ����6:27:54
 */
package com.james.musicplayer.db;

import com.james.musicplayer.config.AppConstant;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

/**
 * com.james.musicplayer.db.MusicProvider
 * 
 * @author ̷���� Create at 2014-10-16 ����6:27:54
 */
public class MusicProvider extends ContentProvider {

	DBHelper helper;
	SQLiteDatabase db;

	private static UriMatcher matcher;// ����ƥ����
	static {
		// ����UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����(-1)
		matcher = new UriMatcher(UriMatcher.NO_MATCH);
		matcher.addURI(AppConstant.MUSIC_PROVIDER_AUTOHORITY, DBInfo.TABLE_NAME, DBInfo.ITEMS);
		matcher.addURI(AppConstant.MUSIC_PROVIDER_AUTOHORITY, DBInfo.TABLE_NAME+"/#", DBInfo.ITEM_ID);
	}

	@Override
	public boolean onCreate() {
		this.helper = new DBHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		db = helper.getWritableDatabase();
		Cursor c;
		switch (matcher.match(uri)) {
		case DBInfo.ITEMS:
			c = db.query(DBInfo.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		case DBInfo.ITEM_ID:
			String id = uri.getPathSegments().get(1);
			c = db.query(DBInfo.TABLE_NAME, projection, DBInfo.ID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')'
							: ""), selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri); //�ڲ�ѯʱ����ʱ�۲������Ƿ��б䶯������б仯�ͻ��з��ء�
		return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (matcher.match(uri)) {
		case DBInfo.ITEMS:
			return DBInfo.CONTENT_TYPE;
		case DBInfo.ITEM_ID:
			return DBInfo.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = helper.getWritableDatabase();
        long rowId;
        if (matcher.match(uri) != DBInfo.ITEMS) {
            throw new IllegalArgumentException("Unknown URI" + uri);
        }
        rowId = db.insert(DBInfo.TABLE_NAME, DBInfo.ID, values);
        if (rowId > 0) {
        	/*ContentUris �����ڻ�ȡUri·�������ID����
        	1)Ϊ·������ID: withAppendedId(uri, id)*/
            Uri noteUri = ContentUris.withAppendedId(DBInfo.MUSIC_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }
        throw new IllegalArgumentException("Unknown URI" + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		 db = helper.getWritableDatabase();
	        int count = 0;
	        switch (matcher.match(uri)) {
	            case DBInfo.ITEMS :
	                count = db.delete(DBInfo.TABLE_NAME, selection, selectionArgs);
	                break;
	            case DBInfo.ITEM_ID :
	                String id = uri.getPathSegments().get(1);
	                count = db.delete(DBInfo.TABLE_NAME, DBInfo.ID + "=" + id
	                        + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""), selectionArgs);
	                break;
	            default:
	                throw new IllegalArgumentException("Unknown URI" + uri);
	        }
	        getContext().getContentResolver().notifyChange(uri, null);
	        return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		db = helper.getWritableDatabase();
        int count = 0;
        switch (matcher.match(uri)) {
            case DBInfo.ITEMS :
                count = db.update(DBInfo.TABLE_NAME, values,selection, selectionArgs);
                break;
            case DBInfo.ITEM_ID :
                String id = uri.getPathSegments().get(1);
                count = db.update(DBInfo.TABLE_NAME, values,DBInfo.ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
	}

}
