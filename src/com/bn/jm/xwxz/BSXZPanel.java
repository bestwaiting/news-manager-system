package com.bn.jm.xwxz;

import static com.bn.core.Constant.picPath;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.jltitle;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.grxxxg.GRXXGLPanel;
import com.bn.util.SocketUtil;
import com.sunking.swing.JDatePicker;

public class BSXZPanel extends JPanel
{
	private JTextField jtfLine=new JTextField();
	private JLabel jlTitle=new JLabel("版式选择");
	private JPanel jpBS=new JPanel();
	JScrollPane js=new JScrollPane(jpBS);
	JButton jbutton1=null;
	JButton jbutton2=null;
	JButton jbutton3=null;
	
	public int BSid=0;
	public String ygid=null;
	public MainJFrame mf=null;
	
	
	
	
	
	
	public BSXZPanel(String ygid,MainJFrame mf) 
	{
		this.ygid=ygid;
		this.mf=mf;
		
		
		this.setLayout(null);
		jlTitle.setBounds(25,15,100,20);
		jlTitle.setFont(subtitle); 
		this.add(jlTitle);
		
		//分割线jtf
		jtfLine.setBounds(20, 60,1140, 4);
		jtfLine.setEnabled(false);
		this.add(jtfLine);
		
		//板式选择Panel
		jpBS.setLayout(null);
		js.setViewportView(jpBS);
		this.add(js);
		js.setBounds(20, 90, 745, 570);
		//js.setBounds(20, 90, 1140, 570);
		
		instantBSPic();
		addButtonListener();
		
	}
	
	private void instantBSPic()
	{
		Icon icon1=new ImageIcon(picPath+"style1.jpg");
		jbutton1=new JButton();
		jbutton1.setBounds(20, 20, 220, 520);
		jbutton1.setIcon(icon1);
		
		Icon icon2=new ImageIcon(picPath+"style2.jpg");
		jbutton2=new JButton();
		jbutton2.setBounds(20+240, 20, 220, 520);
		jbutton2.setIcon(icon2);
		
		Icon icon3=new ImageIcon(picPath+"style3.jpg");
		jbutton3=new JButton();
		jbutton3.setBounds(20+240+240, 20, 220, 520);
		jbutton3.setIcon(icon3);
		
		jpBS.add(jbutton1);
		jpBS.add(jbutton2);
		jpBS.add(jbutton3);	
	}
	

	
	private void addButtonListener()
	{
		jbutton1.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				BSid=1;
				mf.gotoXWXZ(BSid);
				
			}
			
		});
		
		jbutton2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				BSid=2;
				mf.gotoXWXZ(BSid);
			}
			
		});
		
		jbutton3.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) {
				BSid=3;
				mf.gotoXWXZ(BSid);
				
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
