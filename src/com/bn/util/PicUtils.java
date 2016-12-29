package com.bn.util;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

//ͼƬ������
public class PicUtils 
{

	//��image�����������
	public static BufferedImage scaledImage(Image img)
	{
		Image imgScaled=img.getScaledInstance(400, 320, Image.SCALE_SMOOTH);
		BufferedImage bufImg=toBufferedImage(imgScaled);
		return bufImg;
	}
	

	
	//��image����ת��ΪbufferedImage
	public static BufferedImage toBufferedImage(Image image) {  
        if (image instanceof BufferedImage) {  
            return (BufferedImage) image;  
        }  
        image = new ImageIcon(image).getImage();  
        boolean hasAlpha = false;  
        BufferedImage bimage = null;  
        GraphicsEnvironment ge = GraphicsEnvironment  
                .getLocalGraphicsEnvironment();  
        try {  
            int transparency = Transparency.OPAQUE;  
            if (hasAlpha) {  
                transparency = Transparency.BITMASK;  
            }  
            GraphicsDevice gs = ge.getDefaultScreenDevice();  
            GraphicsConfiguration gc = gs.getDefaultConfiguration();  
            bimage = gc.createCompatibleImage(image.getWidth(null),  
                    image.getHeight(null), transparency);  
        } catch (HeadlessException e) {  
        }  
        if (bimage == null) {  
            int type = BufferedImage.TYPE_INT_RGB;  
            if (hasAlpha) {  
                type = BufferedImage.TYPE_INT_ARGB;  
            }  
            bimage = new BufferedImage(image.getWidth(null),  
                    image.getHeight(null), type);  
        }  
        Graphics g = bimage.createGraphics();  
        g.drawImage(image, 0, 0, null);  
        g.dispose();  
        return bimage;  
    }  

	//*************************************************************************
	  //ͼƬ�ļ�ת��Ϊbyte[]
		public static byte[] getBytePic(String path)
		{
			byte[] pic = null;
			try {
				System.out.println("ͼƬ·��"+path);
				BufferedImage buf=ImageIO.read(new File(path));
				//Image img=Toolkit.getDefaultToolkit().getImage(path);
				BufferedImage bufImg=scaledImage((Image)buf);
				ByteArrayOutputStream bos=new ByteArrayOutputStream();
				ImageIO.write(bufImg, "JPG", bos);
				pic=bos.toByteArray();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			return pic;
		}
		
	//byte[]ת��Ϊimage����
	public static Image bytesToImage(byte[] picByte)
	{
		ImageIcon tempImageIcon = new ImageIcon(picByte);
		Image img = tempImageIcon.getImage();
		return img;
	}
	
	//imageת��Ϊbyte[]
	public static byte[] imageToByte(Image img)
	{
		byte[] picByte = null;
		BufferedImage bufImg=toBufferedImage(img);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
			boolean flag = ImageIO.write(bufImg, "JPG", out);
			picByte = out.toByteArray();
		    out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return picByte;
	}

}
