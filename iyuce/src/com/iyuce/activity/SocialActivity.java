package com.iyuce.activity;

import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;

public class SocialActivity extends Activity{

	private WebView web;
	
	private String URL = "http://bbs.iyuce.com/bar";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		
		initView();
		initEvent();
	}

	private void initView() {
		web = (WebView) findViewById(R.id.web_social);
	}
	
	@SuppressLint("SetJavaScriptEnabled")                    //Ñ¹ÖÆLint
	private void initEvent() {
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
}