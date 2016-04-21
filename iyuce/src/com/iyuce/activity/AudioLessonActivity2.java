package com.iyuce.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.AudioLessonAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Audio;
import com.woyuce.activity.R;

import android.app.Activity;
import android.app.ProgressDialog;
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

public class AudioLessonActivity2 extends Activity implements OnItemClickListener, OnClickListener {

	private ListView listview;
	private TextView txtback;
	private Button btnListening, btnSpeaking, btnReading, btnWritting;

	private List<Audio> audioList = new ArrayList<Audio>();
	private String URL_LIST = "http://php.ipredicting.com/service/audiolistingroup.php";
	private String URL_DETAIL = "http://php.ipredicting.com/service/audiodetail.php?id=";

	private String localAudioUrl, localAudioTitle;
	private AudioLessonAdapter adapter;
	
	private ProgressDialog pd ;

	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiolesson);

		initView();
		getListJson(URL_LIST);
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.listview_audiolesson);
		txtback = (TextView) findViewById(R.id.txt_audiolesson_back);
		btnListening = (Button) findViewById(R.id.btn_audiolesson_listening);
		btnSpeaking = (Button) findViewById(R.id.btn_audiolesson_reading);
		btnReading = (Button) findViewById(R.id.btn_audiolesson_speaking);
		btnWritting = (Button) findViewById(R.id.btn_audiolesson_writting);

		listview.setOnItemClickListener(this);
		txtback.setOnClickListener(this);
		btnListening.setOnClickListener(this);
		btnSpeaking.setOnClickListener(this);
		btnReading.setOnClickListener(this);
		btnWritting.setOnClickListener(this);
	}

	private void getListJson(String url) {
		pd = new ProgressDialog(this);
		pd.setCancelable(false);
		pd.show();
		StringRequest strinrequest = new StringRequest(Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject obj;
				JSONArray data;
				Audio audio;
				try {
					String parseString = new String(response.getBytes("ISO-8859-1"), "utf-8");
					data = new JSONArray(parseString);
					for (int i = 0; i < data.length(); i++) {
						audio = new Audio();
						obj = data.getJSONObject(i);
						audio.id = obj.getString("id");
						audio.title = obj.getString("title");
						audioList.add(audio);
					}
				} catch (JSONException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				adapter = new AudioLessonAdapter(AudioLessonActivity2.this, audioList);
				listview.setAdapter(adapter);
				pd.cancel();     //与dismiss效果相当，可能还关闭了某些，释放资源
			}
		}, errorback());
		strinrequest.setTag("post");
		MyApplication.getHttpQueue().add(strinrequest);
	}

	private void getDetailJson(String url) { // 抽出的方法，通过Item事件选择 ,拿到ID后，传入参数，执行该方法，成功则进入下一个Activity，并附带AudioUrl
		StringRequest strinrequest = new StringRequest(Method.GET, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject obj;
				try {
					String parseString = new String(response.getBytes("ISO-8859-1"), "utf-8");
					obj = new JSONObject(parseString);
					localAudioUrl = obj.getString("url");
					localAudioTitle = obj.getString("title");
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				Intent it = new Intent(AudioLessonActivity2.this, AudioContentActivity.class);
				it.putExtra("url", localAudioUrl);
				it.putExtra("title", localAudioTitle);
				Log.e("local", "localAudioUrl = " + localAudioUrl + ", localAudioTitle = " + localAudioTitle);
				startActivity(it);
			}
		}, errorback());
		strinrequest.setTag("post");
		MyApplication.getHttpQueue().add(strinrequest);
	}

	private ErrorListener errorback() { // 抽出错误回调
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "联接错误原因： " + error.getMessage(), error);
			}
		};
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Audio audio = (Audio) audioList.get(position);
		String localid = audio.id;
		getDetailJson(URL_DETAIL + localid); // 执行Volley 拿Audio的 url
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_audiolesson_back:
			finish();
			break;
		case R.id.btn_audiolesson_listening:
			String URL_LISTENING = URL_LIST + "?type=1";
			audioList.clear();
			adapter.notifyDataSetChanged();
			getListJson(URL_LISTENING);
			break;
		case R.id.btn_audiolesson_reading:
			String URL_READING = URL_LIST + "?type=3";
			audioList.clear();
			adapter.notifyDataSetChanged();
			getListJson(URL_READING);
			break;
		case R.id.btn_audiolesson_speaking:
			String URL_SPEAKING = URL_LIST + "?type=7";
			audioList.clear();
			adapter.notifyDataSetChanged();
			getListJson(URL_SPEAKING);
			break;
		case R.id.btn_audiolesson_writting:
			String URL_WRITTING = URL_LIST + "?type=4";
			audioList.clear();
			adapter.notifyDataSetChanged();
			getListJson(URL_WRITTING);
			break;
		}
	}
}