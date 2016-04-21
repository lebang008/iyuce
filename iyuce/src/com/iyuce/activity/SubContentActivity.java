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
import com.iyuce.application.MyApplication;
import com.iyuce.entity.SubContent;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoView;

public class SubContentActivity extends Activity implements OnClickListener{
	
	private TextView txtTitle;
	private Button btnVote,btnBack;
	private PhotoView imgSubContent;
	
	private String URL = "http://iphone.ipredicting.com/kysubContent.aspx";
	private String localsubCategoryid,localsubid;        //从上一级不同Activity中柱状图Item被选中后传递过来的不同本地参数
	private String localImg,localsubID,localsubName;     //本类中从Json解析后， 拿到的属性值
	
	private List<SubContent> subContentList = new ArrayList<SubContent>();

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subcontent);
		
		initView();
		getJson();
	}

	private void initView() {
		Intent it_subContent = getIntent();
		localsubCategoryid = it_subContent.getStringExtra("localsubCategoryid");
		localsubid = it_subContent.getStringExtra("localsubid");
		Log.e("ID", "ID = " + localsubCategoryid + "，id = " + localsubid);
		
		imgSubContent = (PhotoView) findViewById(R.id.img_subcontent);
		txtTitle = (TextView) findViewById(R.id.txt_subcontent_title);
		btnVote = (Button) findViewById(R.id.btn_subcontent_vote);
		btnBack = (Button) findViewById(R.id.button_subcontent_back);
		
		btnVote.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}
	
	private void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST,URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					SubContent subContent;
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
							jsonObject = data.getJSONObject(0);
							subContent = new SubContent();
							subContent.subanswer = jsonObject.getString("subanswer");
							subContent.subid = jsonObject.getString("subid");
							subContent.subimg = jsonObject.getString("subimg");
							subContent.subname = jsonObject.getString("subname");
							subContent.timestamp = jsonObject.getString("timestamp");
							subContentList.add(subContent);
							localImg = subContent.subimg;
							localsubID = subContent.subid;          //取出后给下一级 "投票" 使用
							localsubName = subContent.subname;      //取出后给下一级 "投票" 使用
							getImageView();                         //设置图片
							txtTitle.setText(subContent.subname);   //显示Title
 					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
//				Log.e("DATA-BACK", "JSON接口返回的信息： " + response);  //这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "联接错误原因： " + error.getMessage() ,error);
			}
		}){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> hashMap = new HashMap<String,String>();
				if(localsubCategoryid != null){
					hashMap.put("subCategoryid", localsubCategoryid);
				}else{
					hashMap.put("subid", localsubid);
				}
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}
	
	private void getImageView() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .showImageOnLoading(R.anim.loading)  
                .showImageOnFail(R.drawable.icon_error)  
                .cacheInMemory(true)  
                .cacheOnDisk(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
        ImageLoader.getInstance().displayImage(localImg, imgSubContent, options);  
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_subcontent_vote:
			Intent it_vote = new Intent(this,VoteActivity.class);
			it_vote.putExtra("localsubID", localsubID);
			it_vote.putExtra("localsubName", localsubName);
			startActivity(it_vote);
			break;
		case R.id.button_subcontent_back:
			finish();
			break;
		}
	}
}