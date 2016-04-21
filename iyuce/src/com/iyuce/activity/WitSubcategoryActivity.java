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
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.WitSubcategoryAdapter;
import com.iyuce.adapter.WitspnAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.WitCategory;
import com.iyuce.entity.WitSubcategory;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class WitSubcategoryActivity extends Activity
		implements OnClickListener, OnItemClickListener, OnItemSelectedListener {

	private TextView txtTitle;
	private AutoCompleteTextView autoTxt;
	private ImageView imgBack;
	private Button btnSearch;
	private GridView gridView;
	private Spinner spnCategory;

	private List<WitSubcategory> witsubcategoryList = new ArrayList<>();
	private List<WitCategory> witcategoryList = new ArrayList<>();
	private String localid, localname;
	private String localsubCategoryid; // 从Json中拿到的数据保存
	private String URL_CATEGORY = "http://iphone.ipredicting.com/xzsubCategory.aspx";
	private String URL_TOTAL = "http://iphone.ipredicting.com/xzCategoryApi.aspx";
	private boolean isfirst = true;

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_witsubcategory);

		initView();
		getSpinnerJson();
		getJson();
	}

	private void initView() {
		Intent it_witSubcategory = getIntent();
		localid = it_witSubcategory.getStringExtra("localid");
		localname = it_witSubcategory.getStringExtra("localname");

		txtTitle = (TextView) findViewById(R.id.txt_witsubcategory_title);
		autoTxt = (AutoCompleteTextView) findViewById(R.id.auto_witsubcategory);
		imgBack = (ImageView) findViewById(R.id.arrow_back);
		btnSearch = (Button) findViewById(R.id.btn_witsubcategory_search);
		gridView = (GridView) findViewById(R.id.gridview_witsubCategory);
		spnCategory = (Spinner) findViewById(R.id.spn_witsubcategory);

		txtTitle.setText(localname);
		btnSearch.setOnClickListener(this);
		imgBack.setOnClickListener(this);
		gridView.setOnItemClickListener(this);
	}

	private void setspnDate() {
		WitspnAdapter spnadapter = new WitspnAdapter(this, witcategoryList);
		spnCategory.setAdapter(spnadapter);
		spnCategory.setOnItemSelectedListener(this);
	}

	private void getSpinnerJson() {
		StringRequest strinrequest = new StringRequest(Method.POST, URL_TOTAL, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonobj;
				WitCategory witcategory;
				try {
					jsonobj = new JSONObject(response);
					int result = jsonobj.getInt("code");
					if (result == 0) {
						JSONArray data = jsonobj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonobj = data.getJSONObject(i);
							witcategory = new WitCategory();
							witcategory.name = jsonobj.getString("name");
							witcategory.id = jsonobj.getString("id");
							witcategoryList.add(witcategory);
						}
					} else {
						Log.e("Code Error", "code spinnerwrong" + response);
					}
					setspnDate(); // 数据加载完成后再放入
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null);
		strinrequest.setTag("post");
		MyApplication.getHttpQueue().add(strinrequest);
	}

	private void getJson() {
		StringRequest strinrequest = new StringRequest(Method.POST, URL_CATEGORY, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonobj;
				WitSubcategory witsubcategory;
				try {
					jsonobj = new JSONObject(response);
					int result = jsonobj.getInt("code");
					if (result == 0) {
						JSONArray data = jsonobj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonobj = data.getJSONObject(i);
							witsubcategory = new WitSubcategory();
							witsubcategory.bgColor = jsonobj.getString("bgColor").trim();
							witsubcategory.name = jsonobj.getString("name");
							witsubcategory.subCategoryid = jsonobj.getString("subCategoryid");
							witsubcategoryList.add(witsubcategory);
						}
					} else {
						Log.e("Code Error", "code wrong" + response);
					}
					WitSubcategoryAdapter adapter = new WitSubcategoryAdapter(WitSubcategoryActivity.this,witsubcategoryList);
					gridView.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<>();
				hashMap.put("categoryid", localid);
				return hashMap;
			}
		};
		strinrequest.setTag("post");
		MyApplication.getHttpQueue().add(strinrequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.arrow_back:
			finish();
			break;
		case R.id.btn_witsubcategory_search:
			String localkey = autoTxt.getText().toString();
			Intent it_search = new Intent(this, WitSearchActivity.class);
			it_search.putExtra("localid", localid);
			it_search.putExtra("localkey", localkey);
			startActivity(it_search);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WitSubcategory witsubcategory = (WitSubcategory) witsubcategoryList.get(position);
		localsubCategoryid = witsubcategory.subCategoryid;
		String localname = witsubcategory.name;
		Intent it_witContent = new Intent(this, WitContentActivity.class);
		it_witContent.putExtra("localsubCategoryid", localsubCategoryid);
		it_witContent.putExtra("localname", localname);
		startActivity(it_witContent);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(isfirst == true){
			isfirst = false;
		}else{
			WitCategory witcategory = (WitCategory) witcategoryList.get(position);
			localid = witcategory.id;
			localname = witcategory.name;
			txtTitle.setText(localname);
			witsubcategoryList.clear();
			getJson();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
}