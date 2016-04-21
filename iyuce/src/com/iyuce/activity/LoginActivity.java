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

	private String strPassword, strUserName; // �����б����������´ε�¼ʱ���Զ���¼������
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
			// �����˺���Ϣ��sharepreferences���ݿ���
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
							toast("��½�ɹ�");
							JSONArray datalist = jsonObject.getJSONArray("data");
							jsonObject = datalist.getJSONObject(0);
							// �ó����з�������
							String userId = jsonObject.getString("userId");
							String mUserName = jsonObject.getString("userName");
							String Permission = jsonObject.getString("Permission");
							String money = jsonObject.getString("money");
							String token = jsonObject.getString("token");
							String update = jsonObject.getString("update");
							// ���������ݱ��浽sharepreferences���ݿ���
							PreferenceUtil.save(LoginActivity.this, "userId", userId);
							PreferenceUtil.save(LoginActivity.this, "mUserName", mUserName);
							PreferenceUtil.save(LoginActivity.this, "Permission", Permission);
							PreferenceUtil.save(LoginActivity.this, "money", money);
							PreferenceUtil.save(LoginActivity.this, "token", token);
							PreferenceUtil.save(LoginActivity.this, "update", update);
							// �õ�JSON�е�token ��ӡ����
							Log.e("sys.out", "�������� " + userId + "->" + mUserName + "->" + Permission + "->" + money
									+ "->" + token + "->" + update);
							// ��tokenֵ���ݸ���һ��Activity
							Intent it = new Intent(LoginActivity.this, MainActivity.class);
							it.putExtra("user_token", jsonObject.getString("token"));
							startActivity(it);
							finish();
						} else {
							toast("�˺��������");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					// Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response); 
				}
			}, new ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("Wrong-Back", "���Ӵ���ԭ�� " + error.getMessage(), error);
					toast("�������Ӳ�ͨ");
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