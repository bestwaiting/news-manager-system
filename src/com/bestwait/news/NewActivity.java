package com.bestwait.news;

import java.util.List;

import com.bestwait.util.DBUtil;
import com.bestwait.util.SocketUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class NewActivity extends Activity implements OnGestureListener
{	
	private final static int FLING_MIN_DISTANCE=120;
	private final static int FLING_MIN_VELOCITY=50;
	TextView news_title=null;
	TextView news_comer=null;
	TextView news_time=null;
	LinearLayout llnew=null;
	Button back=null;
	
	LinearLayout xwbs1;
	LinearLayout xwbs2;
	LinearLayout xwbs3;
	
	Intent intent;
	LayoutInflater inflater;
	
	//将要设置图片的宽度和高度
	int picWidth;
	int picHeight;
	
	
	int xwid;
	
	
	
	private GestureDetector gestureDetector = null;
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) 
		{
			if(msg.what==-1)
			{
				Toast.makeText(NewActivity.this, "网络不通，请稍候再试",
						Toast.LENGTH_SHORT).show();
				llnew.removeAllViews();
				//从本地获得数据
								
			}else if(msg.what>=0)
			{
				int xwid=msg.what;
				//从本地获得数据
				getNewDetailByDB(xwid);
			}
		}
	};
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏
		setContentView(R.layout.new_detail);
		gestureDetector = new GestureDetector(this);
		//获得屏幕宽高
		WindowManager wm = this.getWindowManager();
	    int width = wm.getDefaultDisplay().getWidth();
	    this.picWidth=width-80;
	    this.picHeight=(int) (picWidth*0.8);

		intent=this.getIntent();
		String[] data=intent.getStringArrayExtra("data");
		news_title=(TextView) findViewById(R.id.new_title);
		news_comer=(TextView) findViewById(R.id.new_comer);
		news_time=(TextView) findViewById(R.id.new_time);
		llnew=(LinearLayout) findViewById(R.id.llnew);
		back=(Button) findViewById(R.id.back);
		back.setOnClickListener(new buOnClickListener());
	
		inflater = LayoutInflater.from(this);
		xwbs1 = (LinearLayout) inflater.inflate(R.layout.xwbs1, null);
		xwbs2 = (LinearLayout) inflater.inflate(R.layout.xwbs2, null);
		xwbs3 = (LinearLayout) inflater.inflate(R.layout.xwbs3, null);
		
		xwid=Integer.parseInt(data[0]);
		news_title.setText(data[1]);
		news_comer.setText(data[2]);
		news_time.setText(data[3]);
		
		getNew(xwid);
	}
	
	
	
	
	class buOnClickListener implements OnClickListener
	{
		@Override
		public void onClick(View arg0) 
		{
			intent.removeExtra("data");
			NewActivity.this.finish();			
		}
		
	};
	
	
	private void initBS(int bsid,String xwnr)
	{
		llnew.removeAllViews();
		 switch(bsid)
		 {
		 case 1:
			 TextView tvContent1=(TextView) xwbs1.findViewById(R.id.txtContent);
			 tvContent1.setText(xwnr);
			 llnew.addView(xwbs1);
			 break;
		 case 2:
			 ImageView img1=(ImageView) xwbs2.findViewById(R.id.pic1);
			 img1.setLayoutParams(new LinearLayout.LayoutParams(this.picWidth,this.picHeight));
			 TextView imgms1=(TextView) xwbs2.findViewById(R.id.pic1ms);
			 new ImgAsyncTask(img1, imgms1).execute(String.valueOf(xwid),"1");
			 TextView tvContent2=(TextView) xwbs2.findViewById(R.id.txtContent);
			 tvContent2.setText(xwnr);
			 llnew.addView(xwbs2);
			 break;
		 case 3:
			 ImageView bs3_img1=(ImageView) xwbs3.findViewById(R.id.pic1);
			 bs3_img1.setLayoutParams(new LinearLayout.LayoutParams(this.picWidth,this.picHeight));
			 TextView bs3_ms1=(TextView) xwbs3.findViewById(R.id.pic1ms);
			 new ImgAsyncTask(bs3_img1, bs3_ms1).execute(String.valueOf(xwid),"1");
			 ImageView bs3_img2=(ImageView) xwbs3.findViewById(R.id.pic2);
			 bs3_img2.setLayoutParams(new LinearLayout.LayoutParams(this.picWidth,this.picHeight));
			 TextView bs3_ms2=(TextView) xwbs3.findViewById(R.id.pic2ms);
			 new ImgAsyncTask(bs3_img2, bs3_ms2).execute(String.valueOf(xwid),"2");
			 TextView tvContent3=(TextView) xwbs3.findViewById(R.id.txtContent);
			 tvContent3.setText(xwnr);
			 llnew.addView(xwbs3);
			 break; 
		 }
		
	}
	
	
	private void getNew(final int xwid)
	{
		 List<String[]> list=DBUtil.getNEW(String.valueOf(xwid));
		 if(list!=null)
		 {
			 String[] str=list.get(0);
			 int bsid=Integer.parseInt(str[0]);
			 String xwnr=str[1];
			 initBS(bsid,xwnr);
		 }else
		 {
			 new Thread()
			 {
				 public void run() 
				 {
					 getNewDetailByNet(xwid); 
				 };
			 }.start();
			 
		 }
		
	
	}
	
	
	private void getNewDetailByNet(int xwid)
	{
		// 发送消息获得数据
		String msg = Constant.GET_NEWA;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(xwid);
		sb.append(msg);
		String result = SocketUtil.sendAndGetMsg(sb.toString());
		// System.out.println(result);
		if (result.equals("fail")) {
			// 网络访问错误时，返回-1
			Message msge = handler.obtainMessage();
			msge.what = -1;
			handler.sendMessage(msge);
			return;
		}
		List<String[]> newData = SocketUtil.strToList(result);
		DBUtil.insertNew(newData);

		// 联网更新栏目信息，返回lmid
		Message msge = handler.obtainMessage();
		msge.what = xwid;
		handler.sendMessage(msge);
		
	}
	
	private void getNewDetailByDB(int xwid)
	{
		 List<String[]> list=DBUtil.getNEW(String.valueOf(xwid));
		 if(list!=null)
		 {
			 String[] str=list.get(0);
			 int bsid=Integer.parseInt(str[0]);
			 String xwnr=str[1];
			 initBS(bsid,xwnr);
		 }

	}


	
	
	// 实现OnGestureListener接口中的方法
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}


	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3)
	{
		//System.out.println("滑动的像素为"+(arg0.getX() - arg1.getX()) );
		//对手指滑动的距离进行了计算，如果滑动距离大于120像素，就做切换动作，否则不做任何切换动作。
		// 从左向右滑动
		if (arg0.getX() - arg1.getX() < -FLING_MIN_DISTANCE&&
				Math.abs(arg2)>FLING_MIN_VELOCITY)
		{
			intent.removeExtra("data");
			NewActivity.this.finish();	
		}
		return true;
	}


	@Override
	public void onLongPress(MotionEvent e) {}


	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}


	@Override
	public void onShowPress(MotionEvent e) {}


	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		return this.gestureDetector.onTouchEvent(event);
	}	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		super.dispatchTouchEvent(ev);
		return gestureDetector.onTouchEvent(ev);
	}
}
