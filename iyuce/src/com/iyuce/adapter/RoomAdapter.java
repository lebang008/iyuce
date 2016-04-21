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
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
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
