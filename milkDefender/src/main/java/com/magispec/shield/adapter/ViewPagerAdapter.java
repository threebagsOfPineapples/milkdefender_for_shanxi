package com.magispec.shield.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
/**
 * 类说明
 * @author xp
 * @since 创建时间：2016年5月12日
 * @version V1.0
 *
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList = new ArrayList<Fragment>();

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public ViewPagerAdapter(FragmentManager fragmentManager,
			List<Fragment> arrayList) {
		super(fragmentManager);
		this.fragmentList = arrayList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragmentList.get(arg0);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
