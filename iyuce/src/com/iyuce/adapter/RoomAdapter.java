package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Room;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoomAdapter extends BaseAdapter{

	private List<Room> mList;
	private LayoutInflater mInflater;
	
//	������������Ҫ���������� 1��contextȥ��ʾ�Լ��Ľ��棬 2��dataȥ��ʾdata�Ľ���
	public RoomAdapter(Context context, List<Room> data) {
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
			viewHolder.txtRoomName = (TextView) convertView.findViewById(R.id.txt_item_vote_area);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.txtRoomName.setText(mList.get(position).roomname);		
		return convertView;
	}
	
	class ViewHolder{
		public TextView txtRoomName;
	}
}
