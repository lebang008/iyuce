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
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.VotenoAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Voteno;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class StatisActivity extends Activity implements OnClickListener, OnItemSelectedListener, OnItemClickListener {
	private LinearLayout llBack;
	private ListView listViewVote;
	private Spinner spnPart, spnDate, spnCity;
	private Button btnMore, btnBack;
	private TextView txtSearch;
	private AutoCompleteTextView autoTxt;

	private String URL_CITY = "http://iphone.ipredicting.com/kyCityApi.aspx";
	private String URL_VOTE = "http://iphone.ipredicting.com/kysubOrder.aspx";
	private int localpartid, localdateid, localcityid;

	private ArrayAdapter<String> partAdapter, dateAdapter, cityAdapter;
	private List<String> partList = new ArrayList<String>();
	private List<String> dateList = new ArrayList<String>();
	private List<String> cityList = new ArrayList<String>();
	private List<String> cityidList = new ArrayList<String>();
	private List<Voteno> votenoList = new ArrayList<Voteno>();
	private VotenoAdapter votenoadapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statis);

		initView();
		getCityList();
	}

	private void initView() {
		autoTxt = (AutoCompleteTextView) findViewById(R.id.autotxt_statis);
		llBack = (LinearLayout) findViewById(R.id.ll_statis_back);
		btnMore = (Button) findViewById(R.id.btn_statis_more);
		btnBack = (Button) findViewById(R.id.button_statis_back);
		listViewVote = (ListView) findViewById(R.id.listview_statis_vote);
		txtSearch = (TextView) findViewById(R.id.txt_statis_search);
		spnPart = (Spinner) this.findViewById(R.id.spinner_statis_part);
		spnDate = (Spinner) this.findViewById(R.id.spinner_statis_date);
		spnCity = (Spinner) this.findViewById(R.id.spinner_statis_city);

		llBack.setOnClickListener(this);
		btnMore.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		txtSearch.setOnClickListener(this);
		listViewVote.setOnItemClickListener(this);
		spnPart.setOnItemSelectedListener(this);
		spnDate.setOnItemSelectedListener(this);
		spnCity.setOnItemSelectedListener(this);

		ArrayAdapter<String> autoadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,cityList); // AUTO的示例
		autoTxt.setAdapter(autoadapter);
	}

	private void initEvent() {
		partList.add("part1");
		partList.add("part2");
		dateList.add("30天");
		dateList.add("120天");

		partAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, partList);
		partAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnPart.setAdapter(partAdapter);

		dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateList);
		dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnDate.setAdapter(dateAdapter);

		cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCity.setAdapter(cityAdapter);
	}

	private void getCityList() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_CITY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				String cityname;
				String cityid;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							cityid = jsonObject.getString("cityid");
							cityidList.add(cityid); // 读取城市ID
							cityname = jsonObject.getString("cityname");
							cityList.add(cityname); // 读取城市
						}
					} else {
						Log.e("code!=0 DATA_BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					initEvent();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("Data_BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, voteErrorListener());
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	/**
	 * 抽出Volley中的访问成功voteResponseListener方法 (仅用于getVote()及相关方法)
	 */
	private Listener<String> voteResponseListener() {
		return new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Voteno voteno;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							voteno = new Voteno();
							voteno.subid = jsonObject.getString("subid");
							voteno.categoryName = jsonObject.getString("categoryName");
							voteno.votetotal = jsonObject.getString("votetotal");
							votenoList.add(voteno);
						}
					} else {
						String message = jsonObject.getString("message");
						Log.e("code!=0 DATA_BACK", "读取页面失败： " + message);
					}
					votenoadapter = new VotenoAdapter(StatisActivity.this, votenoList);
					listViewVote.setAdapter(votenoadapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("Data_BACK", "JSON接口返回的信息： " + response);//这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		};
	}

	/**
	 * 抽出Volley中访问失败voteErrorListener方法 (本类通用)
	 */
	private ErrorListener voteErrorListener() {
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "连接错误原因： " + error.getMessage(), error); // 可以做错误处理
			}
		};
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		votenoList.clear();
		switch (parent.getId()) {
		case R.id.spinner_statis_part:
			localpartid = position + 1;
			StringRequest strinRequest = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(),voteErrorListener()) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("partid", localpartid + "");
					hashMap.put("cityid", localcityid + "");
					hashMap.put("days", localdateid + "");
					Log.e("id", "partid = " + localpartid + ",dateid = " + localdateid + ",cityid" + localcityid);
					return hashMap;
				}
			};
			strinRequest.setTag("post");
			MyApplication.getHttpQueue().add(strinRequest);
			break;
		case R.id.spinner_statis_date:
			localdateid = position;
			StringRequest strinRequest1 = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(),voteErrorListener()) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("partid", localpartid + "");
					hashMap.put("cityid", localcityid + "");
					hashMap.put("days", localdateid + "");
					Log.e("id", "dateid = " + localdateid + ",cityid" + localcityid + ",partid = " + localpartid);
					return hashMap;
				}
			};
			strinRequest1.setTag("post");
			MyApplication.getHttpQueue().add(strinRequest1);
			break;
		case R.id.spinner_statis_city:
			localcityid = Integer.parseInt(cityidList.get(position));
			StringRequest strinRequest2 = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(),voteErrorListener()) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("partid", localpartid + "");
					hashMap.put("cityid", localcityid + "");
					hashMap.put("days", localdateid + "");
					Log.e("id", "cityid" + localcityid + ",partid = " + localpartid + ",dateid = " + localdateid);
					return hashMap;
				}
			};
			strinRequest2.setTag("post");
			MyApplication.getHttpQueue().add(strinRequest2);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) { // ListView中,获取subid  ,传递给SubContent
		Voteno voteno = (Voteno) votenoList.get(position);
		String localsubid = voteno.subid;
		Intent it_subContent = new Intent(this, SubContentActivity.class);
		it_subContent.putExtra("localsubid", localsubid);
		startActivity(it_subContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_statis_back:
			finish();
			overridePendingTransition(0, 0); // ****设置无跳转动画
			break;
		case R.id.btn_statis_more:
			Intent it_more = new Intent(this, CategoryActivity.class);
			startActivity(it_more);
			break;
		case R.id.button_statis_back:
			finish();
			overridePendingTransition(0, 0); // ****设置无跳转动画
			break;
		case R.id.txt_statis_search:
			String localsearch = autoTxt.getText().toString();
			Intent it_search = new Intent(StatisActivity.this, SearchActivity.class);
			it_search.putExtra("localsearch", localsearch);
			startActivity(it_search);
			break;
		}
	}
}