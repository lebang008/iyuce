package com.iyuce.utils;

import com.iyuce.application.MyApplication;

import android.widget.Toast;

public class ToastUtils {

	/**
	 * 显示toast
	 * @param content    内容
	 * @param duration   持续时间
	 */
	public static void toast(String content, int duration) {
		if (content == null) {
			return;
		} else {
			MyApplication.ToastMgr.builder.display(content, duration);
		}
	}

	/**
	 * 显示默认持续时间为short的Toast
	 * @param content       内容
	 */
	public static void toast(String content) {
		if (content == null) {
			return;
		} else {
			MyApplication.ToastMgr.builder.display(content,Toast.LENGTH_SHORT);
		}
	}
}
