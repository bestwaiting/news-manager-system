package com.bestwait.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtil {
	//�������ݿ�����
	public static SQLiteDatabase createOrOpenDatabase(String tableName)
	{
		SQLiteDatabase sld=null;
		try
    	{
			sld=SQLiteDatabase.openDatabase 
	    	( 
	    			"/data/data/com.bestwait.news/newsdb", //���ݿ�����·��
	    			null, 							//�α깤��
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //��д�����������򴴽�
	    	);	
			if(tableName.equals("lm"))
			{
				String sql="create table if not exists " +tableName+
		    			" (lmid integer,lmmc varchar2(50),sxid integer,xwcount integer)";
				sld.execSQL(sql); 
				Log.v("bestwait", "openDatabase lm");
			}
			else if(tableName.equals("tp")) 
			{
				String sql="create table if not exists " +tableName+
		    			" (tpid integer,tpms varchar2(100),tplj varchar2(50),xwid integer,tplx integer)";
				sld.execSQL(sql); 					
			}
			else if(tableName.equals("newdetail"))
			{
				String sql="create table if not exists " +tableName+
			    			" (xwid integer,bsid integer,xwnr varchar2(6000))";
				sld.execSQL(sql);		
			}else if(tableName.startsWith("xwlist"))//xwlist+lmid
			{
				String sql="create table if not exists " +tableName+
			    			" (xwid integer,xwbt varchar2(100),xwgs varchar2(200),sxid integer,xwly varchar2(50),fbsj varchar2(50))";
				sld.execSQL(sql);
			}						
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
		return sld;
	}
	//�ر����ݿ�ķ���
    public static void closeDatabase(SQLiteDatabase sld)
    {
    	try
    	{
	    	sld.close();     		
    	}
		catch(Exception e)
		{
			e.printStackTrace();
		}
    }
    
    
    
    
    //������Ŀ��Ϣ
	private static Object insertLm = new Object();
    public static void insertLm(List<String[]> list)
    {
		synchronized(insertLm)
		{
    	SQLiteDatabase sld=null;
    	String sql;
    	try
    	{
    		delTable("lm");
    		sld=createOrOpenDatabase("lm");//�����ݿ�
    		for(String[] lmxx:list)
    		{
    			sql="insert into lm values("+lmxx[0]+" , '"+lmxx[1]+"' , "+lmxx[2]+","+lmxx[3]+")";
    			sld.execSQL(sql);
    		}
    		  		
    	}  
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
		}
    }
    //�����ݿ��ȡ��Ŀ��Ϣ
    public static List<String[]> getLm()  
    {
    	
    	SQLiteDatabase sld=null;
    	List<String[]> list=new ArrayList<String[]>();
    	try
    	{
    		sld=createOrOpenDatabase("lm");//�����ݿ�
    		String sql="select lmid, lmmc,xwcount from lm order by sxid asc";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			String str[]=new String[3];
    			str[0]=cur.getString(0);//lmID
    			str[1]=cur.getString(1);//lm����
    			str[2]=cur.getString(2);//��Ŀ������������
    			list.add(str);
    		}
    		cur.close();        	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return list;
    }
    
    
    
    
    //�����ݿ��ȡ�����б�����
    public static List<String[]> getNews(String lmid,int startId,int lineSize)  
    {
    	String tableName="xwlist"+lmid;
    	SQLiteDatabase sld=null;
    	List<String[]> list=new ArrayList<String[]>();
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//�����ݿ�
    		String sql="select xwid, xwbt, xwgs, xwly, fbsj from "+tableName+" order by sxid asc"+" limit "+startId+", "+lineSize;
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			String str[]=new String[5];
    			str[0]=cur.getString(0);//����ID
    			str[1]=cur.getString(1);//���ű���
    			str[2]=cur.getString(2);//���Ÿ���
    			str[3]=cur.getString(3);//������Դ
    			str[4]=cur.getString(4).substring(0, 16);//����ʱ��
    			list.add(str);
    		}
    		cur.close();        	
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	} 
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	return list;
    }
    
      
    
    //����Ŀ��Ӧ�������б��в��������б�����
	private static Object updateNews = new Object();
    public static void updateNews(List<String[]> list,String lmid,int startId,int lineSize)
    {
    	synchronized (updateNews) {
    	String tableName="xwlist"+lmid;
    	SQLiteDatabase sld=null;
    	String sql;
    	try
    	{
    		if(startId==0)
    		{
        		delTable(tableName);
    		}
    		sld=createOrOpenDatabase(tableName);//�����ݿ�
    		for(String[] oneNew:list)
    		{
    			sql="insert into "+tableName+" values("+oneNew[0]+" , '"+oneNew[1]+"' , '"+oneNew[2]+"',"+oneNew[3]+
    					",'"+oneNew[4]+"' ,'"+oneNew[5]+"')";
    			sld.execSQL(sql);
    		}
    		  		
    	}  
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	}
    }
    
   
    
	// ���ͼƬ
	private static Object addPic = new Object();
	public static void addPic(String tpms, String xwid, String tplj, int tplx)
	{
		synchronized (addPic) {
    	String tableName="tp";
    	SQLiteDatabase sld=null;
			try {
				sld=createOrOpenDatabase(tableName);//�����ݿ�
				String str = "select max(tpid) from tp";
	    		Cursor cur=sld.rawQuery(str, new String[]{});
	    		cur.moveToNext();
				int max = cur.getInt(0) + 1;
				cur.close(); 
				String sql = null;
				if (tplx == 0) {
					sql = "insert into tp values(" + max + " , null, '" + tplj
							+ "'," + xwid + ",0)";
					System.out.println(sql);
				} else {
					sql = "insert into tp values(" + max + " , '" + tpms
							+ "', '" + tplj + "'," + xwid + "," + tplx + ")";
				}
				sld.execSQL(sql);
			} catch (Exception e) {
				System.out.println(e.toString()+"addPic_________error");
				e.printStackTrace();
			} finally {
				try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
			}
		}
	}
	
	
	// ���ָ����ͼƬ
	public static List<String[]> getPic(String xwid,int tplx) {
    	String tableName="tp";
    	SQLiteDatabase sld=null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			sld=createOrOpenDatabase(tableName);//�����ݿ�
			String task = "select tplj, tpms from tp where xwid=" + xwid+" and tplx =" + tplx;
    		Cursor cur=sld.rawQuery(task, new String[]{});
    		cur.moveToFirst();
			String[] str = new String[2];
			str[0] = cur.getString(0);
			str[1] = cur.getString(1);
			list.add(str);
			cur.close(); 
		} catch (Exception e) {
			System.out.println(e.toString()+"getPic_________error");
			e.printStackTrace();
			return null;
		} finally {
			try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
		}
		return list;
	}
	
    
    //����Ŀ��Ӧ�������б��в��������б�����
	private static Object insertNew = new Object();
    public static void insertNew(List<String[]> list)
    {
    	synchronized (insertNew) {
    	String tableName="newdetail";
    	SQLiteDatabase sld=null;
    	String sql;
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//�����ݿ�
    		for(String[] oneNew:list)
    		{
    			sql="insert into "+tableName+" values("+oneNew[0]+" , "+oneNew[1]+" , '"+oneNew[2]+"')";
    			sld.execSQL(sql);
    		}
    		  		
    	}  
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    	}
    }
    
    
	// ���ָ��������
	public static List<String[]> getNEW(String xwid) {
    	String tableName="newdetail";
    	SQLiteDatabase sld=null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			sld=createOrOpenDatabase(tableName);//�����ݿ�
			String task = "select bsid, xwnr from "+tableName+" where xwid=" + xwid;
    		Cursor cur=sld.rawQuery(task, new String[]{});
    		cur.moveToFirst();
			String[] str = new String[2];
			str[0] = cur.getString(0);
			str[1] = cur.getString(1);
			list.add(str);
			cur.close(); 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
		}
		return list;
	}
    


  
    //������ݿ���ָ���ı�
    public static void delTable(String tableName)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//�����ݿ�
        	String sql="drop table "+tableName;
        	sld.execSQL(sql);    		 		
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	finally
    	{
    		try{closeDatabase(sld);}catch(Exception e){e.printStackTrace();}
    	}
    }
    
}
