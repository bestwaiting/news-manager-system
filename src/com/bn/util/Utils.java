package com.bn.util;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;

public final class Utils 
{
	//从输入流读取byte数组的方法
	public static byte[]readBytes(DataInputStream din)
	{
		byte data[] = null;
		ByteArrayOutputStream out= new ByteArrayOutputStream(1024); 
		try {
			byte[] temp =null; 
			int size = 0; 
			try {//循环接受数据
				int temRev =0;
				byte[] ba=new byte[4];
				din.read(ba);
				int len=byte2Int(ba);
				temp=new byte[len-temRev];
				while ((size = din.read(temp)) != -1)
				{ 	
					temRev+=size;
					out.write(temp, 0, size);
					if(temRev>=len)
					{
						break;
					}
					temp = new byte[len-temRev];
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			data = out.toByteArray();
			return data;
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {out.close();} catch (IOException e) {e.printStackTrace();}
		}
		return data;
	}
	//从io中读取字符串
	public static String readStr(DataInputStream din) throws IOException
	{
		String res=null;;
		byte str[] = null;
		ByteArrayOutputStream out= new ByteArrayOutputStream(1024); 
		try {
			byte[] temp =null;
			int size = 0; 
			try {//循环接受数据
				int temRev =0;
				byte[] ba=new byte[4];//存放数据的长度
				din.read(ba);
				int len=byte2Int(ba);
				temp=new byte[len-temRev];
				while ((size = din.read(temp)) != -1)
				{ 	
					temRev+=size;
					out.write(temp, 0, size);
					if(temRev>=len)
					{
						break;
					}
					temp = new byte[len-temRev]; 
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			str = out.toByteArray();
			res = new String(str, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {out.close();} catch (IOException e) {e.printStackTrace();}
		}
		return res;
	}
	//v2版本的新方法，代替din.writeUTF();
	public static void writeStr(DataOutputStream dout,String str) throws IOException
	{
		PrintStream PS = new PrintStream(dout, true);
		String resultStr = str;
		PS.println(resultStr);// 将数据写入到SOCKET中，返回客户端。
		dout.flush();
	}
	
	@SuppressWarnings("unused")
	private static byte[] int2Byte(int intValue) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (intValue >> 8 * (3 - i) & 0xFF);
		}
		return b;
	}

	private static int byte2Int(byte[] b) {
		int intValue = 0;
		for (int i = 0; i < b.length; i++) {
			intValue += (b[i] & 0xFF) << (8 * (3 - i));
		}
		return intValue;
	}
		

	
	//byte数组转换为bufferedImage
	public static BufferedImage bytesToBufImg(byte[]pic) throws IOException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(pic);//byte-->图片
		BufferedImage bufImg = ImageIO.read(bis);//创建图片
		bis.close();
		return bufImg;
	}
	//保存图片(image to jpg)
	public static void saveImage(BufferedImage img,String path) throws IOException
	{
		File f=new File(path); 
		if(!f.getParentFile().exists())
		{
			f.getParentFile().mkdirs();
		}
	    FileImageOutputStream imgout=new FileImageOutputStream(f);
	    ImageIO.write(img,"JPG",f); 
	    imgout.close();
	}
	
	
	//从磁盘获得图片
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
