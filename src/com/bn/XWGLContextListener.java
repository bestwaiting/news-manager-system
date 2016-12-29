package com.bn;


import static com.bn.Constant.stop;
import javax.servlet.*;

public class XWGLContextListener implements ServletContextListener
{
	/*
	 * 这个监听器的添加是在本项目的
	 * WebRoot-WEB-INF-web.xml目录下添加的
	 */
	
	// 这个方法在Web应用服务做好接受请求的时候被调用。
	public void contextInitialized(ServletContextEvent sce)
	{
		stop=true;
		new ServerThread().start();
	}
	// 这个方法在Web应用服务被移除，没有能力再接受请求的时候被调用。
	public void contextDestroyed(ServletContextEvent sce)
	{
		stop=false;
	}	
}