package com.magispec.shield.activity;

import java.util.ArrayList;
import java.util.List;

import com.magispec.shield.R;
import com.magispec.shield.fragment.FragmentGuide1;
import com.magispec.shield.fragment.FragmentGuide2;
import com.magispec.shield.fragment.FragmentGuide3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class GuideActivity extends FragmentActivity {
	private ViewPager viewPage;
	private FragmentGuide1 mFragment1;
	private FragmentGuide2 mFragment2;
	private FragmentGuide3 mFragment3;
	private Button btnGo;
	private SharedPreferences sp;
	private PagerAdapter mPgAdapter;
	private RadioGroup dotLayout;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		initView();
		viewPage.addOnPageChangeListener(new MyPagerChangeListener());
	}
	private void initView() {
		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		viewPage = (ViewPager) findViewById(R.id.viewpager);
		mFragment1 = new FragmentGuide1();
		mFragment2 = new FragmentGuide2();
		mFragment3 = new FragmentGuide3();
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mPgAdapter = new com.magispec.shield.adapter.ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
		viewPage.setAdapter(mPgAdapter);
		btnGo = (Button) findViewById(R.id.btn_go);
		btnGo.setVisibility(View.GONE);
		btnGo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 结束当前页面，跳转至主页面
				// 否则，进入主页面
				if (sp.getBoolean("IsFirstRunning", true)) {
					Intent intent = new Intent(GuideActivity.this, WXLoginActivity.class);
					startActivity(intent);
					// 设置 sp 中 isFirstRunning为false
					sp.edit().putBoolean("IsFirstRunning", false).commit();
					finish();
				} else {
					finish();
				}
			}
		});
	}
	public class MyPagerChangeListener implements OnPageChangeListener {
		public void onPageSelected(int position) {
			// 如果是最后一个页面，就显示按钮
			if (position == mPgAdapter.getCount() - 1) {
				btnGo.setVisibility(View.VISIBLE);
			} else {
				// 如果不是最后一个页面，那么就，隐藏按钮
				btnGo.setVisibility(View.GONE);
			}
		}
		public void onPageScrollStateChanged(int arg0) {
		}
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
		}
	}
	/*
	 * private int[] imageids = new int[] { R.drawable.dis_guide1,
	 * R.drawable.dis_guide2,R.drawable.dis_guide3};
	 * 
	 * private List<ImageView> imageList; private ViewPager viewpager; private
	 * SharedPreferences sp; private Button btnGo; protected void
	 * onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
	 * requestWindowFeature(Window.FEATURE_NO_TITLE);
	 * setContentView(R.layout.activity_guide); sp =
	 * getSharedPreferences("config", MODE_PRIVATE); imageList = new
	 * ArrayList<ImageView>(); for (int i = 0; i < imageids.length; i++) {
	 * 
	 * ImageView image = new ImageView(this);
	 * image.setBackgroundResource(imageids[i]); imageList.add(image); }
	 *//**
		 * 适用于滑动轮播图至最后一页时出现一个button
		 */
	/*
	 * btnGo = (Button) findViewById(R.id.btn_go);
	 * btnGo.setVisibility(View.GONE); btnGo.setOnClickListener(new
	 * View.OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // 结束当前页面，跳转至主页面 // 否则，进入主页面
	 * Intent intent = new Intent(GuideActivity.this, WXLoginActivity.class);
	 * startActivity(intent); // 设置 sp 中 isFirstRunning为false
	 * sp.edit().putBoolean("isFirstRunning", false).commit(); finish(); } });
	 * viewpager = (ViewPager) findViewById(R.id.viewPager);
	 * 
	 * adapter = new MyAdapter();
	 * 
	 * viewpager.setAdapter(adapter);
	 * 
	 * viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
	 * 
	 * @Override // 当选择的页面发生变化的时候，回调此方法 public void onPageSelected(int position)
	 * { // 如果是最后一个页面，就显示按钮 if (position == adapter.getCount()-1) {
	 * 
	 * btnGo.setVisibility(View.VISIBLE); } else { // 如果不是最后一个页面，那么就，隐藏按钮
	 * btnGo.setVisibility(View.GONE); } }
	 * 
	 * @Override public void onPageScrolled(int position, float positionOffset,
	 * int positionOffsetPixels) { }
	 *//**
		 * 滑动到最后一页时再次滑动会执行跳转 如果不是第一次启动app则执行返回
		 */
	/*
	 * @Override public void onPageScrollStateChanged(int state) {
	 * 
	 * boolean misScrolled = false; switch (state) { case
	 * ViewPager.SCROLL_STATE_DRAGGING: misScrolled = false; break; case
	 * ViewPager.SCROLL_STATE_SETTLING: misScrolled = true; break; case
	 * ViewPager.SCROLL_STATE_IDLE: if (viewpager.getCurrentItem() ==
	 * viewpager.getAdapter().getCount() - 1 && !misScrolled) { boolean
	 * isFirstRunning = sp.getBoolean("isFirstRunning", true);
	 * if(isFirstRunning){ sp.edit().putBoolean("isFirstRunning",
	 * false).commit(); startActivity(new Intent(GuideActivity.this,
	 * WXLoginActivity.class)); GuideActivity.this.finish(); } else{
	 * onBackPressed(); } } misScrolled = true; break; } } }); }; private
	 * MyAdapter adapter; private class MyAdapter extends PagerAdapter {
	 * 
	 * @Override // 告诉viewPager 有多少个页面 public int getCount() { return
	 * imageList.size(); }
	 * 
	 * @Override
	 *//**
		 * 实例化条目，页面 该方法有二件事要干 1- 根据position 获得一个view 并添加至 container 2- 返回一个和view
		 * 有关系的 对象
		 */
	/*
	 * public Object instantiateItem(ViewGroup container, int position) { // 1-
	 * 根据position 获得一个view 并添加至 container ImageView view =
	 * imageList.get(position); container.addView(view); // 2- 返回一个和view 有关系的 对象
	 * return view; }
	 * 
	 * @Override
	 *//**
		 * 判断view 与object 之间的关系 view 是 instantiateItem 方法中添加至container 中的view
		 * object 是 instantiateItem 方法中的返回的对象
		 */
	/*
	 * public boolean isViewFromObject(View view, Object object) { return view
	 * == object; }
	 * 
	 * @Override
	 *//**
		 * 当需要销毁某个条目时，调用此方法 position 要销毁的条目的下标 object 是instantiateItem 方法的返回
		 *//*
		 * public void destroyItem(ViewGroup container, int position, Object
		 * object) { // 下面这句话，一定要删除，否则会崩的。 // super.destroyItem(container,
		 * position, object); // 将对应的view从container 删除
		 * container.removeView((View) object); } }
		 */
}
