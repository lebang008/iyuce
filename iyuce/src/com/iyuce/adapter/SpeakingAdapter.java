package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Speaking;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpeakingAdapter extends BaseAdapter{

	private List<Speaking> mList;
	private LayoutInflater mInflater;
	
//	������������Ҫ���������� 1��contextȥ��ʾ�Լ��Ľ��棬 2��dataȥ��ʾdata�Ľ���
	public SpeakingAdapter(Context context, List<Speaking> data) {
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
			convertView = mInflater.inflate(R.layout.listitem_activity_speaking, null);
			viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.img_speaking_icon);
			viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_speaking_username);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_speaking_content);
			viewHolder.txtRoom = (TextView) convertView.findViewById(R.id.txt_speaking_examroom);
			viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_speaking_examtime);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imgIcon.setImageResource(R.drawable.img_duck);
		viewHolder.txtName.setText(mList.get(position).uname);
		viewHolder.txtContent.setText(mList.get(position).message);
		viewHolder.txtRoom.setText(mList.get(position).examroom);
		viewHolder.txtTime.setText(mList.get(position).vtime);
		return convertView;
	}

	class ViewHolder{
		public ImageView imgIcon;
		public TextView txtName, txtContent,txtRoom,txtTime;
	}
}
