package com.bn.jm.xwxz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.bpicPath;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.bn.jm.xwxz.XWXZPanel.JdialogShowPic;

public class Style1Panel extends JPanel
{
	public JLabel jlabelContent = new JLabel("以下录入新闻正文！", new ImageIcon(
			bpicPath + "text.png"), JLabel.CENTER);
	public JTextArea jtaContent=new JTextArea();
	public JScrollPane jsp=new JScrollPane(jtaContent);
	
	private String content=null;
	//private String patternStr="\\n";

	
	
	public Style1Panel() 
	{
		this.setLayout(null);
		this.setPreferredSize(new Dimension(710, 1040));
		this.setBackground(Color.WHITE);
		jlabelContent.setBounds(0, 0, 710, 40);
		this.add(jlabelContent);
		jtaContent.setLineWrap(true);
		jsp.setBounds(0,40, 690, 1000);
		jsp.setWheelScrollingEnabled(true);
		this.add(jsp);	
	}
	
	public String getContent()
	{
		content=jtaContent.getText();
		return content;
	}
	
	//接受了数据之后，更新各控件显示的方法
		public void flushContent(String content)
		{
			jtaContent.setText(content);
			this.content=content;
		}
	
	public void clear()
	{
		jtaContent.setText(null);
		content=null;
	}



}
