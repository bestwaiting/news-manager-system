package com.bn.sjb;

import com.bn.util.PicUtils;

public class NewPC2 extends NewPC implements java.io.Serializable {
	private static final long serialVersionUID = -6627190243762427108L;
	public String xwbt; // ���ű���
	public String xwgs; // ���Ÿ���
	public String xwly; // ������Դ
	public String fbsj; // ����ʱ��
	public String xwnr; // ��������
	public String ygid; // Ա��id
	public int ztid; // ����״̬
	public int bsid; // ��ʽid
	public byte[] picTitle;// ����ͼƬ
	public String pic1MS;
	public byte[] pic1;// ��ͼ1

	public NewPC2() {
	}

	// ���в�ͼ
	public NewPC2(String xwbt, String xwgs, String xwly, String fbsj,
			String xwnr, String ygid, int ztid, int bsid, byte[] picTitle,
			byte[] pic1,String pic1MS) {
		this.xwbt = xwbt;
		this.xwgs = xwgs;
		this.xwly = xwly;
		this.fbsj = fbsj;
		this.xwnr = xwnr;
		this.ygid = ygid;
		this.ztid = ztid;
		this.picTitle = picTitle;
		this.pic1 = pic1;
		this.bsid = bsid;
		this.pic1MS=pic1MS;
	}

	// ���в�ͼ
	/*public NewPC2(String xwbt, String xwgs, String xwly, String fbsj,
			String xwnr, String ygid, int ztid, int bsid, String picTitle,
			String pic1,String pic1MS) {
		this.xwbt = xwbt;
		this.xwgs = xwgs;
		this.xwly = xwly;
		this.fbsj = fbsj;
		this.xwnr = xwnr;
		this.ygid = ygid;
		this.ztid = ztid;
		this.picTitle = PicUtils.getBytePic(picTitle);
		this.pic1 = PicUtils.getBytePic(pic1);
		this.bsid = bsid;
		this.pic1MS=pic1MS;
	}*/

}
