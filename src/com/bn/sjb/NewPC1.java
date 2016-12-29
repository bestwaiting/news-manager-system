package com.bn.sjb;

import com.bn.util.PicUtils;

public class NewPC1 extends NewPC implements java.io.Serializable
{
	private static final long serialVersionUID = -6627190243762427107L;
	public String xwbt;  //新闻标题
	public String xwgs;  //新闻概述
	public String xwly;  //新闻来源
	public String fbsj;  //发布时间
	public String xwnr;  //新闻内容
	public String ygid;  //员工id	
	public int ztid;  //新闻状态
	public int bsid;   //板式id
	public byte[] picTitle;//标题图片
	
	
	
	public NewPC1(){}
	
	//含有插图
	public NewPC1(String xwbt,String xwgs,String xwly,String fbsj,String xwnr,
			String ygid,int ztid,int bsid,byte[] picTitle)
	{
		this.xwbt=xwbt;
		this.xwgs=xwgs;
		this.xwly=xwly;
		this.fbsj=fbsj;
		this.xwnr=xwnr;
		this.ygid=ygid;
		this.ztid=ztid;
		this.picTitle=picTitle;
		this.bsid=bsid;
	}
	
	//含有插图
		/*public NewPC1(String xwbt,String xwgs,String xwly,String fbsj,String xwnr,
				String ygid,int ztid,int bsid,String picTitle)
		{
			this.xwbt=xwbt;
			this.xwgs=xwgs;
			this.xwly=xwly;
			this.fbsj=fbsj;
			this.xwnr=xwnr;
			this.ygid=ygid;
			this.ztid=ztid;
			this.picTitle=PicUtils.getBytePic(picTitle);
			this.bsid=bsid;
		}*/

	
}
