package com.bn.jm.jbqxck;

import static com.bn.core.Constant.GET_JBQX;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.subtitle;


import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Vector;

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

import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.jm.jbqxck.JBQXCKPanel;
import com.bn.util.SocketUtil;
import com.bn.jm.jbqxck.JBQXTableModel;
import com.bn.jm.tableheader.GroupableTableHeader;
/*
 * 基本权限查看
 */
@SuppressWarnings("serial")
public class JBQXCKPanel extends JPanel
{
	//权限表格每一列的类型
	@SuppressWarnings("rawtypes")
	Class[] typeArrayQX={Integer.class,String.class};
	//权限表头
	String[] headQX={"权限ID","权限名称"};
	//角色表格数据
	Vector<String[]> tableDataQX;
	Vector<String[]> origindataQX;
    //子定义的表格数据模型
	JBQXTableModel tmQX;
	
	
	
	//权限表头
	JLabel jlQXTableHead = new JLabel("基本权限表");
	//权限表格
	JTable jtQX = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    //存放表格的JScrollPane
	JScrollPane jspQX = new JScrollPane(jtQX);
	
	
	public JBQXCKPanel()
	{
		this.setLayout(null);
		//表头标签
		jlQXTableHead.setBounds(228, 10, 120, 30);
		jlQXTableHead.setFont(subtitle); 
		this.add(jlQXTableHead);
		//设置表格
		jspQX.setBounds(25,40,496,600);
		this.add(jspQX);	
	}
	
	public void initTableQX()
	{
		//设置行高
        jtQX.setRowHeight(30);
		//设置只能单选
        jtQX.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtQX.setDefaultRenderer(Integer.class, dtcr);
		
		
		//设置表头绘制器
        jtQX.getTableHeader().setUI(new GroupableTableHeaderUI());
		//获得表头
		JTableHeader tableHeader = jtQX.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		//获得每一列的引用
		TableColumn tc0 = jtQX.getColumnModel().getColumn(0);
		TableColumn tc1 = jtQX.getColumnModel().getColumn(1);
		//设置每一列宽度
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(155);
		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
	}

	public void flushDataJSQX() 
	{
		//发送消息获得数据
		String msg = GET_JBQX;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(0);
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
			 			int rowCount = list.size();
			 			//更新表格模型
			 			tmQX=new JBQXTableModel(JBQXCKPanel.this);
			 			//初始化表格数据
			 			tableDataQX = new Vector<String[]>();		
			 			//将表中的数据进行一下暂时的备份  
			 			//作用是 ：当点"修改"按钮时 判断当前表格内数据 与  这个备份的数据 是否相同  相同的话 提示相关信息
			 			origindataQX =  new Vector<String[]>();	
			 			//通过循环将数据库中数据
			 			for(int i=0;i<rowCount;i++)
			 			{
			 				tableDataQX.add(list.get(i));
			 				origindataQX.add(list.get(i));
			 			}
			 			//设置表格模型
			 			jtQX.setModel(tmQX);	
			 			//更新表格外观
			 			initTableQX();		 			
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
	
	
	/*
	 * 表格显示步骤：
	 * 1.在JScrollPane中添加JTable，在Panel中添加JScrollPane。
	 * 2.通过flushDataXXX()方法从服务器获得数据，并将数据放入XXXTableModel中，
	 *   最后JTable.setModel(XXXTableModel);
	 * 3.通过initTableQX()方法更新表格外观
	 * 
	 */
	
	

}
