package com.iyuce.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.application.MyApplication;
import com.iyuce.utils.PreferenceUtil;
import com.woyuce.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShareActivity extends BaseActivity implements OnClickListener, OnItemSelectedListener {

	private LinearLayout llBack;
	private Button btnNext,btnBack,btnRoomChoose;
	private TextView userName;
	private Spinner spnExamTime;
	
	private List<String> timeList = new ArrayList<String>();
	private ArrayAdapter<String> timeAdapter;

	private String URL_TIME = "http://iphone.ipredicting.com/ksexamtime.aspx";
	private String localRoom, localRoomID, localTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);

		initView();
		initEvent();
		getTimeList();
	}

	private void initView() {
		llBack = (LinearLayout) findViewById(R.id.ll_activity_share);
		btnNext = (Button) findViewById(R.id.button_share_next);
		btnBack = (Button) findViewById(R.id.button_share_back);
		btnRoomChoose = (Button) findViewById(R.id.btn_share_RoomChoose);
		userName = (TextView) findViewById(R.id.txt_share_userName);
		spnExamTime = (Spinner) findViewById(R.id.spn_share_examTime);
		
		llBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnRoomChoose.setOnClickListener(this);
	}

	private void initEvent() {         //Text ��ʾ�û��ǳ�
		String init = PreferenceUtil.getSharePre(this).getString("mUserName", "").toString();
		if (init!= "") {
			userName.setText(init);
		} else {
			userName.setText("��û�е�½");
		}
	}

	private void getTimeList() {
		StringRequest strinRequest = new StringRequest(Method.GET, URL_TIME, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				String examtime;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							examtime = jsonObject.getString("examtime");
							timeList.add(examtime); // ��ȡ�����¼�
						}
					} else {
						Log.e("code!=0 DATA_BACK", "��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
					setTimeData(); // ���ݼ�����ɺ��ٷ���
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "���Ӵ���ԭ�� " + error.getMessage(), error); // ��������
			}
		});
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}
	
	private void setTimeData() {
		timeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timeList); 
		timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnExamTime.setAdapter(timeAdapter);
		spnExamTime.setOnItemSelectedListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case 1:
			localRoom = data.getExtras().getString("localRoom");
			localRoomID = data.getExtras().getString("localRoomID");
			btnRoomChoose.setText(localRoom);
			Log.e("ALL", "all room = " + localRoom + ", " + localRoomID); // Log���ݷ���
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		localTime = timeList.get(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_activity_share:              //  ****  ���"��Ƶͳ��" , ���� Activity-ͳ�ƣ�  �����濴��������ת�仯
			Intent it_statis = new Intent(this,StatisActivity.class);
			startActivity(it_statis);
			overridePendingTransition(0, 0);
			break;
		case R.id.button_share_back:             //  ****  ���"����"��ť,  ����  ��һ��"����"���� 	
			finish();
			overridePendingTransition(0, 0); 
			break;	
		case R.id.btn_share_RoomChoose:
			Intent it_roomid = new Intent(this,RoomChooseActivity.class);
			startActivityForResult(it_roomid,1);
			break;
		case R.id.button_share_next:            //  ***  ���"��һ��"��ť,������һ��"����"����
			if( localRoom == null || localRoom == ""){
				toast("��ѡ�񿼳�");
			}else{
				Intent it_share2 = new Intent(this,ShareActivity2.class);
				it_share2.putExtra("localRoom", localRoom);
				it_share2.putExtra("localRoomID", localRoomID);
				it_share2.putExtra("localTime", localTime);
				startActivity(it_share2);
				overridePendingTransition(0,0);    
			    }
			break;
		}
	}
}