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
import com.iyuce.adapter.PageAdapter;
import com.iyuce.adapter.SectionAdapter;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Page;
import com.iyuce.entity.Section;
import com.woyuce.activity.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PageActivity extends BaseActivity implements OnClickListener, OnItemClickListener {

	private TextView mTitle;
	private ImageView mBack;
	private GridView mGridViewPage, mGridViewSection;
	private FrameLayout flSection;

	private String URL_SECTION = "http://iphone.ipredicting.com/getReadSection.aspx";
	private String URL_PAGE = "http://iphone.ipredicting.com/getContentList.aspx";
	private String token, bookid, bookname; // ��һ����������ֵ Intent��ȡ����
	private String localsectionid;          // ƪ�´��ݵĲ���
	private List<Page> pageList = new ArrayList<Page>();
	private List<Section> sectionList = new ArrayList<Section>();

	private ProgressDialog pd;
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.getHttpQueue().cancelAll("post");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_page);

		initView();
		getSectionJson();
		getPageJson();
	}

	private void initView() {
		Intent it_page = getIntent();
		token = it_page.getStringExtra("token");
		bookid = it_page.getStringExtra("bookid");
		bookname = it_page.getStringExtra("bookname");

		mTitle = (TextView) findViewById(R.id.txt_page_title);
		mBack = (ImageView) findViewById(R.id.arrow_back);
		flSection = (FrameLayout) findViewById(R.id.fl_section);
		mGridViewPage = (GridView) findViewById(R.id.gridview_page);
		mGridViewSection = (GridView) findViewById(R.id.gridview_section);

		mTitle.setText(bookname);
		mBack.setOnClickListener(this);
		mGridViewPage.setOnItemClickListener(this);
		mGridViewSection.setOnItemClickListener(this);
	}

	private void getSectionJson() {
		StringRequest strinRequest = new StringRequest(Method.POST, URL_SECTION, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject sectionobj;
				Section section;
				try {
					sectionobj = new JSONObject(response);
					int result = sectionobj.getInt("code");
					if (result == 0) {
						JSONArray data = sectionobj.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							sectionobj = data.getJSONObject(i);
							section = new Section();
							section.sectionid = sectionobj.getString("sectionid");
							section.sectionname = sectionobj.getString("sectionname");
							section.sectioncolor = sectionobj.getString("sectioncolor");
							section.sectionstate = sectionobj.getString("sectionstate");
							sectionList.add(section);
							flSection.setVisibility(View.VISIBLE); // ���ò���Frame�ɼ�
						}
					} else {
						Log.e("code!=0 Data-BACK", "Section��ȡҳ��ʧ�ܣ� " + sectionobj.getString("message")); // ʧ�ܴ���
					}
					SectionAdapter adapterSection = new SectionAdapter(PageActivity.this, sectionList);
					mGridViewSection.setAdapter(adapterSection);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response);
			}
		}, volleyErrorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("token", token);
				hashMap.put("bookid", bookid);
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private void getPageJson() {
		pd = new ProgressDialog(this);
		pd.show();
		StringRequest strinRequest = new StringRequest(Method.POST, URL_PAGE, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Page page;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							page = new Page();
							page.pageid = jsonObject.getString("pageid");
							page.pageno = jsonObject.getString("pageno");
							page.pagecolor = jsonObject.getString("pagecolor");
							page.pagestate = jsonObject.getString("pagestate");
							pageList.add(page);
						}
					} else {
						Log.e("code!=0 Data-BACK", "Page��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
					PageAdapter adapter = new PageAdapter(PageActivity.this, pageList);
					mGridViewPage.setAdapter(adapter);
					pd.cancel();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response);
			}
		}, volleyErrorListener()) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> hashMap = new HashMap<String, String>();
				hashMap.put("token", token);
				hashMap.put("bookid", bookid);
				if (localsectionid != null) {
					hashMap.put("sectionid", localsectionid);
				}
				return hashMap;
			}
		};
		strinRequest.setTag("post");
		MyApplication.getHttpQueue().add(strinRequest);
	}

	private ErrorListener volleyErrorListener() { // *���Volley�е�ErrorListener*
		return new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong_BACK", "���Ӵ���ԭ�� " + error.getMessage(), error);
			}
		};
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {
		case R.id.gridview_section:
			Section section = (Section) sectionList.get(position);
			localsectionid = section.sectionid;
			Integer localsectionstate = Integer.parseInt(section.sectionstate);
			if (localsectionstate == 1) {
				pageList.clear();
				getPageJson();
			} else {
				toast("��ƪ�²��ɲ���");
			}
			break;
		case R.id.gridview_page:
			Page page = (Page) pageList.get(position);
			String pagenomax = pageList.get(pageList.size() - 1).pageno; // ��ҳ�����ֵ --> Ϊ��һ�����̵����
			String pagenomin = pageList.get(0).pageno;                   // ��ҳ����Сֵ --> Ϊ��һ�����̵����
			String localpageid = page.pageid;
			String localpageno = page.pageno;
			Integer localpagestate = Integer.parseInt(page.pagestate); // ����page��״̬0 ����1 Ȼ�����ж�,/ "=="�� equals�����⣬���Ը�ֵ��Integer�ж�
			if (localpagestate == 1) {
				Intent it_content = new Intent(this, ContentActivity.class);
				it_content.putExtra("token", token);
				it_content.putExtra("bookid", bookid);
				it_content.putExtra("localsectionid", localsectionid);

				it_content.putExtra("localpageid", localpageid);
				it_content.putExtra("bookname", bookname);       // �����鱾����
				it_content.putExtra("localpageno", localpageno); // ������ҳ��
				it_content.putExtra("pagenomax", pagenomax);     // �������ֵ -->Ϊ��һ�����̵����
				it_content.putExtra("pagenomin", pagenomin);     // ������Сֵ -->Ϊ��һ�����̵����
				startActivity(it_content);
			} else {
				toast("��ҳ״̬Ϊ���ɲ�");
			}
			break;
		}
	}
}