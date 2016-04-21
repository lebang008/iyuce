package com.iyuce.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.application.MyApplication;
import com.woyuce.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterInfoActivity extends BaseActivity implements OnClickListener {

	private EditText edtusername, edtpassword, edtrepassword, edtrealname, edtphonenumber, edtemailnumber, edtTime,
			edtCity, edtProvince;
	private TextView txtback;
	private Button btnfinish;

	private String URL = "http://iphone.ipredicting.com/userRegister.aspx";
	private String URL_CHECKEMAIL = "http://iphone.ipredicting.com/checkksEmail.aspx";
	private String URL_CHECKNAME = "http://iphone.ipredicting.com/checkksUserName.aspx";
	private boolean isOnlyName = false;
	private boolean isOnlyEmail = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registerinfo);

		initView();
	}

	private void initView() {
		edtusername = (EditText) findViewById(R.id.edt_registerinfo_username);
		edtpassword = (EditText) findViewById(R.id.edt_registerinfo_password);
		edtrepassword = (EditText) findViewById(R.id.edt_registerinfo_repassword);
		edtrealname = (EditText) findViewById(R.id.edt_registerinfo_realname);
		edtphonenumber = (EditText) findViewById(R.id.edt_registerinfo_phonenumber);
		edtemailnumber = (EditText) findViewById(R.id.edt_registerinfo_emailnumber);
		edtTime = (EditText) findViewById(R.id.edt_registerinfo_time);
		edtCity = (EditText) findViewById(R.id.edt_registerinfo_city);
		edtProvince = (EditText) findViewById(R.id.edt_registerinfo_province);
		btnfinish = (Button) findViewById(R.id.btn_registerinfo_finish);
		txtback = (TextView) findViewById(R.id.txt_registerinfo_back);

		btnfinish.setOnClickListener(this);
		txtback.setOnClickListener(this);
	}

	private void getJson() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					String parseString = new String(response.getBytes("ISO-8859-1"), "utf-8");
					jsonObject = new JSONObject(parseString);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						toast("恭喜您,注册成功!");
						startActivity(new Intent(RegisterInfoActivity.this, LoginActivity.class));
						finish();
					} else {
						toast("请检查信息是否完整");
					}
				} catch (JSONException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON接口返回的信息： " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-Back", "连接错误原因： " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("ksusername", edtusername.getText().toString().trim());
				hashMap.put("kspwd", edtpassword.getText().toString().trim());
				hashMap.put("ksname", edtrealname.getText().toString().trim());
				hashMap.put("ksphone", edtphonenumber.getText().toString().trim());
				hashMap.put("ksemail", edtemailnumber.getText().toString().trim());
				hashMap.put("ksexamTime", edtTime.getText().toString().trim());
				hashMap.put("ksprovince", edtProvince.getText().toString().trim());
				hashMap.put("kscity", edtCity.getText().toString().trim());
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	private void onlyName() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL_CHECKNAME, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					String parseString = new String(response.getBytes("ISO-8859-1"), "utf-8");
					jsonObject = new JSONObject(parseString);
					int result = jsonObject.getInt("code");
					if (result != 0) {
						isOnlyName = false;
					}
				} catch (JSONException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON接口返回的信息： " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-Back", "连接错误原因： " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("ksname", edtusername.getText().toString().trim());
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	private void onlyEmail() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL_CHECKEMAIL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					String parseString = new String(response.getBytes("ISO-8859-1"), "utf-8");
					jsonObject = new JSONObject(parseString);
					int result = jsonObject.getInt("code");
					if (result != 0) {
						isOnlyEmail = false;
					}
				} catch (JSONException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON接口返回的信息： " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-Back", "连接错误原因： " + error.getMessage(), error);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("ksemail", edtemailnumber.getText().toString().trim());
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_registerinfo_back:
			finish();
			break;

		case R.id.btn_registerinfo_finish:
			onlyName();    //判断用户名唯一
			if (isOnlyName == false) {
				isOnlyName = true;
				toast("很遗憾，用户名已被使用");
				return;
			}
			onlyEmail();   //判断邮箱唯一
			if (isOnlyEmail == false) {
				isOnlyEmail = true;
				toast("很遗憾，邮箱已被使用");
				return;
			}
			//  判断密码一致
			if (edtpassword.getText().toString().equals(edtrepassword.getText().toString())) {
				getJson();      //成功则Toast，并返回Login界面
			} else {
				toast("您设置的密码不一致");
			}
			break;
		}
	}
}