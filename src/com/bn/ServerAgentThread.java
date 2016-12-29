package com.bn;
import static com.bn.Constant.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.bestwait.pic.PicObject;
import com.bn.util.DBUtil;
import com.bn.util.MyConverter;
import com.bn.util.Utils;
public class ServerAgentThread extends Thread
{
	private Socket sc;
	private DataInputStream in;
	private DataOutputStream out;
	
	private static final String ok="ok";
	private static final String fail="fail";
	public ServerAgentThread(Socket sc)
	{
		try
		{
			this.sc=sc;
			in=new DataInputStream(sc.getInputStream());
			out=new DataOutputStream(sc.getOutputStream());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try {
			String msg=Utils.readStr(in);
			System.out.println(msg);
			
			
			
			
			//android方法比较少，考虑性能问题，AD在先
			////////////////////////////////////////////////////////////////////
			//		 					For AD 								  //
			////////////////////////////////////////////////////////////////////
			//获得栏目信息
			if(msg.startsWith(GET_LMA))
			{
				List<String[]>list=DBUtil.getLMA();
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					//System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
				
			
			
			//获得栏目信息
			//<#GET_LM_NEWSA#>lmid<->startId<->lineSize<#GET_LM_NEWSA#>
			if(msg.startsWith(GET_LM_NEWSA))
			{
				String[] str1 = msg.split(GET_LM_NEWSA);
				String[] str2 =str1[1].split("<->");
				List<String[]>list=DBUtil.getLMNewsA(str2[0],str2[1],str2[2]);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					//System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			

			
			//获得指定的图片
			if(msg.startsWith(GET_PICA))
			{
				String[] str1 = msg.split(GET_PICA);
				String[] str2 = str1[1].split("<->");
				List<String[]> list=DBUtil.getPic(str2[0],str2[1]);
		        if(list!=null)
				{
					String[] res=list.get(0);
					PicObject pico=new PicObject(Utils.getPic(picPath+res[0]),res[1],Integer.parseInt(res[2]));
					ObjectOutputStream oin = new ObjectOutputStream(out);
					oin.writeObject(pico);
				}else 
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
		        return;
			}
			
			
			
			//获得新闻信息xwid
			if(msg.startsWith(GET_NEWA))
			{
				String[] str1 = msg.split(GET_NEWA);
				List<String[]>list=DBUtil.getNEWA(str1[1]);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					//System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			
	////////////////////////////////////////////////////////////////////
	//		 					For PC								  //
	////////////////////////////////////////////////////////////////////
			/*
			 * 返回值只有3种：
			 * 1.  ok                       update/delete成功
			 * 2.  list转换的字符串                          select查询的结果
			 * 3.  fail                     数据库访问出现异常，数据库返回null
			 */
			//登录
			if(msg.startsWith(LOGIN))
			{
				String[] str1 = msg.split(LOGIN);
				String[] str2 = str1[1].split("<->");
				String res=DBUtil.login(str2[0],str2[1]);
				StringBuilder sb=new StringBuilder();
				if(res==null)
				{
					//数据库异常
					Utils.writeStr(out, MyConverter.escape(fail));
				}else if(res.equals(fail))
				{
					sb.append("<#>");
					sb.append(fail+"<->");
					sb.append("yhff");//用户非法
					sb.append("<#>");
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}else{
					sb.append("<#>");
					sb.append(ok+"<->");//登录成功
					sb.append(res);
					sb.append("<#>");
					Utils.writeStr(out, MyConverter.escape(sb.toString()));
					//<#>ok<->ygid<->jsqxid1,2,3,4,<#>
				}
				return;
			}
			
			//角色基本权限
			if(msg.startsWith(GET_JBQX))
			{
				String[] str1=msg.split(GET_JBQX);
				String jsid=str1[1];
				List<String[]>list=DBUtil.getJBQX(jsid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			//修改角色
			if(msg.startsWith(XG_JS))
			{
				String[] str1 = msg.split(XG_JS);
				String[] str2 = str1[1].split("<->");
				int jsid = Integer.parseInt(str2[0]);//获角色ID 
				String jsmc = str2[1];//角色名称
				String result = DBUtil.xgJSMC(jsid,jsmc);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			//获得角色信息
			if(msg.startsWith(GET_JS))
			{
				List<String[]>list=DBUtil.getJS();
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			//添加角色
			if(msg.startsWith(ADD_JS))
			{
				String res=DBUtil.addJS();
				if(res.equals(ok))
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}else
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
				return;
			}
			
			//角色能否删除
			if(msg.startsWith(SC_JS))
			{
				String[] str1 = msg.split(SC_JS);
				String jsid =str1[1];	
				String res=DBUtil.scJS(jsid);
				if(res.equals(fail))
				{
					Utils.writeStr(out, MyConverter.escape("delete_fail"));
				}else
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			//获得不具有的权限
			if(msg.startsWith(GET_BJYDQX))
			{
				String[] str1 = msg.split(GET_BJYDQX);
				String tem[] =str1[1].split("<->");	
				String jsid=tem[0];
				List<String[]> list=DBUtil.getBJYDQX(jsid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			//添加权限
			if(msg.startsWith(ADD_JSQX_BY_QXMC))
			{//给指定角色  添加 名称为qxmc 的权限ID
				String[] str1 = msg.split(ADD_JSQX_BY_QXMC);
				String[] str2 = str1[1].split("<->");
				int jsid = Integer.parseInt(str2[0]);//获角色ID 
				String qxmc = str2[1];//权限名称
				String result = DBUtil.addJSQXByQXMC(jsid,qxmc);
				if(result.equals(ok))
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}else{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
				return;
			}
			
			//删除权限
			if(msg.startsWith(SC_QX))
			{
				String[] str1 = msg.split(SC_QX);
				String tem[] =str1[1].split("<->");	
				int jsid=Integer.parseInt(tem[0]);
				int qxid=Integer.parseInt(tem[1]);
				String res=DBUtil.scQX(jsid,qxid);
				if(res.equals(ok))
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}else
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
				return;
			}
			
			//获得员工信息
			if(msg.startsWith(GET_YG))
			{
				String[] str0=msg.split(GET_YG);
				int lzyf=Integer.parseInt(str0[1]);
				List<String[]>list=DBUtil.getYG(lzyf);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			//更改员工信息
			if(msg.startsWith(XG_YG))
			{
				String[] str1 = msg.split(XG_YG);
				String[] str2 =str1[1].split("<->");
				//ygid,bmid,jsid,lzyf
				String result = DBUtil.updateYGXX(str2);//更新成功返回"ok" 失败返回"fail"
				if(result==null){
					Utils.writeStr(out, MyConverter.escape(fail));
				}else{
					Utils.writeStr(out, MyConverter.escape(ok));
				}
				return;
			}
			
			//添加员工
			if(msg.startsWith(ADD_YG))
			{
				String[] str1 = msg.split(ADD_YG);
				String[] str2 = str1[1].split("<->");
				//登陆账号   登陆密码   员工姓名  联系方式  性别   部门ID ,角色id
				String result = DBUtil.addYG(str2[0],str2[1],str2[2],str2[3],str2[4],str2[5],str2[6]);
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			//请求获得部门
			if(msg.startsWith(GET_BM))
			{
				StringBuffer sb=new StringBuffer();
				List<String[]>list=DBUtil.getBM();
				if(list==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));
				}else
				{
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						sb.append(str[0]+"<->");
						sb.append(str[1]+"<->");
						sb.append(str[2]+"<->");
						sb.append(str[3]);
						sb.append("<#>");
					}
					Utils.writeStr(out, MyConverter.escape(sb.toString()));	
				}
				return;
			}
			
			//重组部门(要重组的节点的id,要挂到的节点id,即pid)
			if(msg.startsWith(CZ_BM))
			{
				String[] str1 = msg.split(CZ_BM);
				String[] str2 = str1[1].split("<->");
				int id = Integer.parseInt(str2[0]);
				int pid= Integer.parseInt(str2[1]);
				String result = DBUtil.czBM(id,pid);
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			//获得最大部门id
			if(msg.startsWith(GET_MAX_BMID))
			{
				String maxBMID = DBUtil.getMaxBMID();
				if(maxBMID==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out, MyConverter.escape(maxBMID));
				}
				return;
			}
			
			
			//通过部门ID获取此部门以及子部门的员工
			if(msg.startsWith(GET_YG_BY_BMID))
			{
				StringBuffer sb = new StringBuffer();
				String[] strTemp = msg.split(GET_YG_BY_BMID);
				int id = Integer.parseInt(strTemp[1]);
				List<String[]>list=DBUtil.getYGByBMID(id);
				if(list==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}else
				{
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						sb.append(str[0]+"<->");
						sb.append(str[1]);
						sb.append("<#>");
					}
					Utils.writeStr(out, MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			//删除部门
			if(msg.startsWith(DELETE_BM))
			{
				String[] str1 = msg.split(DELETE_BM);
				int id = Integer.parseInt(str1[1]);
				String result = DBUtil.deleteBM(id);
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			
			//添加或者修改部门
			/*
			 * <#ADD_BM#>flag<->fbmid/fbmid<->w部门<->w部门描述<#ADD_BM#>
			 * 第一个参数：flag为0表示添加部门，为1表示更新部门
			 * 第二个参数：当为添加部门时，为fbmid;当为更新部门时，为bmid
			 */
			if(msg.startsWith(ADD_BM))
			{
				String[] str1 = msg.split(ADD_BM);
				String[] str2 = str1[1].split("<->");
				int flag = Integer.parseInt(str2[0]);
				if(flag==0)//添加部门
				{
					int pid = Integer.parseInt(str2[1]);
					String mc = str2[2];
					String ms = str2[3];
					String result = DBUtil.addBM(pid, mc, ms);
					if(result!=null)
					{
						Utils.writeStr(out, MyConverter.escape(ok));
					}else
					if(result==null)
					{
						Utils.writeStr(out, MyConverter.escape(fail));
					}
				}else
				if(flag==1)
				{
					int id = Integer.parseInt(str2[1]);
					String mc = str2[2];
					String ms = str2[3];
					String result = DBUtil.updateBM(id, mc, ms);
					if(result!=null)
					{
						Utils.writeStr(out, MyConverter.escape(ok));	
					}else
					if(result==null)
					{
						Utils.writeStr(out, MyConverter.escape(fail));	
					}
				}
				return;
			}
			
			//个人信息修改
			//ygid ysmm xmm  ygxm ygxb lxfs
			if(msg.startsWith(UPDATE_YG))
			{
				String[] str1 = msg.split(UPDATE_YG);
				String[] str2 = str1[1].split("<->");
				String res=DBUtil.updata_yg(str2[0],str2[1],str2[2],str2[3],str2[4],str2[5]);
				if(res==null)
				{
					//数据库异常
					Utils.writeStr(out, MyConverter.escape(fail));
				}else if(res.equals(fail))
				{
					Utils.writeStr(out,MyConverter.escape("mmcw"));
				}else{
					Utils.writeStr(out, MyConverter.escape("ok"));
				}
				return;
			}
			
			
			
			//获得新闻信息
			if(msg.startsWith(GET_NEW))
			{
				String[] str1 = msg.split(GET_NEW);
				List<String[]>list=DBUtil.getNEW(str1[1]);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//获得指定新闻
			if(msg.startsWith(GET_NEW_By_XWID))
			{
				String[] str1 = msg.split(GET_NEW_By_XWID);
				String[] str2 = str1[1].split("<->");
				int xwid=Integer.parseInt(str2[0]);
				List<String[]>list=DBUtil.getNewById(xwid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					//System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//删除新闻,还有新闻对应的审核记录
			if(msg.startsWith(DEL_NEW))
			{
				String[] str1 = msg.split(DEL_NEW);
				String tem[] =str1[1].split("<->");	
				int xwid=Integer.parseInt(tem[0]);
				String res=DBUtil.delNEW(xwid);
				if(res.equals(ok))
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}else
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
				return;
			}
			


			
			
			//获得审核记录
			if(msg.startsWith(GET_SHJL))
			{
				List<String[]>list=DBUtil.getSHJL();
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//获得过滤之后的审核记录
			if(msg.startsWith(GET_SHJL_FILTER))
			{
				String[] str1 = msg.split(GET_SHJL_FILTER);
				String[] str2 = str1[1].split("<->");
				//状态id ,  开始日期  截止日期,新闻编号
				List<String[]>list=DBUtil.getSHJLFilter(str2[0],str2[1],str2[2]);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//获得过滤之后的审核记录
			if(msg.startsWith(GET_GRXW_FILTER))
			{
				String[] str1 = msg.split(GET_GRXW_FILTER);
				String[] str2 = str1[1].split("<->");
				//状态id ,  开始日期  截止日期
				List<String[]>list=DBUtil.getGRXWFilter(str2[0],str2[1],str2[2],str2[3]);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//获得栏目
			if(msg.startsWith(GET_LM))
			{
				List<String[]>list=DBUtil.getLM();
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//修改栏目
			if(msg.startsWith(XG_LM))
			{
				String[] str1 = msg.split(XG_LM);
				String[] str2 = str1[1].split("<->");
				int lmid = Integer.parseInt(str2[0]);//获角色ID 
				String lmmc = str2[1];//角色名称
				String result = DBUtil.xgLMMC(lmid,lmmc);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//栏目所含新闻
			if(msg.startsWith(GET_LM_NEW))
			{
				String[] str1=msg.split(GET_LM_NEW);
				String lmid=str1[1];
				List<String[]>list=DBUtil.getLMXW(lmid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//发布新闻查看界面，栏目所含新闻
			if(msg.startsWith(GET_LM_FB_NEW))
			{
				String[] str1=msg.split(GET_LM_FB_NEW);
				String lmid=str1[1];
				List<String[]>list=DBUtil.getLMFBXW(lmid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//待发布的新闻
			if(msg.startsWith(GET_DFB_NEW))
			{
				List<String[]>list=DBUtil.getXWDFB();
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			
			//获得指定审核记录
			if(msg.startsWith(GET_SH_By_SHID))
			{
				String[] str1 = msg.split(GET_SH_By_SHID);
				String[] str2 = str1[1].split("<->");
				int shid=Integer.parseInt(str2[0]);
				List<String[]>list=DBUtil.getSHById(shid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
			

			//更新审核记录
			//shid,ztid, shrxm, shsj, shyj, 
			if(msg.startsWith(UPDATE_SHJL))
			{
				String[] str1 = msg.split(UPDATE_SHJL);
				String[] str2 = str1[1].split("<->");
				String result = DBUtil.updateSHJL(str2[0],str2[1],str2[2],str2[3],str2[4]);
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			
			
			//获得指定新闻的审核记录
			if(msg.startsWith(GET_SHJL_BY_XWID))
			{
				String[] str1 = msg.split(GET_SHJL_BY_XWID);
				String[] str2 = str1[1].split("<->");
				int xwid=Integer.parseInt(str2[0]);
				List<String[]>list=DBUtil.getSHByXWId(xwid);
				if(list==null)
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}else
				{
					StringBuilder sb = new StringBuilder();
					sb.append("<#>");
					for(int i=0;i<list.size();i++)
					{
						String[] str=list.get(i);
						int length = str.length;
						for(int j=0;j<length-1;j++)
						{
							sb.append(str[j]+"<->");
						}
						sb.append(str[length-1]);
						sb.append("<#>");
					}
					System.out.println(sb.toString());
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}
				return;
			}
			
					
			//添加新栏目
			if(msg.startsWith(ADD_LM))
			{
				String result = DBUtil.addLM();
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			
			//删除栏目
			if(msg.startsWith(DEL_LM))
			{
				String[] str1 = msg.split(DEL_LM);
				int id = Integer.parseInt(str1[1]);
				String result = DBUtil.deleteLM(id);
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}else if(result.equals("hasNew"))
				{
					Utils.writeStr(out, MyConverter.escape("hasNew"));	
				}else
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}
				return;
			}
			
			
			//调整栏目顺序
			if(msg.startsWith(TRAN_LM))
			{
				String[] str1 = msg.split(TRAN_LM);
				String[] str2 = str1[1].split("<->");
				String result = DBUtil.tranLM(str2[0],str2[1],str2[2],str2[3]);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//调整新闻顺序
			if(msg.startsWith(TRAN_XW))
			{
				String[] str1 = msg.split(TRAN_XW);
				String[] str2 = str1[1].split("<->");
				String result = DBUtil.tranXW(str2[0],str2[1],str2[2],str2[3]);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//修改栏目id
			if(msg.startsWith(XG_LMID))
			{
				String[] str1 = msg.split(XG_LMID);
				String[] str2 = str1[1].split("<->");
				int xwid = Integer.parseInt(str2[0]);//新闻ID 
				int lmid = Integer.parseInt(str2[1]);//新闻ID 
				String result = DBUtil.xgLMID(xwid,lmid);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			
			//修改栏目id
			if(msg.startsWith(XG_FBZTID))
			{
				String[] str1 = msg.split(XG_FBZTID);
				String[] str2 = str1[1].split("<->");
				int xwid = Integer.parseInt(str2[0]);//新闻ID 
				int fbztid = Integer.parseInt(str2[1]);//新闻ID 
				String result = DBUtil.xgFBZTID(xwid,fbztid);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//添加新闻
			//xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid,pic1MS,pic2MS,picTitelPath,pic1Path,pic2Path,
			if(msg.startsWith(ADD_NEW))
			{
				String result=null;
				String xwid=null;
				String[] str1 = msg.split(ADD_NEW);
				String[] str2 = str1[1].split("<->");
				String[] res = DBUtil.addNEW(str2[0],str2[1],str2[2],str2[3],str2[4],str2[5],str2[6],str2[7]);
				if(res!=null)
				{
					result=res[0];
					xwid=res[1];
					int bsid=Integer.parseInt(str2[7]);
					//图片名称格式 xwid_titel_xwid.jpg或xwid_pic_1/2.jpg
					if(bsid==1)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						DBUtil.addPic(null,xwid,name0,0);
					}else if(bsid==2)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						DBUtil.addPic(null,xwid,name0,0);
						//pic1
						byte[] pic1 = Utils.readBytes(in);
						String name1=xwid+"_pic_1.jpg";
						Utils.saveImage(Utils.bytesToBufImg(pic1),picPath+name1);
						DBUtil.addPic(str2[8],xwid,name1,1);
						
					}else if(bsid==3)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						DBUtil.addPic(null,xwid,name0,0);
						//pic1
						byte[] pic1 = Utils.readBytes(in);
						String name1=xwid+"_pic_1.jpg";
						Utils.saveImage(Utils.bytesToBufImg(pic1),picPath+name1);
						DBUtil.addPic(str2[8],xwid,name1,1);
						//pic2
						byte[] pic2 = Utils.readBytes(in);
						String name2=xwid+"_pic_2.jpg";
						Utils.saveImage(Utils.bytesToBufImg(pic2),picPath+name2);
						DBUtil.addPic(str2[9],xwid,name2,2);
					}
				}
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
			
			//获得指定的图片
			if(msg.startsWith(GET_PIC))
			{
				String[] str1 = msg.split(GET_PIC);
				String[] str2 = str1[1].split("<->");
				List<String[]> list=DBUtil.getPic(str2[0],str2[1]);
		        if(list!=null)
				{
					String[] res=list.get(0);
					PicObject pico=new PicObject(Utils.getPic(picPath+res[0]),res[1],Integer.parseInt(res[2]));
					ObjectOutputStream oin = new ObjectOutputStream(out);
					oin.writeObject(pico);
				}else 
				{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
		        return;
			}
			
			//更新新闻			
			//xwid,xwbt,xwgs,xwly,fbsj,xwnr,ygid,ztid,bsid,pic1MS,pic2MS,picTitelPath,pic1Path,pic2Path,
			if(msg.startsWith(UPDATE_NEW))
			{
				String result=null;
				String xwid=null;
				String[] str1 = msg.split(UPDATE_NEW);
				String[] str2 = str1[1].split("<->");
				String[] res = DBUtil.updateNEW(str2[0],str2[1],str2[2],str2[3],str2[4],str2[5],str2[6],str2[7],str2[8]);
				if(res!=null)
				{
					result=res[0];
					xwid=res[1];
					int bsid=Integer.parseInt(str2[8]);
					//图片名称格式 xwid_titel_xwid.jpg或xwid_pic_1/2.jpg
					if(bsid==1)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name0);
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						String tpid=DBUtil.getPicId(str2[0],0);
						DBUtil.updatePic(tpid, null, name0);
					}else if(bsid==2)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name0);
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						String tpid=DBUtil.getPicId(str2[0],0);
						DBUtil.updatePic(tpid,null, name0);
						//pic1
						byte[] pic1 = Utils.readBytes(in);
						String name1=xwid+"_pic_1.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name1);
						Utils.saveImage(Utils.bytesToBufImg(pic1),picPath+name1);
						String tpid1=DBUtil.getPicId(str2[0],1);
						DBUtil.updatePic(tpid1, str2[9], name1);
						
					}else if(bsid==3)
					{
						byte[] picTitel = Utils.readBytes(in);
						String name0=xwid+"_titel.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name0);
						Utils.saveImage(Utils.bytesToBufImg(picTitel),picPath+name0);
						String tpid=DBUtil.getPicId(str2[0],0);
						DBUtil.updatePic(tpid,null, name0);
						//pic1
						byte[] pic1 = Utils.readBytes(in);
						String name1=xwid+"_pic_1.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name1);
						Utils.saveImage(Utils.bytesToBufImg(pic1),picPath+name1);
						String tpid1=DBUtil.getPicId(str2[0],1);
						DBUtil.updatePic(tpid1, str2[9], name1);
						//pic2
						byte[] pic2 = Utils.readBytes(in);
						String name2=xwid+"_pic_2.jpg";
						com.bn.util.FileUtiles.DeleteFolder(picPath+name2);
						Utils.saveImage(Utils.bytesToBufImg(pic2),picPath+name2);
						String tpid2=DBUtil.getPicId(str2[0],2);
						DBUtil.updatePic(tpid2, str2[10], name2);
					}
				}
				if(result!=null)
				{
					Utils.writeStr(out, MyConverter.escape(ok));	
				}else
				if(result==null)
				{
					Utils.writeStr(out, MyConverter.escape(fail));	
				}
				return;
			}
			
				

			
			
			
			
			
			
			
			
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
			try {Utils.writeStr(out,MyConverter.escape(fail));}catch(IOException e1) {e1.printStackTrace();}
			
		}finally{
			try{out.close();}catch(Exception e) {e.printStackTrace();}
			try{in.close();}catch(Exception e) {e.printStackTrace();}
			try{sc.close();}catch(Exception e) {e.printStackTrace();}
		}
	}
}





