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
	
	public String returnSubid2(){                    //创建该方法给Activity调用， 返回Subid
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
							if(Integer.parseInt(part.pid) == 1){          //比较int值, 而不是比较栈空间        调用continue跳出本轮循环
								continue;
							}
							part.subid = jsonObject.getString("subid");
							part.subname = jsonObject.getString("subname");
							partList.add(part);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "part2读取页面失败： " + jsonObject.getString("message"));
					}
//                    第二步，将数据放到适配器中
					PartAdapter2 adapter = new PartAdapter2(getActivity(), partList);
					listviewPart2.setAdapter(adapter);	
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "联接错误原因： " + error.getMessage() ,error);
			}
		});
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		  Part localpart = (Part)partList.get(position);
		
		  ViewHolder holder = (ViewHolder) view.getTag();                         // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的ckBox实例的步骤
          holder.ckBox.toggle();   						                          // 改变CheckBox的状态
          PartAdapter2.getIsSelected().put(position, holder.ckBox.isChecked());   // 将CheckBox的选中状况记录下来 
          
          if (holder.ckBox.isChecked() == true) {     							  //   ***** 调整选定条目
        	  if(checkNum == 1){
        		/* 这里存在一个问题,设置Checked为false后， 上下刷新列表后，不可选中处会出现状态改变。 ****   可以尝试的解决方法，参考ListVie中多页的重复选择问题 */
            	  Log.e("part2", "checkNum > 1 ");
//            	  Toast.makeText(getActivity(), "part2 只能分享一项哦亲", Toast.LENGTH_SHORT).show();
            	  holder.ckBox.setChecked(false);
              }else{
            	  checkNum++;
                  localsubid = localpart.subid; 
              }
          } else {
              checkNum--;
              localsubid = null;
          }
          Log.e("Part2", "已选" + checkNum+ "项" + ", SubId2 = " + localsubid);
		  returnSubid2();
	}
}