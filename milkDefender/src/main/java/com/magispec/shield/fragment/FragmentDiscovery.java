package com.magispec.shield.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.magispec.shield.R;
import com.magispec.shield.activity.WebActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.DateUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import okhttp3.Call;
public class FragmentDiscovery extends Fragment {
	private ViewPager viewPager;
	//private MyPagerAdapter pageAdapter;
	private TextView tvDesc;
	private boolean isRunning = false;
	private LinearLayout llPointGroup;
	private static ProgressDialog dialog;
	/**
	 * 上拉刷新的控件
	 */
	private PullToRefreshListView mPullRefreshListView;
	//MyAdapter adapter = null;
	private ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
	private List<ImageView> imageList;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discovery, null);
	/*	viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		llPointGroup = (LinearLayout) view.findViewById(R.id.ll_point_group);
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.discovery_pull_refresh_list);*/
		return view;
	}
	/*@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		dialog = new ProgressDialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请求网络中...");
		if (Constant.imageDescriptions != null && Constant.imageDescriptions.length >= 1) {
			initBannerView();
		} else {
			dialog.show();
			new GetListTask().execute();
		}
		// 初始化数据
		adapter = new MyAdapter(getContext());
		// 设置适配器
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(adapter);
		mPullRefreshListView.setMode(Mode.BOTH);// 设置底部下拉刷新模式
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// 显示最后更新的时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 模拟加载任务
				new GetDataTask().execute();
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				new GetDataTask().execute();
			}
		});
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("position:" + position + "size----------" + Constant.listItem_discovery_list.size());
				Intent intent = new Intent();
				intent.setClass(getContext(), WebActivity.class);
				intent.putExtra("url", (String) Constant.listItem_discovery_list.get(position - 1).get("webURL"));
				intent.putExtra("title", (String) Constant.listItem_discovery_list.get(position - 1).get("title"));
				startActivity(intent);
			}
		});
	}
	private class GetDataTask extends AsyncTask<Void, Void, HashMap<String, Object>> {
		@Override
		protected HashMap<String, Object> doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			try {
				map = new HashMap<String, Object>();
				map.put("date", "3/4-11:10");
				map.put("position", "北京市");
				map.put("resultGoodName", "伊利 金装3段");
				map.put("srcGoodName", "伊利 金装3段");
				map.put("result", "测试正确");
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
			return map;
		}
		// 这里是对刷新的响应，可以利用addFirst（）和addLast()函数将新加的内容加到LISTView中
		// 根据AsyncTask的原理，onPostExecute里的result的值就是doInBackground()的返回值
		@Override
		protected void onPostExecute(HashMap<String, Object> result) {
			// 在头部增加新添内容
			try {
				// 得到当前的模式
				Mode mode = mPullRefreshListView.getCurrentMode();
				// 判断当前模式 为上拉加载或者下拉刷新
				if (mode == Mode.PULL_FROM_START) {
					// 模拟单条添加数据
					Constant.listItem_discovery_list.clear();
					getDiscoveryList(Constant.OPENID, Constant.SESSIONID, "0", "3");
				} else {
					// 添加list
					getDiscoveryList(Constant.OPENID, Constant.SESSIONID, Constant.listItem_discovery_list.size()+"", "3");
				}
				// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
				adapter.notifyDataSetChanged();
				mPullRefreshListView.onRefreshComplete();
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onPostExecute(result);
		}
	}
	public final class ViewHolder {
		public ImageView pic;
		public TextView title;
		public TextView content;
	}
	*//**
	 * 自定义适配器
	 * @author xp
	 *//*
	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return Constant.listItem_discovery_list.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				Display display = getActivity().getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
				convertView = mInflater.inflate(R.layout.fragment_discovery_list, null);
				holder.pic = (ImageView) convertView.findViewById(R.id.discovery_list_iv);
				android.view.ViewGroup.LayoutParams lp;
				lp = holder.pic.getLayoutParams();
				lp.width = display.getWidth();
				lp.height = display.getWidth() * 9 / 16;
				holder.pic.setLayoutParams(lp);
				holder.title = (TextView) convertView.findViewById(R.id.discovery_list_article_title);
				holder.content = (TextView) convertView.findViewById(R.id.discovery_list_article_content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			System.out.println("posio:---------------------------------" + position);
			holder.title.setText((String) Constant.listItem_discovery_list.get(position).get("title"));
			holder.content.setText((String) Constant.listItem_discovery_list.get(position).get("general"));
			final ViewHolder a;
			a = holder;
			Picasso.with(getContext()).load((String) Constant.listItem_discovery_list.get(position).get("photoURL"))
					.fit().into(a.pic);
			*//*
			 * OkHttpUtils.get().url((String)
			 * Constant.listItem_discovery_list.get(position).get("photoURL")).
			 * build() .execute(new BitmapCallback() {
			 * 
			 * @Override public void onResponse(Bitmap arg0) {
			 * a.pic.setImageBitmap(arg0); System.out.println("下载成功"); }
			 * 
			 * @Override public void onError(Call arg0, Exception e) {
			 * a.pic.setBackgroundResource(R.drawable.homepage);
			 * System.out.println("error" + e.getMessage()); } });
			 *//*
			return convertView;
		}
	}
	public ArrayList<HashMap<String, Object>> getData() {
		HashMap<String, Object> map = new HashMap<>();
		for (int i = 0; i <= 3; i++) {
			map.put("title", "测试标题" + i);
			map.put("content", "测试内容" + i);
			map.put("url",
					"https://ss2.baidu.com/-vo3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=a4b3d7085dee3d6d2293d48b252b5910/0e2442a7d933c89524cd5cd4d51373f0830200ea.jpg");
			listItem.add(map);
		}
		return listItem;
	}
	*//**
	 * 实现重复循环的方式： 1、 Timer + TimerTask 2\ while(falg) 循环 + 休眠 3 - 使用handler
	 *//*
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (isRunning) {
				// 切换至下一而
				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
				// 发送延时信息, 3秒以后再执行 handleMessage
				handler.sendEmptyMessageDelayed(99, 3000);
			}
		};
	};
	*//**
	 * 页面切换后，上一个页面的下标
	 *//*
	private int lastPosition;

	@SuppressWarnings("deprecation")
	private void regListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			*//*
			 * 当选择的页面发生改变的时候 position 新选择的页面的下标
			 *//*
			public void onPageSelected(int position) {
				// 防止position 下标越界
				position = position % imageList.size();

				tvDesc.setText(Constant.imageDescriptions[position]);
				// 让上一个指示点enable = false
				llPointGroup.getChildAt(lastPosition).setEnabled(false);
				// 让当前指示点enable = true
				llPointGroup.getChildAt(position).setEnabled(true);
				// 为上个指示点，赋值
				lastPosition = position;
			}

			@Override
			// 当页面滑动的时候，回调
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			// 当页面滑动状态改变的时候回调
			public void onPageScrollStateChanged(int state) {
			}
		});
	}
	*//*
	 * ViewPager 运行过程： ViewPager 默认会保存当前屏幕看到的页面，和左边一个，右边一个，共三个页面，超出这个范围的就会被销掉，
	 * 进入这个范围的时候，就会被创建出来。
	 *//*

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		*//**
		 * 告诉viewPager 有多少个页面
		 *//*
		public int getCount() {
			*//*
			 * if (imageList.size() == 1) {// 一张图片时不用流动 return imageList.size();
			 * }
			 *//*
			return Integer.MAX_VALUE;
		}
		@Override
		*//**
		 * 实例化条目，即，实例化每个页面
		 * 
		 * @param position
		 *            就是页面的下标 >=0 同时 <getCount()
		 * @param container
		 *            要添加页面的布局，其实就是ViewPager 自身
		 *//*
		public Object instantiateItem(ViewGroup container, int position) {
			System.out.println("instantiateItem::position:" + position);
			position = position % imageList.size();
			// 1、根据position 获得一个对应的view ,同时将view 添加至 container
			final ImageView v = imageList.get(position);
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null) {
				parent.removeView(v);
			}
			Picasso.with(getContext()).load(Constant.imageUrls[position]).fit().into(v);

			container.addView(v);
			// 为 每个 pager 添加点击事件
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 通过 lastPosition 监听点击的是那个页面
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					Uri url = Uri.parse("http://www.specda.com");
					intent.setData(url);
					startActivity(intent);
				}
			});
			return v;
		}
		@Override
		*//**
		 * 判断view 与object 之间的关系
		 * 
		 * @param view
		 *            是 instantiateItem 方法 中，添加至 container 中的view
		 * @param object
		 *            是 instantiateItem 方法 中，的返回值
		 *//*
		public boolean isViewFromObject(View view, Object object) {
			if (view == object) {
				return true;
			} else {
				return false;
			}
		}
		@Override
		*//**
		 * 当锁毁某个条目（即页面）时，调用该方法
		 * 
		 * @param container
		 *            其实就是ViewPager 自身
		 * @param position
		 *            要删除的页面的下标
		 * @param object
		 *            是该页面对应的object 即 instantiateItem 的返回结果
		 * 
		 *//*
		public void destroyItem(ViewGroup container, int position, Object object) {
			System.out.println("destroyItem::position:" + position);
			// super.destroyItem 必须删除，否则就是崩，原因：你懂的。
			// super.destroyItem(container, position, object);
			// 删除相应的条目
			container.removeView((View) object);
			// 当图片少于三张时
			if (imageList.size() < 4
					&& (viewPager.getCurrentItem() - 1) % imageList.size() == position % imageList.size())
				instantiateItem(container, position % imageList.size());
			// 下面这句，要根据实际情况而定。
			object = null;
		}
	}

	public void getbannerPicture(String mOpenId, String mSessionId, String index, String count) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
		json.put(Constant.JSON_KEY_OPENID, mOpenId);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_ROLLING);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		JSONObject data = new JSONObject();
		data.put("startIndex", index);
		data.put("count", count);
		json.put(Constant.JSON_KEY_DATA, data);
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onResponse(String string) {
						System.out.println("String:" + string);
						JSONObject json_resp = null;
						JSONObject data = null;
						if (string != null) {
							try {
								json_resp = new JSONObject(string);
							} catch (JSONException e) {

								e.printStackTrace();
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
										&& json_resp.get(Constant.JSON_KEY_DATA) != null) {
									data = new JSONObject(json_resp.getString(Constant.JSON_KEY_DATA));
									if (data != null && data.get("count") != null) {
										int i = (int) data.get("count");										
										Constant.imageUrls = new String[i];
										Constant.imageDescriptions = new String[i];
										Constant.webUrls = new String[i];
										for (int j = 0; j < i; j++) {
											Constant.imageUrls[j] = (String) data.get("ct" + j + "_photoURL");
											Constant.imageDescriptions[j] = (String) data.get("ct" + j + "_title");
											Constant.webUrls[j] = (String) data.get("ct" + j + "_webURL");
										}
										getDiscoveryList(Constant.OPENID, Constant.SESSIONID, "0", "3");
										System.out.println("长度" + Constant.imageDescriptions.length);
										initBannerView();
									}
								} else {

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onError(Call arg0, Exception arg1) {
						// TODO Auto-generated method stub
						System.out.println("onError");
					}
				});
	}

	private class GetListTask extends AsyncTask<Void, Void, HashMap<String, Object>> {

		@Override
		protected HashMap<String, Object> doInBackground(Void... params) {
			try {
				getbannerPicture(Constant.OPENID, Constant.SESSIONID, "0", "3");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	private void initBannerView() {
		tvDesc.setText(Constant.imageDescriptions[0]);
		imageList = new ArrayList<ImageView>();//
		System.out.println(" Constant.imageDescriptions.length:" + Constant.imageDescriptions.length);
		for (int i = 0; i < Constant.imageDescriptions.length; i++) {
			final ImageView image = new ImageView(this.getActivity());
			System.out.println("constant---i" + Constant.imageUrls[i]);
			imageList.add(image);
			// image.setBackgroundResource(imageIds[i]);
			// 添加指示点
			ImageView point = new ImageView(this.getActivity());
			point.setBackgroundResource(R.drawable.point_bg);
			// 默认第一个是选中的状态
			if (i == 0) {
				point.setEnabled(true);
			} else {
				point.setEnabled(false);
			}
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, -2);
			params.leftMargin = 15; // 设置左边距为15个象素
			// 布局添加view时，布局参数要和布局，保持一致
			llPointGroup.addView(point, params);
		}
		pageAdapter = new MyPagerAdapter();

		viewPager.setAdapter(pageAdapter);
		regListener();
		*//*
		 * 设置当前屏幕显示的条目 参数为条目的下标
		 *//*
		viewPager.setCurrentItem(10000 - (10000 % imageList.size()));
		isRunning = true;
		// 发送延时信息, 3秒以后再执行 handleMessage
		handler.sendEmptyMessageDelayed(99, 2000);
	}

	public void getDiscoveryList(String mOpenId, String mSessionId, String index, String count) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
		json.put(Constant.JSON_KEY_OPENID, mOpenId);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_LIST);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		JSONObject data = new JSONObject();
		data.put("startIndex", index);
		data.put("count", count);
		json.put(Constant.JSON_KEY_DATA, data);
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onResponse(String string) {
						System.out.println("String:" + string);
						JSONObject json_resp = null;
						JSONObject data = null;
						if (string != null) {
							try {
								json_resp = new JSONObject(string);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
										&& json_resp.get(Constant.JSON_KEY_DATA) != null) {
									data = new JSONObject(json_resp.getString(Constant.JSON_KEY_DATA));
									if (data != null && data.get("count") != null) {
										int i = (int) data.get("count");
										for (int j = 0; j < i; j++) {
											HashMap<String, Object> map = new HashMap<String, Object>();
											map.put("title", data.get("ct" + j + "_title"));
											map.put("general", data.get("ct" + j + "_general"));
											map.put("photoURL", data.get("ct" + j + "_photoURL"));
											map.put("webURL", data.get("ct" + j + "_webURL"));
											System.out.println("title:" + data.get("ct" + j + "_title") + "general:"
													+ data.get("ct" + j + "_general") + "photoURL:"
													+ data.get("ct" + j + "_photoURL") + "webUrl:"
													+ data.get("ct" + j + "_webURL"));
											Constant.listItem_discovery_list.add(map);
										}
										adapter.notifyDataSetChanged();
										mPullRefreshListView.onRefreshComplete();
										dialog.dismiss();
										// System.out.println("-----------填充数据----------listitem大小"+Constant.listItem.get(6).get("resultGoodName"));
									}
								} else {

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					@Override
					public void onError(Call arg0, Exception arg1) {
						// TODO Auto-generated method stub
						System.out.println("onError");
					}
				});
	}*/
}
