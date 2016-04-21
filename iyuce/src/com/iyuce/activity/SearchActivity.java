package com.iyuce.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.SearchAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Search;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends Activity implements OnItemClickListener, OnClickListener {

	private LinearLayout llback;
	private ListView lvSearch;
	private Button btnBack;
	private TextView txtNull;

	private String localsearch;
	private String URL_SEARCH = "http://iphone.ipredicting.com/kysubSearch.aspx";
	private List<Search> searchList = new ArrayList<Search>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		initView();
		getJson();
	}

	private void initView() {
		Intent it_search = getIntent();
		localsearch = it_search.getStringExtra("localsearch");

		txtNull = (TextView) findViewById(R.id.txt_search_null);
		btnBack = (Button) findViewById(R.id.button_search_back);
		lvSearch = (ListView) findViewById(R.id.listview_search);
		llback = (LinearLayout) findViewById(R.id.ll_search_back);

		btnBack.setOnClickListener(this);
		llback.setOnClickListener(this);
		lvSearch.setOnItemClickListener(this);
	}

	public void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_SEARCH, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Search search;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						if (data.length() == 0) {
							txtNull.setText("û���ҵ�����Ҫ�������أ���...");
						}
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							search = new Search();
							search.subCategoryid = jsonObject.getString("subCategoryid");
							search.subname = jsonObject.getString("subname");
							searchList.add(search);
						}
					} else {
						Log.e("code!=0 Data-BACK", "��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
						txtNull.setText("��û��������������Ŷ����!");
					}
					// �ڶ����������ݷŵ���������
					SearchAdapter adapter = new SearchAdapter(SearchActivity.this, searchList);
					lvSearch.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response);     ���Ƿ��ص�����JSON��Ϣ��δ���������ҪJSON���ݣ����Դ�������
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "���Ӵ���ԭ�� " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("key", localsearch);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Search search = (Search) searchList.get(position);
		String localsubCategoryid = search.subCategoryid;
		Intent it_subContent = new Intent(this, SubContentActivity.class);
		it_subContent.putExtra("localsubCategoryid", localsubCategoryid);
		startActivity(it_subContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_search_back:
			Intent it_speaking = new Intent(this,SpeakingActivity.class);
			startActivity(it_speaking);
			overridePendingTransition(0, 0); // ****��������ת����
			break;
		case R.id.button_search_back:
			finish();
			break;
		}
	}
}