package com.iyuce.activity;

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

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private TextView txtback;
	private EditText edtRegister;
	private Button btnSend;

	private String URL = "http://iphone.ipredicting.com/checkksEmail.aspx";

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		initView();
	}

	private void initView() {
		txtback = (TextView) findViewById(R.id.txt_register_back);
		edtRegister = (EditText) findViewById(R.id.edit_register);
		btnSend = (Button) findViewById(R.id.btn_register_send);

		txtback.setOnClickListener(this);
		btnSend.setOnClickListener(this);
	}

	private void getJson() {
		StringRequest stringRequest = new StringRequest(Method.POST, URL, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						Intent it = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
						startActivity(it);
						toast("已发送，请前往邮箱查收哦,亲!");
					} else {
						toast("发送失败,请重试");
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON接口返回的信息： " + response);
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-Back", "连接错误原因： " + error.getMessage(), error);
				toast("发送失败,请重试");
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("ksemail", edtRegister.getText().toString().trim());
				return hashMap;
			}
		};
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txt_register_back:
			finish();
			break;
		case R.id.btn_register_send:
			String email = edtRegister.getText().toString().trim();
			if (email.contains("@") && email.contains(".com")) {
				getJson();
			} else {
				toast("您输入的格式不正确");
			}
			break;
		}
	}
}