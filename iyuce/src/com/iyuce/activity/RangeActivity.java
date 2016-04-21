package com.iyuce.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.RangeAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Range;
import com.iyuce.utils.PreferenceUtil;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class RangeActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ImageView mBack;
	private ListView mListView;

	private String URL = "http://iphone.ipredicting.com/getmonth.aspx";
	private List<Range> rangeList = new ArrayList<Range>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_range);

		initView();
		getJson();
	}

	private void initView() {
		mBack = (ImageView) findViewById(R.id.arrow_back);
		mListView = (ListView) findViewById(R.id.listview_activity_rang);

		mBack.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
	}

	private void getJson() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Range range;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							range = new Range();
							range.monthid = jsonObject.getString("monthid");
							range.name = jsonObject.getString("name");
							rangeList.add(range);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					// 第二步，将数据放到适配器中
					RangeAdapter adapter = new RangeAdapter(RangeActivity.this, rangeList);
					mListView.setAdapter(adapter);
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
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("token", PreferenceUtil.getSharePre(getApplicationContext()).getString("token",
						"89F862A5EA64D897FB1D05F95C113AD8"));
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Range range = (Range) rangeList.get(position);
		String localmonthid = range.monthid;
		String localname = range.name;
		Intent it_lesson = new Intent(this, LessonActivity.class);
		it_lesson.putExtra("localname", localname);
		it_lesson.putExtra("monthid", localmonthid);
		it_lesson.putExtra("token", PreferenceUtil.getSharePre(getApplicationContext()).getString("token","89F862A5EA64D897FB1D05F95C113AD8"));
//		Log.i("sys.out", "输出range的参数 ： monthid = " + localmonthid + ", name =" + localname + ", token = "+ PreferenceUtil.getSharePre(getApplicationContext()).getString("token", ""));
		startActivity(it_lesson);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}