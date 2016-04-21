package com.iyuce.activity;

import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SuggestionActivity extends Activity implements OnClickListener {

	private TextView txtBack;
	private Button btnSure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggestion);

		initView();
	}

	private void initView() {
		txtBack = (TextView) findViewById(R.id.txt_suggestion_back);
		btnSure = (Button) findViewById(R.id.btn_suggestion_sure);

		txtBack.setOnClickListener(this);
		btnSure.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_suggestion_back:
			finish();
			break;
		case R.id.btn_suggestion_sure:
			new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).setTitle("感谢您的宝贵意见")
					.setPositiveButton("提交成功!", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SuggestionActivity.this.finish();
						}
					}).show();
			break;
		}
	}
}