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
import com.iyuce.adapter.AreaAdapter;
import com.iyuce.adapter.CityAdapter;
import com.iyuce.adapter.RoomAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Area;
import com.iyuce.entity.City;
import com.iyuce.entity.Room;
import com.woyuce.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class VoteActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	private TextView txtTitle;
	private Button btnCancel, btnVote;
	private Spinner spnArea, spnCity, spnRoom;

	private String localsubID, localsubName;
	private String URL_AREA = "http://iphone.ipredicting.com/kyAreaApi.aspx";
	private String URL_CITY = "http://iphone.ipredicting.com/kyCityApi.aspx";
	private String URL_ROOM = "http://iphone.ipredicting.com/kyRoomApi.aspx";
	private String URL_VOTE = "http://iphone.ipredicting.com/kysubVote.aspx";
	private String localAreaId, localCityId, localRoomId;

	private List<Area> areaList = new ArrayList<Area>();
	private List<City> cityList = new ArrayList<City>();
	private List<Room> roomList = new ArrayList<Room>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote);

		initView();
		getAreaJson();
	}

	private void initView() {
		Intent it_vote = getIntent();
		localsubID = it_vote.getStringExtra("localsubID");
		localsubName = it_vote.getStringExtra("localsubName");

		txtTitle = (TextView) findViewById(R.id.txt_vote_Title);
		btnCancel = (Button) findViewById(R.id.btn_vote_cancel);
		btnVote = (Button) findViewById(R.id.btn_vote_vote);
		spnArea = (Spinner) findViewById(R.id.spn_vote_area);
		spnCity = (Spinner) findViewById(R.id.spn_vote_city);
		spnRoom = (Spinner) findViewById(R.id.spn_vote_room);
		
		btnCancel.setOnClickListener(this);
		btnVote.setOnClickListener(this);
		txtTitle.setText(localsubName);
	}

	private void setAreaData() {
		AreaAdapter areaAdapter = new AreaAdapter(this, areaList);
		spnArea.setAdapter(areaAdapter);
		spnArea.setOnItemSelectedListener(this);
	}

	private void setCityData() {
		CityAdapter cityAdapter = new CityAdapter(this, cityList);
		spnCity.setAdapter(cityAdapter);
		spnCity.setOnItemSelectedListener(this);
	}

	private void setRoomData() {
		RoomAdapter roomAdapter = new RoomAdapter(this, roomList);
		spnRoom.setAdapter(roomAdapter);
		spnRoom.setOnItemSelectedListener(this);
	}

	private void getAreaJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_AREA, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					Area area;
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							area = new Area();
							area.subAreaName = jsonObject.getString("subAreaName");
							area.subAreaid = jsonObject.getString("subAreaid");
							areaList.add(area);
						}
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setAreaData(); // 数据加载完成后再放入 ******************************
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, errorListener());
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void getCityJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_CITY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					City city;
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							city = new City();
							city.cityid = jsonObject.getString("cityid");
							city.cityname = jsonObject.getString("cityname");
							cityList.add(city);
						}
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setCityData(); // 数据加载完成后再放入 ******************************
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				if (localAreaId == null) {
					hashMap.put("areaid", "45");
				}
				hashMap.put("areaid", localAreaId);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void getRoomJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_ROOM, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					Room room;
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							room = new Room();
							room.roomid = jsonObject.getString("roomid");
							room.roomname = jsonObject.getString("roomname");
							roomList.add(room);
						}
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setRoomData(); // 数据加载完成后再放入 ******************************
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON接口返回的信息： " + response); // 这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("cityid", localCityId);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void toVote() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_VOTE, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						toast("投票成功");
						finish();
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				 Log.e("DATA-BACK", "JSON接口返回的信息： " + response);               //这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, errorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("areaid", localAreaId);
				hashMap.put("cityid", localCityId);
				hashMap.put("roomid", localRoomId);
				hashMap.put("subid", localsubID);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private ErrorListener errorListener() {         //抽出的错误报告方法*******************
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "联接错误原因： " + error.getMessage(), error);
			}
		};
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.spn_vote_area:
			Area area = (Area) areaList.get(position);
			localAreaId = area.subAreaid;
			cityList.removeAll(cityList);
			getCityJson();
			break;
		case R.id.spn_vote_city:
			City city = (City) cityList.get(position);
			localCityId = city.cityid;
			roomList.removeAll(roomList);
			getRoomJson();
			break;
		case R.id.spn_vote_room:
			Room room = (Room) roomList.get(position);
			localRoomId = room.roomid;
			Log.e("all vote id ","all vote id = " + localAreaId + "," + localCityId + "," + localRoomId + "," + localsubID); // Log出所有Vote所需的数据
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_vote_cancel:
			finish();
			break;
		case R.id.btn_vote_vote:    //考虑是否需要投票成功后直接跳转    ,跳转是为了直接刷新数据，增强用户体验
			toVote();     
//			Intent it_speaking = new Intent(this,SpeakingActivity.class);   
//			startActivity(it_speaking);
//			finish();
//			overridePendingTransition(0, 0);
			break;
		}
	}
}