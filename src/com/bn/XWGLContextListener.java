package com.bn;


import static com.bn.Constant.stop;
import javax.servlet.*;

public class XWGLContextListener implements ServletContextListener
{
	/*
	 * �����������������ڱ���Ŀ��
	 * WebRoot-WEB-INF-web.xmlĿ¼����ӵ�
	 */
	
	// ���������WebӦ�÷������ý��������ʱ�򱻵��á�
	public void contextInitialized(ServletContextEvent sce)
	{
		stop=true;
		new ServerThread().start();
	}
	// ���������WebӦ�÷����Ƴ���û�������ٽ��������ʱ�򱻵��á�
	public void contextDestroyed(ServletContextEvent sce)
	{
		stop=false;
	}	
}