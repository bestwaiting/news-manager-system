package com.bestwait.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBUtil {
	//创建数据库连接
	public static SQLiteDatabase createOrOpenDatabase(String tableName)
	{
		SQLiteDatabase sld=null;
		try
    	{
			sld=SQLiteDatabase.openDatabase 
	    	( 
	    			"/data/data/com.bestwait.news/newsdb", //数据库所在路径
	    			null, 							//游标工厂
	    			SQLiteDatabase.OPEN_READWRITE|SQLiteDatabase.CREATE_IF_NECESSARY //读写、若不存在则创建
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
	//关闭数据库的方法
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
    
    
    
    
    //插入栏目信息
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
    		sld=createOrOpenDatabase("lm");//打开数据库
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
    //从数据库获取栏目信息
    public static List<String[]> getLm()  
    {
    	
    	SQLiteDatabase sld=null;
    	List<String[]> list=new ArrayList<String[]>();
    	try
    	{
    		sld=createOrOpenDatabase("lm");//打开数据库
    		String sql="select lmid, lmmc,xwcount from lm order by sxid asc";
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			String str[]=new String[3];
    			str[0]=cur.getString(0);//lmID
    			str[1]=cur.getString(1);//lm名称
    			str[2]=cur.getString(2);//栏目下新闻总数量
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
    
    
    
    
    //从数据库获取新闻列表数据
    public static List<String[]> getNews(String lmid,int startId,int lineSize)  
    {
    	String tableName="xwlist"+lmid;
    	SQLiteDatabase sld=null;
    	List<String[]> list=new ArrayList<String[]>();
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//打开数据库
    		String sql="select xwid, xwbt, xwgs, xwly, fbsj from "+tableName+" order by sxid asc"+" limit "+startId+", "+lineSize;
    		Cursor cur=sld.rawQuery(sql, new String[]{});
    		while(cur.moveToNext())
    		{
    			String str[]=new String[5];
    			str[0]=cur.getString(0);//新闻ID
    			str[1]=cur.getString(1);//新闻标题
    			str[2]=cur.getString(2);//新闻概述
    			str[3]=cur.getString(3);//新闻来源
    			str[4]=cur.getString(4).substring(0, 16);//发布时间
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
    
      
    
    //向栏目对应的新闻列表中插入新闻列表数据
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
    		sld=createOrOpenDatabase(tableName);//打开数据库
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
    
   
    
	// 添加图片
	private static Object addPic = new Object();
	public static void addPic(String tpms, String xwid, String tplj, int tplx)
	{
		synchronized (addPic) {
    	String tableName="tp";
    	SQLiteDatabase sld=null;
			try {
				sld=createOrOpenDatabase(tableName);//打开数据库
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
	
	
	// 获得指定的图片
	public static List<String[]> getPic(String xwid,int tplx) {
    	String tableName="tp";
    	SQLiteDatabase sld=null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			sld=createOrOpenDatabase(tableName);//打开数据库
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
	
    
    //向栏目对应的新闻列表中插入新闻列表数据
	private static Object insertNew = new Object();
    public static void insertNew(List<String[]> list)
    {
    	synchronized (insertNew) {
    	String tableName="newdetail";
    	SQLiteDatabase sld=null;
    	String sql;
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//打开数据库
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
    
    
	// 获得指定的新闻
	public static List<String[]> getNEW(String xwid) {
    	String tableName="newdetail";
    	SQLiteDatabase sld=null;
		List<String[]> list = new ArrayList<String[]>();
		try {
			sld=createOrOpenDatabase(tableName);//打开数据库
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
    


  
    //清除数据库中指定的表
    public static void delTable(String tableName)
    {
    	SQLiteDatabase sld=null;
    	try
    	{
    		sld=createOrOpenDatabase(tableName);//打开数据库
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
