package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Voteno;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VotenoAdapter extends BaseAdapter{

	private List<Voteno> mList;
	private LayoutInflater mInflater;
	
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
	public VotenoAdapter(Context context, List<Voteno> data) {
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
			convertView = mInflater.inflate(R.layout.listitem_statis, null);
			viewHolder.voteView = (View) convertView.findViewById(R.id.view_vote);
			viewHolder.txtExamTitle = (TextView) convertView.findViewById(R.id.txt_item_examTitle);
			viewHolder.txtVoteNumber = (TextView) convertView.findViewById(R.id.txt_item_voteNumber);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.voteView.setX((float) -(250 - Long.parseLong(mList.get(position).votetotal)*1.1));   // 用 "-" 号反向偏移X的值
		viewHolder.txtExamTitle.setText(mList.get(position).categoryName);
		viewHolder.txtVoteNumber.setText(mList.get(position).votetotal);
		return convertView;
	}
	
	class ViewHolder{
		public TextView txtExamTitle,txtVoteNumber;
		public View voteView;
	}
}
