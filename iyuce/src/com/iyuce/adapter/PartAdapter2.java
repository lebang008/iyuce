package com.iyuce.adapter;

import java.util.HashMap;
import java.util.List;

import com.iyuce.entity.Part;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class PartAdapter2 extends BaseAdapter{

	private List<Part> mList;
	private LayoutInflater mInflater;
	private static HashMap<Integer, Boolean> isSelected;
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
	public PartAdapter2(Context context, List<Part> data) {
		mList = data;
		mInflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();
		initData();
	}
	
	private void initData() {
		for (int i = 0; i < mList.size(); i++) {
			getIsSelected().put(i, false);
		}
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_part, null);
			viewHolder.txtsubname = (TextView) convertView.findViewById(R.id.text_item_part);
			viewHolder.ckBox = (CheckBox) convertView.findViewById(R.id.ckbox_item_part);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtsubname.setText(mList.get(position).subname);
		viewHolder.ckBox.setChecked(getIsSelected().get(position));       //通过该方法HashMap与 调用Adapter的View 联动
		return convertView;
	}
	
	public class ViewHolder{
		public TextView txtsubname;
		public CheckBox ckBox;
	}
	
	public static HashMap<Integer, Boolean> getIsSelected() {
		return isSelected;
	}

	public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
		PartAdapter2.isSelected = isSelected;
	}
}