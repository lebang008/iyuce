package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.WitSubcategory;
import com.woyuce.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WitSubcategoryAdapter extends BaseAdapter{
	
	private List<WitSubcategory> mList;
	private LayoutInflater mInflater;

	public WitSubcategoryAdapter(Context context,List<WitSubcategory> data){
		mList = data;
		mInflater = LayoutInflater.from(context);
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if(convertView == null){
			viewholder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.gvitem_witsubcategory , null);
			viewholder.txtSubcategory = (TextView) convertView.findViewById(R.id.txt_witsubcategory);
			convertView.setTag(viewholder);
		}else{
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.txtSubcategory.setText(mList.get(position).name);
		viewholder.txtSubcategory.setTextColor(Color.parseColor("#"+ mList.get(position).bgColor));    
		return convertView;
	}

	class ViewHolder{
		public TextView txtSubcategory;
	}
}