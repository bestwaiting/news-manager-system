
package com.bn.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class Constant 
{
	//***********************�����������ڵ�����**************************************
    public static final int xwglxt=-1;
    public static final int xwgl=101;
    public static final int xwxz=1;
    public static final int grxwgl=2;
    public static final int fbxwck=3;                                                       
    public static final int shgl=4;
    public static final int lmgl=5;
    public static final int yhqxgl=103;
    public static final int jsqxgl=6;
    public static final int jbqxck=7;
    public static final int bmyggl=104;
    public static final int bmgl=8;
    public static final int ygxxgl=9;
    public static final int grxxgl=10;

	//***********************ͼƬ����Ļ��װ�����**************************************
    public static Color nodeBg=new Color(49, 106, 197);//���ڵ㱳��
    public static Color selectedBg=new Color(51, 153, 255);//���ѡ��ʱ�ı���ɫ
	public static int SCREEN_WIDTH =(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() ;//��Ļ���
	public static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();//��Ļ�߶�
	public static final String picPath="Pic//";
	public static final String tpicPath="Pic/tree//";
	public static final String bpicPath="Pic/button//";//��ťͼƬ·��
	public static Image winIcon=Toolkit.getDefaultToolkit().getImage(picPath+"logo.png");//����ͼ��
	public static final Color C_START = new Color(240,240,235);
	public static final Color C_END = new Color(59,141,199 );
	public static final Font subtitle=new Font("����",Font.BOLD ,15);//�ӱ�������
	public static final Font jltitle=new Font("����",Font.PLAIN ,14);//�ӱ�������2
	

	//****************************�������������**************************************
	public static final String SERVER_IP="127.0.0.1"; //������ip
	public static final int SERVER_PORT=31418; //�������˿�
	public static boolean dataGeted=false;//�Ƿ�������������ݣ�ͬʱ�ǵȴ��Ի���Ŀ���
	public static String USER_ID="";
	
	//**************************�������ͨ���������**************************************
	public static final String LOGIN="<#LOGIN#>";//��¼
	public static final String GET_JBQX="<#GET_JBQX#>";//��û���Ȩ��
	public static final String GET_JS="<#GET_JS#>";//��ý�ɫ��Ϣ
	public static final String GET_YG="<#GET_YG#>";//���Ա����Ϣ
	public static final String XG_JS="<#XG_JS#>";//�޸Ľ�ɫ
	public static final String ADD_JS="<#ADD_JS#>";//��ӽ�ɫ
	public static final String SC_JS="<#SC_JS#>";//ɾ����ɫ
	public static final String ADD_JSQX_BY_QXMC="<#ADD_JSQX_BY_QXMC#>";///��ָ����ɫ�������Ϊqxmc��Ȩ��ID
	public static final String GET_BJYDQX="<#GET_BJYDQX#>";//��ò����е�Ȩ��
	public static final String SC_QX="<#SC_QX#>";//ɾ��Ȩ��
	public static final String XG_YG="<#XG_YG#>";//�޸�Ա��
	public static final String GET_BM="<#GET_BM#>";//��ò�����ͷ
	public static final String CZ_BM="<#CZ_BM#>";//���鲿��   (�϶�������Ч��)
	public static final String GET_MAX_BMID="<#GET_MAX_BMID#>";//�������id
	public static final String GET_YG_BY_BMID="<#GET_YG_BY_BMID#>";//�ɲ��Ż��Ա����Ϣ��ͷ
	public static final String DELETE_BM="<#DELETE_BM#>";//ɾ��������ͷ
	public static final String ADD_BM="<#ADD_BM#>";//��Ӳ�����ͷ
	public static final String ADD_YG="<#ADD_YG#>";//���Ա��
	public static final String UPDATE_YG="<#UPDATE_YG#>";//Ա��������Ϣ�޸�
	public static final String ADD_NEW="<#ADD_NEW#>";//Ա��������Ϣ�޸�
	public static final String GET_NEW="<#GET_NEW#>";//�������
	public static final String DEL_NEW="<#DEL_NEW#>";//ɾ������
	public static final String GET_NEW_By_XWID="<#GET_NEW_By_XWID#>";//���ָ������
	public static final String UPDATE_NEW="<#UPDATE_NEW#>";//��������
	public static final String GET_SHJL="<#GET_SHJL#>";//�����˼�¼
	public static final String UPDATE_SHJL="<#UPDATE_SHJL#>";//������˼�¼
	public static final String GET_SHJL_FILTER="<#GET_SHJL_FILTER#>";//�����˼�¼��������
	public static final String GET_GRXW_FILTER="<#GET_GRXW_FILTER#>";//��ø���������������
	public static final String GET_LM="<#GET_LM#>";//�����Ŀ
	public static final String XG_LM="<#XG_LM#>";//�޸���Ŀ��
	public static final String GET_LM_NEW="<#GET_LM_NEW#>";//�����Ŀ��������
	public static final String GET_LM_FB_NEW="<#GET_LM_FB_NEW#>";//�����Ŀ������������
	public static final String GET_FB_NEW="<#GET_FB_NEW#>";//��������
	public static final String GET_FBNEW_FILTER="<#GET_FBNEW_FILTER#>";//��÷���������������
	public static final String GET_DFB_NEW="<#GET_DFB_NEW#>";//����������
	public static final String GET_SH_By_SHID="<#GET_SH_By_SHID#>";//���ָ����˼�¼
	public static final String GET_SHJL_BY_XWID="<#GET_SHJL_BY_XWID#>";//���ָ�����ŵ���˼�¼
	public static final String ADD_SHJL_BY_XWID="<#ADD_SHJL_BY_XWID#>";//���ָ�����ŵ���˼�¼
	public static final String ADD_LM="<#ADD_LM#>";//�����Ŀ
	public static final String DEL_LM="<#DEL_LM#>";//ɾ����Ŀ
	public static final String TRAN_LM="<#TRAN_LM#>";//������Ŀ˳��
	public static final String TRAN_XW="<#TRAN_XW#>";//������Ŀ˳��
	public static final String XG_LMID="<#XG_LMID#>";//�޸���Ŀid
	public static final String XG_FBZTID="<#XG_FBZTID#>";//�޸ķ���״̬ID
	public static final String GET_PIC="<#GET_PIC#>";//���ͼƬ
}
