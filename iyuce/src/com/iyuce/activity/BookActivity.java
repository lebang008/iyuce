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
import com.iyuce.adapter.BookAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Book;
import com.woyuce.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BookActivity extends Activity implements OnItemClickListener,OnClickListener{

	private TextView mTitle;
	private ImageView mBack;
	private GridView mGridView;
	
	private String URL = "http://iphone.ipredicting.com/getbooklist.aspx";
	private String token,monthid,booktype;              //从上一级拿到的数据保存，  可再传递到下一级
	private List<Book> bookList = new ArrayList<Book>();
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_book);
		
		initView();
		getJson();
	}
	
	private void initView() {
		Intent it_book = getIntent();
		token = it_book.getStringExtra("token");
		monthid = it_book.getStringExtra("monthid");
		booktype = it_book.getStringExtra("booktype");
		
		mTitle = (TextView) findViewById(R.id.txt_book_typeName);
		mBack = (ImageView) findViewById(R.id.arrow_back);
		mGridView = (GridView) findViewById(R.id.gridview_book);
		mBack.setOnClickListener(this);
		mGridView.setOnItemClickListener(this);
	
		switch (booktype) {          //设置Title
		case "1":
			mTitle.setText("听力");
			break;
		case "4":
			mTitle.setText("阅读");
			break;
		case "5":
			mTitle.setText("写作");
			break;
		case "6":
			mTitle.setText("口语");
			break;
		}
	}

	private void getJson() {
		StringRequest strinRequest = new StringRequest(Method.POST,URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Book book;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for(int i=0; i < data.length(); i++){
							jsonObject = data.getJSONObject(i);
							book = new Book();
							book.bookid = jsonObject.getString("bookid");
							book.bookname = jsonObject.getString("bookname");
							book.bookimg = jsonObject.getString("bookimg");
							book.timestamp = jsonObject.getString("timestamp");
							book.booktype = jsonObject.getString("booktype");
							bookList.add(book);
						}
					} else {
						Log.e("code!=0 Data-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
//                    第二步，将数据放到适配器中
					BookAdapter adapter = new BookAdapter(BookActivity.this, bookList);
					mGridView.setAdapter(adapter);	
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
				hashMap.put("monthid", monthid);
				hashMap.put("booktype", booktype);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Book book = (Book) bookList.get(position);
		String localbookid = book.bookid;
		String localbookname = book.bookname;
		Intent it_page = new Intent(this, PageActivity.class);
		it_page.putExtra("token", token);
		it_page.putExtra("bookid", localbookid);
		it_page.putExtra("bookname", localbookname);
//		Log.e("sys.out", "输出book的参数 ： bookid = " + localbookid + ", bookname = " + localbookname + ", token = " + token);
		startActivity(it_page);
	}

	@Override
	public void onClick(View v) {
		finish();
	}
}