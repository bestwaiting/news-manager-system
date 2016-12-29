package com.bestwait.news;

import com.bestwait.util.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	private final static String TAG="bestwait";
	
	private SharedPreferences share;
	private GridView gv_scroll_text;
	private ImageView iv_scroll_bar;
	private ViewPager vp_content;
	public SimpleAdapter gridView_adpter;
	private DataPagerAdapter dataAdapter;
	// 各个栏目的listview控件
	private List<MyListView> list_listview = new ArrayList<MyListView>();
	// 各个栏目的listview控件对应的包含数据的list
	private List<List<Map<String, String>>> listDatas = new ArrayList<List<Map<String, String>>>();
	private int current_index = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 强制竖屏
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		share=getSharedPreferences("newsdata", Activity.MODE_PRIVATE);
		
		gv_scroll_text=(GridView)findViewById(R.id.gv_scroll_text);
		iv_scroll_bar=(ImageView) findViewById(R.id.iv_scroll_bar);
		vp_content=(ViewPager) findViewById(R.id.vp_content);
		gv_scroll_text.setOnItemClickListener(new gridViewOnItemClick());
	
		dataAdapter=new DataPagerAdapter(list_listview);
		vp_content.setAdapter(dataAdapter);
		vp_content.setCurrentItem(0);
		vp_content.setOnPageChangeListener(new DataOnPageChangeListener());
		//加载栏目和内容
		getTitleAndContent();
	}
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == -1)// -1代表网络不通
			{
				Toast.makeText(MainActivity.this, "网络不通，请稍候再试",Toast.LENGTH_SHORT).show();
				//不能联网直从本地获得数据
				if(gridView_adpter!=null)
				{
					String lmid_first = ((Map<String, String>) gridView_adpter.getItem(0)).get("lmid");
					getNewsByLocal(0, lmid_first,0,Constant.lineSize);
				}
			} else if (msg.what == -2)// -2代表从网络获得栏目数据成功
			{
				// 从数据库访问后更新显示
				getTitleByLocal();
			} else if (msg.what > 0) {
				int lmid = msg.what;
				int index = msg.arg1;
				int startId=msg.arg2;
				// 从数据库访问后更新显示
				getNewsByLocal(index, String.valueOf(lmid),startId,Constant.lineSize);
			}
		}
	};

	class gridViewOnItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3)
		{
			TextView gridviewback = (TextView) arg1;
			for (int i = 0; i < arg0.getCount(); i++) {
				TextView gridview_text_temp = (TextView) arg0.getChildAt(i);
				gridview_text_temp.setTextColor(getResources().getColor(R.color.grid_title_color));
			}
			gridviewback.setTextColor(getResources().getColor(R.color.grid_title_color_selected));
			// 修改记录选择的哪一项
			if (current_index != position) {
				AnimationControl.translate(iv_scroll_bar, current_index, position);
				current_index = position;
			}
			vp_content.setCurrentItem(position);
			@SuppressWarnings("unchecked")
			Map<String, String> map_lm = (Map<String, String>) MainActivity.this.gridView_adpter.getItem(position);
			String lmid = map_lm.get("lmid");
			getNews(position, lmid,0,Constant.lineSize);
		}
	}
	// 保存栏目标头相关信息
	public List<Map<String, String>> listLm = new ArrayList<Map<String, String>>();
	
	/**
	 * ViewPager适配器
	 */
	class DataPagerAdapter extends PagerAdapter {
		public List<MyListView> mListViews;

		public DataPagerAdapter(List<MyListView> list_listview) {
			this.mListViews = list_listview;
		}

		/**
		 * 从指定的position销毁page 参数同上
		 */
		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		/**
		 * 从指定的position创建page 
		 * @param container ViewPager容器
		 * @param position  The page position to be instantiated.
		 * @return 返回指定position的page，这里不需要是一个view，也可以是其他的视图容器.
		 */
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			// 向viewPager当前页面添加控件
			((ViewPager) arg0).addView(mListViews.get(arg1));
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}
		
		@Override
		public int getItemPosition(Object object) 
		{
		    return POSITION_NONE;
		}
	}
	
	/*
	 * 滑动切屏监听器
	 */
	class DataOnPageChangeListener implements OnPageChangeListener {

		// activity从1到2滑动，2被加载后掉用此方法
		public void onPageSelected(int position) {
			// 栏目条，字体变黑，和动画效果
			TextView gridviewback = (TextView)gv_scroll_text.getChildAt(position);
			for (int i = 0; i < MainActivity.this.gv_scroll_text.getCount(); i++) {
				TextView gridview_text_temp = (TextView) MainActivity.this.gv_scroll_text
						.getChildAt(i);
				gridview_text_temp.setTextColor(getResources().getColor(
						R.color.grid_title_color));
			}
			gridviewback.setTextColor(getResources().getColor(
					R.color.grid_title_color_selected));

			// 修改记录选择的哪一项
			if (current_index != position) {
				AnimationControl.translate(iv_scroll_bar, current_index, position);
				current_index = position;
			}
			//重新获取新闻列表	
			Map<String, String> map_lm = (Map<String, String>) MainActivity.this.gridView_adpter.getItem(position);
			String lmid = map_lm.get("lmid");
			getNews(position, lmid,0,Constant.lineSize);
		}

		// 从1到2滑动，在1滑动前调用
		public void onPageScrolled(int arg0, float arg1, int arg2) {}

		// 状态有三个0空闲，1是增在滑行中，2目标加载完毕
		public void onPageScrollStateChanged(int arg0) {}
	}
	
	/*
	 * 点击内容列表，启动详细窗体
	 */
	class listViewOnItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3)
		{
			// TODO Auto-generated method stub
			@SuppressWarnings("unchecked")
			Map<String,Object> mapNew=(Map<String, Object>) arg0.getAdapter().getItem(position); 
			if(mapNew!=null){
				String[] data=new String[4]; 
				data[0]=(String)mapNew.get("item_new_id"); 
				data[1]=(String)mapNew.get("item_new_title");
				data[2]=(String)mapNew.get("item_new_comer"); 
				data[3]=(String)mapNew.get("item_new_time"); 
				Intent intent=new Intent(MainActivity.this,NewActivity.class); 
				intent.putExtra("data",data);
				startActivityForResult(intent, 0);  
			}
		}
		
	}
	
	class DataOnRefreshListener implements MyListView.OnUpdateListListener
	{
		private MyListView listview;
		private List<Map<String, String>> data;
		private MySimpleAdapter adapter;
		
		public DataOnRefreshListener(MyListView listview,List<Map<String, String>> data) 
		{
			this.data=data;
			this.listview=listview;	
			adapter=(MySimpleAdapter) listview.getAdapter();
		}

		@Override
		public void onRefresh(final int index,final int lmid,final int startId,final int lineSize) 
		{			
			new AsyncTask<Void, Void, Void>() {
				protected Void doInBackground(Void... params) {
					getNewsByNet(index, String.valueOf(lmid), startId, lineSize);
					return null;
				}			

				@Override
				protected void onPostExecute(Void result) {
					listview.onRefreshComplete();
				}

			}.execute();
		}

		@Override
		public void onLoadMore(int index,int lmid,int startId,int lineSize) 
		{
			getNewsByNet(index, String.valueOf(lmid), startId, lineSize);
		}		
	}
	
	/*
	 * 1、从本地存储中获取数据
	 * 2、多线程网络获取数据
	 */
	private void getTitleAndContent(){
		getTitleByLocal();
		new Thread(){
			public void run()
			{
				getTitleByNet();
			}
		}.start();
	}
	private void getTitleByLocal(){
		List<String[]> lm = DBUtil.getLm();
		if (lm.size() != 0) {
			this.listLm.clear();
			this.list_listview.clear();
			for (int i = 0; i < lm.size(); i++) 
			{
				HashMap<String, String> hash = new HashMap<String, String>();
				String[] str = lm.get(i);
				for (int j = 0; j < str.length; j++) 
				{
					if (j == 0) {
						hash.put("lmid", str[j]);
					} else if (j == 1) {
						hash.put("grid_title", str[j]);
					} 
				}
				listLm.add(hash);
				List<Map<String, String>> data=new ArrayList<Map<String, String>>();
				listDatas.add(data);
				MyListView mylv=new MyListView(this,i,Integer.parseInt(str[0]),Integer.parseInt(str[2]));
				mylv.setonRefreshListener(new DataOnRefreshListener(mylv,data));
				mylv.setOnItemClickListener(new listViewOnItemClick());
				this.list_listview.add(mylv);
			}
			if (gv_scroll_text.getAdapter() == null)
			{
				gridView_adpter = new SimpleAdapter(MainActivity.this, listLm,
						R.layout.gridview_textview, new String[] { "grid_title" },
						new int[] { R.id.grid_title });
				gv_scroll_text.setNumColumns(gridView_adpter.getCount());
				gv_scroll_text.setLayoutParams(new FrameLayout.LayoutParams(
						150 * gridView_adpter.getCount(), 72));
				gv_scroll_text.setAdapter(gridView_adpter);
			} else
			// 不是第一次打开，更新gridview即可
			{
				// 动态的设置gridview一行的列数
				gv_scroll_text.setNumColumns(gridView_adpter.getCount());
				gv_scroll_text.setLayoutParams(new FrameLayout.LayoutParams(
						150 * gridView_adpter.getCount(), 72));
				gridView_adpter.notifyDataSetChanged();
			}
//			if(isFirst)
//			{
//				isFirst=false;//修改标志位，表示不是第一次执行
//			}else
			{
				String lmid_first = ((Map<String, String>) gridView_adpter.getItem(0)).get("lmid");
				getNews(0, lmid_first,0,Constant.lineSize);
			}
		}

	}
	
	private void getTitleByNet(){
		// 发送消息获得数据
		String msg = Constant.GET_LMA;
		String result = SocketUtil.sendAndGetMsg(msg);
		 
		if (result.equals("fail")) 
		{
			// 网络访问错误时，返回-1
			Message message = handler.obtainMessage();
			message.what = -1;
			handler.sendMessage(message);
			return;
		}
		List<String[]> listLmData = SocketUtil.strToList(result);
		DBUtil.insertLm(listLmData);
		// 联网更新栏目信息，返回-2
		Message message = handler.obtainMessage();
		message.what = -2;
		handler.sendMessage(message);
	}
	private void getNews(final int index, final String lmid,final int startId,final int lineSize) 
	{
		final MyListView myListview=this.list_listview.get(index);
        if(!myListview.has_freshed)
        {
        	getNewsByLocal(index, lmid,startId,lineSize);
			new Thread() {
				public void run() {
					getNewsByNet(index, lmid,startId,lineSize);
					myListview.has_freshed=true;
				};
			}.start();	
        }		
	}
	/*
	 * 从本地数据库获得新闻列表信息，并且更新显示
	 */
	private void getNewsByLocal(int index, String lmid,int startId,int lineSize) 
	{
		List<String[]> newsList = DBUtil.getNews(lmid,startId,lineSize);
		if (newsList.size() != 0) {
			List<Map<String, String>> xwlist = (List<Map<String, String>>) listDatas.get(index);
			if(startId==0)
			{
				xwlist.clear();
			}
			for (int i = 0; i < newsList.size(); i++) {
				HashMap<String, String> hash = new HashMap<String, String>();
				String[] str = newsList.get(i);
				for (int j = 0; j < str.length; j++) {
					if (j == 0) {
						hash.put("item_new_id", str[j]);
					} else if (j == 1) {
						hash.put("item_new_title", str[j]);
					} else if (j == 2) {
						hash.put("item_new_text", str[j]);
					} else if(j==3)
					{
						hash.put("item_new_comer", str[j]);
					}else if(j==4)
					{
						hash.put("item_new_time", str[j]);
					}
				}
				xwlist.add(hash);
			}
			if (list_listview.get(index).getAdapter() == null) {
				list_listview.get(index).setAdapter(new MySimpleAdapter(
						MainActivity.this, this.listDatas.get(index), R.layout.list_item, 
						new String[] {"item_new_title", "item_new_text" },
						new int[] {R.id.list_item_title, R.id.list_item_text }),handler);
				dataAdapter.notifyDataSetChanged();
			} else// 不是第一次打开，更新listview即可
			{
				list_listview.get(index).myAdapter.notifyDataSetChanged();	
			}
			
		}

	}
	
	/*
	 * 从服务器获得新闻列表信息，并且更新显示
	 */
	private void getNewsByNet(int index, String lmid,int startId,int lineSize)
	{
		// 发送消息获得数据
		String msg = Constant.GET_LM_NEWSA;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(lmid);
		sb.append("<->");
		sb.append(startId);
		sb.append("<->");
		sb.append(lineSize);
		sb.append(msg);
		String result = SocketUtil.sendAndGetMsg(sb.toString());
		if (result.equals("fail")) {
			// 网络访问错误时，返回-1
			Message msge = handler.obtainMessage();
			msge.what = -1;
			handler.sendMessage(msge);
			return;
		}
		List<String[]> listNewsData = SocketUtil.strToList(result);
		DBUtil.updateNews(listNewsData, lmid,startId,lineSize);
		// 联网更新信息，返回lmid
		Message msge = handler.obtainMessage();
		msge.what = Integer.parseInt(lmid);
		msge.arg1 = index;
		msge.arg2= startId;
		handler.sendMessage(msge);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
		
	private final static int CLEAR_TIME=2;
	/*
	 * 判断是否需要清除本地数据
	 */
	private boolean needClear()
	{		
		int dayofyear=new GregorianCalendar().get(GregorianCalendar.DAY_OF_YEAR);
		int lastclearday=share.getInt("lastclearday", -1);
		if(lastclearday==-1)
		{
			Editor editor=share.edit();
			editor.putInt("lastclearday", dayofyear);
			editor.commit();
		}else
		{
			if(Math.abs(dayofyear-lastclearday)>=CLEAR_TIME)
			{
				Editor editor=share.edit();
				editor.putInt("lastclearday", dayofyear);
				editor.commit();
				return true;
			}
		}
		return false;
	}
	
	private void clearData()
	{
		DBUtil.delTable("tp");
		DBUtil.delTable("newdetail");
		FileUtiles.deleteDirectory(Constant.PATH); 	
	}
	
	private long mExitTime;
	/*
	 * 按两次回按钮退出程序
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				if(needClear()){
					clearData();
				}
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
