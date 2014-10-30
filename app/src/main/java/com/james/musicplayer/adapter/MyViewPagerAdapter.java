/**
 * MyViewPagerAdapter.java [V1.0.0]
 * classes : com.james.musicplayer.adapter.MyViewPagerAdapter
 * ̷���� Create at 2014-10-16 ����5:54:36
 */
package com.james.musicplayer.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ��ҳViewPager������
 * com.james.musicplayer.adapter.MyViewPagerAdapter
 * @author ̷���� 
 * Create at 2014-10-16 ���� 9:14:36
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragments = new ArrayList<Fragment>();

	public MyViewPagerAdapter(FragmentManager fragmentManager,
			ArrayList<Fragment> fragments) {
		super(fragmentManager);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
