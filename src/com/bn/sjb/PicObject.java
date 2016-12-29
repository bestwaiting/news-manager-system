package com.bn.sjb;

import java.io.Serializable;

public class PicObject implements Serializable{
	private static final long serialVersionUID = 1024768L;
	
	public byte[] pic;
	public String picMs;
	public int picLx;
	
	public PicObject(byte[] pic,String picMs, int picLx) 
	{
		this.pic=pic;
		this.picMs=picMs;
		this.picLx=picLx;	
	}

}
