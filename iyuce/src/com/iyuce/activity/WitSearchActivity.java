package com.iyuce.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.WitSearchAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.WitSearch;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class WitSearchActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private TextView txtResult;
	private Button btnBack;
	private ListView witsearchlistview;
	
	private String URL_SEARCH = "http://iphone.ipredicting.com/xzsubSearch.aspx";
	private String localkey,localid,localsubid;
	private List<WitSearch> witsearchList = new ArrayList<WitSearch>();
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_witsearch);
		
		initView();
		getJson();
	}

	private void initView() {
		Intent it_search = getIntent();
		localkey = it_search.getStringExtra("localkey");
		localid = it_search.getStringExtra("localid");
		Log.e("all key and id", "key = " + localkey + ", id = " + localid);
		
		txtResult = (TextView) findViewById(R.id.txt_witsearch_result);
		btnBack = (Button) findViewById(R.id.btn_witsearch_back);
		witsearchlistview = (ListView) findViewById(R.id.listview_witsearch);
		witsearchlistview.setOnItemClickListener(this);
		btnBack.setOnClickListener(this);
	}

	private void getJson() {
		StringRequest strinrequest = new StringRequest(Method.POST,URL_SEARCH, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				WitSearch witsearch;
				JSONObject jsonobj;
				try {
					jsonobj = new JSONObject(response);
					int result = jsonobj.getInt("code");
						if(result == 0){
							JSONArray data = jsonobj.getJSONArray("data");
							if(data.length() == 0){
								txtResult.setText("没有找到您要的结果呢，亲...");
							}
							for (int i = 0; i < data.length(); i++) {
								jsonobj = data.getJSONObject(i);
								witsearch = new WitSearch();
								witsearch.subid = jsonobj.getString("subid");
								witsearch.subname = jsonobj.getString("subname");
								witsearchList.add(witsearch);
							}
							WitSearchAdapter adapter = new WitSearchAdapter(WitSearchActivity.this, witsearchList);
							witsearchlistview.setAdapter(adapter);
						}else{
							txtResult.setText("您没有输入内容哦，亲!");
							Log.e("Code Wrong", "请求失败 =" + jsonobj);
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.e("all response", "response = " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				txtResult.setText("没有找到您要的结果呢，亲...");
				Log.e("Request Wrong", "请求失败 =" + error);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> hashMap = new HashMap<String, String>();
				hashMap.put("key", localkey);
				if(localid != null){
					hashMap.put("categoryid", localid);
				}
				return hashMap;
			}
		};
		strinrequest.setTag("post");
		MyApplication.getHttpQueue().add(strinrequest);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_witsearch_back:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WitSearch witsearch = (WitSearch)witsearchList.get(position);
		localsubid = witsearch.subid;
		Intent it_witcontent = new Intent(this,WitContentActivity.class);
		it_witcontent.putExtra("localsubid", localsubid);
		startActivity(it_witcontent);
	}
}