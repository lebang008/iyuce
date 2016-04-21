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
	protected void onRestart() {                // ���������ڵ��÷���ˢ�����ݣ�û�ܳɹ�         
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
						Log.e("code!=0 --DATA-BACK", "��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
					// �ڶ����������ݷŵ���������
					adapter = new SpeakingAdapter(SpeakingActivity.this, speakingList);
					mListView.setAdapter(adapter);
					pd.cancel();
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response); // ���Ƿ��ص�����JSON��Ϣ��δ���������ҪJSON���ݣ����Դ�������
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "���Ӵ���ԭ�� " + error.getMessage(), error);
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
			overridePendingTransition(0, 0); // ****��������ת����
			break;
		case R.id.ll_speaking_gaopintongji:
			startActivity(new Intent(this,StatisActivity.class));
			overridePendingTransition(0, 0); // ****��������ת����
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