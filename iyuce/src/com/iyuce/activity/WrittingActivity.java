package com.iyuce.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.WrittingAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.WitCategory;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class WrittingActivity extends Activity implements OnClickListener, OnItemClickListener {

	private GridView gvCategory;
	private ImageView imgBack;
	private Button btnSearch;
	private AutoCompleteTextView autoTxt;

	private String URL_WITCATEGORY = "http://iphone.ipredicting.com/xzCategoryApi.aspx";
	private String localid, localname, localkey;
	private List<WitCategory> witcategoryList = new ArrayList<WitCategory>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writting);

		initView();
		getJson();
	}

	private void initView() {
		gvCategory = (GridView) findViewById(R.id.gridview_writting);
		imgBack = (ImageView) findViewById(R.id.arrow_back);
		btnSearch = (Button) findViewById(R.id.btn_writting_search);
		autoTxt = (AutoCompleteTextView) findViewById(R.id.auto_writting);

		imgBack.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		gvCategory.setOnItemClickListener(this);
	}

	private void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_WITCATEGORY, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				WitCategory witcategory;
				JSONObject obj;
				try {
					obj = new JSONObject(response);
					int result = obj.getInt("code");
					if (result == 0) {
						JSONArray data = obj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							obj = data.getJSONObject(i);
							witcategory = new WitCategory();
							witcategory.name = obj.getString("name");
							witcategory.id = obj.getString("id");
							witcategoryList.add(witcategory);
						}
					} else {
						System.out.println("result £¡= 0");
					}
					WrittingAdapter adapter = new WrittingAdapter(WrittingActivity.this, witcategoryList);
					gvCategory.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null);
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_writting_search:
			localkey = autoTxt.getText().toString();
			Intent it_search = new Intent(this, WitSearchActivity.class);
			it_search.putExtra("localkey", localkey);
			startActivity(it_search);
			break;
		case R.id.arrow_back:
			finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WitCategory witcategory = (WitCategory) witcategoryList.get(position);
		localid = witcategory.id;
		localname = witcategory.name;
		Intent it_witSubcategory = new Intent(this, WitSubcategoryActivity.class);
		it_witSubcategory.putExtra("localid", localid);
		it_witSubcategory.putExtra("localname", localname);
		startActivity(it_witSubcategory);
	}
}