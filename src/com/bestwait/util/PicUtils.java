package com.bestwait.util;

import android.util.Log;

import com.bestwait.news.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public final class PicUtils 
{

	//±£¥ÊÕº∆¨(image to jpg)
	public static void saveImage(byte[] img,String picName)
	{
		String picPath=Constant.path+picName;
		File f=new File(picPath); 
		if(!f.getParentFile().exists())
		{
			f.getParentFile().mkdirs();
		}
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(f);
			fos.write(img);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//¥”¥≈≈ÃªÒµ√Õº∆¨
	public static byte[] getPic(String path) 
	{
		byte[] pic =null;
		try {
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(path));
			ByteArrayOutputStream out = new ByteArrayOutputStream(1024); 
			byte[] temp = new byte[1024]; 
			int size = 0; 
			try {
				while ((size = in.read(temp)) != -1) 
				{ 
					out.write(temp, 0, size); 
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			pic= out.toByteArray();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return pic;
	}



}
