package com.james.musicplayer.activities;

import java.util.ArrayList;

import com.james.musicplayer.R;
import com.james.musicplayer.adapter.MyViewPagerAdapter;
import com.james.musicplayer.service.IMusicControlService;
import com.james.musicplayer.tools.FileScanner;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ViewPager viewPager;// ҳ������
	private ImageView ivMainTitleBottom;// ����ͼƬ
	private TextView txtMusicsList, txtArtistsList, txtAlbumsList;// ����

	// ����ҳ��
	private MusicsListFragment mMusicsFrag;
	private ArtistsListFragment mArtistsFrag;
	private AlbumsListFragment mAlbumsFrag;

	private ArrayList<Fragment> fragmentList;// ҳ���б�

	// ͷ��ҳ�����
	private final int MUSICS_INDEX = 0;
	private final int ARTISTS_INDEX = 1;
	private final int ALBUMS_INDEX = 2;

	private int offset = 0;// ����ͼƬƫ����
	private int currIndex = MUSICS_INDEX;// ��ǰҳ�����
	private int bmpW;// ����ͼƬ���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MainActivity.this.getContentResolver();
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	/**
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		fragmentList = new ArrayList<Fragment>();
		mMusicsFrag = new MusicsListFragment();
		mArtistsFrag = new ArtistsListFragment();
		mAlbumsFrag = new AlbumsListFragment();

		fragmentList.add(mMusicsFrag);
		fragmentList.add(mArtistsFrag);
		fragmentList.add(mAlbumsFrag);

		viewPager.setAdapter(new MyViewPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		viewPager.setCurrentItem(MUSICS_INDEX);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ��ʼ��ͷ��
	 */
	private void InitTextView() {
		txtMusicsList = (TextView) findViewById(R.id.txtMusicsList);
		txtArtistsList = (TextView) findViewById(R.id.txtArtistsList);
		txtAlbumsList = (TextView) findViewById(R.id.txtAlbumsList);

		// ����ͷ��������
		txtMusicsList.setOnClickListener(new MyOnClickListener(
				this.MUSICS_INDEX));
		txtArtistsList.setOnClickListener(new MyOnClickListener(
				this.ARTISTS_INDEX));
		txtAlbumsList.setOnClickListener(new MyOnClickListener(
				this.ALBUMS_INDEX));
	}

	/**
	 * ��ʼ������
	 */
	private void InitImageView() {
		ivMainTitleBottom = (ImageView) findViewById(R.id.ivMainTitleBottom);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.main_title_bottom).getWidth();// ��ȡͼƬ���

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		ivMainTitleBottom.setImageMatrix(matrix);// ���ö�����ʼλ��
	}

	/**
	 * ͷ��������
	 */
	private class MyOnClickListener implements OnClickListener {

		private int index = MainActivity.this.MUSICS_INDEX;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			ivMainTitleBottom.startAnimation(animation);
			Toast.makeText(MainActivity.this,
					"��ѡ����" + viewPager.getCurrentItem() + "ҳ��",
					Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent;
		switch (id) {
		case R.id.menu_scan:
			/*intent = new Intent(this, MusicPlayActivity.class);
			startActivity(intent);*/
			new FileScanner(this).scanMp3File();
			break;
		case R.id.menu_quit:
			intent = new Intent(this, MusicPlayActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_settings:
			intent = new Intent(this, MusicPlayActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
