package com.iyuce.activity;
//package com.woyuce.activity;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.Request.Method;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.toolbox.StringRequest;
//import com.woyuce.adapter.VotenoAdapter;
//import com.woyuce.application.MyApplication;
//import com.woyuce.entity.Voteno;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemSelectedListener;
//
//public class StatisActivity2 extends Activity implements OnClickListener,OnItemSelectedListener,OnItemClickListener {
//
//	private String URL_PART = "http://iphone.ipredicting.com/kyPartApi.aspx";
//	private String URL_CITY = "http://iphone.ipredicting.com/kyCityApi.aspx";
//	private String URL_VOTE = "http://iphone.ipredicting.com/kysubOrder.aspx";
//	
//	private int localpartid;
//	private int localdateid;
//	private int localcity;
//
//	private LinearLayout llBack;
//	private ListView listViewVote;
//	private Spinner spnPart, spnDate, spnCity;
//	private Button btnMore;
//	
//	private ArrayAdapter<String> partAdapter, dateAdapter, cityAdapter;
//	private List<String> partList = new ArrayList<String>();
//	private List<String> dateList = new ArrayList<String>();
//	private List<String> cityList = new ArrayList<String>();
//	private List<Voteno> votenoList = new ArrayList<Voteno>();
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_statis);
//
//		initView();
//		
////		getPartList();
//		getCityList();
//		getVote();
//	}
//
//	private void initView() {
//		llBack = (LinearLayout) this.findViewById(R.id.ll_statis_back);
//		llBack.setOnClickListener(this);
//		btnMore = (Button) this.findViewById(R.id.btn_statis_more);
//		btnMore.setOnClickListener(this);
//		
//		listViewVote = (ListView) this.findViewById(R.id.listview_statis_vote);
//		listViewVote.setOnItemClickListener(this);
//		
//		spnPart = (Spinner) this.findViewById(R.id.spinner_statis_part);
//		spnDate = (Spinner) this.findViewById(R.id.spinner_statis_date);
//		spnCity = (Spinner) this.findViewById(R.id.spinner_statis_city);
//
//		partList.add("part1");
//		partList.add("part2");
//		dateList.add("30天");
//		dateList.add("120天");
//		cityList.add("全国统计");
//
//		partAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, partList);
//		partAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnPart.setAdapter(partAdapter);
//		spnPart.setOnItemSelectedListener(this);     // part的Select事件
//
//		dateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateList);
//		dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnDate.setAdapter(dateAdapter);
//		spnDate.setOnItemSelectedListener(this);
//		
//		cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
//		cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		spnCity.setAdapter(cityAdapter);
//		spnCity.setOnItemSelectedListener(this);
//	}
//
////	private void getPartList() {
////		// MyApplication.getHttpQueue().cancelAll("post");
////		StringRequest strinRequest = new StringRequest(Method.POST, URL_PART, new Response.Listener<String>() {
////			@Override
////			public void onResponse(String response) {
////				JSONObject jsonObject;
////				String partname;
////				try {
////					jsonObject = new JSONObject(response);
////					int result = jsonObject.getInt("code");
////					if (result == 0) {
////						JSONArray data = jsonObject.getJSONArray("data");
////						for (int i = 0; i < data.length(); i++) {
////							jsonObject = data.getJSONObject(i);
////							partname = jsonObject.getString("partname");
////							partList.add(partname);
////						}
////					} else {
////						String message = jsonObject.getString("message");
////						Log.e("code!=0 DATA_BACK", "读取页面失败： " + message);
////					}
////				} catch (JSONException e) {
////					e.printStackTrace();
////				}
//////				Log.e("Data_BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
////			}
////		}, voteErrorListener()) {
////			@Override
////			protected Map<String, String> getParams() throws AuthFailureError {
////				Map<String, String> hashMap = new HashMap<String, String>();
////				// hashMap.put("token", token);
////				return hashMap;
////			}
////		};
////		strinRequest.setTag("post");
////		MyApplication.getHttpQueue().add(strinRequest);
////	}
//
//	private void getCityList() {
//		// MyApplication.getHttpQueue().cancelAll("post");
//		StringRequest strinRequest = new StringRequest(Method.POST, URL_CITY, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				JSONObject jsonObject;
//				// City city;
//				String cityname;
//				try {
//					jsonObject = new JSONObject(response);
//					int result = jsonObject.getInt("code");
//					if (result == 0) {
//						JSONArray data = jsonObject.getJSONArray("data");
//						for (int i = 0; i < data.length(); i++) {
//							jsonObject = data.getJSONObject(i);
//							// city = new City();
//							// city.cityname = jsonObject.getString("cityname");
//							cityname = jsonObject.getString("cityname");
//							cityList.add(cityname); // 读取城市
//						}
//					} else {
//						String message = jsonObject.getString("message");
//						Log.e("code!=0 DATA_BACK", "读取页面失败： " + message);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
////				Log.e("Data_BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
//			}
//		}, voteErrorListener()) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> hashMap = new HashMap<String, String>();
//				// hashMap.put("token", token);
//				return hashMap;
//			}
//		};
//		strinRequest.setTag("post");
//		MyApplication.getHttpQueue().add(strinRequest);
//	}
//
//	private void getVote() {
//		StringRequest strinRequest = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(), voteErrorListener()) {
//			@Override
//			protected Map<String, String> getParams() throws AuthFailureError {
//				Map<String, String> hashMap = new HashMap<String, String>();
//				 hashMap.put("partid", "1");
//				 hashMap.put("cityid", "45");
//				 hashMap.put("days", "0");
//				return hashMap;
//			}
//		};
//		strinRequest.setTag("post");
//		MyApplication.getHttpQueue().add(strinRequest);
//	}
//
///**
// * 抽出Volley中的访问成功voteResponseListener方法    (仅用于getVote()及相关方法)
// */
//	private Listener<String> voteResponseListener() {
//		return new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				JSONObject jsonObject;
//				Voteno voteno;
//				try {
//					jsonObject = new JSONObject(response);
//					int result = jsonObject.getInt("code");
//					if (result == 0) {
//						JSONArray data = jsonObject.getJSONArray("data");
//						for (int i = 0; i < data.length(); i++) {
//							jsonObject = data.getJSONObject(i);
//							voteno = new Voteno();
//							voteno.subid = jsonObject.getString("subid");
//							voteno.categoryName = jsonObject.getString("categoryName");
//							voteno.votetotal = jsonObject.getString("votetotal");
//							votenoList.add(voteno);
//							VotenoAdapter votenoadapter = new VotenoAdapter(StatisActivity2.this, votenoList);
//							listViewVote.setAdapter(votenoadapter);	
//						}
//					} else {
//						String message = jsonObject.getString("message");
//						Log.e("code!=0 DATA_BACK", "读取页面失败： " + message);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
////				Log.e("Data_BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
//			}
//		};
//	}
//
///**
// * 	抽出Volley中访问失败voteErrorListener方法     (本类通用)
// */
//	private ErrorListener voteErrorListener() {
//		return new ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Log.e("Wrong-BACK", "连接错误原因： " + error.getMessage(), error); // 可以做错误处理
//			}
//		};
//	}
//
//	//**点击事件**
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ll_statis_back:
//			finish();
//			overridePendingTransition(0, 0); // ****设置无跳转动画
//			break;
//		case R.id.btn_statis_more:
//			Intent it_more = new Intent(StatisActivity2.this,CategoryActivity.class);
//			startActivity(it_more);
//		}
//	}
//	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		//获取subid 传递给SubContent
//		Voteno voteno = (Voteno)votenoList.get(position);
//		String localsubid = voteno.subid;
//		Intent it_subContent = new Intent(StatisActivity2.this,SubContentActivity.class);
//		it_subContent.putExtra("localsubid", localsubid);
//		startActivity(it_subContent);
//	}
//
//	
///**
// * 	可能应该用Handler去发送消息 ，更新队列   ***********************************
// */
//	@Override
//	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//		switch (parent.getId()) {
//		case R.id.spinner_statis_part:
//			votenoList.clear();  
//			localpartid = position + 1;
//			Log.e("localpartid", "localpartid = " + localpartid);
//			StringRequest strinRequest1 = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(), voteErrorListener()) {
//				@Override
//				protected Map<String, String> getParams() throws AuthFailureError {
//					Map<String, String> hashMap = new HashMap<String, String>();
//					 hashMap.put("partid", localpartid + "");
//					 hashMap.put("cityid", "45");
//					 hashMap.put("days","1");
//					return hashMap;
//				}
//			};
//			strinRequest1.setTag("post");
//			MyApplication.getHttpQueue().add(strinRequest1);
//			break;
//		case R.id.spinner_statis_date:
//			votenoList.clear();  
//			localdateid = position;
//			Log.e("localdateid", "localdateid = " + localdateid);
//			StringRequest strinRequest2 = new StringRequest(Method.POST, URL_VOTE, voteResponseListener(), voteErrorListener()) {
//				@Override
//				protected Map<String, String> getParams() throws AuthFailureError {
//					Map<String, String> hashMap = new HashMap<String, String>();
//					 hashMap.put("partid", "1");
//					 hashMap.put("cityid", "40");
//					 hashMap.put("days",localdateid + "");
//					return hashMap;
//				}
//			};
//			strinRequest2.setTag("post");
//			MyApplication.getHttpQueue().add(strinRequest2);
//			break;
//		}
//	}
//	@Override
//	public void onNothingSelected(AdapterView<?> parent) {
//	}
//}