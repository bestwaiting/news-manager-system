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
			
			
			
			
			//android�����Ƚ��٣������������⣬AD����
			////////////////////////////////////////////////////////////////////
			//		 					For AD 								  //
			////////////////////////////////////////////////////////////////////
			//�����Ŀ��Ϣ
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
				
			
			
			//�����Ŀ��Ϣ
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
			
			

			
			//���ָ����ͼƬ
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
			
			
			
			//���������Ϣxwid
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
			 * ����ֵֻ��3�֣�
			 * 1.  ok                       update/delete�ɹ�
			 * 2.  listת�����ַ���                          select��ѯ�Ľ��
			 * 3.  fail                     ���ݿ���ʳ����쳣�����ݿⷵ��null
			 */
			//��¼
			if(msg.startsWith(LOGIN))
			{
				String[] str1 = msg.split(LOGIN);
				String[] str2 = str1[1].split("<->");
				String res=DBUtil.login(str2[0],str2[1]);
				StringBuilder sb=new StringBuilder();
				if(res==null)
				{
					//���ݿ��쳣
					Utils.writeStr(out, MyConverter.escape(fail));
				}else if(res.equals(fail))
				{
					sb.append("<#>");
					sb.append(fail+"<->");
					sb.append("yhff");//�û��Ƿ�
					sb.append("<#>");
					Utils.writeStr(out,MyConverter.escape(sb.toString()));
				}else{
					sb.append("<#>");
					sb.append(ok+"<->");//��¼�ɹ�
					sb.append(res);
					sb.append("<#>");
					Utils.writeStr(out, MyConverter.escape(sb.toString()));
					//<#>ok<->ygid<->jsqxid1,2,3,4,<#>
				}
				return;
			}
			
			//��ɫ����Ȩ��
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
			
			//�޸Ľ�ɫ
			if(msg.startsWith(XG_JS))
			{
				String[] str1 = msg.split(XG_JS);
				String[] str2 = str1[1].split("<->");
				int jsid = Integer.parseInt(str2[0]);//���ɫID 
				String jsmc = str2[1];//��ɫ����
				String result = DBUtil.xgJSMC(jsid,jsmc);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			//��ý�ɫ��Ϣ
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
			
			//��ӽ�ɫ
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
			
			//��ɫ�ܷ�ɾ��
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
			
			//��ò����е�Ȩ��
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
			
			//���Ȩ��
			if(msg.startsWith(ADD_JSQX_BY_QXMC))
			{//��ָ����ɫ  ��� ����Ϊqxmc ��Ȩ��ID
				String[] str1 = msg.split(ADD_JSQX_BY_QXMC);
				String[] str2 = str1[1].split("<->");
				int jsid = Integer.parseInt(str2[0]);//���ɫID 
				String qxmc = str2[1];//Ȩ������
				String result = DBUtil.addJSQXByQXMC(jsid,qxmc);
				if(result.equals(ok))
				{
					Utils.writeStr(out,MyConverter.escape(ok));
				}else{
					Utils.writeStr(out,MyConverter.escape(fail));
				}
				return;
			}
			
			//ɾ��Ȩ��
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
			
			//���Ա����Ϣ
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
			
			//����Ա����Ϣ
			if(msg.startsWith(XG_YG))
			{
				String[] str1 = msg.split(XG_YG);
				String[] str2 =str1[1].split("<->");
				//ygid,bmid,jsid,lzyf
				String result = DBUtil.updateYGXX(str2);//���³ɹ�����"ok" ʧ�ܷ���"fail"
				if(result==null){
					Utils.writeStr(out, MyConverter.escape(fail));
				}else{
					Utils.writeStr(out, MyConverter.escape(ok));
				}
				return;
			}
			
			//���Ա��
			if(msg.startsWith(ADD_YG))
			{
				String[] str1 = msg.split(ADD_YG);
				String[] str2 = str1[1].split("<->");
				//��½�˺�   ��½����   Ա������  ��ϵ��ʽ  �Ա�   ����ID ,��ɫid
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
			
			//�����ò���
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
			
			//���鲿��(Ҫ����Ľڵ��id,Ҫ�ҵ��Ľڵ�id,��pid)
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
			
			//��������id
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
			
			
			//ͨ������ID��ȡ�˲����Լ��Ӳ��ŵ�Ա��
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
			
			//ɾ������
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
			
			
			//��ӻ����޸Ĳ���
			/*
			 * <#ADD_BM#>flag<->fbmid/fbmid<->w����<->w��������<#ADD_BM#>
			 * ��һ��������flagΪ0��ʾ��Ӳ��ţ�Ϊ1��ʾ���²���
			 * �ڶ�����������Ϊ��Ӳ���ʱ��Ϊfbmid;��Ϊ���²���ʱ��Ϊbmid
			 */
			if(msg.startsWith(ADD_BM))
			{
				String[] str1 = msg.split(ADD_BM);
				String[] str2 = str1[1].split("<->");
				int flag = Integer.parseInt(str2[0]);
				if(flag==0)//��Ӳ���
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
			
			//������Ϣ�޸�
			//ygid ysmm xmm  ygxm ygxb lxfs
			if(msg.startsWith(UPDATE_YG))
			{
				String[] str1 = msg.split(UPDATE_YG);
				String[] str2 = str1[1].split("<->");
				String res=DBUtil.updata_yg(str2[0],str2[1],str2[2],str2[3],str2[4],str2[5]);
				if(res==null)
				{
					//���ݿ��쳣
					Utils.writeStr(out, MyConverter.escape(fail));
				}else if(res.equals(fail))
				{
					Utils.writeStr(out,MyConverter.escape("mmcw"));
				}else{
					Utils.writeStr(out, MyConverter.escape("ok"));
				}
				return;
			}
			
			
			
			//���������Ϣ
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
			
			
			//���ָ������
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
			
			
			//ɾ������,�������Ŷ�Ӧ����˼�¼
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
			


			
			
			//�����˼�¼
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
			
			
			//��ù���֮�����˼�¼
			if(msg.startsWith(GET_SHJL_FILTER))
			{
				String[] str1 = msg.split(GET_SHJL_FILTER);
				String[] str2 = str1[1].split("<->");
				//״̬id ,  ��ʼ����  ��ֹ����,���ű��
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
			
			
			//��ù���֮�����˼�¼
			if(msg.startsWith(GET_GRXW_FILTER))
			{
				String[] str1 = msg.split(GET_GRXW_FILTER);
				String[] str2 = str1[1].split("<->");
				//״̬id ,  ��ʼ����  ��ֹ����
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
			
			
			//�����Ŀ
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
			
			
			//�޸���Ŀ
			if(msg.startsWith(XG_LM))
			{
				String[] str1 = msg.split(XG_LM);
				String[] str2 = str1[1].split("<->");
				int lmid = Integer.parseInt(str2[0]);//���ɫID 
				String lmmc = str2[1];//��ɫ����
				String result = DBUtil.xgLMMC(lmid,lmmc);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//��Ŀ��������
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
			
			
			//�������Ų鿴���棬��Ŀ��������
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
			
			
			//������������
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
			
			
			//���ָ����˼�¼
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
			
			

			//������˼�¼
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
			
			
			
			//���ָ�����ŵ���˼�¼
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
			
					
			//�������Ŀ
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
			
			
			//ɾ����Ŀ
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
			
			
			//������Ŀ˳��
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
			
			
			//��������˳��
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
			
			
			//�޸���Ŀid
			if(msg.startsWith(XG_LMID))
			{
				String[] str1 = msg.split(XG_LMID);
				String[] str2 = str1[1].split("<->");
				int xwid = Integer.parseInt(str2[0]);//����ID 
				int lmid = Integer.parseInt(str2[1]);//����ID 
				String result = DBUtil.xgLMID(xwid,lmid);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			
			//�޸���Ŀid
			if(msg.startsWith(XG_FBZTID))
			{
				String[] str1 = msg.split(XG_FBZTID);
				String[] str2 = str1[1].split("<->");
				int xwid = Integer.parseInt(str2[0]);//����ID 
				int fbztid = Integer.parseInt(str2[1]);//����ID 
				String result = DBUtil.xgFBZTID(xwid,fbztid);
				if(result==null){
					Utils.writeStr(out,MyConverter.escape(fail));
				}else{
					Utils.writeStr(out,MyConverter.escape(ok));
				}
				return;
			}
			
			
			//�������
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
					//ͼƬ���Ƹ�ʽ xwid_titel_xwid.jpg��xwid_pic_1/2.jpg
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
			
			
			//���ָ����ͼƬ
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
			
			//��������			
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
					//ͼƬ���Ƹ�ʽ xwid_titel_xwid.jpg��xwid_pic_1/2.jpg
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





