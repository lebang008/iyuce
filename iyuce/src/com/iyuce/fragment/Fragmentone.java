package com.iyuce.fragment;

import com.iyuce.activity.GongyiActivity;
import com.iyuce.activity.RangeActivity;
import com.iyuce.activity.SpeakingActivity;
import com.iyuce.activity.WaittingActivity;
import com.iyuce.activity.WangluobanActivity;
import com.iyuce.activity.WrittingActivity;
import com.woyuce.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Fragmentone extends Fragment implements OnClickListener {

	private ImageView img_speaking, img_range, img_gongyi, img_wangluo, img_writting, img_waitting;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab1, container, false);

		initView(view);
		return view;
	}

	private void initView(View view) {
		img_speaking = (ImageView) view.findViewById(R.id.img_speaking);
		img_range = (ImageView) view.findViewById(R.id.img_range);
		img_gongyi = (ImageView) view.findViewById(R.id.img_gongyi);
		img_wangluo = (ImageView) view.findViewById(R.id.img_wangluo);
		img_writting = (ImageView) view.findViewById(R.id.img_writting);
		img_waitting = (ImageView) view.findViewById(R.id.img_waitting);

		img_speaking.setOnClickListener(this);
		img_range.setOnClickListener(this);
		img_gongyi.setOnClickListener(this);
		img_wangluo.setOnClickListener(this);
		img_writting.setOnClickListener(this);
		img_waitting.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_speaking:
			startActivity(new Intent(getActivity(),SpeakingActivity.class));
			break;
		case R.id.img_range:
			startActivity(new Intent(getActivity(),RangeActivity.class));
			break;
		case R.id.img_gongyi:
			startActivity(new Intent(getActivity(),GongyiActivity.class));
			break;
		case R.id.img_wangluo:
			startActivity(new Intent(getActivity(),WangluobanActivity.class));
			break;
		case R.id.img_writting:
			startActivity(new Intent(getActivity(),WrittingActivity.class));
			break;
		case R.id.img_waitting:
			startActivity(new Intent(getActivity(),WaittingActivity.class));
			break;
		}
	}
}