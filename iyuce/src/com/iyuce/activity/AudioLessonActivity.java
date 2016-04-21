package com.iyuce.activity;
//package com.woyuce.activity;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.android.volley.Request.Method;
//import com.android.volley.Response;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.woyuce.adapter.AudioLessonAdapter;
//import com.woyuce.application.MyApplication;
//import com.woyuce.entity.Audio;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class AudioLessonActivity extends Activity implements OnItemClickListener,OnClickListener{
//
//	private ListView listview;
//	private TextView txtback;
//	private Button btnListening, btnSpeaking, btnReading, btnWritting;
//	
//	private List<Audio> audioList = new ArrayList<Audio>();
//	private String URL_LIST = "http://php.ipredicting.com/service/audiolistingroup.php";    
//	private String URL_DETAIL = "http://php.ipredicting.com/service/audiodetail.php?id=";     
//	private String localAudioUrl,parseString;
//	ProgressDialog pd;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_audiolesson);
//		
//		initView();
//		new ListTask().execute(URL_LIST);
//	}
//
//	private void initView() {
//		listview = (ListView) findViewById(R.id.listview_audiolesson);
//		txtback = (TextView) findViewById(R.id.txt_audiolesson_back);
//		btnListening = (Button) findViewById(R.id.btn_audiolesson_listening);
//		btnSpeaking = (Button) findViewById(R.id.btn_audiolesson_reading);
//		btnReading = (Button) findViewById(R.id.btn_audiolesson_speaking);
//		btnWritting = (Button) findViewById(R.id.btn_audiolesson_writting);
//		
//		txtback.setOnClickListener(this);
//		btnListening.setOnClickListener(this);
//		btnSpeaking.setOnClickListener(this);
//		btnReading.setOnClickListener(this);
//		btnWritting.setOnClickListener(this);
//		listview.setOnItemClickListener(this);
//	}
//	
//	private String readStream (InputStream is){
//		InputStreamReader isr;
//		String result = "";
//		try {
//			String line = "";
//			isr = new InputStreamReader(is,"utf-8");
//			BufferedReader br = new BufferedReader(isr);
//			while ((line = br.readLine()) != null) {
//				result += line;
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	private List<Audio> getJson(String url){
//		try {
//			String jsonstring = readStream(new URL(url).openStream());
//			JSONObject obj;
//			JSONArray data;
//			Audio audio;
//			try {
//				data = new JSONArray(jsonstring);
//				for (int i = 0; i < data.length(); i++) {
//					audio = new Audio();
//					obj = data.getJSONObject(i);
//					audio.id = obj.getString("id");
//					audio.title = obj.getString("title");
//					audioList.add(audio);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return audioList;
//	}
//	
//	public class ListTask extends AsyncTask<String, Void, List<Audio>>{
//		
//		@Override
//		protected void onPreExecute() {             //预加载提示 
//			super.onPreExecute();
//			pd = new ProgressDialog(AudioLessonActivity.this);
//			pd.setTitle("加载中，请稍候");
//			pd.show();
//		}
//		
//		@Override
//		protected List<Audio> doInBackground(String... params) {
//			return getJson(params[0]);
//		}
//		
//		@Override
//		protected void onPostExecute(List<Audio> audioList) {
//			super.onPostExecute(audioList);
//			pd.dismiss();
//			AudioLessonAdapter adapter = new AudioLessonAdapter(AudioLessonActivity.this, audioList);
//			listview.setAdapter(adapter);
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Audio audio = (Audio)audioList.get(position);
//		String localid = audio.id;
//		getVolleyJson(URL_DETAIL + localid);                     //执行Volley  拿Audio的 url
//	}
//
//	private void getVolleyJson(String URL) {                    // 抽出的方法，通过Item事件选择 ，拿到ID后，传入参数，执行该方法，成功则进入下一个Activity，并附带AudioUrl
//		StringRequest strinrequest = new StringRequest(Method.GET, URL, new Response.Listener<String>() {
//			@Override
//			public void onResponse(String response) {
//				JSONObject obj;
//				try {
//					parseString =new String(response.getBytes("ISO-8859-1"),"utf-8");
//					Log.e("volley json", "volley parseString = " + parseString);
//					obj = new JSONObject(parseString);
//					localAudioUrl = obj.getString("url");
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (UnsupportedEncodingException e) {
//					e.printStackTrace();
//				}
//				Log.e("url", "audiourl = " + localAudioUrl);    
//				Intent it = new Intent(AudioLessonActivity.this,AudioContentActivity.class);
//				it.putExtra("url", localAudioUrl);
//				startActivity(it);
//			}
//		}, new ErrorListener() {
//			@Override
//			public void onErrorResponse(VolleyError error) {
//				Log.e("Wrong_BACK", "联接错误原因： " + error.getMessage(), error);
//			}
//		});
//		strinrequest.setTag("post");
//		MyApplication.getHttpQueue().add(strinrequest);
//	}
//	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.txt_audiolesson_back:
//			finish();
//			break;
////		case R.id.btn_audiolesson_listening:
////			String URL_LISTENING = URL_LIST + "?type=1";
////			audioList.clear();
////			adapter.notifyDataSetChanged();
////			getListJson(URL_LISTENING);
////			break;
////		case R.id.btn_audiolesson_reading:
////			String URL_READING = URL_LIST + "?type=3";
////			audioList.clear();
////			adapter.notifyDataSetChanged();
////			getListJson(URL_READING);
////			break;
////		case R.id.btn_audiolesson_speaking:
////			String URL_SPEAKING = URL_LIST + "?type=7";
////			audioList.clear();
////			adapter.notifyDataSetChanged();
////			getListJson(URL_SPEAKING);
////			break;
////		case R.id.btn_audiolesson_writting:
////			String URL_WRITTING = URL_LIST + "?type=4";
////			audioList.clear();
////			adapter.notifyDataSetChanged();
////			getListJson(URL_WRITTING);
////			break;
//		}
//	}
//}