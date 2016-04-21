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
import com.iyuce.adapter.LessonAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Lesson;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LessonActivity extends Activity implements OnItemClickListener, OnClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private GridView mGridView;

	private String URL = "http://iphone.ipredicting.com/GetKsType.aspx";
	private String token, monthid,localname;           //����һ���õ������ݱ��棬  ���ٴ��ݵ���һ��
	private List<Lesson> lessonList = new ArrayList<Lesson>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lesson);

		initView();
		getJson();
	}

	private void initView() {
		Intent it_lesson = getIntent();
		token = it_lesson.getStringExtra("token");
		monthid = it_lesson.getStringExtra("monthid");
		localname = it_lesson.getStringExtra("localname");

		mTitle = (TextView) findViewById(R.id.txt_lesson_title);
		mBack = (ImageView) findViewById(R.id.arrow_back);
		mGridView = (GridView) findViewById(R.id.gridview_lesson);

		mBack.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
		mTitle.setText(localname);
	}

	private void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Lesson lesson;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							lesson = new Lesson();
							lesson.booktype = jsonObject.getString("booktype");
							lesson.imgPath = jsonObject.getString("imgPath");
							lessonList.add(lesson);
						}
					} else {
						Log.e("code!=0 DATA_BACK", "��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
					// �ڶ����������ݷŵ���������
					LessonAdapter adapter = new LessonAdapter(LessonActivity.this, lessonList);
					mGridView.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.e("Data_BACK", "JSON�ӿڷ��ص���Ϣ�� " + response); // ���Ƿ��ص�����JSON��Ϣ��δ���������ҪJSON���ݣ����Դ�������
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "���Ӵ���ԭ�� " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("token", token);
				hashMap.put("monthid", monthid);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Lesson lesson = (Lesson) lessonList.get(position);
		String localbooktype = lesson.booktype;
		Intent it_book = new Intent(this, BookActivity.class);
		it_book.putExtra("token", token);
		it_book.putExtra("monthid", monthid);
		it_book.putExtra("booktype", localbooktype);
//		Log.e("sys.out", "���lesson���ݵĲ��� �� token = " + token + ", monthid =" + monthid + ", booktype = " + localbooktype ); // ʵ����JSON��û��typeName��������
		startActivity(it_book);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}