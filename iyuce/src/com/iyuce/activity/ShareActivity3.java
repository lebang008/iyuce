package com.iyuce.activity;

import com.iyuce.fragment.FragmentPartOne;
import com.iyuce.fragment.FragmentPartTwo;
import com.woyuce.activity.R;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ShareActivity3 extends BaseActivity implements OnClickListener {

	private Button btnBack,btnNext,btnPartOne,btnPartTwo;
	private LinearLayout llBack;

	private String localRoomID, localTime, localMessage;     //上一级传来的数据,还需要传递到下一级
	private String localSubid;

	private FragmentPartOne fragmentpartone;
	private FragmentPartTwo fragmentparttwo;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share3);

		initView();
		initEvent();
	}

	private void initView() {
		Intent it_share3 = getIntent();
		localMessage = it_share3.getStringExtra("localMessage");
		localTime = it_share3.getStringExtra("localTime");
		localRoomID = it_share3.getStringExtra("localRoomID");

		llBack = (LinearLayout) findViewById(R.id.ll_activity_share3);
		btnBack = (Button) findViewById(R.id.button_share3_back);
		btnNext = (Button) findViewById(R.id.button_share3_next);
		btnPartOne = (Button) findViewById(R.id.btn_share3_part1);
		btnPartTwo = (Button) findViewById(R.id.btn_share3_part2);
		
		llBack.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPartOne.setOnClickListener(this);
		btnPartTwo.setOnClickListener(this);

		fragmentManager = getFragmentManager();
	}

	private void initEvent() {
		fragmentpartone = new FragmentPartOne();
		FragmentTransaction transactionPart = fragmentManager.beginTransaction();
		transactionPart.add(R.id.framelayout_share3, fragmentpartone).commit();  
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_activity_share3: // **** 点击" 高频统计" 启动 Activity-统计，
			Intent it_statis = new Intent(this, StatisActivity.class);
			startActivity(it_statis);
			overridePendingTransition(0, 0);
			break;
		case R.id.button_share3_back: // **** 点击"返回"按钮, 返回上一个"分享"界面
			finish();
			overridePendingTransition(0, 0);
			break;
		case R.id.btn_share3_part1:
			btnPartOne.setBackgroundColor(Color.parseColor("#3399ff"));
			btnPartOne.setTextColor(Color.parseColor("#ffffff"));
			btnPartTwo.setBackgroundResource(com.woyuce.activity.R.drawable.buttonstyle);
			btnPartTwo.setTextColor(Color.parseColor("#3399ff"));
			FragmentTransaction transactionPart1 = fragmentManager.beginTransaction();
			if (fragmentpartone == null) {
				fragmentpartone = new FragmentPartOne();
				transactionPart1.add(R.id.framelayout_share3, fragmentpartone).commit();
				break;
			}
			if (fragmentparttwo != null) {
				transactionPart1.hide(fragmentparttwo);
			}
			transactionPart1.show(fragmentpartone);
			transactionPart1.commit();
			break;
		case R.id.btn_share3_part2:
			btnPartTwo.setBackgroundColor(Color.parseColor("#3399ff"));
			btnPartTwo.setTextColor(Color.parseColor("#ffffff"));
			btnPartOne.setBackgroundResource(com.woyuce.activity.R.drawable.buttonstyle);
			btnPartOne.setTextColor(Color.parseColor("#3399ff"));
			FragmentTransaction transactionPart2 = fragmentManager.beginTransaction();
			if (fragmentparttwo == null) {
				fragmentparttwo = new FragmentPartTwo();
				transactionPart2.add(R.id.framelayout_share3, fragmentparttwo).commit();
				break;
			}
			if (fragmentpartone != null) {
				transactionPart2.hide(fragmentpartone);
			}
			transactionPart2.show(fragmentparttwo);
			transactionPart2.commit();
			break;
		case R.id.button_share3_next: // *** 点击"下一步"按钮,启动下一个"分享"界面
			if(fragmentparttwo == null){
				fragmentparttwo = new FragmentPartTwo();                      //先判断有无part2,如果没有，则初始化一个，如果有，则直接比较
				FragmentTransaction transactionPart = fragmentManager.beginTransaction();
				transactionPart.add(R.id.framelayout_share3, fragmentparttwo).commit();
			}
			String subid1 = fragmentpartone.returnSubid1();                   //initView中已经初始化了part1，无需再初始化,直接调用方法
			String subid2 = fragmentparttwo.returnSubid2();                   //实例化part2,否则会报空指针异常
			Log.e("Part 的 ID", "part 1 拿到的 subid = " + subid1 + ", part 2 拿到的subid = " + subid2);  
			if (subid1 == null && subid2 == null) {        // 报错，空指针
				localSubid = "45";                         // 随意赋一个值，让localSubid ！= null， 然后跳出事件
				toast("请选择要分享的题目,亲");
				break;
			} else if (subid1 == null && subid2 != null) { 
				localSubid = subid2;
			} else if (subid1 != null && subid2 == null) { 
				localSubid = subid1;
			} else if (subid1 != null && subid2 != null) { 
				localSubid = "45";                         // 随意赋一个值，让localSubid ！= null， 然后跳出事件
				toast("建议您只选择一道分享的题目,亲");
				break;
			}
			if (localSubid == null) {                      // 随意赋一个值，让localSubid ！= null， 然后跳出事件
				localSubid = "45";
				toast("请选择要分享的题目,亲");
				break;
			}
			Intent it_share4 = new Intent(this, ShareActivity4.class);
			it_share4.putExtra("localMessage", localMessage);
			it_share4.putExtra("localTime", localTime);
			it_share4.putExtra("localRoomID", localRoomID);
			it_share4.putExtra("localSubid", localSubid); 
			startActivity(it_share4);
			overridePendingTransition(0, 0);
			break;
		}
	}
}