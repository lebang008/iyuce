package com.iyuce.application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.iyuce.entity.UserInfo;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyApplication extends Application {

	private static MyApplication mInstance = null;
	private UserInfo info;

	public static RequestQueue mQueue;

	public static MyApplication getInstance() {
		return mInstance;
	}

	public UserInfo getInfo() {
		return this.info;
	}

	/**
	 * ��ʼ��ȫ��
	 */
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		mQueue = Volley.newRequestQueue(getApplicationContext());

		ToastMgr.builder.init(getApplicationContext());

		// ����Ĭ�ϵ�ImageLoader���ò���
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);
	}

	public static RequestQueue getHttpQueue() {
		return mQueue;
	}

	public void setInfo(UserInfo paramUserInfo) {
		this.info = paramUserInfo;
	}

	public enum ToastMgr { // ȫ��Toast
		builder;
		private View view;
		private TextView tv;
		private Toast toast;

		/**
		 * ��ʼ��Toast
		 */
		@SuppressLint("InflateParams")
		public void init(Context context) {
			view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
			tv = (TextView) view.findViewById(R.id.toast_textview);
			toast = new Toast(context);
			toast.setView(view);
		}

		/**
		 * ��ʾToast
		 */
		public void display(CharSequence content, int duration) {
			if (content.length() != 0) {
				tv.setText(content);
				toast.setDuration(duration);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
	}
}