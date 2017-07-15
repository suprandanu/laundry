package com.alfian.android.laundry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 *
 */
public class BerandaViewPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> fragments = new ArrayList<>();

	public BerandaViewPagerAdapter(FragmentManager fm) {
		super(fm);

		fragments.clear();
		fragments.add(FragmentBeranda.newInstance(0));
		fragments.add(FragmentTransaksi.newInstance(0));
		fragments.add(FragmentAkun.newInstance(0));
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}

	/**
	 * Get the current fragment
	 */
	public FragmentBeranda getCurrentFragment() {
		return FragmentBeranda.newInstance(0);
	}
}