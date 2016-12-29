package com.bn.jm.shgl;


import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_SHJL;
import static com.bn.core.Constant.GET_SHJL_FILTER;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.dataGeted;


import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;


import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.util.DateChooserJButton;
import com.bn.util.SocketUtil;
import com.sunking.swing.JDatePicker;

public class SHGLPanel extends JPanel
{
	//======================================个人新闻列表======================================
	//个人新闻每一列的类型
	Class[] typeArray={Integer.class,Integer.class,String.class,String.class,String.class,String.class,String.class,String.class,JButton.class,JButton.class};
	//角色表头
	String[] head={"审核编号","新闻编号","新闻标题","提交人","提交时间","审核人","审核时间","审核状态","查看新闻","审核"};
	//角色表格数据
	Object[][] tableData;
	
	JTable jtSHJL = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    
    SHJLTableModel tmSHJL;
    
    
    //存放表格的JScrollPane
	JScrollPane jspSHJL = new JScrollPane(jtSHJL);
	
	JLabel jlTitle =new JLabel("审核记录列表");
	
	MainJFrame mf;
	
	//=============================================筛选框==============================================
	//筛选
	JScrollPane jspSX = new JScrollPane(); 
	JLabel jlSX = new JLabel("请选择筛选条件：");
	JCheckBox jcbSHZT = new JCheckBox("审核状态");
	JLabel jlSHZT=new JLabel("请选择审核状态：");
	
	
	JCheckBox jcbTJWSH = new JCheckBox("提交未审核");
	JCheckBox jcbWTGSH = new JCheckBox("未通过审核");
	JCheckBox jcbYTGSH = new JCheckBox("已通过审核");
	JCheckBox jcbFS = new JCheckBox("封杀");

	
	
	
	
	JCheckBox jcbRQ = new JCheckBox("提交时间");
	JLabel jlTemp = new JLabel("请选择时间范围：");
	JLabel jlQSRQ = new JLabel("起始时间:");
	JLabel jlJZRQ = new JLabel("截止时间:");
	//起始时间
	DateChooserJButton dateChooserQSSJ=new DateChooserJButton();
	//截止时间
	DateChooserJButton dateChooserJZSJ=new DateChooserJButton();
	
	
	//根据新闻id筛选
	JCheckBox jcbXWBH = new JCheckBox("新闻编号");
	JLabel jlXWBH=new JLabel("请输入新闻编号：");
	JLabel jlXWID=new JLabel("新闻编号：");
	JTextField jtfXWBH=new JTextField(1);
	
	
	//检索  按钮
	JButton jbJS = new JButton("检索");
	//=============================================筛选框==============================================
	
	
	public String filter=null;
	public SHGLPanel(MainJFrame mf) 
	{
		this.mf=mf;
		this.setLayout(null);
		//表头设置
		jspSHJL.setBounds(25, 50, 1135, 470);
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);
		this.add(jspSHJL);
		initFilterPane();
		addFilterListeners();
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
		jspSX.add(jcbTJWSH);
		jcbTJWSH.setBounds(130, 55, 100, 20);
		jspSX.add(jcbWTGSH);
		jcbWTGSH.setBounds(260, 55, 100, 20);
		
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
		jspSX.add(jlQSRQ);
		jlQSRQ.setBounds(500+30, 55, 60, 20);
		dateChooserQSSJ.setBounds(560+30, 55, 170, 20);
		jspSX.add(dateChooserQSSJ);
		
		// "截止时间:" 那一排
		jspSX.add(jlJZRQ);
		jlJZRQ.setBounds(500+30, 80, 60, 20);
		dateChooserJZSJ.setBounds(560+30, 80, 170, 20);
		jspSX.add(dateChooserJZSJ);

		// "新闻编号单选按钮"
		jspSX.add(jcbXWBH);
		jcbXWBH.setBounds(830, 5, 100, 20);
		
		// "请输入新闻编号："
		jspSX.add(jlXWBH);
		jlXWBH.setBounds(830, 30, 100, 20);
		
		// "输入新闻编号"
		jspSX.add(jlXWID);
		jlXWID.setBounds(830, 55, 100, 20);
		jspSX.add(jtfXWBH);
		jtfXWBH.setBounds(890, 55, 130, 20);
		
		
		//默认是没有选中的状态，所有可选项不可用
		jlSHZT.setEnabled(false);
		jcbTJWSH.setEnabled(false);
		jcbWTGSH.setEnabled(false);
		jcbYTGSH.setEnabled(false);
		jcbFS.setEnabled(false);
		jlTemp.setEnabled(false);
		jlQSRQ.setEnabled(false);
		jlJZRQ.setEnabled(false);
		dateChooserQSSJ.setEnabled(false);
		dateChooserJZSJ.setEnabled(false);	
		jlXWBH.setEnabled(false);
		jtfXWBH.setEnabled(false);
		jlXWID.setEnabled(false);
		
		// "检索"按钮
		jspSX.add(jbJS);
		jbJS.setBounds(830+130-25+3, 110, 80, 20);
		
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
						jlSHZT.setEnabled(true);
						jcbTJWSH.setEnabled(true);
						jcbWTGSH.setEnabled(true);
						jcbYTGSH.setEnabled(true);
						jcbFS.setEnabled(true);
						
					}else 
					{
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
						jlQSRQ.setEnabled(true);
						jlJZRQ.setEnabled(true);
						dateChooserQSSJ.setEnabled(true);
						dateChooserJZSJ.setEnabled(true);
						
					}else 
					{
						jlTemp.setEnabled(false);
						jlQSRQ.setEnabled(false);
						jlJZRQ.setEnabled(false);
						dateChooserQSSJ.setEnabled(false);
						dateChooserJZSJ.setEnabled(false);	
					}
					
				}
			}
		);
		
		jcbXWBH.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0) 
				{
					if(jcbXWBH.isSelected())
					{
						jlXWBH.setEnabled(true);
						jtfXWBH.setEnabled(true);
						jlXWID.setEnabled(true);
						
					}else 
					{
						jlXWBH.setEnabled(false);
						jtfXWBH.setEnabled(false);
						jlXWID.setEnabled(false);
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
					sb.append(GET_SHJL_FILTER);
					//审核状态
					if(jcbSHZT.isSelected())
					{
						
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
						if((!jcbTJWSH.isSelected())&&(!jcbWTGSH.isSelected())&&(!jcbYTGSH.isSelected())&&(!jcbFS.isSelected()))
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
						String qssj=dateChooserQSSJ.getText();
						String jzsj=dateChooserJZSJ.getText();
						sb.append(qssj);
						sb.append(",");
						sb.append(jzsj);
						sb.append("<->");
					}else
					{
						sb.append("no");
						sb.append("<->");
					}
					if(jcbXWBH.isSelected())
					{
						String xwbh=SHGLPanel.this.jtfXWBH.getText().trim();
						if(xwbh.length()<=0)
						{
							JOptionPane.showMessageDialog(SHGLPanel.this,"请输入新闻编号！", "提示",JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if(!xwbh.matches("\\d+"))
						{
							JOptionPane.showMessageDialog(SHGLPanel.this,"请确保您输入的是整数！", "提示",JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						sb.append(xwbh);
					}else
					{
						sb.append("no");
					}
					sb.append(GET_SHJL_FILTER);
					filter=sb.toString();
					System.out.println(sb.toString());
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
	
	//初始化审核记录表
	public void initTable()
	{
		//设置表头绘制器
		jtSHJL.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
		jtSHJL.setRowHeight(30);
		//设置只能单选
		jtSHJL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtSHJL.setDefaultRenderer(Integer.class, dtcr);
		

		//为修改按钮和删除按钮添加绘制器
		SHJLButtonRenderer jButtonRenderer=new SHJLButtonRenderer();
		jtSHJL.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		//jtGRXW.setDefaultEditor(JButton.class, ygButtonEidtor);
		

		//获得表头
		JTableHeader tableHeader = jtSHJL.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtSHJL.getColumnModel().getColumn(0);
		TableColumn tc1 = jtSHJL.getColumnModel().getColumn(1);
		TableColumn tc2 = jtSHJL.getColumnModel().getColumn(2);
		TableColumn tc3 = jtSHJL.getColumnModel().getColumn(3);
		TableColumn tc4 = jtSHJL.getColumnModel().getColumn(4);
		TableColumn tc5 = jtSHJL.getColumnModel().getColumn(5);
		TableColumn tc6 = jtSHJL.getColumnModel().getColumn(6);
		TableColumn tc7 = jtSHJL.getColumnModel().getColumn(7);
		TableColumn tc8 = jtSHJL.getColumnModel().getColumn(8);
		TableColumn tc9 = jtSHJL.getColumnModel().getColumn(9);
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(60);
		tc2.setPreferredWidth(175);
		tc3.setPreferredWidth(100);
		tc4.setPreferredWidth(140);
		tc4.setCellRenderer(new JDateRenderer());
		
		
		tc5.setPreferredWidth(100);
		//获得table单元格绘制器
		tc6.setPreferredWidth(140);
		tc6.setCellRenderer(new JDateRenderer());
		tc7.setPreferredWidth(120);
		
		
		tc8.setPreferredWidth(120);
		CKButtonEditor ckButtonEidtor=new CKButtonEditor(this,mf);
		tc8.setCellEditor(ckButtonEidtor);
		
		tc9.setPreferredWidth(120);
		SHButtonEditor shButtonEidtor=new SHButtonEditor(this,mf);
		tc9.setCellEditor(shButtonEidtor);
		
		

		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		tc4.setResizable(false);
		tc5.setResizable(false);
		tc6.setResizable(false);
		tc7.setResizable(false);
		tc8.setResizable(false);
		tc9.setResizable(false);
		
	}
	
	
	

	/*
	 * 数据更新方法
	 */
	
	//更新审核记录表数据的方法
	public void flushData() 
	{
			//发送消息获得数据
			String msg = GET_SHJL;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(msg);
			String result =SocketUtil.sendAndGetMsg(sb.toString());
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
				 			tmSHJL=new SHJLTableModel(SHGLPanel.this);
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
				 			jtSHJL.setModel(tmSHJL);	
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
				 			tmSHJL=new SHJLTableModel(SHGLPanel.this);
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
				 			jtSHJL.setModel(tmSHJL);	
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
