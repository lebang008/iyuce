package com.iyuce.adapter;

import java.util.List;

import com.iyuce.entity.Book;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.woyuce.activity.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class BookAdapter extends BaseAdapter{

	private List<Book> mList;
	private LayoutInflater mInflater;
	
//	适配器本身需要两个参数， 1是context去显示自己的界面， 2是data去显示data的界面
	public BookAdapter(Context context, List<Book> data) {
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
			convertView = mInflater.inflate(R.layout.gvitem_book, null);
			viewHolder.imgbook = (ImageView) convertView.findViewById(R.id.img_item_book);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.imgbook.setImageResource(R.drawable.ic_launcher);
		String url = mList.get(position).bookimg;
		viewHolder.imgbook.setTag(url);
		
//		new BitmapLoader().showImageByThread(viewHolder.ivIcon, url);       // ***方法1
//		mImageLoader.showImageByAsyncTask(viewHolder.imgbook, url);

//		ImageLoader loader = new ImageLoader(MyApplication.getHttpQueue(), new BitmapCache());        // ***方法2
//		ImageListener listener = ImageLoader.getImageListener(viewHolder.imgbook, R.anim.loading, R.drawable.ic_launcher);
//		loader.get(url, listener);
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
                .showImageOnLoading(R.anim.loading)  
                .showImageOnFail(R.drawable.icon_error)  
                .cacheInMemory(true)  
                .cacheOnDisk(true)  
                .bitmapConfig(Bitmap.Config.RGB_565)  
                .build();  
        ImageLoader.getInstance().displayImage(url, viewHolder.imgbook, options);  
		
		return convertView;
	}
	
	class ViewHolder{
		public ImageView imgbook;
	}
}
