package com.bn.util;
/*
 * Socket¡¥Ω”∂‘œÛ
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SocketIOObject {
	Socket sc;
	DataInputStream din;
	DataOutputStream dout;
	
	public SocketIOObject(Socket sc,DataInputStream din,DataOutputStream dout)
	{
		this.sc=sc;
		this.din=din;
		this.dout=dout;
	}
	public void close()
	{
		try{sc.close();}catch(Exception e){e.printStackTrace();}
		try{din.close();}catch(Exception e){e.printStackTrace();}
		try{dout.close();}catch(Exception e){e.printStackTrace();}
	}
	
}
