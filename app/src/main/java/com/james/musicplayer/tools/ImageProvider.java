/**
 * ImageProvider.java [V1.0.0]
 * classes : com.james.musicplayer.tools.ImageProvider
 * ̷���� Create at 2014-10-18 ����10:08:27
 */
package com.james.musicplayer.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

/**
 * ��ȡ��������ͼƬ
 * com.james.musicplayer.tools.ImageProvider
 * @author ̷���� 
 * Create at 2014-10-18 ����10:08:27
 */
public class ImageProvider {
	static Uri sArtworkUri = Uri
			.parse("content://media/external/audio/albumart");
	static BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
	static Bitmap mCacheBit;
	
}
