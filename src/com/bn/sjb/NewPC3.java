package com.bn.sjb;

import com.bn.util.PicUtils;

public class NewPC3 extends NewPC implements java.io.Serializable
{
	private static final long serialVersionUID = -6627190243762427109L;
	public String xwbt;  //���ű���
	public String xwgs;  //���Ÿ���
	public String xwly;  //������Դ
	public String fbsj;  //����ʱ��
	public String xwnr;  //��������
	public String ygid;  //Ա��id	
	public int ztid;  //����״̬
	public int bsid;   //��ʽid
	public byte[] picTitle;//����ͼƬ
	public byte[] pic1;//��ͼ1
	public String pic1MS;//��ͼ1����
	public byte[] pic2;//��ͼ2
	public String pic2MS;//��ͼ2����
	
	
	
	public NewPC3(){}
	
	
	
	public NewPC3(String xwbt,String xwgs,String xwly,String fbsj,String xwnr,
			String ygid,int ztid,int bsid,byte[] picTitle,byte[] pic1,String pic1MS,
			byte[] pic2,String pic2MS)
	{
		this.xwbt=xwbt;
		this.xwgs=xwgs;
		this.xwly=xwly;
		this.fbsj=fbsj;
		this.xwnr=xwnr;
		this.ygid=ygid;
		this.ztid=ztid;
		this.picTitle=picTitle;
		this.pic1=pic1;
		this.pic1MS=pic1MS;
		this.pic2=pic2;
		this.pic2MS=pic2MS;
		this.bsid=bsid;
	}
	
	//���в�ͼ
	/*public NewPC3(String xwbt,String xwgs,String xwly,String fbsj,String xwnr,
			String ygid,int ztid,int bsid,String picTitle,String pic1,String pic1MS
			,String pic2,String pic2MS)
	{
		this.xwbt=xwbt;
		this.xwgs=xwgs;
		this.xwly=xwly;
		this.fbsj=fbsj;
		this.xwnr=xwnr;
		this.ygid=ygid;
		this.ztid=ztid;
		this.picTitle=PicUtils.getBytePic(picTitle);
		this.pic1=PicUtils.getBytePic(pic1);
		this.pic1MS=pic1MS;
		this.pic2=PicUtils.getBytePic(pic2);
		this.pic2MS=pic2MS;
		this.bsid=bsid;
	}*/

	
}
