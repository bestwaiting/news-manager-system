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
	//����������ȡbyte����ķ���
	public static byte[]readBytes(DataInputStream din)
	{
		byte data[] = null;
		ByteArrayOutputStream out= new ByteArrayOutputStream(1024); 
		try {
			byte[] temp =null; 
			int size = 0; 
			try {//ѭ����������
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
	//��io�ж�ȡ�ַ���
	public static String readStr(DataInputStream din) throws IOException
	{
		String res=null;;
		byte str[] = null;
		ByteArrayOutputStream out= new ByteArrayOutputStream(1024); 
		try {
			byte[] temp =null;
			int size = 0; 
			try {//ѭ����������
				int temRev =0;
				byte[] ba=new byte[4];//������ݵĳ���
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
	//v2�汾���·���������din.writeUTF();
	public static void writeStr(DataOutputStream dout,String str) throws IOException
	{
		PrintStream PS = new PrintStream(dout, true);
		String resultStr = str;
		PS.println(resultStr);// ������д�뵽SOCKET�У����ؿͻ��ˡ�
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
		

	
	//byte����ת��ΪbufferedImage
	public static BufferedImage bytesToBufImg(byte[]pic) throws IOException
	{
		ByteArrayInputStream bis = new ByteArrayInputStream(pic);//byte-->ͼƬ
		BufferedImage bufImg = ImageIO.read(bis);//����ͼƬ
		bis.close();
		return bufImg;
	}
	//����ͼƬ(image to jpg)
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
	
	
	//�Ӵ��̻��ͼƬ
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
