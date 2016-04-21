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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

public class RoomChooseActivity extends Activity implements OnClickListener, OnItemSelectedListener {

	private Spinner spnArea, spnCity, spnRoom;
	private Button btnSure, btnCancel;

	private List<Area> areaList = new ArrayList<Area>();
	private List<City> cityList = new ArrayList<City>();
	private List<Room> roomList = new ArrayList<Room>();

	private String URL_AREA = "http://iphone.ipredicting.com/kyAreaApi.aspx";
	private String URL_CITY = "http://iphone.ipredicting.com/kyCityApi.aspx";
	private String URL_ROOM = "http://iphone.ipredicting.com/kyRoomApi.aspx";
	private String localAreaID, localCityID;
	private String localRoomName,localRoomID;    //传递给下一级的数据
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_roomchoose);

		initView();
		getAreaList();
	}

	private void initView() {
		spnArea = (Spinner) findViewById(R.id.spn_choose_area);
		spnCity = (Spinner) findViewById(R.id.spn_choose_city);
		spnRoom = (Spinner) findViewById(R.id.spn_choose_room);

		btnSure = (Button) findViewById(R.id.btn_choose_sure);
		btnCancel = (Button) findViewById(R.id.btn_choose_cancel);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
	}

	private void getAreaList() {
		StringRequest strinRequest = new StringRequest(Method.GET, URL_AREA, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Area area;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							area = new Area();
							jsonObject = data.getJSONObject(i);
							area.subAreaName = jsonObject.getString("subAreaName");
							area.subAreaid = jsonObject.getString("subAreaid");
							areaList.add(area);
						}
					} else {
						Log.e("code!=0 DATA_BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setAreaData(); // 数据加载完成后再放入
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, errorBack());
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void getCityList() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_CITY, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				City city;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							city = new City();
							city.cityname = jsonObject.getString("cityname");
							city.cityid = jsonObject.getString("cityid");  
							cityList.add(city);
						}
					} else {
						Log.e("code!=0 DATA_BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setCityData(); // 数据加载完成后再放入
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, errorBack()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("areaid", localAreaID);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void getRoomList() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_ROOM, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Room room;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							room = new Room();
							jsonObject = data.getJSONObject(i);
							room.roomname = jsonObject.getString("roomname");
							room.roomid = jsonObject.getString("roomid");
							roomList.add(room);
						}
					} else {
						Log.e("code!=0 DATA_BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					setRoomData(); // 数据加载完成后再放入
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, errorBack()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("cityid", localCityID);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private ErrorListener errorBack() {              // 抽出链接错误的callBack
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "连接错误原因： " + error.getMessage(), error); 
			}
		};
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

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {    
		switch (parent.getId()) {
		case R.id.spn_choose_area:
			Area area = (Area)areaList.get(position);
			localAreaID = area.subAreaid;
			cityList.clear();
			getCityList();
			break;
		case R.id.spn_choose_city:
			City city = (City)cityList.get(position);
			localCityID = city.cityid;
			roomList.clear();
			getRoomList();
			break;
		case R.id.spn_choose_room:
			Room room = (Room)roomList.get(position);
			localRoomID = room.roomid;
			localRoomName = room.roomname;     
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_choose_sure:
			Intent intent = new Intent(this,ShareActivity.class);
			intent.putExtra("localRoomID", localRoomID);   // 设置返回数据
			intent.putExtra("localRoom", localRoomName);   // 设置返回数据
			setResult(1, intent);  
			finish();
			break;
		case R.id.btn_choose_cancel:
			finish();
			break;
		}
	}
}