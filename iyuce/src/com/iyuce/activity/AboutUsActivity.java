package com.iyuce.activity;

import com.woyuce.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AboutUsActivity extends Activity implements OnClickListener {

	private TextView txtBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);

		initView();
	}

	private void initView() {
		txtBack = (TextView) findViewById(R.id.txt_aboutus_back);
		txtBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}
