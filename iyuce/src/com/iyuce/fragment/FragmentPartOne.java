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

	public String returnSubid1() { // 创建该方法给Activity调用， 返回Subid
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
							if (Integer.parseInt(part.pid) == 2) { // 比较int值,而不是比较栈空间,调用continue跳出本轮循环
								continue;
							}
							part.subid = jsonObject.getString("subid");
							part.subname = jsonObject.getString("subname");
							partList.add(part);
						}
					} else {
						Log.e("code!=0 --DATA-BACK", "读取页面失败： " + jsonObject.getString("message"));
					}
					// 第二步，将数据放到适配器中
					PartAdapter1 adapter = new PartAdapter1(getActivity(), partList);
					listviewPart1.setAdapter(adapter);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.e("DATA-BACK", "JSON接口返回的信息： " + response);这是返回的完整JSON信息，未解析，如果要JSON数据，可以从这里拿
			}
		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("Wrong-BACK", "联接错误原因： " + error.getMessage(), error);
			}
		});
		stringRequest.setTag("post");
		MyApplication.getHttpQueue().add(stringRequest);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Part localpart = (Part) partList.get(position);

		ViewHolder holder = (ViewHolder) view.getTag();
		
		/* 这里存在一个问题,设置Checked为false后，又无法再进行复选 ****   可以尝试的解决方法，深入理解toggle()方法, 以及记录Item选中状态的hashmap方法 */
		if (checkNum > 2) {
//			Toast.makeText(getActivity(), "part 1最多只能分享3项哦，亲", Toast.LENGTH_SHORT).show();
			holder.ckBox.setChecked(false);
		} else {
			holder.ckBox.toggle();                                                // 改变CheckBox的状态 该方法是改变勾选状态的基础,每一个position的点击变化都由此实现
			PartAdapter1.getIsSelected().put(position, holder.ckBox.isChecked()); // 将CheckBox的Item,和,isChecked记录下来
			if (holder.ckBox.isChecked() == true) {                               // ***** 调整选定条目
				checkNum++;
				localsubid = localpart.subid;
			} else {
				checkNum--;
				localsubid = null;
			}
			Log.e("part1", "已选中" + checkNum + "项" + ", SubId1 = " + localsubid);
		}
		returnSubid1();
	}
}