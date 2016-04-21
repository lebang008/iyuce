package com.iyuce.activity;

import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GongyiActivity extends Activity implements OnClickListener {

	private TextView titleback, txtaudio, txttenceng;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gongyi);

		initView();
		initEvent();
	}

	private void initView() {
		titleback = (TextView) findViewById(R.id.txt_gongyi_title);
		txtaudio = (TextView) findViewById(R.id.txt_gongyi_audio);
		txttenceng = (TextView) findViewById(R.id.txt_gongyi_tencent);

		titleback.setOnClickListener(this);
		txtaudio.setOnClickListener(this);
		txttenceng.setOnClickListener(this);
	}

	private void initEvent() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_gongyi_title:
			finish();
			break;
		case R.id.txt_gongyi_audio:
			startActivity(new Intent(this, AudioLessonActivity2.class));
			break;
		case R.id.txt_gongyi_tencent:
			startActivity(new Intent(this, GongyiLessonActivity.class));
			break;
		}
	}
}