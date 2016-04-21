package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Page;
import com.woyuce.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PageAdapter extends BaseAdapter{

	private List<Page> mList;
	private LayoutInflater mInflater;
	
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
	public PageAdapter(Context context, List<Page> data) {
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
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.gvitem_page, null);
			viewHolder.txtpage = (TextView) convertView.findViewById(R.id.txt_pageno);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtpage.setBackgroundColor( Color.parseColor(mList.get(position).pagecolor));   //   Color.parseColor(localPage.tag)颜色解析的关键方法
		viewHolder.txtpage.setText(mList.get(position).pageno);
		return convertView;
	}
	
	class ViewHolder{
		public TextView txtpage;
	}
}
