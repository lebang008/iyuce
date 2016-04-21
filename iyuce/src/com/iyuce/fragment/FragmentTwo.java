package com.iyuce.fragment;

import com.iyuce.activity.MainActivity;
import com.woyuce.activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentTwo extends Fragment{

//	private WebView web;
	private ImageView img;
	
//	private String URL = "http://bbs.iyuce.com/bar";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab2, container, false);
		
		initView(view);
//		initEvent();
		return view;
	}

	private void initView(View view) {
//		web = (WebView) view.findViewById(R.id.web_tab2);
		img = (ImageView) view.findViewById(R.id.img_tab2);
		
		img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).StartSocialActivity();
			}
		});
	}

//	private void initEvent() {
//		web.loadUrl(URL);
//		web.getSettings().setJavaScriptEnabled(true);            
//		web.getSettings().setSupportZoom(true);
//		web.getSettings().setBuiltInZoomControls(true);
//		web.getSettings().setUseWideViewPort(true);
//		web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//		web.getSettings().setLoadWithOverviewMode(true);
//		
//		web.setWebViewClient(new WebViewClient());
//		web.setWebChromeClient(new WebChromeClient());
//	}
}