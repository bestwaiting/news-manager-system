package com.bestwait.news;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class MySimpleAdapter extends SimpleAdapter {
	private ImageView list_item_img;

	public MySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		list_item_img = (ImageView) view.findViewById(R.id.list_item_img);
		String xwid=((Map<String,?>)this.getItem(position)).get("item_new_id").toString();
		ImgAsyncTask task=new ImgAsyncTask(this.list_item_img);
		task.execute(xwid,"0");	
		return view;
	}

}

