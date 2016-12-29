package com.bestwait.news;

import java.io.File;
import android.os.Environment;

public class Constant 
{
	// ****************************与联网访问相关**************************************
	public static final int SERVER_PORT = 31418; // 服务器端口
	public static final String SERVER_IP = "13.8.2.43"; // 服务器ip
	public static final int lineSize=15;//每页显示条数
	public static boolean dataGeted = false;// 是否从网络获得了数据，同时是等待对话框的开关
	public static String path = Environment.getExternalStorageDirectory()
			.toString() + File.separatorChar + "xwglpic" + File.separator;// 图片路径
	public static String PATH = Environment.getExternalStorageDirectory()
			.toString() + File.separatorChar + "xwglpic";// 图片文件夹路径
	// ****************************服务器命令**************************************
	public static final String GET_LMA = "<#GET_LMA#>";// 获得栏目
	public static final String GET_LM_NEWSA = "<#GET_LM_NEWSA#>";// 栏目所包含新闻列表
	public static final String GET_PICA = "<#GET_PICA#>";// 获得图片
	public static final String GET_NEWA = "<#GET_NEWA#>";// 获得新闻
}
