package com.iyuce.activity;

import com.iyuce.utils.ToastUtils;

import android.app.Activity;

public class BaseActivity extends Activity {

	public void toast(String content, int duration) {
		ToastUtils.toast(content, duration);
	}

	public void toast(String content) {
		ToastUtils.toast(content);
	}
	
}