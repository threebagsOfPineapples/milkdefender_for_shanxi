package com.magispec.shield.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import okhttp3.Call;

public class FragmentRecord extends Fragment {

	private ImageView item_previous, item_next, item_back,testResult;
	private LinkedList<String> mListItems;
	private LinearLayout layout_listview, layout_itemDetails;
	private TextView testDate, scanName, testPosition, testGoodName, itemId;
	private int listItemId;
	/**
	 * 上拉刷新的控件
	 */
	private PullToRefreshListView mPullRefreshListView;
	//MyAdapter adapter = null;

	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_record, container, false);
	}
	/*@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
	*//*	if (Constant.listItem.isEmpty()) {
			System.out.println("listItem 为空所以请求加载数据");
			
		}*//*
		new GetListTask().execute();
		layout_listview = (LinearLayout) view.findViewById(R.id.fragment_record_ll_listview);
		layout_itemDetails = (LinearLayout) view.findViewById(R.id.fragment_record_ll_itemDetails);
		itemId = (TextView) getActivity().findViewById(R.id.fragment_record_item_number);
		testDate = (TextView) getActivity().findViewById(R.id.fragment_record_itemDetails_testTime);
		testGoodName = (TextView) getActivity().findViewById(R.id.fragment_record_itemDetails_testGoodName);
		testPosition = (TextView) getActivity().findViewById(R.id.fragment_record_itemDetails_testPosition);
		testResult = (ImageView) getActivity().findViewById(R.id.fragment_record_itemDetails_testResult);
		scanName = (TextView) getActivity().findViewById(R.id.fragment_record_itemDetails_scanName);
		item_previous = (ImageView) getActivity().findViewById(R.id.fragment_record_item_details_previous);
		item_next = (ImageView) getActivity().findViewById(R.id.fragment_record_item_details_next);
		item_back = (ImageView) getActivity().findViewById(R.id.fragment_record_item_details_back);
		// item点击跳转上一条记录
		item_previous.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listItemId != 0) {
					testDate.setText((String) Constant.listItem.get(listItemId - 1).get("date"));
					testPosition.setText((String) Constant.listItem.get(listItemId - 1).get("position"));
					testGoodName.setText((String) Constant.listItem.get(listItemId - 1).get("barCode"));
					scanName.setText((String) Constant.listItem.get(listItemId - 1).get("srcGoodName"));
					int a;
					a= Integer.valueOf((String) Constant.listItem.get(listItemId - 1).get("result"));
					if (a<=20) {				
						testResult.setImageResource(R.drawable.fragment_red);
						
					} else if (20 <= a && a <= 70) {
						testResult.setImageResource(R.drawable.fragment_yellow);

					} else if (a >= 75) {
						testResult.setImageResource(R.drawable.fragment_green);
					} else {
						testResult.setImageResource(R.drawable.fragment_yellow);
					}
					listItemId--;
				}
			}
		});
		// item点击跳转下一条记录
		item_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listItemId < Constant.listItem.size() - 1) {
					testDate.setText((String) Constant.listItem.get(listItemId + 1).get("date"));
					testPosition.setText((String) Constant.listItem.get(listItemId + 1).get("position"));
					testGoodName.setText((String) Constant.listItem.get(listItemId + 1).get("barCode"));
					scanName.setText((String) Constant.listItem.get(listItemId + 1).get("srcGoodName"));
					int a;
					a= Integer.valueOf((String) Constant.listItem.get(listItemId + 1).get("result"));
					if (a<=20) {				
						testResult.setImageResource(R.drawable.fragment_red);
						
					} else if (20 <= a && a <= 70) {
						testResult.setImageResource(R.drawable.fragment_yellow);

					} else if (a >= 75) {
						testResult.setImageResource(R.drawable.fragment_green);
					} else {
						testResult.setImageResource(R.drawable.fragment_yellow);
					}
					listItemId++;
				} else {
					ToastUtil.showToast(getContext(), "暂无");
				}
			}
		});
		// item点击跳转返回列表
		item_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				layout_itemDetails.setVisibility(View.GONE);
				layout_listview.setVisibility(View.VISIBLE);
			}
		});
		mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
		// 初始化数据
		// 获取LIST数据
		adapter = new MyAdapter(getContext());
		// 设置适配器
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(adapter);
		mPullRefreshListView.setMode(Mode.BOTH);// 设置底部下拉刷新模式
		// 设置监听事件
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				testDate.setText((String) Constant.listItem.get(position - 1).get("date"));
				testPosition.setText((String) Constant.listItem.get(position - 1).get("position"));
				testGoodName.setText((String) Constant.listItem.get(position - 1).get("barCode"));
				scanName.setText((String) Constant.listItem.get(position - 1).get("srcGoodName"));
				int a;
				a= Integer.valueOf((String) Constant.listItem.get(position - 1).get("result"));
				if (a<=20) {				
					testResult.setImageResource(R.drawable.fragment_red);
					
				} else if (20 <= a && a <= 70) {
					testResult.setImageResource(R.drawable.fragment_yellow);

				} else if (a >= 75) {
					testResult.setImageResource(R.drawable.fragment_green);
				} else {
					testResult.setImageResource(R.drawable.fragment_yellow);
				}
				listItemId = position - 1;
				layout_listview.setVisibility(View.GONE);
				layout_itemDetails.setVisibility(View.VISIBLE);
			}
		});
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				// 显示最后更新的时间
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				// 模拟加载任务

				try {
					System.out.println("开始下拉刷新----");
					getData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				System.out.println("开始上拉加载更多");
				try {

					if (Constant.ResultCountInt - 20 > 0) {
						Constant.ResultCountInt -= 20;
						getRecords(Constant.ResultCountInt + "", "20");
						
					} *//*
						 * else { getRecords(Constant.ResultCountInt + "",
						 * Constant.ResultCountInt + "");
						 * 
						 * }
						 *//*
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	private class GetListTask extends AsyncTask<Void, Void, HashMap<String, Object>> {

		@Override
		protected HashMap<String, Object> doInBackground(Void... params) {
			try {
				getData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	*//*private class GetDataTask extends AsyncTask<Void, Void, HashMap<String, Object>> {
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
					// listItem.add(result);
				} else {
					// 添加list
					// listItem.addAll(getData());
				}
				// 通知程序数据集已经改变，如果不做通知，那么将不会刷新mListItems的集合
				adapter.notifyDataSetChanged();
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
			} catch (Exception e) {
				// TODO: handle exception
			}
			super.onPostExecute(result);
		}
	}*//*
	*//**
	 * 获取数据
	 * 
	 * @author xp
	 * @throws JSONException
	 *//*
	private void getData() throws JSONException {

		getRecordCounts();
		System.out.println("------获得记录---------");
	}
	private void getRecordCounts() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, Constant.SESSIONID);
		json.put(Constant.JSON_KEY_OPENID, Constant.OPENID);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_RECORDS_COUNT);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		json.put(Constant.JSON_KEY_DATA, "");
		System.out.println("----json---" + json.toString());
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call arg0, Exception arg1) {
						// TODO Auto-generated method stub
					}
					@Override
					public void onResponse(String string) {
						JSONObject json_resp = null;
						System.out.println("---string----" + string);
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

									Constant.RECORDSCOUNT = (String) json_resp.get(Constant.JSON_KEY_DATA);
									Constant.listItem.clear();
									int a = Integer.parseInt(Constant.RECORDSCOUNT) - 1;
									Constant.ResultCountInt = a;
									getRecords(a + "", "20");

								} else {
									Constant.RECORDSCOUNT = null;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Constant.RECORDSCOUNT = null;
						}

					}
				});
	}
	protected void getRecords(String startIndex, String count) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, Constant.SESSIONID);
		json.put(Constant.JSON_KEY_OPENID, Constant.OPENID);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_RECORDS);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		JSONObject data = new JSONObject();
		data.put("startIndex", startIndex);
		data.put("count", count);
		json.put(Constant.JSON_KEY_DATA, data);
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call arg0, Exception arg1) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onResponse(String string) {
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
											map.put("result", data.get("rec" + j + "_result"));
											
											if (data.get("rec" + j + "_testMilkName").toString().equals(" ")) {

												map.put("srcGoodName", "未知目标");
											} else {
												map.put("srcGoodName", data.get("rec" + j + "_testMilkName"));

											}

											map.put("barCode", data.get("rec" + j + "_barCode"));
											map.put("date", data.get("rec" + j + "_time"));
											map.put("position", data.get("rec" + j + "_position"));
											map.put("demo", j);
											Constant.listItem.add(map);
										}
										// System.out.println("-----------填充数据----------listitem大小"+Constant.listItem.get(6).get("resultGoodName"));
										adapter.notifyDataSetChanged();
										mPullRefreshListView.onRefreshComplete();
									}
								} else {

								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}
				});

	}

	public final class ViewHolder {
		public TextView date;
		// public String result;
		public TextView result;
		public TextView number;
		public TextView srcGoodName;
		public TextView resultGoodName;
		public ImageView testResult;
		public TextView testPosition;
	}
	*//**
	 * 自定义适配器
	 * 
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
			return Constant.listItem.size();
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
				convertView = mInflater.inflate(R.layout.listitem_record, null);
				holder.number = (TextView) convertView.findViewById(R.id.fragment_record_item_number);
				holder.date = (TextView) convertView.findViewById(R.id.fragment_record_item_testTimeDate);
				holder.testPosition = (TextView) convertView
						.findViewById(R.id.fragment_record_itemDetails_testPosition);
				holder.resultGoodName = (TextView) convertView
						.findViewById(R.id.fragment_record_itemDetails_testGoodName);
				holder.srcGoodName = (TextView) convertView.findViewById(R.id.fragment_record_item_scanName);
				holder.testResult = (ImageView) convertView.findViewById(R.id.fragment_record_iv_testResult);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.date.setText((String) Constant.listItem.get(position).get("date"));
			holder.number.setText(position + 1 + "");
			holder.srcGoodName.setText((String) Constant.listItem.get(position).get("srcGoodName"));
			//holder.testResult.setText((String) Constant.listItem.get(position).get("result")+"%");
			int a ;
			a= Integer.valueOf((String) Constant.listItem.get(position).get("result"));
			if (a <= 10) {				
				holder.testResult.setImageResource(R.drawable.fragment_red);
				
			} else if (20 <= a && a <= 70) {
				holder.testResult.setImageResource(R.drawable.fragment_yellow);

			} else if (a >= 75) {
				holder.testResult.setImageResource(R.drawable.fragment_green);
			} else {
				holder.testResult.setImageResource(R.drawable.fragment_yellow);
			}
			return convertView;
		}
	}*/

}
