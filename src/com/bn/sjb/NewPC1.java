package com.bn.sjb;

import com.bn.util.PicUtils;

public class NewPC1 extends NewPC implements java.io.Serializable
{
	private static final long serialVersionUID = -6627190243762427107L;
	public String xwbt;  //���ű���
	public String xwgs;  //���Ÿ���
	public String xwly;  //������Դ
	public String fbsj;  //����ʱ��
	public String xwnr;  //��������
	public String ygid;  //Ա��id	
	public int ztid;  //����״̬
	public int bsid;   //��ʽid
	public byte[] picTitle;//����ͼƬ
	
	
	
	public NewPC1(){}
	
	//���в�ͼ
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
	
	//���в�ͼ
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
