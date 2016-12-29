package com.bestwait.news;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyListView extends ListView implements OnScrollListener {
	
	private static final String TAG = "bestwait.listview";
	//ÿ��ҳ���������
	private int index;
	private int lmid;
	public int current_page=1;//��ҳ��ǰ����ҳ	
	public int xwcount;//������
	public int pageSize;//��ҳ��
	public boolean has_freshed=false;//��listview�Ƿ���¹��ı�־λ,һ����Ϊtrue������Ϊtrue
	
	public MySimpleAdapter myAdapter;

	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;

	// ʵ�ʵ�padding�ľ����������ƫ�ƾ���ı���
	private final static int RATIO = 3;

	private LayoutInflater inflater;
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// ���ڱ�֤startY��ֵ��һ��������touch�¼���ֻ����¼һ��
	private boolean isRecored;

	private int headContentWidth;
	private int headContentHeight;
	private int startY;
	private int firstItemIndex;
	private int state;
	private boolean isBack;
	private OnUpdateListListener refreshListener;
	private boolean isRefreshable;	
	private int visibleLastIndex = 0;   //���Ŀ��������� 	
	private int visibleItemCount;       // ��ǰ���ڿɼ������� 	
	private View loadMoreView;  
	private View loadMoreView_end; 
	//private ProgressBar loadMoreProgressBar;
	//private TextView loadMoreText;

	private Handler handler; 
	
	public MyListView(Context context,int index,int lmid,int xwcount) {
		super(context);
		init(context);
		this.index=index;
		this.lmid=lmid;
		this.xwcount=xwcount;
		this.pageSize=(this.xwcount+Constant.lineSize-1)/Constant.lineSize;
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		//LayoutInflater��������res/layout/�µ�xml�����ļ�������ʵ����
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.head, null);		
		loadMoreView = (LinearLayout)inflater.inflate(R.layout.loadmore, null);
		loadMoreView_end = (LinearLayout)inflater.inflate(R.layout.loadmore_end, null);
        this.addFooterView(loadMoreView);

		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		Log.v("size", "width:" + headContentWidth + " height:" + headContentHeight);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		state = DONE;
		isRefreshable = false;
	}

	
	public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,	int arg3) {
		firstItemIndex = firstVisiableItem;
		this.visibleItemCount = arg2;
		visibleLastIndex = firstItemIndex + visibleItemCount - 1;
	}

	public boolean isEnd=true;//�Ƿ񵽵׵ı�־λ,ֻ�޸�һ��footview
	private boolean isFreshing=false;//�Ƿ��������±�־λ
	/*
	 * ���»����б����ظ�����Ŀ��Ϣ
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 */
	public void onScrollStateChanged(AbsListView arg0, int scrollState) 
	{
		if(!isFreshing)
		{
			//�������ף�����ֹͣ����
			if(visibleLastIndex+1==this.getAdapter().getCount()&&scrollState==OnScrollListener.SCROLL_STATE_IDLE)
			{			
				//��ǰҳ����С����ҳ��
				if(this.current_page<this.pageSize)
				{
					handler.post(new Runnable() {
						@Override
						public void run() {
							MyListView.this.isFreshing=true;
							MyListView.this.current_page++;
							new Thread()
							{
								public void run() 
								{
									onLoadMore(index,lmid,(current_page-1)*Constant.lineSize,Constant.lineSize);
									MyListView.this.isFreshing=false;
								}	
							}.start();
							System.out.println("�˴�currentPage="+current_page+"   ||    pageSize"+pageSize);						
						}
					});
					
				}else//��ǰҳ����������ҳ��
				{
					if(isEnd)
					{
						 this.removeFooterView(loadMoreView);
						 this.addFooterView(loadMoreView_end);
						 isEnd=false;
					}
				}
			}			
		}
	}
	
	private void returnInit()
	{
		this.current_page=1;
		this.isEnd=true;		
		this.removeFooterView(loadMoreView_end);
		this.addFooterView(loadMoreView);
	}
	
	public boolean onTouchEvent(MotionEvent event) {

		if (isRefreshable) {
			switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
					Log.v(TAG, "��downʱ���¼��ǰλ��"+startY);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING && state != LOADING) {
					if (state == DONE) {
						// ʲô������
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
						Log.v(TAG, "������ˢ��״̬����done״̬");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();						
						//�����Զ���ĸ��·���**********************************************************
						onRefresh(index,lmid,0,Constant.lineSize);
						Log.v(TAG, "���ɿ�ˢ��״̬����done״̬");
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!isRecored && firstItemIndex == 0) {
					Log.v(TAG, "��moveʱ���¼��λ��");
					isRecored = true;
					startY = tempY;
				}

				if (state != REFRESHING && isRecored && state != LOADING) {

					// ��֤������padding�Ĺ����У���ǰ��λ��һֱ����head������������б�����Ļ�Ļ����������Ƶ�ʱ���б��ͬʱ���й���

					// ��������ȥˢ����
					if (state == RELEASE_To_REFRESH) {

						setSelection(0);

						// �������ˣ��Ƶ�����Ļ�㹻�ڸ�head�ĳ̶ȣ����ǻ�û���Ƶ�ȫ���ڸǵĵز�
						if (((tempY - startY) / RATIO < headContentHeight)&& (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
							Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽����ˢ��״̬");
						}
						// һ�����Ƶ�����
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							Log.v(TAG, "���ɿ�ˢ��״̬ת�䵽done״̬");
						}
						// �������ˣ����߻�û�����Ƶ���Ļ�����ڸ�head�ĵز�
						else {
							// ���ý����ر�Ĳ�����ֻ�ø���paddingTop��ֵ������
						}
					}
					// ��û�е�����ʾ�ɿ�ˢ�µ�ʱ��,DONE������PULL_To_REFRESH״̬
					if (state == PULL_To_REFRESH) {
						setSelection(0);
						// ���������Խ���RELEASE_TO_REFRESH��״̬
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
							Log.v(TAG, "��done��������ˢ��״̬ת�䵽�ɿ�ˢ��");
						}
						// ���Ƶ�����
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							Log.v(TAG, "��Done��������ˢ��״̬ת�䵽done״̬");
						}
					}
					// done״̬��
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					// ����headView��size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);
					}
					// ����headView��paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	/*
	 * ��״̬�ı�ʱ�򣬵��ø÷������Ը��½���
	 */
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			
			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);
			tipsTextview.setText("�ɿ�ˢ��");
			Log.v(TAG, "��ǰ״̬���ɿ�ˢ��");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
				tipsTextview.setText("����ˢ��");
			} else {
				tipsTextview.setText("����ˢ��");
			}
			Log.v(TAG, "��ǰ״̬������ˢ��");
			break;

		case REFRESHING:

			headView.setPadding(0, 0, 0, 0);

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("����ˢ��...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			Log.v(TAG, "��ǰ״̬,����ˢ��...");
			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			arrowImageView.setImageResource(R.drawable.arrow_down);
			tipsTextview.setText("����ˢ��");
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			Log.v(TAG, "��ǰ״̬��done");
			break;
		}
	}

	public interface OnUpdateListListener {
		public void onRefresh(int index,int lmid,int startId,int lineSize);
		public void onLoadMore(int index,int lmid,int startId,int lineSize);
	}
	
	public void setonRefreshListener(OnUpdateListListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}
	private void onRefresh(int index,int lmid,int startId,int lineSize) {
		if (refreshListener != null) {
			refreshListener.onRefresh(index,lmid,startId,lineSize);
		}
	}
	public void onRefreshComplete() {
		state = DONE;
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("�������:" + date);
		changeHeaderViewByState();
		returnInit();
	}
	private void onLoadMore(int index,int lmid,int startId,int lineSize){
		if(refreshListener != null){
			refreshListener.onLoadMore(index,lmid,startId,lineSize);
		}
	}

	// �˷���ֱ���հ��������ϵ�һ������ˢ�µ�demo���˴��ǡ����ơ�headView��width�Լ�height
	private void measureView(View child) {
		 ViewGroup.LayoutParams p = child.getLayoutParams();
	        if (p == null) {
	            p = new ViewGroup.LayoutParams(
	                    ViewGroup.LayoutParams.FILL_PARENT,
	                    ViewGroup.LayoutParams.WRAP_CONTENT);
	        }
	        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,0 + 0, p.width);
	        int lpHeight = p.height;
	        int childHeightSpec;
	        if (lpHeight > 0) {
	            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
	        } else {
	            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
	        }
	        child.measure(childWidthSpec, childHeightSpec);
	}
	public void setAdapter(MySimpleAdapter adapter,Handler handler) {
		this.handler = handler;
		this.myAdapter=adapter;
		SimpleDateFormat format=new SimpleDateFormat("yyyy��MM��dd��  HH:mm");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("�������:" + date);
		super.setAdapter(adapter);
	}
	
	/*class MyOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			loadMoreButton.setText("���ڼ�����...");   //���ð�ť����
			handler.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					onLoadMore();
					loadMoreButton.setText("�鿴����...");  //�ָ���ť����
				}
			},2000);
		}
		
	}*/
	
}