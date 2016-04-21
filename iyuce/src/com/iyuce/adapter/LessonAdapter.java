package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Lesson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.woyuce.activity.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LessonAdapter extends BaseAdapter{

	private List<Lesson> mList;
	private LayoutInflater mInflater;
	
	
//	������������Ҫ���������� 1��contextȥ��ʾ�Լ��Ľ��棬 2��dataȥ��ʾdata�Ľ���
	public LessonAdapter(Context context, List<Lesson> data) {
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.gvitem_lesson, null);
			viewHolder.imgPath = (ImageView) convertView.findViewById(R.id.img_item_lesson);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String url = mList.get(position).imgPath;
		viewHolder.imgPath.setTag(url);
		
	    DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .showImageOnLoading(R.anim.loading)  
                .showImageOnFail(R.drawable.icon_error)  
                .cacheInMemory(true)  
                .cacheOnDisk(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
        ImageLoader.getInstance().displayImage(url, viewHolder.imgPath, options);  
        
		return convertView;
	}
	
	class ViewHolder{
		public ImageView imgPath;
	}
}