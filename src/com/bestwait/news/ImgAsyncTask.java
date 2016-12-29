package com.bestwait.news;

import com.bestwait.util.*;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class ImgAsyncTask extends AsyncTask<String, Bitmap, Bitmap> 
{
	private ImageView imgView;
	private TextView txtView;
	private boolean isTitlePic;
	private String picms;
	private int piclx;

	public ImgAsyncTask(ImageView img) 
	{
		this.imgView = img;
		this.isTitlePic=true;
	}
	
	public ImgAsyncTask(ImageView img,TextView tv) 
	{
		this.imgView = img;
		this.txtView=tv;
		this.isTitlePic=false;
	}

	@Override
	protected Bitmap doInBackground(String... params) 
	{
		/*
		 * 获得指定图片，并把图片相关数据保存到了成员变量中
		 */
		Log.v("picture", "start pic");
	    Bitmap Pic;
		String xwid=params[0];
		int picLX=Integer.parseInt(params[1]);
		
		List<String[]> list=DBUtil.getPic(xwid, picLX);
		if(list!=null)
		{
			String picName=list.get(0)[0];
			Log.v("sdd", Constant.path+picName);
			picms=list.get(0)[1];
			Pic=BitmapFactory.decodeFile(Constant.path+picName);	
			
			if(Pic!=null)//数据库有图片信息，但是没有图片文件，从网上下载
			{
				return Pic;
			}
		}
			
			
		// 发送消息获得数据
		String msg = Constant.GET_PICA;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(xwid+"<->");
		sb.append(picLX);
		sb.append(msg);
		Log.v("picture", "PicObject1");
		com.bestwait.pic.PicObject pico=SocketUtil.sendAndGetPic(sb.toString());
		Log.v("picture", "PicObject2");
		if(pico!=null)
		{
			Log.v("picture", "PicObject3");
			Pic=BitmapFactory.decodeByteArray(pico.pic, 0, pico.pic.length);
			picms=pico.picMs;
			piclx=pico.picLx;
			publishProgress(Pic);
			//存入数据库
			String picName;
			if(this.isTitlePic)
			{
				picName=xwid+"_title.jpg";
			}else
			{
				picName=xwid+"_pic_"+piclx+".jpg";
			}
			PicUtils.saveImage(pico.pic, picName);
			DBUtil.addPic(picms,xwid,picName,piclx);
		}
		return null;
	}

	@Override
	protected void onPostExecute(Bitmap result) 
	{
		if(result!=null)
		{
			if(!isTitlePic)
			{
				this.txtView.setText(picms);
			}
			imgView.setImageBitmap(result);		
		}

		
	}
	
	@Override
	protected void onProgressUpdate(Bitmap... values) 
	{
		if(values[0]!=null)
		{
			if(!isTitlePic)
			{
				this.txtView.setText(picms);
			}
			imgView.setImageBitmap(values[0]);	
		}
			
	}
	

}

