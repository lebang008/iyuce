package com.iyuce.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.PartAdapter1;
import com.iyuce.adapter.PartAdapter1.ViewHolder;
import com.iyuce.application.MyApplication;
import com.iyuce.entity.Part;
import com.woyuce.activity.R;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class FragmentPartOne extends Fragment implements OnItemClickListener {

	private ListView listviewPart1;
	private String URL_PART1 = "http://iphone.ipredicting.com/kysubNshare.aspx";
	private List<Part> partList = new ArrayList<Part>();
	private String localsubid;
	private int checkNum = 0;

	public String returnSubid1() { // �����÷�����Activity���ã� ����Subid
		Log.e("SubId1", "subId1 = " + localsubid);
		return localsubid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_part, container, false);
		initView(view);
		getJson();
		return view;
	}

	private void initView(View view) {
		listviewPart1 = (ListView) view.findViewById(R.id.listview_part);
		listviewPart1.setOnItemClickListener(this);
	}

	private void getJson() {
		MyApplication.getHttpQueue().cancelAll("post");
		StringRequest stringRequest = new StringRequest(Method.POST, URL_PART1, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Part part;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for (int i = 0; i < data.length(); i++) {
							jsonObject = data.getJSONObject(i);
							part = new Part();
							part.pid = jsonObject.getString("pid");
							if (Integer.parseInt(part.pid) == 2) { // �Ƚ�intֵ,�����ǱȽ�ջ�ռ�,����continue��������ѭ��
								continue;
							}
							part.subid = jsonObject.getString("subid");
							part.subname = jsonObject.getString("subname");
							partList.add(part);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
					// �ڶ����������ݷŵ���������
					PartAdapter1 adapter = new PartAdapter1(getActivity(), partList);
					listviewPart1.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON�ӿڷ��ص���Ϣ�� " + response);���Ƿ��ص�����JSON��Ϣ��δ���������ҪJSON���ݣ����Դ�������
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "���Ӵ���ԭ�� " + error.getMessage(), error);
			}
		});
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Part localpart = (Part) partList.get(position);

		ViewHolder holder = (ViewHolder) view.getTag();
		
		/* �������һ������,����CheckedΪfalse�����޷��ٽ��и�ѡ ****   ���Գ��ԵĽ���������������toggle()����, �Լ���¼Itemѡ��״̬��hashmap���� */
		if (checkNum > 2) {
//			Toast.makeText(getActivity(), "part 1���ֻ�ܷ���3��Ŷ����", Toast.LENGTH_SHORT).show();
			holder.ckBox.setChecked(false);
		} else {
			holder.ckBox.toggle();                                                // �ı�CheckBox��״̬ �÷����Ǹı乴ѡ״̬�Ļ���,ÿһ��position�ĵ���仯���ɴ�ʵ��
			PartAdapter1.getIsSelected().put(position, holder.ckBox.isChecked()); // ��CheckBox��Item,��,isChecked��¼����
			if (holder.ckBox.isChecked() == true) {                               // ***** ����ѡ����Ŀ
				checkNum++;
				localsubid = localpart.subid;
			} else {
				checkNum--;
				localsubid = null;
			}
			Log.e("part1", "��ѡ��" + checkNum + "��" + ", SubId1 = " + localsubid);
		}
		returnSubid1();
	}
}