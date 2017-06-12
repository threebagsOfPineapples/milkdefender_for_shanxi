package com.magispec.shield.service;

import java.io.File;

import com.magispec.shield.activity.WelcomeActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import okhttp3.Call;

public class UpdateInfoService {
	ProgressDialog progressDialog;
	Handler handler;
	Context context;
	/** 超时时间 */
	private static final int TIMEOUT = 100 * 1000;
	/** 下载的连接 */
	private static String down_url ;
	/** 下载成功 */
	private static final int DOWN_OK = 1;
	/** 下载失败 */
	private static final int DOWN_ERROR = 0;
	/** 当前文件的大小 */
	private static long size;
	public UpdateInfoService(Context context) {
		this.context = context;
	}
	/*public UpdateInfo getUpDateInfo() {

		InputStream inputStream;
		try {
			inputStream = WelcomeActivity.in;
			String json = ReadTxtFileUtils.readTextFile(inputStream);
			JSONArray array = new JSONArray(json);

			UpdateInfo updateInfo = new UpdateInfo();
			updateInfo.setVersion(array.getJSONObject(0).getInt("version"));
			updateInfo.setDescription(array.getJSONObject(0).getString("description"));
			updateInfo.setUrl(array.getJSONObject(0).getString("url"));
			this.updateInfo = updateInfo;
			System.out.println("verson:"+array.getJSONObject(0).getInt("version")
					+"des:"+array.getJSONObject(0).getString("description"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return updateInfo;
	}
*/
	public boolean isNeedUpdate() {
		String new_version = WelcomeActivity.info.getVersionCode(); // 最新版本的版本号
		System.out.println("new_version:"+"当前版本号为："+new_version);
		// 获取当前版本号
		int now_version = 0;
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			now_version = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (Integer.valueOf(new_version) <=now_version) {
			return false;
		} else {
			return true;
		}
	}
	public void downLoadFile(final String url, final ProgressDialog pDialog, Handler h) {
		
		System.out.println("url--"+url);
		progressDialog = pDialog;
		handler = h;
		new Thread() {
			public void run() {
				/*HttpClient client = new DefaultHttpClient();
				HttpGet get = null;
				try {
					get = new HttpGet(URLEncoder.encode(url, "utf-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength(); // 获取文件大小
					progressDialog.setMax(length); // 设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(Environment.getExternalStorageDirectory(), "Test.apk");
						fileOutputStream = new FileOutputStream(file);
						// 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一下就下载完了,
						// 看不出progressbar的效果。
						byte[] buf = new byte[1024];
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							progressDialog.setProgress(process); // 这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}*/
			/*	InputStream inputStream;
				OutputStream outputStream = null;
				long totalSize;// 文件总大小
				URL apkUrl;
				try {
					apkUrl = new URL(url);
				
				httpURLConnection = (HttpURLConnection) apkUrl.openConnection();
				httpURLConnection.setConnectTimeout(TIMEOUT);
				httpURLConnection.setReadTimeout(TIMEOUT);
				// 获取下载文件的size
				totalSize = httpURLConnection.getContentLength();
				System.out.println("开始下载-----------"+totalSize);
				progressDialog.setMax((int) totalSize);
				if (httpURLConnection.getResponseCode() == 404) {
					try {
						throw new Exception("fail!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				inputStream = httpURLConnection.getInputStream();
				
			//	outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
				//byte buffer[] = new byte[1024];
				//int readsize = 0;		
				if (inputStream != null) {
					System.out.println("尝试一个下载");
					File file = new File(Environment.getExternalStorageDirectory(), "Test.apk");
					outputStream = new FileOutputStream(file);
					// 这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一下就下载完了,
					// 看不出progressbar的效果。
					byte[] buf = new byte[1024];
					int ch = 0;
					int process = 0;
					while ((ch = inputStream.read(buf)) != -1) {
						outputStream.write(buf, 0, ch);
						process += ch;
						progressDialog.setProgress(process); // 这里就是关键的实时更新进度了！
					}
				}
				
				if (httpURLConnection != null) {
					httpURLConnection.disconnect();
				}
				inputStream.close();
				outputStream.close();
				down();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
		OkHttpUtils
				.get()	
				.url(url)
				.build()
				.execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(),"test.apk") 
				{	
					@Override
					public void onResponse(File file) {
						 Log.e("下载", "onResponse :" + file.getAbsolutePath());
						 down();
					}
					
					@Override
					public void onError(Call arg0, Exception e) {
						 Log.e("下载出现问题", "onError :" + e.getMessage());
						
					}
					
					@Override
					public void inProgress(float progress, long arg1) {
						progressDialog.setProgress((int) (100*progress));	
						
					}
				});
				
				
				
			}		
		}.start();
	}

	void down() {
		handler.post(new Runnable() {
			public void run() {
				progressDialog.cancel();
				update();
			} 
		});
	}
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Test.apk")),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
