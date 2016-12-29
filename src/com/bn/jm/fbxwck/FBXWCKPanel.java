package com.bn.jm.fbxwck;

import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_LM;
import static com.bn.core.Constant.GET_LM_FB_NEW;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.jltitle;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
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
import com.bn.util.SocketUtil;

public class FBXWCKPanel extends JPanel
{
	private JLabel jlTitle=new JLabel("发布新闻查看");
	private JLabel jlLM=new JLabel("栏目列表");
	private JLabel jlLMXW=new JLabel("栏目所含新闻");
	
	
	JTable jtLM = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
	JScrollPane jsLM=new JScrollPane(jtLM);
	
	LMTableModel tmLM;
	
	
	
	JTable jtLMXW = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
	JScrollPane jsLMXW=new JScrollPane(jtLMXW);
	
	LMXWTableModel tmLMXW;
	
	
	//记录栏目表中上次点击的行
	int pressedRowInLM=-1;
    //记录栏目新闻表中上次点击的行
	int pressedRowInLMXW=-1;
	
	int lmid;
	int xwid;
	
	
	MainJFrame mf;
	
	public FBXWCKPanel(MainJFrame mf) 
	{
		this.mf=mf;	
		this.setLayout(null);
		//表头设置
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);
		
		
		//栏目表
		this.add(jlLM);
		jlLM.setBounds(25, 15+30+5, 120, 20);
		jlLM.setFont(jltitle);
		this.add(jsLM);
		jsLM.setBounds(25, 70+10, 280, 580);
		
		
		//栏目所含新闻表
		this.add(jlLMXW);
		jlLMXW.setBounds(365-10, 15+30+5, 120, 20);
		jlLMXW.setFont(jltitle);
		this.add(jsLMXW);
		jsLMXW.setBounds(365-10, 70+10, 780+20, 580);
		//280  780
		
		addTableListeners();
		
	}
	
	
	//初始化表格
	public void initTable(JTable jtLM)
	{
		//设置表头绘制器
		jtLM.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
		jtLM.setRowHeight(30);
		//设置只能单选
		jtLM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtLM.setDefaultRenderer(Integer.class, dtcr);
		

		//获得表头
		JTableHeader tableHeader = jtLM.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtLM.getColumnModel().getColumn(0);
		TableColumn tc1 = jtLM.getColumnModel().getColumn(1);
		TableColumn tc2 = jtLM.getColumnModel().getColumn(2);
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(50);
		tc1.setPreferredWidth(50);
		tc2.setPreferredWidth(160);
		
		
		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
	}
	
	
	
	
	//初始化表格
	public void initTableLMXW(JTable jtLM)
	{
		//设置表头绘制器
		jtLM.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
		jtLM.setRowHeight(30);
		//设置只能单选
		jtLM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtLM.setDefaultRenderer(Integer.class, dtcr);
		

		//获得表头
		JTableHeader tableHeader = jtLM.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtLM.getColumnModel().getColumn(0);
		TableColumn tc1 = jtLM.getColumnModel().getColumn(1);
		TableColumn tc2 = jtLM.getColumnModel().getColumn(2);
		TableColumn tc3 = jtLM.getColumnModel().getColumn(3);
		TableColumn tc4 = jtLM.getColumnModel().getColumn(4);
		TableColumn tc5 = jtLM.getColumnModel().getColumn(5);
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(50);
		tc1.setPreferredWidth(50);
		tc2.setPreferredWidth(160);
		tc3.setPreferredWidth(290+20);
		tc4.setPreferredWidth(130);
		tc4.setCellRenderer(new JDateRenderer());
		tc5.setPreferredWidth(80);
		
		
		
		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		tc4.setResizable(false);
		tc5.setResizable(false);
	}
	
	
	public void addTableListeners()
	{//添加表格的监听器
		jtLM.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtLM.getSelectedRow();
					if(pressedRowInLM!=row)
					{//如果点击的不在同一行
						pressedRowInLM = row;
						lmid=Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString());
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataLMXW(Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString()));
								dataGeted=true;
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();
					}
				}
				
			}
		);
		
		
		//给栏目新闻添加表格监听器
		jtLMXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtLMXW.getSelectedRow();
					if(pressedRowInLMXW!=row)
					{//如果点击的不在同一行
						pressedRowInLMXW = row;
					}
				}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					if(e.getClickCount()==2)
					{
						String xwid=tmLMXW.tableData[pressedRowInLMXW][1].toString();
						mf.gotoXWCK(xwid, false);	
					}			
				}
			}
		);
		
		
		
		
		
	}
	
	
	
	/*
	 * 数据更新方法
	 */
	
	//更新栏目表数据的方法
	public void flushDataLM() 
	{
			//发送消息获得数据
			String msg = GET_LM;
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
				 			tmLM=new LMTableModel(FBXWCKPanel.this);
				 			//初始化表格数据
				 			tmLM.tableData = new Object[rowCount][colCount];
				 			//tmLM.OritableData = new Object[rowCount][colCount];
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLM.tableData[i][j]=list.get(i)[j];
				 					//tmLM.OritableData[i][j]=list.get(i)[j];
				 				}
				 			}
				 			//设置表格模型
				 			jtLM.setModel(tmLM);	
				 			//更新表格外观
				 			initTable(jtLM);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//更新栏目所含新闻表数据的方法
	public void flushDataLMXW(int lmid) 
	{
			//发送消息获得数据
			String msg = GET_LM_FB_NEW;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(lmid);
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
				 			tmLMXW=new LMXWTableModel(FBXWCKPanel.this);
				 			//初始化表格数据
				 			tmLMXW.tableData = new Object[rowCount][colCount];
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLMXW.tableData[i][j]=list.get(i)[j];
				 					
				 				}
				 			}
				 			//设置表格模型
				 			jtLMXW.setModel(tmLMXW);	
				 			//更新表格外观
				 			initTableLMXW(jtLMXW);
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
