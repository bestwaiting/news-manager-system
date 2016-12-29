package com.bn.jm;

import static com.bn.core.Constant.picPath;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.LOGIN;
import static com.bn.core.Constant.USER_ID;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.winIcon;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import com.bn.util.SocketUtil;

/**
 * 登录窗口
 */
@SuppressWarnings("serial")
public class LoginWindow extends JFrame
{
	private JLabel jlTxt[] = {	new JLabel("用户名："),new JLabel("密  码：")	};
	private JTextField jtfname  = new JTextField(1);
	private JPasswordField jpassword  = new JPasswordField(1);
	private JButton login = new JButton("登录");
	private JButton cancel = new JButton("取消");
	private JLabel jlIcon = new JLabel(new ImageIcon(picPath+"login.png"));//上面图标JLabel
	private JPanel jpInput = new JPanel()//下面输入面板
	{
		@Override
		protected void paintComponent(Graphics g) 
		{
			Graphics2D g2 = (Graphics2D) g;  
	        // 绘制渐变     起始坐标  起始颜色
	        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
	        g2.fillRect(0, 0, getWidth(), getHeight());  
		}
	};
	//主窗体的静态引用
	public static MainJFrame mf=null;
	public LoginWindow()
    {
		this.add(jlIcon);
		jlIcon.setBounds(0, 15, 330, 70);
		this.add(jpInput);
		jpInput.setLayout(null);
		jpInput.setBounds(0, 70, 330, 250-70);
		
		//将控件加入输入面板
		jpInput.add(jlTxt[0]);
		jpInput.add(jlTxt[1]);
		jpInput.add(jtfname);
		jpInput.add(jpassword);
		jpInput.add(login);
		jpInput.add(cancel);
		
		jlTxt[0].setBounds(60, 100-70, 120, 20);
		jlTxt[1].setBounds(60, 125-70, 120, 20);
		jtfname.setBounds(120, 100-70, 140, 20);
		jpassword.setBounds(120, 125-70, 140, 20);
		jpassword.setEchoChar('*');
		login.setBounds(100+50, 180-70, 70, 20);
		cancel.setBounds(180+50, 180-70, 70, 20);
		login.setOpaque(false);
		cancel.setOpaque(false);
		jtfname.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jpassword.requestFocus();}});
		jpassword.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){login.requestFocus();}});
		login.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				login.setEnabled(false);
				dataGeted=false;
				//任务线程
				new Thread()
				{
					public void run()
					{
						String uid=jtfname.getText().trim();
						String password=new String(jpassword.getPassword());
						//用户名是否输入验证
						if(uid.length()<=0)
						{
							JOptionPane.showMessageDialog(LoginWindow.this,"请输入用户名！","提示",JOptionPane.WARNING_MESSAGE);
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//用户名输入是否为数字验证
						if(!uid.matches("[0-9a-zA-Z]+"))
						{
							JOptionPane.showMessageDialog(LoginWindow.this, "用户名必须为数字或字母！", "提示", JOptionPane.WARNING_MESSAGE);	
							jtfname.setText("");
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//密码输入验证
						if(password.length()<=0)
						{
							JOptionPane.showMessageDialog(LoginWindow.this,"请输入密码！","提示",JOptionPane.WARNING_MESSAGE);
							login.setEnabled(true);
							jpassword.requestFocus();
							return;
						}

						//监视线程
						LoginWindow.watchThread();
						String loginInfo=LOGIN+uid+"<->"+password+LOGIN;
						String msg=null;
						try
						{
							//返回登录信息
						    msg=SocketUtil.sendAndGetMsg(loginInfo);
						}catch (Exception e) {
							login.setEnabled(true);
							return;
						}
						
						System.out.println(msg);
						List<String[]>list=SocketUtil.strToList(msg);
						String []logininfo=list.get(0);
						if(logininfo[0].equals("ok"))
						{
							dataGeted=true;
						}else if(logininfo[0].equals("fail")&&logininfo[1].equals("yhff"))
						{
							dataGeted=true;
							JOptionPane.showMessageDialog(LoginWindow.this,"非法用户！",	"提示",JOptionPane.WARNING_MESSAGE);jtfname.setText("");jpassword.setText("");
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}else
						{
							dataGeted=true;
							JOptionPane.showMessageDialog(LoginWindow.this,"网络故障，请稍后再试！","错误",JOptionPane.ERROR_MESSAGE);
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//获得权限信息
						//??????????????????????????????????????????????????????
						 String ygid=logininfo[1];
						 String qxlb[]=logininfo[2].split(",");
						 String ygxm=logininfo[3];
						for(String s:qxlb)
						{
							System.out.print(s+".");
						}
						Set<Integer>  qxSet=new HashSet<Integer>();
						for(String s:qxlb)
						{
							int t=Integer.parseInt(s);
							qxSet.add(t);	//功能树权限
							//qxSetAll.add(t);//所有权限
						}
						//??????????????????????????????????????????????????????
						//创建主窗口
						USER_ID=ygid;
						mf = new MainJFrame(qxSet,USER_ID,ygxm);
						LoginWindow.this.dispose();
						
					}
				}.start();
			}
		});
		//取消按钮
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		
        this.setTitle("登录");
        this.setLayout(null);
        this.setSize(330, 250);
        this.setLocation((int) (SCREEN_WIDTH -330 ) / 2,   (int) (SCREEN_HEIGHT -250) / 2);
        this.setResizable(false);
        this.setIconImage(winIcon);
		this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
	public static void main(String[]args)
	{
		try {//windows风格
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LoginWindow();
	}
	
	//等待对话框出现并关闭线程
	public static void watchThread()
	{
		new Thread()
		{
			public void run()
			{
				if(mf!=null)
				{//一出现等待对话框就将主窗口不可用
					mf.setEnabled(false);
				}
				WaitDialog wd=null;//等待对话框的引用
				long timeStart=System.nanoTime();//获得开始时间
				boolean isdhk=false;//表示等待对话框是否打开
				while(true)
				{
					try { Thread.sleep(40); } catch (InterruptedException e) { e.printStackTrace(); }
					if(dataGeted)//如果数据获得了
					{
						if(isdhk)//过等待对话框是开着的
						{
							wd.dispose();//关闭等待对话框
							if(mf!=null)
							{
								mf.setAlwaysOnTop(true);								
								mf.setAlwaysOnTop(false);
							}	
						}	
						if(mf!=null)//关闭等待对话框之后，设置主界面可用
						{
							mf.setEnabled(true);
						}						
						break;//中断循环，退出线程
					}
					if(!isdhk)//如果等待对话框没开
					{
						long currTime=System.nanoTime();//获得当前时间
						if(currTime-timeStart>1000*1000000)//如果大于1S中还没有获得数据
						{//则弹出等待对话框
							wd=new WaitDialog("请等待.....");
							if(mf!=null)
							{//设置主界面不可用
								mf.setEnabled(false);
							}
							isdhk=true;//更新标识位，等待对话框以打开
						}
					}
				}
			}
		}.start();
	}
}
