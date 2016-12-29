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
 * ��¼����
 */
@SuppressWarnings("serial")
public class LoginWindow extends JFrame
{
	private JLabel jlTxt[] = {	new JLabel("�û�����"),new JLabel("��  �룺")	};
	private JTextField jtfname  = new JTextField(1);
	private JPasswordField jpassword  = new JPasswordField(1);
	private JButton login = new JButton("��¼");
	private JButton cancel = new JButton("ȡ��");
	private JLabel jlIcon = new JLabel(new ImageIcon(picPath+"login.png"));//����ͼ��JLabel
	private JPanel jpInput = new JPanel()//�����������
	{
		@Override
		protected void paintComponent(Graphics g) 
		{
			Graphics2D g2 = (Graphics2D) g;  
	        // ���ƽ���     ��ʼ����  ��ʼ��ɫ
	        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
	        g2.fillRect(0, 0, getWidth(), getHeight());  
		}
	};
	//������ľ�̬����
	public static MainJFrame mf=null;
	public LoginWindow()
    {
		this.add(jlIcon);
		jlIcon.setBounds(0, 15, 330, 70);
		this.add(jpInput);
		jpInput.setLayout(null);
		jpInput.setBounds(0, 70, 330, 250-70);
		
		//���ؼ������������
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
				//�����߳�
				new Thread()
				{
					public void run()
					{
						String uid=jtfname.getText().trim();
						String password=new String(jpassword.getPassword());
						//�û����Ƿ�������֤
						if(uid.length()<=0)
						{
							JOptionPane.showMessageDialog(LoginWindow.this,"�������û�����","��ʾ",JOptionPane.WARNING_MESSAGE);
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//�û��������Ƿ�Ϊ������֤
						if(!uid.matches("[0-9a-zA-Z]+"))
						{
							JOptionPane.showMessageDialog(LoginWindow.this, "�û�������Ϊ���ֻ���ĸ��", "��ʾ", JOptionPane.WARNING_MESSAGE);	
							jtfname.setText("");
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//����������֤
						if(password.length()<=0)
						{
							JOptionPane.showMessageDialog(LoginWindow.this,"���������룡","��ʾ",JOptionPane.WARNING_MESSAGE);
							login.setEnabled(true);
							jpassword.requestFocus();
							return;
						}

						//�����߳�
						LoginWindow.watchThread();
						String loginInfo=LOGIN+uid+"<->"+password+LOGIN;
						String msg=null;
						try
						{
							//���ص�¼��Ϣ
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
							JOptionPane.showMessageDialog(LoginWindow.this,"�Ƿ��û���",	"��ʾ",JOptionPane.WARNING_MESSAGE);jtfname.setText("");jpassword.setText("");
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}else
						{
							dataGeted=true;
							JOptionPane.showMessageDialog(LoginWindow.this,"������ϣ����Ժ����ԣ�","����",JOptionPane.ERROR_MESSAGE);
							login.setEnabled(true);
							jtfname.requestFocus();
							return;
						}
						//���Ȩ����Ϣ
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
							qxSet.add(t);	//������Ȩ��
							//qxSetAll.add(t);//����Ȩ��
						}
						//??????????????????????????????????????????????????????
						//����������
						USER_ID=ygid;
						mf = new MainJFrame(qxSet,USER_ID,ygxm);
						LoginWindow.this.dispose();
						
					}
				}.start();
			}
		});
		//ȡ����ť
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0);
			}
		});
		
        this.setTitle("��¼");
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
		try {//windows���
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new LoginWindow();
	}
	
	//�ȴ��Ի�����ֲ��ر��߳�
	public static void watchThread()
	{
		new Thread()
		{
			public void run()
			{
				if(mf!=null)
				{//һ���ֵȴ��Ի���ͽ������ڲ�����
					mf.setEnabled(false);
				}
				WaitDialog wd=null;//�ȴ��Ի��������
				long timeStart=System.nanoTime();//��ÿ�ʼʱ��
				boolean isdhk=false;//��ʾ�ȴ��Ի����Ƿ��
				while(true)
				{
					try { Thread.sleep(40); } catch (InterruptedException e) { e.printStackTrace(); }
					if(dataGeted)//������ݻ����
					{
						if(isdhk)//���ȴ��Ի����ǿ��ŵ�
						{
							wd.dispose();//�رյȴ��Ի���
							if(mf!=null)
							{
								mf.setAlwaysOnTop(true);								
								mf.setAlwaysOnTop(false);
							}	
						}	
						if(mf!=null)//�رյȴ��Ի���֮���������������
						{
							mf.setEnabled(true);
						}						
						break;//�ж�ѭ�����˳��߳�
					}
					if(!isdhk)//����ȴ��Ի���û��
					{
						long currTime=System.nanoTime();//��õ�ǰʱ��
						if(currTime-timeStart>1000*1000000)//�������1S�л�û�л������
						{//�򵯳��ȴ��Ի���
							wd=new WaitDialog("��ȴ�.....");
							if(mf!=null)
							{//���������治����
								mf.setEnabled(false);
							}
							isdhk=true;//���±�ʶλ���ȴ��Ի����Դ�
						}
					}
				}
			}
		}.start();
	}
}
