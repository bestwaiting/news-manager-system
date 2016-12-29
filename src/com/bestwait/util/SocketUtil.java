package com.bestwait.util;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.bestwait.news.*;
import com.bestwait.pic.PicObject;

public class SocketUtil 
{
	//�����������
	private static SocketIOData getDataConnection()
	{
		SocketIOData sid=null;
		try {
			Socket sc=new Socket(Constant.SERVER_IP,Constant.SERVER_PORT);
			DataInputStream din=new DataInputStream(sc.getInputStream());
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			sid=new SocketIOData(sc,din,dout);
		} 
		catch (Exception e) 
		{
			System.out.println(e.toString());
			e.printStackTrace();
		} 
		return sid;
		
	}
	
	//�������ȡ���ݲ�������Ӧ��Ϣ
	public static String sendAndGetMsg(String msg)
	{
		String res=null;
		SocketIOData sid=getDataConnection();
		try {
			sendStr(sid.dout,msg);
			res=MyConverter.unescape(sid.din.readLine().trim());
		} catch (Exception e) 
		{
			e.printStackTrace();
			return "fail";
		}finally
		{
			try
			{
				sid.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return res;
		
	}
	
	
	 //����ͼƬ���ݰ�
	public static com.bestwait.pic.PicObject sendAndGetPic(String msg)
	{
		com.bestwait.pic.PicObject pico=null;
		SocketIOData sio=getDataConnection();
		try {
			sendStr(sio.dout,msg);
			ObjectInputStream oin = new ObjectInputStream(sio.din); 
			pico = (PicObject) oin.readObject();
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}finally{
			if(sio!=null)
			{
				sio.close();
			}
		}
		return pico;
	}
	
	
	
	
	
	
	
	//ֻ�����ַ���
	private static void  sendStr(DataOutputStream dout,String msg) throws Exception
	{
		byte[] str=msg.getBytes("utf-8");
		byte[] len=int2Byte(str.length);
		dout.write(len);
		dout.write(str);
	}
	
	private static byte[] int2Byte(int intValue) 
	{
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) 
		{
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
		}
		return b;
	}
	
	
	
	
	
	
	//******************************************�ض����ܷ���*************************************
	//����ʽ��Stringת����List
		public static List<String[]> strToList(String msg)
		{
			List<String[]> list =new ArrayList<String[]>();
			String []str=msg.split("<#>");
			for(int i=0;i<str.length;i++)
			{
				if(str[i].length()>0)
					list.add(str[i].split("<->"));
			}
			return list;
		}
		//��ӡList<String[]>
		public static void printList(List<String[]> list)
		{
			for(String ss[]:list){
				for(String s:ss){
					System.out.print(s+" ");
				}
				System.out.println();
			}
		}
	

}
