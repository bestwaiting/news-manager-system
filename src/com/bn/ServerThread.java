package com.bn;
import java.net.ServerSocket;
import java.net.Socket;
import static com.bn.Constant.*;

public class ServerThread extends Thread
{
     public ServerSocket ss;
     @Override
     public void run()
     {
     	try
     	{
     		ss=new ServerSocket(31418);
     		System.out.println("�����������ɹ�, �˿ںţ�31418");
     	}
     	catch(Exception e)
     	{
     		e.printStackTrace();
     	}
 		while(stop)
 		{
 			try
 			{
 				Socket sc=ss.accept();
 				System.out.println("�ͻ������󵽴"+sc.getInetAddress());
 				ServerAgentThread sat=new ServerAgentThread(sc); 	
 				sat.start();	
 			}
 			catch(Exception e)
	     	{
	     		e.printStackTrace();
	     	} 			
 		}
     }	
}


