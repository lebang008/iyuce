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
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.CategoryAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Category;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

public class CategoryActivity extends Activity implements OnItemClickListener, OnClickListener {

	private LinearLayout llBack;
	private GridView gridView;
	private Button btnBack, btnPart1, btnPart2;
	
	private String URL = "http://iphone.ipredicting.com/kysubCategoryApi.aspx";
	private int localPartid = 1;
	private List<Category> categoryList = new ArrayList<Category>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);

		initView();
		getJson();
	}

	private void initView() {
		llBack = (LinearLayout) findViewById(R.id.ll_category_back);
		btnBack = (Button) findViewById(R.id.button_category_back);
		btnPart1 = (Button) findViewById(R.id.btn_category_part1);
		btnPart2 = (Button) findViewById(R.id.btn_category_part2);
		gridView = (GridView) findViewById(R.id.gridview_category);

		llBack.setOnClickListener(this);
		btnPart2.setOnClickListener(this);
		btnPart1.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
	}

	public void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Category category;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							category = new Category();
							category.subCategoryid = jsonObject.getString("subCategoryid");
							category.subCategoryname = jsonObject.getString("subCategoryname");
							category.fontColor = jsonObject.getString("fontColor");
							categoryList.add(category);
						}
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, categoryList);
					gridView.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON接口返回的信息： " + response); //这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "联接错误原因： " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("partid", localPartid + ""); 
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Category category = (Category) categoryList.get(position);
		String localsubCategoryid = category.subCategoryid;
		Intent it_subContent = new Intent(this, SubContentActivity.class);
		it_subContent.putExtra("localsubCategoryid", localsubCategoryid);
		startActivity(it_subContent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_category_back:
			Intent it_speaking = new Intent(this,SpeakingActivity.class);
			startActivity(it_speaking);
			overridePendingTransition(0, 0); // ****设置无跳转动画
			break;
		case R.id.button_category_back:
			finish();
			break;
		case R.id.btn_category_part1:
			categoryList.clear();
			btnPart1.setBackgroundColor(Color.parseColor("#3399ff"));
			btnPart1.setTextColor(Color.parseColor("#ffffff"));
			btnPart2.setBackgroundResource(com.woyuce.activity.R.drawable.buttonstyle);
			btnPart2.setTextColor(Color.parseColor("#3399ff"));
			localPartid = 1;
			getJson();
			break;
		case R.id.btn_category_part2:
			categoryList.clear();
			btnPart1.setBackgroundResource(com.woyuce.activity.R.drawable.buttonstyle);
			btnPart1.setTextColor(Color.parseColor("#3399ff"));
			btnPart2.setBackgroundColor(Color.parseColor("#3399ff"));
			btnPart2.setTextColor(Color.parseColor("#ffffff"));
			localPartid = 2;
			getJson();
			break;
		}
	}
}