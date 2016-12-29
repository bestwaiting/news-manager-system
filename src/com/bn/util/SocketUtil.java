package com.bn.util;
/*
 * 联网数据收发工具类
 */

import static com.bn.core.Constant.SERVER_IP;
import static com.bn.core.Constant.SERVER_PORT;
import static com.bn.core.Constant.dataGeted;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.bn.sjb.*;

import com.bn.jm.LoginWindow;


@SuppressWarnings("unchecked")
public class SocketUtil {

	private SocketUtil(){}
	//获得链接对象
	private static SocketIOObject getConnection()
	{
		SocketIOObject sio=null;
		try {
			Socket sc = new Socket(SERVER_IP,SERVER_PORT);
			DataInputStream din=new DataInputStream(sc.getInputStream());
			DataOutputStream dout=new DataOutputStream(sc.getOutputStream());
			sio=new SocketIOObject(sc,din,dout);
		} catch (Exception e) {
			dataGeted=true;//关闭等待对话框
			JOptionPane.showMessageDialog(null, "网络链接错误，请稍后再试！！！","错误",JOptionPane.ERROR_MESSAGE);
			LoginWindow.mf.setEnabled(true);
			e.printStackTrace();
		}
		return sio;
	}
	//收发普通消息 参数msg表示请求字头和参数
	public static String sendAndGetMsg(String msg)
	{
		String res = null;
		SocketIOObject sio=getConnection();
		try {
			res=sendAndGetMsg(sio.din,sio.dout,msg);
			if(res.equals("fail"))
			{
				dataGeted = true;
				JOptionPane.showMessageDialog(LoginWindow.mf,"网络故障，请稍后再试！","错误",JOptionPane.ERROR_MESSAGE);
				LoginWindow.mf.setEnabled(true);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			sio.close();
		}
		return res;
	}
	
	
	
	
	 //接受图片数据包
		public static PicObject sendAndGetPic(String msg)
		{
			PicObject pico=null;
			SocketIOObject sio=getConnection();
			try {
				sendStr(sio.dout,msg);
				ObjectInputStream oin = new ObjectInputStream(sio.din);
				pico = (PicObject)oin.readObject();
			} catch (Exception e) {
				e.printStackTrace();
				dataGeted = true;
				JOptionPane.showMessageDialog(LoginWindow.mf,"网络故障，请稍后再试！","错误",JOptionPane.ERROR_MESSAGE);
				LoginWindow.mf.setEnabled(true);
			}finally{
				sio.close();
			}
			return pico;
		}
	
	//发送新闻对象
	public static String sendNewObject(NewPC newpc,boolean add,String xwid)
	{		
		String res=null;
		Socket sc=null;
		DataInputStream din=null;
		DataOutputStream dout=null;
		try
		{
			sc = new Socket(SERVER_IP,SERVER_PORT);
			din=new DataInputStream(sc.getInputStream());			
			dout=new DataOutputStream(sc.getOutputStream());
			if(newpc instanceof NewPC1)
			{
				NewPC1 newpc1=(NewPC1)newpc;
				StringBuilder sb=null;
				if(add)
				{
				   sb=new StringBuilder("<#ADD_NEW#>");	
				}else
				{
				   sb=new StringBuilder("<#UPDATE_NEW#>");
					sb.append(xwid);
					sb.append("<->");
				}
				sb.append(newpc1.xwbt);
				sb.append("<->");
				sb.append(newpc1.xwgs);
				sb.append("<->");
				sb.append(newpc1.xwly);
				sb.append("<->");
				sb.append(newpc1.fbsj);
				sb.append("<->");
				sb.append(newpc1.xwnr);
				sb.append("<->");
				sb.append(newpc1.ygid);
				sb.append("<->");
				sb.append(newpc1.ztid);
				sb.append("<->");
				sb.append(newpc1.bsid);
				if(add)
				{
					sb.append("<#ADD_NEW#>");
				}else
				{
					sb.append("<#UPDATE_NEW#>");
				}
				sendStr(dout,sb.toString());
				dout.flush();
				sendPic(dout,newpc1.picTitle);
			}else if(newpc instanceof NewPC2)
			{
				NewPC2 newpc2=(NewPC2)newpc;
				StringBuilder sb=null;
				if(add)
				{
				   sb=new StringBuilder("<#ADD_NEW#>");	
				}else
				{
				   sb=new StringBuilder("<#UPDATE_NEW#>");	
					sb.append(xwid);
					sb.append("<->");
				}
				sb.append(newpc2.xwbt);
				sb.append("<->");
				sb.append(newpc2.xwgs);
				sb.append("<->");
				sb.append(newpc2.xwly);
				sb.append("<->");
				sb.append(newpc2.fbsj);
				sb.append("<->");
				sb.append(newpc2.xwnr);
				sb.append("<->");
				sb.append(newpc2.ygid);
				sb.append("<->");
				sb.append(newpc2.ztid);
				sb.append("<->");
				sb.append(newpc2.bsid);
				sb.append("<->");
				sb.append(newpc2.pic1MS);
				if(add)
				{
					sb.append("<#ADD_NEW#>");
				}else
				{
					sb.append("<#UPDATE_NEW#>");
				}
				sendStr(dout,sb.toString());
				dout.flush();
				sendPic(dout,newpc2.picTitle);
				sendPic(dout,newpc2.pic1);	
			}else if(newpc instanceof NewPC3)
			{
				NewPC3 newpc3=(NewPC3)newpc;
				StringBuilder sb=null;
				if(add)
				{
				   sb=new StringBuilder("<#ADD_NEW#>");	
				}else
				{
				   sb=new StringBuilder("<#UPDATE_NEW#>");
					sb.append(xwid);
					sb.append("<->");
				}
				sb.append(newpc3.xwbt);
				sb.append("<->");
				sb.append(newpc3.xwgs);
				sb.append("<->");
				sb.append(newpc3.xwly);
				sb.append("<->");
				sb.append(newpc3.fbsj);
				sb.append("<->");
				sb.append(newpc3.xwnr);
				sb.append("<->");
				sb.append(newpc3.ygid);
				sb.append("<->");
				sb.append(newpc3.ztid);
				sb.append("<->");
				sb.append(newpc3.bsid);
				sb.append("<->");
				sb.append(newpc3.pic1MS);
				sb.append("<->");
				sb.append(newpc3.pic2MS);
				if(add)
				{
					sb.append("<#ADD_NEW#>");
				}else
				{
					sb.append("<#UPDATE_NEW#>");
				}
				sendStr(dout,sb.toString());
				dout.flush();
				sendPic(dout,newpc3.picTitle);
				sendPic(dout,newpc3.pic1);	
				sendPic(dout,newpc3.pic2);	
			}
			res=MyConverter.unescape(din.readLine().trim());
			System.out.println("msg:"+res);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dataGeted = true;
			JOptionPane.showMessageDialog(LoginWindow.mf,"网络故障，请稍后再试！","错误",JOptionPane.ERROR_MESSAGE);
			LoginWindow.mf.setEnabled(true);
		}
		finally
		{
			try{sc.close();}catch (IOException e){e.printStackTrace();}
			try{din.close();}catch (IOException e){e.printStackTrace();}
			try{dout.close();}catch (IOException e){e.printStackTrace();}
		}
		return res;
	}
	 
	
	
	
	
	
	//发送图片
	private static void sendPic(DataOutputStream dout,byte[] pic) throws IOException
	{
		dout.write(int2Byte(pic.length));
		dout.flush();
		dout.write(pic);
		dout.flush();
	}
	
	@SuppressWarnings("deprecation")
	private static String sendAndGetMsg(DataInputStream din,DataOutputStream dout,String str) throws IOException
	{
		String res=null;
		byte strData[]=str.getBytes("utf-8");
		dout.write(int2Byte(strData.length));
		dout.write(strData);
		res=MyConverter.unescape(din.readLine());
		return res;
	}
	
	private static void sendStr(DataOutputStream dout,String str) throws IOException
	{
		byte strData[]=str.getBytes("utf-8");
		dout.write(int2Byte(strData.length));
		dout.write(strData);
	}
	
	private static byte[] int2Byte(int intValue) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
		}
		return b;
	}
//	private static int byte2Int(byte[] b) {
//		int intValue = 0;
//		for (int i = 0; i < b.length; i++) {
//			intValue += (b[i] & 0xFF) << (8 * (3 - i));
//		}
//		return intValue;
//	}
	
	
	//******************************************以下为特定功能方法*************************************
	
	//按格式将String转换成List
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
	//打印List<String[]>
	public static void printList(List<String[]> list)
	{
		for(String ss[]:list){
			for(String s:ss){
				System.out.print(s+" ");
			}
			System.out.println();
		}
	}
	
	//******************************************图片处理的方法*************************************
	//从磁盘获得图片
	public static byte[]getPic(String path) 
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

