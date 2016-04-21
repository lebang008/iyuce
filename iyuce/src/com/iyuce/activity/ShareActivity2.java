package com.iyuce.activity;

import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShareActivity2 extends Activity implements OnClickListener{

	private Button btnBack,btnNext;
	private LinearLayout llBack;
	private TextView txtExamRoom,txtExamTime;
	private EditText edtMessage;
	
	private String localRoom,localTime,localRoomID;    //��һ������������,����id��timeҪ������һ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share2);
		
		initView();
		initEvent();
	}

	private void initView() {
		Intent it_share2 = getIntent();
		localRoom = it_share2.getStringExtra("localRoom");
		localRoomID = it_share2.getStringExtra("localRoomID");
		localTime = it_share2.getStringExtra("localTime");
		
		txtExamRoom = (TextView) findViewById(R.id.txt_share2_examRoom);
		txtExamTime = (TextView) findViewById(R.id.txt_share2_examTime);
		edtMessage = (EditText) findViewById(R.id.edit_share2_message);
		llBack = (LinearLayout) findViewById(R.id.ll_activity_share2);
		btnBack = (Button) findViewById(R.id.button_share2_back);
		btnNext = (Button) findViewById(R.id.button_share2_next);

		llBack.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
	}
	
	private void initEvent() {
		txtExamRoom.setText(localRoom);    //��ʾ������  
		txtExamTime.setText(localTime);    //��ʾ����ʱ��
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_activity_share2:             //  ****  ���" ��Ƶͳ��" ���� Activity-ͳ�ƣ�  �����濴��������ת�仯
			Intent it_statis = new Intent(this,StatisActivity.class);
			startActivity(it_statis);
			overridePendingTransition(0, 0); 
			break;
		case R.id.button_share2_back:            //  ****  ���"����"��ť,  ������һ��"����"����	
			finish();
			overridePendingTransition(0, 0); 
			break;
		case R.id.button_share2_next:           //  ****  ���"��һ��"��ť��   ������һ��"����"����
			String localMessage = edtMessage.getText().toString();
			Intent it_share3 = new Intent(this,ShareActivity3.class);
			it_share3.putExtra("localMessage", localMessage);
			it_share3.putExtra("localTime", localTime);
			it_share3.putExtra("localRoomID", localRoomID);
			Log.e("ALL ", "aLL info = " + localRoomID +" , " + localMessage + " ," + localTime);
			startActivity(it_share3);
			overridePendingTransition(0,0);     
			break;
		}
	}
}