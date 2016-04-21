package com.iyuce.activity;

import com.iyuce.entity.Speaking;
import com.woyuce.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SpeakingDetailActivity extends Activity implements OnClickListener {

	private TextView txtBack, txtName, txtTime, txtRoom, txtContent;
	private Speaking localspeaking;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localspeaking = (Speaking) getIntent().getSerializableExtra("localspeaking");
		setContentView(R.layout.activity_speakingdetail);

		initView();
		initEvent();
	}

	private void initView() {
		txtBack = (TextView) findViewById(R.id.txt_speakingdetail_back);
		txtName = (TextView) findViewById(R.id.txt_speakingdetail_username);
		txtTime = (TextView) findViewById(R.id.txt_speakingdetail_examtime);
		txtRoom = (TextView) findViewById(R.id.txt_speakingdetail_examroom);
		txtContent = (TextView) findViewById(R.id.txt_speakingdetail_content);
		
		txtBack.setOnClickListener(this);
	}

	private void initEvent() {
		txtName.setText(localspeaking.uname);
		txtTime.setText(localspeaking.vtime);
		txtRoom.setText(localspeaking.examroom);
		txtContent.setText(localspeaking.message);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}