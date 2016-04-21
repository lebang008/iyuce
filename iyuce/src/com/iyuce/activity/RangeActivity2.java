package com.iyuce.activity;
//package com.woyuce.activity;
////*************************************************慕课法原生InputStream*********************************************** 
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.woyuce.adapter.RangeAdapter;
//import com.woyuce.entity.Range;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ImageView;
//import android.widget.ListView;
//
//public class RangeActivity2 extends Activity implements OnItemClickListener,OnClickListener {
//
//	private ImageView mBack;
//	private ListView mListView;
//	// private String URL = "http://iphone.ipredicting.com/getmonth.aspx"; //
//	// 苹果接口
//	private String HOLO_URL = "http://apapi.ipredicting.com/getmenulist.aspx?token=89F862A5EA64D897FB1D05F95C113AD8";
//	private String BASE_URL = "http://apapi.ipredicting.com/getmenulist.aspx?token=";
//
//	List<Range> rangeList = new ArrayList<Range>();
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setContentView(R.layout.activity_range);
//
//		initView();
//		new NewsAsyncTask().execute(HOLO_URL);
//	}
//
//	private void initView() {
//		mBack = (ImageView) findViewById(R.id.arrow_back);
//		mListView = (ListView) findViewById(R.id.listview_activity_rang);
//		mListView.setOnItemClickListener(this);
//		mBack.setOnClickListener(this);
//		// 拿到带token的完整URL
//		// HOLO_URL = BASE_URL + ((MainActivity)getActivity()).gettoken();
//	}
//
//	private List<Range> getJsonData(String url) {
//		// List<Range> rangeList = new ArrayList<Range>();
//		// 放在成员变量里，方便拿数据的时候调用*******************
//		try {
//			String jsonString = readStream(new URL(url).openStream());
//			JSONObject jsonobject;
//			Range range;
//			try {
//				jsonobject = new JSONObject(jsonString);
//				int result = jsonobject.getInt("return");
//				if (result == 0) {
//					JSONObject data = jsonobject.getJSONObject("data");
//					JSONArray datalist = data.getJSONArray("datalist");
//					Log.e("TAG", "我拿到Range的datalist了吗" + datalist);
//					for (int i = 0; i < datalist.length(); i++) {
//						jsonobject = datalist.getJSONObject(i);
//						range = new Range();
//						range.name = jsonobject.getString("name");
//						range.monthid = jsonobject.getString("menuid");
//						rangeList.add(range);
//						Log.e("xys", " 访问rangeList =  " + rangeList);  
//					}
//				} else {
//					JSONObject detail = jsonobject.getJSONObject("detail");
//					Log.e("xys", " 访问出错原因 ；" + detail); // 访问失败时观察日志
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return rangeList;
//	}
//
//	private String readStream(InputStream is) {
//		InputStreamReader isr;
//		String result = "";
//		try {
//			String line = "";
//			isr = new InputStreamReader(is, "utf-8");
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
//// ***********************************内部类开启异步任务**********************************------
//	private class NewsAsyncTask extends AsyncTask<String, Void, List<Range>> {
//
//		@Override
//		protected List<Range> doInBackground(String... params) {
//			return getJsonData(params[0]);
//		}
//
//		@Override
//		protected void onPostExecute(List<Range> range) {
//			super.onPostExecute(range);
//			Log.e("TAG", "范围 = " + range);
//			// ******************2、外部类调用适配器*******************
//			RangeAdapter adapter = new RangeAdapter(RangeActivity2.this, range);
//			mListView.setAdapter(adapter);
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//		Range range = (Range) rangeList.get(position);
//		String monthid = range.monthid;
//		String name = range.name;
//		Intent it_booklist = new Intent(RangeActivity2.this, LessonActivity.class);
//		it_booklist.putExtra("monthid", monthid);
//		it_booklist.putExtra("name", name);
//		Log.e("TAG", "输出range的参数 ： monthid = " + monthid + ", name =" + name);
//		startActivity(it_booklist);
//	}
//
//	@Override
//	public void onClick(View v) {
//		finish();
//	}
//}
