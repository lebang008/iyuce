package com.iyuce.activity;

import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.TextView;

public class WangluobanActivity extends Activity implements OnClickListener {

	private TextView title;
	private WebView web;

	private String URL = "http://www.iyuce.com/wlb/index.aspx";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gongyilesoon);

		initView();
		initEvent();
	}

	private void initView() {
		title = (TextView) findViewById(R.id.txt_gongyilesson_title);
		web = (WebView) findViewById(R.id.web_gongyilesson);

		title.setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initEvent() {
		title.setBackgroundColor(Color.parseColor("#f25f11"));
		title.setText("ÍøÂç°à×¨Êô");

		web.loadUrl(URL);
		web.getSettings().setJavaScriptEnabled(true);
		web.getSettings().setSupportZoom(true);
		web.getSettings().setBuiltInZoomControls(true);
		web.getSettings().setUseWideViewPort(true);
		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		web.getSettings().setLoadWithOverviewMode(true);

		web.setWebViewClient(new WebViewClient());
		web.setWebChromeClient(new WebChromeClient());
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}