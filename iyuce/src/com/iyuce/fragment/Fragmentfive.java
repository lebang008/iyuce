package com.iyuce.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.activity.AboutUsActivity;
import com.iyuce.activity.LoginActivity;
import com.iyuce.activity.SuggestionActivity;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Room;
import com.iyuce.utils.PreferenceUtil;
import com.woyuce.activity.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Fragmentfive extends Fragment implements OnClickListener {

	private TextView txtName, txtMoney, txtAboutUs, txtUpdate, txtSuggestion, txtRoom;  //txtSubject;
	private ImageView imgIcon;

	private String URL_ROOM = "http://iphone.ipredicting.com/kymyroom.aspx";
	private List<Room> roomList = new ArrayList<Room>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.tab5, container, false);

		initView(view);
		initEvent();
		getSubjectJson();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		initEvent();
	}

	private void initView(View view) {
		imgIcon = (ImageView) view.findViewById(R.id.img_tab5_icon);
		txtName = (TextView) view.findViewById(R.id.txt_tab5_username);
		txtMoney = (TextView) view.findViewById(R.id.txt_tab5_localmoney);
		txtAboutUs = (TextView) view.findViewById(R.id.txt_to_aboutus);
		txtUpdate = (TextView) view.findViewById(R.id.txt_to_update);
		txtSuggestion = (TextView) view.findViewById(R.id.txt_to_suggestion);
		txtRoom = (TextView) view.findViewById(R.id.txt_tab5_localroom);
//		txtSubject = (TextView) view.findViewById(R.id.txt_tab5_localsubject);

		imgIcon.setOnClickListener(this);
		txtAboutUs.setOnClickListener(this);
		txtUpdate.setOnClickListener(this);
		txtSuggestion.setOnClickListener(this);
		txtRoom.setOnClickListener(this);
	}

	private void initEvent() { // fragment 生命周期，打开时
		txtName.setText(share().getString("mUserName", "点击登录"));
		txtMoney.setText(share().getString("money", ""));

	}

	private SharedPreferences share() { // 从initEvent 抽出，方便调用，代码简洁
		return PreferenceUtil.getSharePre(getActivity());
	}

	private void getRoomJson() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL_ROOM, new Response.Listener<String>() {
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
							jsonObject = data.getJSONObject(i);
							room = new Room();
							room.roomid = jsonObject.getString("id");
							room.roomname = jsonObject.getString("examroom");
							roomList.add(room);
							Log.e("RoomList", "room：  " + roomList);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "room读取失败： " + jsonObject.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.e("RoomList", "room：  " + roomList);
			}
		}, null) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("uname", PreferenceUtil.getSharePre(getActivity()).getString("mUserName", ""));
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	private void getSubjectJson() {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_tab5_icon:
			startActivity(new Intent(getActivity(), LoginActivity.class));
			break;
		case R.id.txt_to_aboutus:
			startActivity(new Intent(getActivity(), AboutUsActivity.class));
			break;
		case R.id.txt_to_update:
			Toast.makeText(getActivity(), "已经是最新版啦，亲", Toast.LENGTH_SHORT).show();
			break;
		case R.id.txt_to_suggestion:
			startActivity(new Intent(getActivity(), SuggestionActivity.class));
			break;
		case R.id.txt_tab5_localroom:
			Room localroom;
			for (int i = 0; i < roomList.size(); i++) {
				localroom = (Room)roomList.get(i);
				Log.e("localroom", "localroom" + localroom.roomname);
			}
			getRoomJson();
//			Room localroom = (Room)roomList.get(0);
//			txtRoom.setText(localroom.roomname);
			break;
		}
	}
}