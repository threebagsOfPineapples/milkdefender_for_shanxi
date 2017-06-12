package com.magispec.shield.network;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.magispec.shield.service.BaseApplicaton;

/**
 * author:x.p
 * 
 * explain:请求服务器的类
 * 
 */
public class HttpConnect {
	private static RequestQueue mQueue = Volley.newRequestQueue(BaseApplicaton.getAppContext());
	private static HttpConnect httpconnect = null;
	private HttpConnect() {

	}
	public static HttpConnect getInstance() {
		if (httpconnect == null) {
			httpconnect = new HttpConnect();
		}
		return httpconnect;
	}
	// post 请求
	public void StringRequest(String url, Listener<String> response, ErrorListener error) {
		StringRequest jr = new StringRequest(Request.Method.POST, url, response, error) {
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				try {
					String dataString = new String(response.data, "UTF-8");
					return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new ParseError(e));
				}
			}
		};
		mQueue.add(jr);
	}
	//get 请求
	public void TextStringRequest(String url, Listener<String> response, ErrorListener error) {
		StringRequest jr = new StringRequest(Request.Method.GET, url, response, error) {
			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				try {
					String dataString = new String(response.data, "UTF-8");
					return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new ParseError(e));
				}
			}
		};
		mQueue.add(jr);
	}
	// 带参请求数据
	public void StringRequest(String url, final HashMap<String, String> paramsMap, Listener<String> response,
			ErrorListener error) {
		StringRequest sr = new StringRequest(Request.Method.POST, url, response, error) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				paramsMap.put("ak", "asd");
				return paramsMap;
			}

			@Override
			protected Response<String> parseNetworkResponse(NetworkResponse response) {
				try {
					Map<String, String> responseHeaders = response.headers;
					String rawCookies = responseHeaders.get("Set-Cookie");
					if (rawCookies != null) {
						// 保存cookie值
					}
					String dataString = new String(response.data, "UTF-8");
					return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
				} catch (UnsupportedEncodingException e) {
					return Response.error(new ParseError(e));
				}
			}

			@Override
			public RetryPolicy getRetryPolicy() {
				RetryPolicy retryPolicy = new DefaultRetryPolicy(100000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
				return retryPolicy;
			}

		};

		mQueue.add(sr);
	}

	public static void net(String url) {
		StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d("TAG", response);
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
		mQueue.add(stringRequest);
	}

	public static void netre(String url) {
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Log.d("TAG", response.toString());
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
		mQueue.add(jsonObjectRequest);
	}
	
	//验证是否有网络
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager
		.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
		return mNetworkInfo.isAvailable();
		}
		}
		return false;
	}
	
	
	
}