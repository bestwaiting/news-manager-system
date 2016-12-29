package com.bestwait.util;
/*
 * Socket¡¥Ω”∂‘œÛ
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketIOData {
	Socket sc;
	DataInputStream din;
	DataOutputStream dout;
	
	public SocketIOData(Socket sc,DataInputStream din,DataOutputStream dout)
	{
		this.sc=sc;
		this.din=din;
		this.dout=dout;
	}
	public void close()
	{
		try{din.close();}catch(Exception e){e.printStackTrace();}
		try{dout.close();}catch(Exception e){e.printStackTrace();}
		try{sc.close();}catch(Exception e){e.printStackTrace();}
	}
	
}
