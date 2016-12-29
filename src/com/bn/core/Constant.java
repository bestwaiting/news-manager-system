
package com.bn.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class Constant 
{
	//***********************主界面树各节点名称**************************************
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

	//***********************图片和屏幕，装饰相关**************************************
    public static Color nodeBg=new Color(49, 106, 197);//树节点背景
    public static Color selectedBg=new Color(51, 153, 255);//表格选中时的背景色
	public static int SCREEN_WIDTH =(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() ;//屏幕宽度
	public static int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();//屏幕高度
	public static final String picPath="Pic//";
	public static final String tpicPath="Pic/tree//";
	public static final String bpicPath="Pic/button//";//按钮图片路径
	public static Image winIcon=Toolkit.getDefaultToolkit().getImage(picPath+"logo.png");//窗口图标
	public static final Color C_START = new Color(240,240,235);
	public static final Color C_END = new Color(59,141,199 );
	public static final Font subtitle=new Font("宋体",Font.BOLD ,15);//子标题字体
	public static final Font jltitle=new Font("宋体",Font.PLAIN ,14);//子标题字体2
	

	//****************************与联网访问相关**************************************
	public static final String SERVER_IP="127.0.0.1"; //服务器ip
	public static final int SERVER_PORT=31418; //服务器端口
	public static boolean dataGeted=false;//是否从网络获得了数据，同时是等待对话框的开关
	public static String USER_ID="";
	
	//**************************与服务器通信相关命令**************************************
	public static final String LOGIN="<#LOGIN#>";//登录
	public static final String GET_JBQX="<#GET_JBQX#>";//获得基本权限
	public static final String GET_JS="<#GET_JS#>";//获得角色信息
	public static final String GET_YG="<#GET_YG#>";//获得员工信息
	public static final String XG_JS="<#XG_JS#>";//修改角色
	public static final String ADD_JS="<#ADD_JS#>";//添加角色
	public static final String SC_JS="<#SC_JS#>";//删除角色
	public static final String ADD_JSQX_BY_QXMC="<#ADD_JSQX_BY_QXMC#>";///给指定角色添加名称为qxmc的权限ID
	public static final String GET_BJYDQX="<#GET_BJYDQX#>";//获得不具有的权限
	public static final String SC_QX="<#SC_QX#>";//删除权限
	public static final String XG_YG="<#XG_YG#>";//修改员工
	public static final String GET_BM="<#GET_BM#>";//获得部门字头
	public static final String CZ_BM="<#CZ_BM#>";//重组部门   (拖动树结点的效果)
	public static final String GET_MAX_BMID="<#GET_MAX_BMID#>";//由最大部门id
	public static final String GET_YG_BY_BMID="<#GET_YG_BY_BMID#>";//由部门获得员工信息字头
	public static final String DELETE_BM="<#DELETE_BM#>";//删除部门字头
	public static final String ADD_BM="<#ADD_BM#>";//添加部门字头
	public static final String ADD_YG="<#ADD_YG#>";//添加员工
	public static final String UPDATE_YG="<#UPDATE_YG#>";//员工个人信息修改
	public static final String ADD_NEW="<#ADD_NEW#>";//员工个人信息修改
	public static final String GET_NEW="<#GET_NEW#>";//获得新闻
	public static final String DEL_NEW="<#DEL_NEW#>";//删除新闻
	public static final String GET_NEW_By_XWID="<#GET_NEW_By_XWID#>";//获得指定新闻
	public static final String UPDATE_NEW="<#UPDATE_NEW#>";//更新新闻
	public static final String GET_SHJL="<#GET_SHJL#>";//获得审核记录
	public static final String UPDATE_SHJL="<#UPDATE_SHJL#>";//更新审核记录
	public static final String GET_SHJL_FILTER="<#GET_SHJL_FILTER#>";//获得审核记录条件检索
	public static final String GET_GRXW_FILTER="<#GET_GRXW_FILTER#>";//获得个人新闻条件检索
	public static final String GET_LM="<#GET_LM#>";//获得栏目
	public static final String XG_LM="<#XG_LM#>";//修改栏目名
	public static final String GET_LM_NEW="<#GET_LM_NEW#>";//获得栏目所含新闻
	public static final String GET_LM_FB_NEW="<#GET_LM_FB_NEW#>";//获得栏目所含发布新闻
	public static final String GET_FB_NEW="<#GET_FB_NEW#>";//发布新闻
	public static final String GET_FBNEW_FILTER="<#GET_FBNEW_FILTER#>";//获得发布新闻条件检索
	public static final String GET_DFB_NEW="<#GET_DFB_NEW#>";//待发布新闻
	public static final String GET_SH_By_SHID="<#GET_SH_By_SHID#>";//获得指定审核记录
	public static final String GET_SHJL_BY_XWID="<#GET_SHJL_BY_XWID#>";//获得指定新闻的审核记录
	public static final String ADD_SHJL_BY_XWID="<#ADD_SHJL_BY_XWID#>";//添加指定新闻的审核记录
	public static final String ADD_LM="<#ADD_LM#>";//添加栏目
	public static final String DEL_LM="<#DEL_LM#>";//删除栏目
	public static final String TRAN_LM="<#TRAN_LM#>";//调整栏目顺序
	public static final String TRAN_XW="<#TRAN_XW#>";//调整栏目顺序
	public static final String XG_LMID="<#XG_LMID#>";//修改栏目id
	public static final String XG_FBZTID="<#XG_FBZTID#>";//修改发布状态ID
	public static final String GET_PIC="<#GET_PIC#>";//获得图片
}
