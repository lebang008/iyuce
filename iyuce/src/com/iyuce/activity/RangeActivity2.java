package com.iyuce.activity;
//package com.woyuce.activity;
////*************************************************Ľ�η�ԭ��InputStream*********************************************** 
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
//	// ƻ���ӿ�
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
//		// �õ���token������URL
//		// HOLO_URL = BASE_URL + ((MainActivity)getActivity()).gettoken();
//	}
//
//	private List<Range> getJsonData(String url) {
//		// List<Range> rangeList = new ArrayList<Range>();
//		// ���ڳ�Ա��������������ݵ�ʱ�����*******************
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
//					Log.e("TAG", "���õ�Range��datalist����" + datalist);
//					for (int i = 0; i < datalist.length(); i++) {
//						jsonobject = datalist.getJSONObject(i);
//						range = new Range();
//						range.name = jsonobject.getString("name");
//						range.monthid = jsonobject.getString("menuid");
//						rangeList.add(range);
//						Log.e("xys", " ����rangeList =  " + rangeList);  
//					}
//				} else {
//					JSONObject detail = jsonobject.getJSONObject("detail");
//					Log.e("xys", " ���ʳ���ԭ�� ��" + detail); // ����ʧ��ʱ�۲���־
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
//// ***********************************�ڲ��࿪���첽����**********************************------
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
//			Log.e("TAG", "��Χ = " + range);
//			// ******************2���ⲿ�����������*******************
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
//		Log.e("TAG", "���range�Ĳ��� �� monthid = " + monthid + ", name =" + name);
//		startActivity(it_booklist);
//	}
//
//	@Override
//	public void onClick(View v) {
//		finish();
//	}
//}
