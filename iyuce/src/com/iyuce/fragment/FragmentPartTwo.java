package com.iyuce.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.iyuce.adapter.PartAdapter2;
import com.iyuce.adapter.PartAdapter2.ViewHolder;
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

public class FragmentPartTwo extends Fragment implements OnItemClickListener{
	
	private ListView listviewPart2;
	private String URL_PART2 = "http://iphone.ipredicting.com/kysubNshare.aspx";
	private List<Part> partList = new ArrayList<Part>();
	private String localsubid;

	private int checkNum = 0;
	
	public String returnSubid2(){                    //�����÷�����Activity���ã� ����Subid
		Log.e("SubId2", "subId2 = " + localsubid);
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
		listviewPart2 = (ListView) view.findViewById(R.id.listview_part);
		listviewPart2.setOnItemClickListener(this);
	}

	private void getJson() {
		MyApplication.getHttpQueue().cancelAll("post");
		StringRequest stringRequest = new StringRequest(Method.POST,URL_PART2, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				JSONObject jsonObject;
				Part part;
				try {
					jsonObject = new JSONObject(response);
					int result = jsonObject.getInt("code");
					if (result == 0) {
						JSONArray data = jsonObject.getJSONArray("data");
						for(int i=0; i < data.length(); i++){
							jsonObject = data.getJSONObject(i);
							part = new Part();
							part.pid = jsonObject.getString("pid");
							if(Integer.parseInt(part.pid) == 1){          //�Ƚ�intֵ, �����ǱȽ�ջ�ռ�        ����continue��������ѭ��
								continue;
							}
							part.subid = jsonObject.getString("subid");
							part.subname = jsonObject.getString("subname");
							partList.add(part);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "part2��ȡҳ��ʧ�ܣ� " + jsonObject.getString("message"));
					}
//                    �ڶ����������ݷŵ���������
					PartAdapter2 adapter = new PartAdapter2(getActivity(), partList);
					listviewPart2.setAdapter(adapter);	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "���Ӵ���ԭ�� " + error.getMessage() ,error);
			}
		});
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  Part localpart = (Part)partList.get(position);
		
		  ViewHolder holder = (ViewHolder) view.getTag();                         // ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��ckBoxʵ���Ĳ���
          holder.ckBox.toggle();   						                          // �ı�CheckBox��״̬
          PartAdapter2.getIsSelected().put(position, holder.ckBox.isChecked());   // ��CheckBox��ѡ��״����¼���� 
          
          if (holder.ckBox.isChecked() == true) {     							  //   ***** ����ѡ����Ŀ
        	  if(checkNum == 1){
        		/* �������һ������,����CheckedΪfalse�� ����ˢ���б�󣬲���ѡ�д������״̬�ı䡣 ****   ���Գ��ԵĽ���������ο�ListVie�ж�ҳ���ظ�ѡ������ */
            	  Log.e("part2", "checkNum > 1 ");
//            	  Toast.makeText(getActivity(), "part2 ֻ�ܷ���һ��Ŷ��", Toast.LENGTH_SHORT).show();
            	  holder.ckBox.setChecked(false);
              }else{
            	  checkNum++;
                  localsubid = localpart.subid; 
              }
          } else {
              checkNum--;
              localsubid = null;
          }
          Log.e("Part2", "��ѡ" + checkNum+ "��" + ", SubId2 = " + localsubid);
		  returnSubid2();
	}
}