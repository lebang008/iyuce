package com.iyuce.activity;

import java.util.HashMap;
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
import com.iyuce.utils.PreferenceUtil;
import com.woyuce.activity.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button btninto, btnLogin, btnRegister;
	private EditText edtUsername, edtPassword;

	private String strPassword, strUserName; // 本类中变量，用于下次登录时作自动登录的数据
	private String LOGIN_URL = "http://iphone.ipredicting.com/userlogin.aspx";

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		initView();
	}

	private void initView() {
		btninto = (Button) findViewById(R.id.btn_login);
		btnLogin = (Button) findViewById(R.id.btn_loginAtOnce);
		btnRegister = (Button) findViewById(R.id.btn_register);
		edtUsername = (EditText) findViewById(R.id.edt_username);
		edtPassword = (EditText) findViewById(R.id.edt_password);

		btninto.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnRegister.setOnClickListener(this);
		edtUsername.setText(PreferenceUtil.getSharePre(this).getString("username", ""));
		edtPassword.setText(PreferenceUtil.getSharePre(this).getString("password", ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			strUserName = edtUsername.getText().toString();
			strPassword = edtPassword.getText().toString();
			// 保存账号信息到sharepreferences数据库中
			PreferenceUtil.save(LoginActivity.this, "username", LoginActivity.this.strUserName);
			PreferenceUtil.save(LoginActivity.this, "password", LoginActivity.this.strPassword);

			StringRequest stringRequest = new StringRequest(Method.POST, LOGIN_URL, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {
					JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(response);
						int result = jsonObject.getInt("code");
						if (result == 0) {
							toast("登陆成功");
							JSONArray datalist = jsonObject.getJSONArray("data");
							jsonObject = datalist.getJSONObject(0);
							// 拿出所有返回数据
							String userId = jsonObject.getString("userId");
							String mUserName = jsonObject.getString("userName");
							String Permission = jsonObject.getString("Permission");
							String money = jsonObject.getString("money");
							String token = jsonObject.getString("token");
							String update = jsonObject.getString("update");
							// 将所有数据保存到sharepreferences数据库中
							PreferenceUtil.save(LoginActivity.this, "userId", userId);
							PreferenceUtil.save(LoginActivity.this, "mUserName", mUserName);
							PreferenceUtil.save(LoginActivity.this, "Permission", Permission);
							PreferenceUtil.save(LoginActivity.this, "money", money);
							PreferenceUtil.save(LoginActivity.this, "token", token);
							PreferenceUtil.save(LoginActivity.this, "update", update);
							// 拿到JSON中的token 打印出来
							Log.e("sys.out", "所有数据 " + userId + "->" + mUserName + "->" + Permission + "->" + money
									+ "->" + token + "->" + update);
							// 将token值传递给下一个Activity
							Intent it = new Intent(LoginActivity.this, MainActivity.class);
							it.putExtra("user_token", jsonObject.getString("token"));
							startActivity(it);
							finish();
						} else {
							toast("账号密码错误");
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
					toast("网络链接不通");
				}
			}) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("username", strUserName);
					hashMap.put("password", strPassword);
					return hashMap;
				}
			};
			stringRequest.setTag("post");
			MyApplication.getHttpQueue().add(stringRequest);
			break;
		case R.id.btn_loginAtOnce:
			startActivity(new Intent(this, MainActivity.class));
			PreferenceUtil.remove(this);
//			PreferenceUtil.clear(this);
			finish();
			break;
		case R.id.btn_register:
			startActivity(new Intent(this, RegisterActivity.class));
			break;
		}
	}
}