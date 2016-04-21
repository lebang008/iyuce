package com.iyuce.activity;

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
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Content;
import com.iyuce.entity.Page;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.woyuce.activity.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import uk.co.senab.photoview.PhotoView;

public class ContentActivity extends BaseActivity implements OnClickListener {

	private ImageView mBack, mPre,mNext;
	private PhotoView mContent;
	private TextView mTitle,mTxtBottom;
	
	private String URL_PAGE = "http://iphone.ipredicting.com/getContentList.aspx";
	private String URL = "http://iphone.ipredicting.com/getpage.aspx";
	private String token,pageid,bookname,bookid,localsectionid;        //上一级传来的参数， 主要用于访问URL，设置题头
	private Integer localpageid,localpageno,pagenomax,pagenomin;       //上一级传来的参数，主要用于在上下页事件中的逻辑
	private String localpagestate,localcontentimg;                     //本类中拿到的内容imgUrl,保存到成员变量，以便上翻、下翻使用
	
	private Content content;
	private List<Page> pageList = new ArrayList<Page>();
	private List<Content> contentList = new ArrayList<Content>();
	
	@Override 
	protected void onStop() {           //离开该Activity时，取消所有的Volley请求队列
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
		MyApplication.getHttpQueue().cancelAll("pre");
		MyApplication.getHttpQueue().cancelAll("next");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_content);

		initView();
		getPageJson();
		getJson();
	}

	private void initView() {
		Intent it_content = getIntent();
		token = it_content.getStringExtra("token");
		bookid = it_content.getStringExtra("bookid");
		localsectionid = it_content.getStringExtra("localsectionid");
		pageid = it_content.getStringExtra("localpageid");
		bookname = it_content.getStringExtra("bookname");                                //设置Title用
		localpageno = Integer.parseInt(it_content.getStringExtra("localpageno")) ;       //设置TxtButtom用
		pagenomax = Integer.parseInt(it_content.getStringExtra("pagenomax"));            //限制上翻到顶
		pagenomin = Integer.parseInt(it_content.getStringExtra("pagenomin"));			 //限制下翻到底
		
		mTxtBottom = (TextView) findViewById(R.id.txt_contentnumber);
		mTitle = (TextView) findViewById(R.id.txt_content_title);
		mBack = (ImageView) findViewById(R.id.back);
		mPre = (ImageView) findViewById(R.id.img_pre);
		mNext = (ImageView) findViewById(R.id.img_next);
		mContent = (PhotoView) findViewById(R.id.img_content);
		
		mBack.setOnClickListener(this);
		mPre.setOnClickListener(this);
		mNext.setOnClickListener(this);
		mTitle.setText(bookname);                        //设置书本名称bookname
		mTxtBottom.setText("第" + localpageno+ "页");     //设置书本页码localpageno          
	}

	private void getPageJson() {
		StringRequest strinRequest = new StringRequest(Method.POST,URL_PAGE, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Page page;
				try {
					jsonObject = new JSONObject(response);
						JSONArray data = jsonObject.getJSONArray("data");
						for(int i=0; i < data.length(); i++){
							jsonObject = data.getJSONObject(i);
							page = new Page();
							page.pageid = jsonObject.getString("pageid");
							page.pageno = jsonObject.getString("pageno");
							page.pagecolor = jsonObject.getString("pagecolor");
							page.pagestate = jsonObject.getString("pagestate");
							pageList.add(page);
						}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null){
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String,String> hashMap = new HashMap<String,String>();
				hashMap.put("token", token);
				hashMap.put("bookid", bookid);
				if(localsectionid != null){
					hashMap.put("sectionid", localsectionid);
				}
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}
	
	private void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST,URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for(int i=0; i < data.length(); i++){
							jsonObject = data.getJSONObject(i);
							content = new Content();
							content.pageid = jsonObject.getString("pageid");
							content.contentimg = jsonObject.getString("contentimg");
							content.timestamp = jsonObject.getString("timestamp");
							contentList.add(content);
							localcontentimg = content.contentimg;         //将图片地址保存在成员变量中
							getImage();      		                      //*****if中拿到图片地址，执行getImage方法 **********************
						}
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
				hashMap.put("token", token);
				hashMap.put("pageid", pageid);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private StringRequest loadimage() {                   //抽取出Volley请求URL的方法，上下页时均需要调用该代码
		StringRequest strinRequest = new StringRequest(Method.POST,URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for(int i=0; i < data.length(); i++){
							jsonObject = data.getJSONObject(i);
							content.pageid = jsonObject.getString("pageid");
							content.contentimg = jsonObject.getString("contentimg");
							content.timestamp = jsonObject.getString("timestamp");
							contentList.add(content);
							localcontentimg = content.contentimg;    //将图片地址保存在成员变量中
							getImage();      			
						}
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
				hashMap.put("token", token);
				hashMap.put("pageid", localpageid.toString());         
				return hashMap;
			}
		};
		return strinRequest;
	}
	
	private void getImage() {                   //		获得，并加载图片内容localcontentimg   (Image_loader框架)
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .showImageOnLoading(R.anim.loading)  
                .showImageOnFail(R.drawable.icon_error)  
                .cacheInMemory(true)  
                .cacheOnDisk(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
        ImageLoader.getInstance().displayImage(localcontentimg, mContent, options);  
	}
	
	@Override
	public void onClick(View v) {                    //主要方法,实现上下翻页，以及页面边界禁止越界
//		Log.e("MAX&MIN", "max = " + pagenomax + ",  min = " + pagenomin);
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.img_pre:
			if(localpageno == 1 || localpageno <= pagenomin){
				toast("已经是第一页啦,亲!");
				break;
			}
			else{
				content =(Content)contentList.get(0);
			    localpageno = localpageno - 1;
				int i;
				for(i=0 ; i < pageList.size(); i++){
					if(Integer.parseInt(pageList.get(i).pageno) == localpageno){
						localpagestate = pageList.get(i).pagestate;
						localpageid = Integer.parseInt(pageList.get(i).pageid);
						break;
					}
				}
				if(localpagestate.toString().equals("0")){
					toast("第" + localpageno +"页已命中删除");
					break;
				}                            
			    mTxtBottom.setText("第" + localpageno + "页");
				StringRequest preRequest = loadimage();
				preRequest.setTag("pre");
				MyApplication.getHttpQueue().add(preRequest);    
				break;
			}
		case R.id.img_next:
			if(localpageno >= pagenomax){
				toast("已经是最后一页啦,亲!");
				break;
			}else{
				content =(Content)contentList.get(0);
			    localpageno = localpageno + 1;
			    int i;
				for(i=0 ; i < pageList.size(); i++){
					if(Integer.parseInt(pageList.get(i).pageno) == localpageno){
						localpagestate = pageList.get(i).pagestate;
						localpageid = Integer.parseInt(pageList.get(i).pageid);
						break;
					}
				}
				if(localpagestate.toString().equals("0")){
					toast("第" + localpageno +"页已命中删除");
					break;
				}                     
			    mTxtBottom.setText("第" + localpageno + "页");
			    StringRequest nextRequest = loadimage();
				nextRequest.setTag("next");
				MyApplication.getHttpQueue().add(nextRequest);    
				break;
			}
		}
	}
}