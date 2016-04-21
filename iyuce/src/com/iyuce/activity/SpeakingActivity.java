package com.iyuce.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.SpeakingAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Speaking;
import com.woyuce.activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SpeakingActivity extends Activity implements OnClickListener,OnItemClickListener{

	private Button btnShare;
	private LinearLayout llStatis;
	private ListView mListView;

	private String URL = "http://iphone.ipredicting.com/getvoteMge.aspx";
	private List<Speaking> speakingList = new ArrayList<Speaking>();
	private SpeakingAdapter adapter;
	
	private ProgressDialog pd;
	
	@Override
	protected void onRestart() {                // 该生命周期调用方法刷新数据，没能成功         
		super.onRestart();
		speakingList.clear();
		adapter.notifyDataSetChanged();
		getJson();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_speaking);

		initView();
		getJson();
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview_speaking_vote);
		btnShare = (Button) findViewById(R.id.button_speaking_share);
		llStatis = (LinearLayout) findViewById(R.id.ll_speaking_gaopintongji);
		
		btnShare.setOnClickListener(this);
		llStatis.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void getJson() {
		pd = new ProgressDialog(this);
		pd.show();
		StringRequest stringRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Speaking speaking;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							speaking = new Speaking();
							speaking.uname = jsonObject.getString("uname");
							speaking.message = jsonObject.getString("message");
							speaking.examroom = jsonObject.getString("examroom");
							speaking.vtime = jsonObject.getString("vtime");
							speakingList.add(speaking);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					// 第二步，将数据放到适配器中
					adapter = new SpeakingAdapter(SpeakingActivity.this, speakingList);
					mListView.setAdapter(adapter);
					pd.cancel();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.e("DATA-BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "联接错误原因： " + error.getMessage(), error);
			}
		}) ;
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_speaking_share:
			startActivity(new Intent(this,ShareActivity.class));
			overridePendingTransition(0, 0); // ****设置无跳转动画
			break;
		case R.id.ll_speaking_gaopintongji:
			startActivity(new Intent(this,StatisActivity.class));
			overridePendingTransition(0, 0); // ****设置无跳转动画
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Speaking localspeaking = (Speaking)speakingList.get(position);
		Intent it = new Intent(this,SpeakingDetailActivity.class);
		it.putExtra("localspeaking",localspeaking);
		startActivity(it);
	}
}