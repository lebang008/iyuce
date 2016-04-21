package com.iyuce.activity;

import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WitAnswerActivity extends Activity implements OnClickListener{

	private ImageView imgBack;
	private WebView web;

	private String localanswerUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_witanswer);

		initView();
		initEvent();
	}

	private void initView() {
		Intent it_answer = getIntent();
		localanswerUrl = it_answer.getStringExtra("localanswerUrl");

		web = (WebView) findViewById(R.id.webView_witanswer);
		imgBack = (ImageView) findViewById(R.id.arrow_back);
		imgBack.setOnClickListener(this);
	}

	@SuppressLint("SetJavaScriptEnabled")           //Ñ¹ÖÆLint
	private void initEvent() {
		web.loadUrl(localanswerUrl);

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
