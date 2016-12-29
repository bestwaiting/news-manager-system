package com.bn.jm.grxwgl;

import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_NEW;
import static com.bn.core.Constant.GET_GRXW_FILTER;
import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.jsqxgl.JSQXGLPanel;
import com.bn.jm.shgl.SHGLPanel;
import com.bn.jm.shgl.SHJLTableModel;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.jm.ygxxgl.YGBMJTreeComboBoxEidtor;
import com.bn.jm.ygxxgl.YGBMRenderer;
import com.bn.jm.ygxxgl.YGButtonEditor;
import com.bn.jm.ygxxgl.YGButtonRenderer;
import com.bn.jm.ygxxgl.YGJSJComboBoxEidtor;
import com.bn.jm.ygxxgl.YGJSRenderer;
import com.bn.jm.ygxxgl.YGLZJComboBoxEditor;
import com.bn.jm.ygxxgl.YGLZRenderer;
import com.bn.jm.ygxxgl.YGTableModel;
import com.bn.jm.ygxxgl.YGXXGLPanel;
import com.bn.util.DateChooserJButton;
import com.bn.util.SocketUtil;
import com.sunking.swing.JDatePicker;

public class GRXWGLPanel extends JPanel
{
	//======================================个人新闻列表======================================
	//个人新闻每一列的类型
	Class[] typeArray={Integer.class,String.class,String.class,String.class,String.class,String.class,JButton.class,JButton.class};
	//角色表头
	String[] head={"新闻编号","新闻标题","新闻来源","提交时间","新闻状态","发布状态","编辑","删除"};
	//角色表格数据
	Object[][] tableData;
	
	JTable jtGRXW = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    
    GRXWTableModel tmGRXW;
    
    
    //存放表格的JScrollPane
	JScrollPane jspGRXW = new JScrollPane(jtGRXW);
	
	JLabel jlTitle =new JLabel("个人新闻列表");

	
	MainJFrame mf;
	String ygid;
	
	
	//=============================================筛选框==============================================
	//筛选
	JScrollPane jspSX = new JScrollPane(); 
	JLabel jlSX = new JLabel("请选择筛选条件：");
	JCheckBox jcbSHZT = new JCheckBox("审核状态");
	JLabel jlSHZT=new JLabel("请选择审核状态：");
	
	
	JCheckBox jcbWTJSH = new JCheckBox("未提交审核");
	JCheckBox jcbTJWSH = new JCheckBox("提交未审核");
	JCheckBox jcbWTGSH = new JCheckBox("未通过审核");
	JCheckBox jcbYTGSH = new JCheckBox("已通过审核");
	JCheckBox jcbFS = new JCheckBox("封杀");
	
	
	JCheckBox jcbFBZT = new JCheckBox("发布状态");
	JLabel jlFBZT=new JLabel("请选择发布状态：");
	JCheckBox jcbWFB = new JCheckBox("未发布");
	JCheckBox jcbYFB = new JCheckBox("已发布");
	JCheckBox jcbYGQ = new JCheckBox("已过期");

	
	
	
	
	JCheckBox jcbRQ = new JCheckBox("提交时间");
	JLabel jlTemp = new JLabel("请选择时间范围：");
	JLabel jlQSSJ = new JLabel("起始时间:");
	JLabel jlJZSJ = new JLabel("截止时间:");
	//起始日期
	DateChooserJButton dateChooserQSSJ = new DateChooserJButton();
	//截止日期
	DateChooserJButton dateChooserJZSJ = new DateChooserJButton();
	
	
	//检索  按钮
	JButton jbJS = new JButton("检索");
	//查看新闻相关审核记录  按钮
	JButton jbCKSH = new JButton("查看相关审核记录");
	//=============================================筛选框==============================================
	
	//记录表中上次点击的行
	int pressedRow=-1;
	
	public String filter=null;
	public GRXWGLPanel(MainJFrame mf,String ygid) 
	{
		this.ygid=ygid;
		this.mf=mf;
		this.setLayout(null);
		jbCKSH.setBounds(25, 17, 130, 23);
		
		//表头设置
		jspGRXW.setBounds(25, 50, 1135, 470);
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);

		this.add(jlTitle);
		this.add(jspGRXW);
		this.add(jbCKSH);
		initFilterPane();
		addFilterListeners();
		addTableListeners();
		addButtonListener();
		
		
	}
	
	
	//初始化员工表
	public void initTable()
	{
		//设置表头绘制器
		jtGRXW.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
		jtGRXW.setRowHeight(30);
		//设置只能单选
		jtGRXW.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtGRXW.setDefaultRenderer(Integer.class, dtcr);
		

		//为修改按钮和删除按钮添加绘制器
		ButtonRenderer jButtonRenderer=new ButtonRenderer();
		jtGRXW.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		

		//获得表头
		JTableHeader tableHeader = jtGRXW.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtGRXW.getColumnModel().getColumn(0);
		TableColumn tc1 = jtGRXW.getColumnModel().getColumn(1);
		TableColumn tc2 = jtGRXW.getColumnModel().getColumn(2);
		TableColumn tc3 = jtGRXW.getColumnModel().getColumn(3);
		TableColumn tc4 = jtGRXW.getColumnModel().getColumn(4);
		TableColumn tc5 = jtGRXW.getColumnModel().getColumn(5);
		TableColumn tc6 = jtGRXW.getColumnModel().getColumn(6);
		TableColumn tc7 = jtGRXW.getColumnModel().getColumn(7);

		
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(80);
		tc1.setPreferredWidth(220);
		tc2.setPreferredWidth(140);
		tc3.setCellRenderer(new JDateRenderer());
		tc3.setPreferredWidth(175);
		tc4.setPreferredWidth(140);

		//获得table单元格绘制器
		tc5.setPreferredWidth(140);
		
		//为修改按钮添加编辑器
		tc6.setPreferredWidth(120);
		XGButtonEditor xgButtonEidtor=new XGButtonEditor(this,mf);
		tc6.setCellEditor(xgButtonEidtor);
		//为删除按钮添加编辑器
		tc7.setPreferredWidth(120);
		SCButtonEditor scButtonEidtor=new SCButtonEditor(this);
		tc7.setCellEditor(scButtonEidtor);
		
		

		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		tc4.setResizable(false);
		tc5.setResizable(false);
		tc6.setResizable(false);
		tc7.setResizable(false);

		
	}
	
	
	//初始化筛选Panel
	public void initFilterPane()
	{
		this.add(jspSX);
		jspSX.setLayout(null);
		jspSX.setBounds(25, 540, 1135, 140);
		
		// "请选择筛选条件"
		jspSX.add(jlSX);
		jlSX.setBounds(5, 5, 100, 20);
		
		// "审核状态"
		jspSX.add(jcbSHZT);
		jcbSHZT.setBounds(130, 5, 100, 20);
		
		// "请选择审核状态："
		jspSX.add(jlSHZT);
		jlSHZT.setBounds(130, 30, 100, 20);
		
		// "提交未审核"
		jspSX.add(jcbWTJSH);
		jcbWTJSH.setBounds(130, 55, 100, 20);
		
		jspSX.add(jcbTJWSH);
		jcbTJWSH.setBounds(260, 55, 100, 20);
		
		jspSX.add(jcbWTGSH);
		jcbWTGSH.setBounds(390, 55, 100, 20);
		
		jspSX.add(jcbYTGSH);
		jcbYTGSH.setBounds(130, 80, 100, 20);
		
		jspSX.add(jcbFS);
		jcbFS.setBounds(260, 80, 100, 20);
		
		
		// "时间"
		jspSX.add(jcbRQ);
		jcbRQ.setBounds(500+30, 5, 100, 20);
		
		// "请选择时间范围："
		jspSX.add(jlTemp);
		jlTemp.setBounds(500+30, 30, 100, 20);
		
		// "起始时间:" 那一排
		jspSX.add(jlQSSJ);
		jlQSSJ.setBounds(500+30, 55, 60, 20);
		dateChooserQSSJ.setBounds(560+30, 55, 170, 20);
		//jdpQSRQ.setEditable(false);
		jspSX.add(dateChooserQSSJ);
		
		// "截止时间:" 那一排
		jspSX.add(jlJZSJ);
		jlJZSJ.setBounds(500+30, 80, 60, 20);
		dateChooserJZSJ.setBounds(560+30, 80, 170, 20);
		jspSX.add(dateChooserJZSJ);

		// "发布状态"
		jspSX.add(jcbFBZT);
		jcbFBZT.setBounds(830, 5, 100, 20);
		
		// "请选择发布状态："
		jspSX.add(jlFBZT);
		jlFBZT.setBounds(830, 30, 100, 20);
		
		// "未发布"
		jspSX.add(jcbWFB);
		jcbWFB.setBounds(830, 55, 100, 20);
		
		jspSX.add(jcbYFB);
		jcbYFB.setBounds(830+130, 55, 100, 20);
		
		jspSX.add(jcbYGQ);
		jcbYGQ.setBounds(830, 55+25, 100, 20);
		
		
		//默认是没有选中的状态，所有可选项不可用
		jlSHZT.setEnabled(false);
		jcbWTJSH.setEnabled(false);
		jcbTJWSH.setEnabled(false);
		jcbWTGSH.setEnabled(false);
		jcbYTGSH.setEnabled(false);
		jcbFS.setEnabled(false);
		jlTemp.setEnabled(false);
		jlQSSJ.setEnabled(false);
		jlJZSJ.setEnabled(false);
		dateChooserQSSJ.setEnabled(false);
		dateChooserJZSJ.setEnabled(false);	
		jlFBZT.setEnabled(false);
		jcbWFB.setEnabled(false);
		jcbYFB.setEnabled(false);
		jcbYGQ.setEnabled(false);
		
		
		// "检索"按钮
		jspSX.add(jbJS);
		//jbJS.setBounds(650+30, 110, 80, 20);
		jbJS.setBounds(830+130-25+3, 110, 80, 20);
		
	}
	
	/*
	 * 添加监听器方法
	 */
	public void addTableListeners()
	{//添加表格的监听器
		jtGRXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtGRXW.getSelectedRow();
					if(pressedRow!=row)
					{//如果点击的不在同一行
						pressedRow = row;
					}
				}
						
			}
		);
		
		jtGRXW.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//row实时记录单击的行
					final int row = jtGRXW.getSelectedRow();
					if(pressedRow!=row)
					{//如果点击的不在同一行
						pressedRow = row;
					}
			  }
			}
		);
		
	}
	
	public void addButtonListener()
	{
		jbCKSH.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(pressedRow==-1)
				{
					JOptionPane.showMessageDialog(GRXWGLPanel.this,"请选择一条新闻！","提示",JOptionPane.INFORMATION_MESSAGE);
				}else
				{
					String xwid=tableData[pressedRow][0].toString();
					mf.gotoCKSH(xwid);
				}	
			}
		});
	}
	
	public void addFilterListeners()
	{
		jcbSHZT.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jcbSHZT.isSelected())
					{
						jcbWTJSH.setEnabled(true);
						jlSHZT.setEnabled(true);
						jcbTJWSH.setEnabled(true);
						jcbWTGSH.setEnabled(true);
						jcbYTGSH.setEnabled(true);
						jcbFS.setEnabled(true);
						
					}else 
					{
						jcbWTJSH.setEnabled(false);
						jlSHZT.setEnabled(false);
						jcbTJWSH.setEnabled(false);
						jcbWTGSH.setEnabled(false);
						jcbYTGSH.setEnabled(false);
						jcbFS.setEnabled(false);
					}
				}
			}
		);
		
		jcbRQ.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jcbRQ.isSelected())
					{
						jlTemp.setEnabled(true);
						jlQSSJ.setEnabled(true);
						jlJZSJ.setEnabled(true);
						dateChooserQSSJ.setEnabled(true);
						dateChooserJZSJ.setEnabled(true);
						
					}else 
					{
						jlTemp.setEnabled(false);
						jlQSSJ.setEnabled(false);
						jlJZSJ.setEnabled(false);
						dateChooserQSSJ.setEnabled(false);
						dateChooserJZSJ.setEnabled(false);
					}
					
				}
			}
		);
		
		jcbFBZT.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jcbFBZT.isSelected())
					{
						jcbWFB.setEnabled(true);
						jlFBZT.setEnabled(true);
						jcbYFB.setEnabled(true);
						jcbYGQ.setEnabled(true);
			
					}else 
					{
						jcbWFB.setEnabled(false);
						jlFBZT.setEnabled(false);
						jcbYFB.setEnabled(false);
						jcbYGQ.setEnabled(false);
					}
				}
			}
		);
		
		jbJS.addActionListener
		(
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
				    final StringBuilder sb = new StringBuilder();
					sb.append(GET_GRXW_FILTER);
					//审核状态
					if(jcbSHZT.isSelected())
					{
						if(jcbWTJSH.isSelected())
						{
							sb.append("0,");
						}
						
						if(jcbTJWSH.isSelected())
						{
							sb.append("1,");
						}
						if(jcbWTGSH.isSelected())
						{
							sb.append("2,");
						}
						if(jcbYTGSH.isSelected())
						{
							sb.append("3,");
						}
						if(jcbFS.isSelected())
						{
							sb.append("4,");
						}
						if((!jcbWTJSH.isSelected())&&(!jcbTJWSH.isSelected())&&(!jcbWTGSH.isSelected())&&(!jcbYTGSH.isSelected())&&(!jcbFS.isSelected()))
						{
							sb.append("no");
						}
						sb.append("<->");
						
					}else
					{
						sb.append("no");
						sb.append("<->");
					}
					if(jcbRQ.isSelected())
					{
						String qssj =dateChooserQSSJ.getText() ;  
						String jzsj =dateChooserJZSJ.getText();    
						sb.append(qssj);
						sb.append(",");
						sb.append(jzsj);
					}else
					{
						sb.append("no");
					}
					sb.append("<->");
					//发布状态
					if(jcbFBZT.isSelected())
					{
						if(jcbWFB.isSelected())
						{
							sb.append("0,");
						}
						
						if(jcbYFB.isSelected())
						{
							sb.append("1,");
						}
						if(jcbYGQ.isSelected())
						{
							sb.append("2,");
						}
						if((!jcbWFB.isSelected())&&(!jcbYFB.isSelected())&&(!jcbYGQ.isSelected()))
						{
							sb.append("no");
						}
						sb.append("<->");
						
					}else
					{
						sb.append("no");
						sb.append("<->");
					}
					sb.append(ygid);
					sb.append(GET_GRXW_FILTER);
					filter=sb.toString();
					System.out.println("++++++++:"+filter);
					dataGeted=false;
					new Thread()
					{
						public void run()
						{
							flushDataFilter(filter);
							dataGeted=true;
						}
					}.start();
					//监视线程
					LoginWindow.watchThread();
				}
			}
		);
	}
	
	
	

	/*
	 * 数据更新方法
	 */
	
	//获得个人新闻的方法
	public void flushData() 
	{
			//发送消息获得数据
			String msg = GET_NEW;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(ygid);
			sb.append(msg);
			String result =SocketUtil.sendAndGetMsg(sb.toString());//"<#GET_NEW#>ygid<->ztid<#GET_NEW#>"
			final List<String[]> list = SocketUtil.strToList(result);
			try {
				SwingUtilities.invokeAndWait
				(
				     new Runnable()
				     {
				    	 public void run()
				    	 {
				    		 int colCount=0;
				 			 int rowCount = list.size();
				    		 if(rowCount!=0)
				    		 {
				    			colCount = list.get(0).length; 
				    		 }
				 			//更新表格模型
				 			tmGRXW=new GRXWTableModel(GRXWGLPanel.this);
				 			//初始化表格数据
				 			tableData = new Object[rowCount][colCount+2];		
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 				   tableData[i][j]=list.get(i)[j];		
				 				}
				 				tableData[i][colCount]=new JButton();
				 				tableData[i][colCount+1]=new JButton();
				 			}
				 			//设置表格模型
				 			jtGRXW.setModel(tmGRXW);	
				 			//更新表格外观
				 			initTable();
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//筛选之后更新审核记录表数据的方法
	public void flushDataFilter(String sb) 
	{
			//发送消息获得数据
			String result =SocketUtil.sendAndGetMsg(sb);
			final List<String[]> list = SocketUtil.strToList(result);
			try {
				SwingUtilities.invokeAndWait
				(
				     new Runnable()
				     {
				    	 public void run()
				    	 {
				    		 int colCount=0;
				 			 int rowCount = list.size();
				    		 if(rowCount!=0)
				    		 {
				    			colCount = list.get(0).length; 
				    		 }
				 			//更新表格模型
				 			tmGRXW=new GRXWTableModel(GRXWGLPanel.this);
				 			//初始化表格数据
				 			tableData = new Object[rowCount][colCount+2];		
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					if(list.get(i)[j].equals("null"))
				 					{
				 						tableData[i][j]="暂无数据";
				 					}else
				 					{
				 						tableData[i][j]=list.get(i)[j];
				 					}	
				 				}
				 				tableData[i][colCount]=new JButton();
				 				tableData[i][colCount+1]=new JButton();
				 			}
				 			//设置表格模型
				 			jtGRXW.setModel(tmGRXW);	
				 			//更新表格外观
				 			initTable();
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
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
