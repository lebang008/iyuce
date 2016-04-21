package com.iyuce.activity;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.application.MyApplication;
import com.iyuce.utils.PreferenceUtil;
import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShareActivity4 extends Activity implements OnClickListener {

	private Button btnBack;
	private LinearLayout llBack;

	private String URL_REQUEST = "http://iphone.ipredicting.com/kysubshare.aspx";
	private String localRoomID, localTime, localMessage, localSubid; // 上一级传来的数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share4);

		initView();
		dataRequest();
	}

	private void initView() {
		Intent it_share4 = getIntent();
		localRoomID = it_share4.getStringExtra("localRoomID");
		localTime = it_share4.getStringExtra("localTime");
		localMessage = it_share4.getStringExtra("localMessage");
		localSubid = it_share4.getStringExtra("localSubid");

		llBack = (LinearLayout) findViewById(R.id.ll_activity_share4);
		btnBack = (Button) findViewById(R.id.button_share4_back);

		llBack.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	@SuppressLint("InlinedApi")
	private void dataRequest() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL_REQUEST, new Response.Listener<String>() {
			@SuppressWarnings("deprecation")
			@Override
			public void onResponse(String response) {
				new AlertDialog.Builder(ShareActivity4.this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("分享结果")
						.setPositiveButton("分享成功 !", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent it_speaking = new Intent(ShareActivity4.this, SpeakingActivity.class);
								startActivity(it_speaking);
								ShareActivity4.this.finish();
							}
						}).show();
				Log.e("SUCCESS", "DATA = " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "联接错误原因： " + error.getMessage(), error);
				new AlertDialog.Builder(ShareActivity4.this).setTitle("分享结果")
						.setPositiveButton("分享失败，请重试", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ShareActivity4.this.finish();
							}
						}).show();
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("uname", PreferenceUtil.getSharePre(getApplicationContext()).getString("mUserName", ""));
				hashMap.put("umessage", localMessage);
				hashMap.put("roomid", localRoomID);
				hashMap.put("subid", localSubid);
				hashMap.put("examtime", localTime);
				Log.e("all", "all data" + localMessage + " ," + localRoomID + " ," + localSubid + " ," + localTime
						+ " ," + PreferenceUtil.getSharePre(getApplicationContext()).getString("mUserName", "le"));
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_activity_share4: // **** 点击" 高频统计" 启动 Activity-统计， 但界面看起来无跳转变化
			Intent it_statis = new Intent(this, StatisActivity.class);
			startActivity(it_statis);
			overridePendingTransition(0, 0);
			break;
		case R.id.button_share4_back: // **** 点击"返回"按钮, 返回上一个"分享"界面
			Intent it_speaking = new Intent(this, SpeakingActivity.class);
			startActivity(it_speaking);
			overridePendingTransition(0, 0);
			break;
		}
	}
}