package com.bn.jm.grxwgl;

import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_SHJL_BY_XWID;
import static com.bn.core.Constant.subtitle;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
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
import com.bn.jm.MainJFrame;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.util.SocketUtil;

public class CKSHPanel extends JPanel
{
	
	//======================================查看新闻审核记录列表======================================
	//个人新闻每一列的类型
	Class[] typeArray={Integer.class,String.class,String.class,String.class};
	//角色表头
	String[] head={"审核编号","审核人","审核时间","审核意见"};
	//角色表格数据
	Object[][] tableData;
	
	JTable jtCKSH = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    
    CKSHTableModel tmCKSH;
    
    
    //存放表格的JScrollPane
	JScrollPane jspSHJL = new JScrollPane(jtCKSH);
	
	JLabel jlTitle =new JLabel("此条新闻相关审核记录列表");

	MainJFrame mf;
	
	JButton jbBack=new JButton("返回");
	
	
	public CKSHPanel(final MainJFrame mf) 
	{
		this.mf=mf;
		this.setLayout(null);
		//表头设置
		jbBack.setBounds(25, 20, 80,23);
		jspSHJL.setBounds(25, 50, 1135, 600);
		jlTitle.setBounds(480, 20, 200, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);
		jbBack.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
			   mf.gotoBackGRXWGL(false);	
			}
			
		});
		this.add(jbBack);
		this.add(jspSHJL);
		
		
		
		
	}
	
	//初始化审核记录表
	public void initTable()
	{
		//设置表头绘制器
		jtCKSH.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
		jtCKSH.setRowHeight(30);
		//设置只能单选
		jtCKSH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtCKSH.setDefaultRenderer(Integer.class, dtcr);
		

		//为修改按钮和删除按钮添加绘制器
		//SHJLButtonRenderer jButtonRenderer=new SHJLButtonRenderer();
		//jtSHJL.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		//jtGRXW.setDefaultEditor(JButton.class, ygButtonEidtor);
		

		//获得表头
		JTableHeader tableHeader = jtCKSH.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtCKSH.getColumnModel().getColumn(0);
		TableColumn tc1 = jtCKSH.getColumnModel().getColumn(1);
		TableColumn tc2 = jtCKSH.getColumnModel().getColumn(2);
		TableColumn tc3 = jtCKSH.getColumnModel().getColumn(3);
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(100);
		tc1.setPreferredWidth(160);
		tc2.setPreferredWidth(160);
		tc2.setCellRenderer(new JDateRenderer());
		tc3.setPreferredWidth(710);
		
		
		
		

		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		
	}
	
	
	

	/*
	 * 数据更新方法
	 */
	
	//更新审核记录表数据的方法
	public void flushData(String xwid) 
	{
			//发送消息获得数据
			String msg = GET_SHJL_BY_XWID;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(xwid);
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
				 			tmCKSH=new CKSHTableModel(CKSHPanel.this);
				 			//初始化表格数据
				 			tableData = new Object[rowCount][colCount];		
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
			
				 			}
				 			//设置表格模型
				 			jtCKSH.setModel(tmCKSH);	
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
