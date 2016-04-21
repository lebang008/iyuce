package com.iyuce.utils;

import com.iyuce.application.MyApplication;

import android.widget.Toast;

public class ToastUtils {

	/**
	 * ��ʾtoast
	 * @param content    ����
	 * @param duration   ����ʱ��
	 */
	public static void toast(String content, int duration) {
		if (content == null) {
			return;
		} else {
			MyApplication.ToastMgr.builder.display(content, duration);
		}
	}

	/**
	 * ��ʾĬ�ϳ���ʱ��Ϊshort��Toast
	 * @param content       ����
	 */
	public static void toast(String content) {
		if (content == null) {
			return;
		} else {
			MyApplication.ToastMgr.builder.display(content,Toast.LENGTH_SHORT);
		}
	}
}
