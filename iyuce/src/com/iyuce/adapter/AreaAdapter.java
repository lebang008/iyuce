package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Area;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AreaAdapter extends BaseAdapter{

	private List<Area> mList;
	private LayoutInflater mInflater;
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
	public AreaAdapter(Context context, List<Area> data) {
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
			convertView = mInflater.inflate(R.layout.listitem_vote_area, null);
			viewHolder.txtArea = (TextView) convertView.findViewById(R.id.txt_item_vote_area);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtArea.setText(mList.get(position).subAreaName);
		return convertView;
	}

	class ViewHolder{
		public TextView txtArea;
	}
}
