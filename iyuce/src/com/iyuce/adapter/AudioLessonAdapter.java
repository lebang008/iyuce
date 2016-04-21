package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Audio;
import com.woyuce.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AudioLessonAdapter extends BaseAdapter {

		private List<Audio> mList;
		private LayoutInflater mInflater;

//************ ������������Ҫ���������� 1��contextȥ��ʾ�Լ��Ľ��棬 2��dataȥ��ʾdata�Ľ���*********************************
		public AudioLessonAdapter(Context context, List<Audio> data) {
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
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.listitem_audiolesson, null);
				viewHolder.txt_name = (TextView) convertView.findViewById(R.id.txt_audiolesson_content);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			
			viewHolder.txt_name.setText(mList.get(position).title);
			return convertView;
		}

		class ViewHolder {
			public TextView txt_name;
		}
	}