package com.bestwait.news;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationControl 
{	
	/*
	 * from为开始的索引
	 * to为要移动到的索引
	 */
	public static void translate(View view,int from,int to)
	{
		Animation tran=new TranslateAnimation(Animation.RELATIVE_TO_SELF,from*1.0f,
				 Animation.RELATIVE_TO_SELF,to*1.0f,
				 Animation.RELATIVE_TO_SELF,0.0f,
				 Animation.RELATIVE_TO_SELF,0.0f);
		tran.setDuration(300);
		tran.setFillAfter(true);
		view.startAnimation(tran);
	}


}
