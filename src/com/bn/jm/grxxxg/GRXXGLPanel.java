package com.bn.jm.grxxxg;


import static com.bn.core.Constant.UPDATE_YG;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.jltitle;

import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bn.jm.LoginWindow;
import com.bn.jm.ygxxgl.NewYGPanel;
import com.bn.util.SocketUtil;

public class GRXXGLPanel extends JPanel
{
	String ygid=null;
	String ysmm=null;
	String xmm=null;
	String qrmm=null;
	String ygxm=null;
	String lxfs=null;
	String ygxb=null;
	
	
	private JLabel jlTitle=new JLabel("个人信息修改");
	private JLabel[] jlArry={
		new JLabel("员工 I D:"),
		new JLabel("原始密码:")	,
		new JLabel("新 密 码:")	,
		new JLabel("确认密码:")	,
		new JLabel("真实姓名:")	,
		new JLabel("员工性别:")	,
		new JLabel("联系方式:")	
	};
	
	private JTextField jtfYGID=new JTextField(1);
	private JPasswordField jtfYSMM=new JPasswordField(1);
	private JPasswordField jtfXMM=new JPasswordField(1);
	private JPasswordField jtfQRMM=new JPasswordField(1);
	private JTextField jtfXM=new JTextField(1);
    String[] str={"男","女"};
	private JComboBox jcbXB=new JComboBox(str);
	private JTextField jtfLXFS=new JTextField(1);
	
	
	
	private JButton jbXG=new JButton("修改");
	
	
	public GRXXGLPanel(String ygid) 
	{
		this.ygid=ygid;
		this.setLayout(null);
		jlTitle.setBounds(25,15,100,20);
		this.add(jlTitle);
		jlTitle.setFont(subtitle);
		for(int i=0;i<jlArry.length;i++)
		{
			jlArry[i].setBounds(25, 50+i*30, 70, 20);
			jlArry[i].setFont(jltitle);
			this.add(jlArry[i]);
		}
		jtfYGID.setBounds(95, 50+0*30, 150, 20);
		jtfYSMM.setBounds(95, 50+1*30, 150, 20);
		jtfXMM.setBounds(95, 50+2*30, 150, 20);
		jtfQRMM.setBounds(95, 50+3*30, 150, 20);
		jtfXM.setBounds(95, 50+4*30, 150, 20);
		jcbXB.setBounds(95, 50+5*30, 150, 20);
		jtfLXFS.setBounds(95, 50+6*30, 150, 20);
		jbXG.setBounds(25,265, 80, 25);
		jbXG.setOpaque(false);
		jtfYGID.setText(ygid);
		jtfYGID.setEditable(false);
		this.add(jtfYGID);
		this.add(jtfYSMM);
		this.add(jtfXMM);
		this.add(jtfQRMM);
		this.add(jtfXM);
		this.add(jcbXB);
		this.add(jtfLXFS);
		this.add(jbXG);
		addFocusListener();
		initButton();
	}
	
	//添加回车监听
	private void addFocusListener()
	{
		
		jtfYSMM.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtfXMM.requestFocus();}});
		jtfXMM.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtfQRMM.requestFocus();}});
		jtfQRMM.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtfXM.requestFocus();}});
		jtfXM.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jcbXB.requestFocus();}});
		jtfLXFS.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jbXG.requestFocus();}});
	};
	
	//为修改按钮加监听
	private void initButton()
	{
		jbXG.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(new String(jtfYSMM.getPassword()).length()<=0)
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"请输入原始密码！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				ysmm=new String(jtfYSMM.getPassword()).trim();
				if(new String(jtfXMM.getPassword()).length()<=0)
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"请输入新密码！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				xmm=new String(jtfXMM.getPassword()).trim();
				if(new String(jtfQRMM.getPassword()).length()<=0)
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"请输入确认的密码！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				qrmm=new String(jtfQRMM.getPassword()).trim();
				if(!xmm.equals(qrmm))
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"两次输入的密码不相同！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if(jtfXM.getText().trim().length()<=0)
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"请输入姓名！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				 ygxm=jtfXM.getText().trim();
				if(jtfLXFS.getText().trim().length()<=0)
				{
					JOptionPane.showMessageDialog(GRXXGLPanel.this,"请输入联系方式！","提示",JOptionPane.WARNING_MESSAGE);
					return;
				}
				lxfs=jtfLXFS.getText().trim();
				ygxb=jcbXB.getSelectedItem().toString();
				
				//任务线程
				dataGeted=false;
				new Thread()
				{
					public void run()
					{
						StringBuilder sb=new StringBuilder();
						sb.append(UPDATE_YG);
						sb.append(ygid+"<->");
						sb.append(ysmm+"<->");
						sb.append(xmm+"<->");
						sb.append(ygxm+"<->");
						sb.append(ygxb+"<->");
						sb.append(lxfs);
						sb.append(UPDATE_YG);
						String msg=SocketUtil.sendAndGetMsg(sb.toString());
						dataGeted=true;
						if(msg.equals("mmcw"))
						{
							JOptionPane.showMessageDialog(GRXXGLPanel.this,"原始密码不正确，请重新输入！","提示",JOptionPane.WARNING_MESSAGE);
							jtfYSMM.setText(null);
							jtfYSMM.requestFocus();
							return;
							
						}else
						{
							JOptionPane.showMessageDialog(GRXXGLPanel.this,"恭喜，个人信息修改成功！","提示",JOptionPane.NO_OPTION);
							jtfYSMM.setText(null);
							jtfXMM.setText(null);
							jtfQRMM.setText(null);
							jtfXM.setText(null);
							jcbXB.setSelectedIndex(0);
							jtfLXFS.setText(null);
						}
									
					}
				}.start();
				//监视线程
				LoginWindow.watchThread();	
			}
		});
		
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;  
        // 绘制渐变     起始坐标  起始颜色
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));  
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
