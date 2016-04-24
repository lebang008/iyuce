package com.iyuce.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iyuce.application.MyApplication;
import com.woyuce.activity.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UpdateManager {

	private ProgressBar pb;
	private Dialog mDownLoadDialog;

	private static final String URL_FINAL = "http://php.ipredicting.com/ueditor/php/upload/video/20150424/1429853714100844.mp3";
	private static final int DOWNLOADING = 1;
	private static final int DOWNLOAD_FINISH = 0;

	private String mVersion;
	private String mSavePath;
	private int mProgress;
	private boolean mIsCancel = false;

	private Context mcontext;

	public UpdateManager(Context context) {
		mcontext = context;
	}

	private Handler mGetVersionHandler = new Handler() {
		public void handleMessage(Message msg) {
			JSONObject jsonObject = (JSONObject) msg.obj;
			Log.e("data", "I need Update " + jsonObject.toString());
			try {
				int result = jsonObject.getInt("code");
				if (result == 0) {
					mVersion = jsonObject.getString("version");
				}
				mVersion = "2"; // ****
				Log.e("version", "version = " + mVersion);
				if (isUpdate()) {
					Toast.makeText(mcontext, "need update", Toast.LENGTH_SHORT).show();
					showDownloadDialog();
				} else {
					Toast.makeText(mcontext, "don't need update", Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private Handler mUpdateProgressHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOADING:
				pb.setProgress(mProgress);
				break;
			case DOWNLOAD_FINISH:
				mDownLoadDialog.dismiss();
				installAPK();
				break;
			}
		};
	};

	public void checkUpdate() {
		JsonObjectRequest request = new JsonObjectRequest(URL_FINAL, null, new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				Message msg = Message.obtain();
				msg.obj = response;
				mGetVersionHandler.sendMessage(msg);
			}
		}, null);
		request.setTag("post");
		MyApplication.getHttpQueue().add(request);

		Toast.makeText(mcontext, "need update", Toast.LENGTH_SHORT).show();
		showNoticeDialog();
	}

	/**
	 * 比较本地版本是否需要更新
	 */

	protected boolean isUpdate() {
		int serverVersion = Integer.parseInt(mVersion);
		int localVersion = 1;

		try {
			localVersion = mcontext.getPackageManager().getPackageInfo("com.woyuce.activity", 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		if (serverVersion > localVersion) {
			return true;
		} else {
			return false;
		}
	}

	protected void showNoticeDialog() {
		AlertDialog.Builder builder = new Builder(mcontext);
		builder.setTitle("提示");
		String message = "软件有更新，要下载安装吗？\n";
		builder.setMessage(message);

		builder.setPositiveButton("更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownloadDialog();
			}
		});

		builder.setNegativeButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	protected void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mcontext);
		builder.setTitle("下载中");

		View view = LayoutInflater.from(mcontext).inflate(R.layout.dialog_progress, null);
		pb = (ProgressBar) view.findViewById(R.id.update_progress);
		builder.setView(view);

		builder.setNegativeButton("取消下载", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				mIsCancel = true;
			}
		});
		mDownLoadDialog = builder.create();
		mDownLoadDialog.show();

		// 下载文件
		downloadAPK();
	}

	private void downloadAPK() {
		new Thread(new Runnable() {
			public void run() {
				try {
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						String sdPath = Environment.getExternalStorageDirectory() + "/";// sd卡
																						// 根目录
						mSavePath = sdPath + "jikedowndownload";

						File dir = new File(mSavePath);
						if (!dir.exists()) {
							dir.mkdir();
						}

						HttpURLConnection conn = (HttpURLConnection) new URL(URL_FINAL).openConnection();
						conn.connect();
						InputStream is = conn.getInputStream();
						int length = conn.getContentLength();

						File apkFile = new File(mSavePath, mVersion);
						FileOutputStream fos = new FileOutputStream(apkFile);

						int count = 0;
						byte[] buffer = new byte[1024];
						while (!mIsCancel) {
							int numread = is.read(buffer);
							count += numread;
							mProgress = (int) (((float) count / length) * 100);
							// 更新进度条
							mUpdateProgressHandler.sendEmptyMessage(DOWNLOADING);
							// 下载完成
							if (numread < 0) {
								mUpdateProgressHandler.sendEmptyMessage(DOWNLOAD_FINISH);
								break;
							}
							fos.write(buffer, 0, numread);
						}
						fos.close();
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void installAPK() {
		File apkFile = new File(mSavePath, mVersion);
		if (!apkFile.exists()) {
			return;
		}
		Intent it = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse("file://" + apkFile.toString());
		it.setDataAndType(uri, "application/vnd.android.package-archive");
		mcontext.startActivity(it);
	}
}