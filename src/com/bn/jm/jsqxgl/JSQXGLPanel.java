package com.bn.jm.jsqxgl;



import static com.bn.core.Constant.SC_QX;
import static com.bn.core.Constant.ADD_JSQX_BY_QXMC;
import static com.bn.core.Constant.GET_BJYDQX;
import static com.bn.core.Constant.SC_JS;
import static com.bn.core.Constant.XG_JS;
import static com.bn.core.Constant.GET_JBQX;
import static com.bn.core.Constant.GET_JS;
import static com.bn.core.Constant.ADD_JS;

import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.subtitle;




import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.bn.jm.jsqxgl.QXTableModel;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.util.SocketUtil;
import com.bn.jm.jsqxgl.JSTableModel;
import com.bn.jm.tableheader.GroupableTableHeader;

@SuppressWarnings({"serial","rawtypes"})
public class JSQXGLPanel extends JPanel
{
	//======================================角色表======================================
	//角色表格每一列的类型
	Class[] typeArrayJS={Integer.class,String.class};
	//角色表头
	String[] headJS={"角色ID","角色名称"};
	//角色表格数据
	Vector<String[]> tableDataJS;
	Vector<String[]> origindataJS;
	
	JSTableModel tmJS;
	//角色表格
	JTable jtJS = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    //存放表格的JScrollPane
	JScrollPane jspJS = new JScrollPane(jtJS);

	//角色表头
	JLabel jlJSTableHead = new JLabel("角色表");
	
	JButton jbAddJS = new JButton("添加角色");
	JButton jbDeleteJS = new JButton("删除角色");
	
	//记录角色表中上次点击的行
	int pressedRowInJS=-1;
	//记录单元格数据是否改变
	boolean isDataChanged=false;
	//记录刚刚编辑了的行
    int lastEditRowInJS=-1;
	//======================================角色表======================================
	
	
	//======================================权限表======================================

	//权限表格每一列的类型
	Class[] typeArrayQX={Integer.class,String.class};
	//权限表头
	String[] headQX={"权限ID","权限名称"};
	//角色表格数据
	Vector<String[]> tableDataQX;
	Vector<String[]> origindataQX;
	
	QXTableModel tmQX;
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

	//权限表头
	JLabel jlQXTableHead = new JLabel("权限表");

	JButton jbAddJBQX = new JButton("添加权限");
	JComboBox jcbTJQX = new JComboBox();
    Object[] message = { "请选择添加权限", new JScrollPane(jcbTJQX)};
    JOptionPane paneTJQX = new JOptionPane(message,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
    JDialog dialogTJQX = paneTJQX.createDialog(LoginWindow.mf, "添加权限");
	JButton jbDeleteJBQX = new JButton("删除权限");
	//======================================权限表======================================
	
	
	
	
	
	
	
	
	public JSQXGLPanel()
	{
		this.setLayout(null);	

		//======================================角色表======================================
		jspJS.setBounds(25,40, 496, 264);
		//表头设置
		jlJSTableHead.setBounds(250, 10, 120, 30);
		jlJSTableHead.setFont(subtitle); 
		//按钮设置
		jbAddJS.setBounds(530, 60, 80, 30);
		jbAddJS.setOpaque(false);
		jbDeleteJS.setBounds(530, 120, 80, 30);
		jbDeleteJS.setOpaque(false);
		//======================================角色表======================================
		
		
		//======================================权限表======================================
		jspQX.setBounds(25,340, 496, 264);
		//表头设置
		jlQXTableHead.setBounds(250, 310, 120, 30);
		jlQXTableHead.setFont(subtitle); 
		//按钮设置
		jbAddJBQX.setBounds(530, 360, 80, 30);
		jbAddJBQX.setOpaque(false);
		jbDeleteJBQX.setBounds(530, 420, 80, 30);
		jbDeleteJBQX.setOpaque(false);
		//======================================权限表======================================
		addTableListeners();
		addButtonListeners();
		
		this.add(jbAddJS);
		this.add(jbDeleteJS);
		this.add(jspJS);
		this.add(jlJSTableHead);
		
		this.add(jbAddJBQX);
		this.add(jbDeleteJBQX);
		this.add(jspQX);
		this.add(jlQXTableHead);
	}
	
	
	//初始化角色表
	public void initTableJS()
	{
		//设置表头绘制器
        jtJS.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
        jtJS.setRowHeight(30);
		//设置只能单选
        jtJS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtJS.setDefaultRenderer(Integer.class, dtcr);

		//获得表头
		JTableHeader tableHeader = jtJS.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtJS.getColumnModel().getColumn(0);
		TableColumn tc1 = jtJS.getColumnModel().getColumn(1);
		
		//设置每一列宽度
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(155);

		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
	}
	
	//初始化权限表
	public void initTableQX()
	{
		//设置表头绘制器
        jtQX.getTableHeader().setUI(new GroupableTableHeaderUI());
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
	

	/*
	 * 添加监听器方法
	 */
	public void addTableListeners()
	{//添加表格的监听器
		jtJS.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtJS.getSelectedRow();
					if(pressedRowInJS!=row)
					{//如果点击的不在同一行
						pressedRowInJS = row;
						if(isDataChanged)//如果数据改变了  问是否保存数据
						{
							isDataChanged=false;
							if(tableDataJS.get(lastEditRowInJS)[1].equals(""))
							{
								JOptionPane.showMessageDialog(JSQXGLPanel.this, "角色名称不能为空！", "提示",JOptionPane.INFORMATION_MESSAGE);
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
								return;
							}
							int i = JOptionPane.showConfirmDialog(JSQXGLPanel.this, "是否保存对角色表的修改?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//如果点"是"
							{
								xgJS(tableDataJS.get(lastEditRowInJS)[0],tableDataJS.get(lastEditRowInJS)[1]);
							}else
							{//如果点"否"或者"×"
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
							}
						}
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataJSQX(Integer.parseInt(tableDataJS.get(row)[0]));
								dataGeted=true;
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();
					}
				}
			}
		);
		
		jtJS.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//row实时记录单击的行
					final int row = jtJS.getSelectedRow();
					if(pressedRowInJS!=row)
					{//如果点击的不在同一行
						pressedRowInJS = row;
						if(isDataChanged)//如果数据改变了  问是否保存数据
						{
							isDataChanged=false;
							if(tableDataJS.get(lastEditRowInJS)[1].equals(""))
							{
								JOptionPane.showMessageDialog(JSQXGLPanel.this, "角色名称不能为空！", "提示",JOptionPane.INFORMATION_MESSAGE);
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
								return;
							}
							int i = JOptionPane.showConfirmDialog(JSQXGLPanel.this, "是否保存对角色表的修改?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//如果点"是"
							{
								xgJS(tableDataJS.get(lastEditRowInJS)[0],tableDataJS.get(lastEditRowInJS)[1]);
							}else
							{//如果点"否"或者"×"
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
							}
						}
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataJSQX(Integer.parseInt(tableDataJS.get(row)[0]));
								dataGeted=true;
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();
					}
				}
			}
		);
		
	}
	
	public void addButtonListeners()
	{//添加各种Button的监听器
		jbAddJS.addActionListener
		(//添加角色
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					addJS();
				}
			}
		);
		jbDeleteJS.addActionListener
		(//删除角色
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int row = jtJS.getSelectedRow();
					if(row!=-1)
					{
						scJS(tableDataJS.get(row)[0]);//删除角色
					}
				}
			}
		);
		jbAddJBQX.addActionListener
		(//添加基本权限
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int rowJS = jtJS.getSelectedRow();
					if(rowJS==-1)
					{//默认的角色表选择的是第一行
						rowJS=0;
					}
					String jsid = tableDataJS.get(rowJS)[0];
					//添加当前角色不具有的权限
					add_bjydqx(jsid);
				}
			}
		);
		jbDeleteJBQX.addActionListener
		(//删除基本权限
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int rowJS = jtJS.getSelectedRow();
					int rowQX = jtQX.getSelectedRow();
					if(rowJS==-1)
					{
						JOptionPane.showMessageDialog(JSQXGLPanel.this, "请选择某种角色！", "提示", JOptionPane.NO_OPTION);
						return;
					}
					if(rowQX==-1)
					{
						JOptionPane.showMessageDialog(JSQXGLPanel.this, "请选择某种权限！", "提示", JOptionPane.NO_OPTION);
						return;
					}
					String jsid = tableDataJS.get(rowJS)[0];
					String qxid = tableDataQX.get(rowQX)[0];
					scQX(jsid,qxid);//删除某角色对应的权限
				}
			}
		);
		
		
		
	}
	
	
	/*
	 * 修改服务器数据方法
	 */
	//修改角色名称
	public void xgJS(final String jsid,final String jsmc)//角色ID  角色名称
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = XG_JS;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append("<->");
				sb.append(jsmc);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "修改成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataJS();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "网络故障，修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	//添加角色
	public void addJS()
	{
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = ADD_JS;//添加角色
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				if(result.equals("ok"))
				{
					flushDataJS();
				}
				else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "网络故障，添加失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
				dataGeted=true;
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	//删除角色
	//判断该角色下有没有员工  
	public void scJS(final String jsid)//参数是 角色ID
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = SC_JS;//能否删除该角色
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.equals("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "角色删除成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataJS();
					dataGeted=true;
					flushDataJSQX(1000);
					
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "该角色下还有员工，删除失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	//添加权限(添加不具有的)
	public void add_bjydqx(final String jsid)
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = GET_BJYDQX;//获得不具有权限
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				List<String[]> list= SocketUtil.strToList(result);
				dataGeted=true;
				if(list.size()==0)
				{//如果当前角色具有所有权限
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "当前员工已具有所有权限！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				//将不具有的权限放进下拉列表
				jcbTJQX.removeAllItems();
				for(int i=0;i<list.size();i++)
				{
					jcbTJQX.addItem(list.get(i)[1]);
				}
				dialogTJQX.setVisible(true);
				dialogTJQX.setAlwaysOnTop(true);
				if(paneTJQX.getValue()!=null)//返回用户所选值。 null 表示用户没有选取任何项便关闭了窗口。否则，返回值将为在此对象中所定义的选项之一。
				{//如果没点 ×
					if(Integer.parseInt(paneTJQX.getValue().toString())==0)
					{//如果点击 "确定"
						String selectedQX = jcbTJQX.getSelectedItem().toString();
						//添加选择的权限
						msg = ADD_JSQX_BY_QXMC;//添加权限
						sb = new StringBuilder();
						sb.append(msg);
						sb.append(jsid);
						sb.append("<->");
						sb.append(selectedQX);
						sb.append(msg);
						dataGeted=false;
						LoginWindow.watchThread();
						result =SocketUtil.sendAndGetMsg(sb.toString());
						dataGeted=true;
						if(result.equals("ok"))
						{
							JOptionPane.showMessageDialog(JSQXGLPanel.this, "权限添加成功！", "提示", JOptionPane.NO_OPTION);
							dataGeted=false;
							LoginWindow.watchThread();
							flushDataJSQX(Integer.parseInt(tableDataJS.get(jtJS.getSelectedRow())[0]));
							dataGeted=true;
						}else
						{
							JOptionPane.showMessageDialog(JSQXGLPanel.this, "网络故障，添加失败！", "提示", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	//删除权限
	public void scQX(final String jsid,final String qxid)//参数是 角色ID
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = SC_QX;//删除权限
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append("<->");
				sb.append(qxid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "权限删除成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					int rowJS = jtJS.getSelectedRow();
					if(rowJS==-1)
					{//默认的角色表选择的是第一行
						rowJS=0;
					}
					flushDataJSQX(Integer.parseInt(tableDataJS.get(rowJS)[0]));
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "网络故障，删除失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	








	/*
	 * 数据更新方法
	 */
	
    //更新角色表数据模型
	public void flushDataJS()
	{
		//发送消息获得数据
		String msg = GET_JS;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());//"<#GET_JS#>""<#GET_JS#>"
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
			 			tmJS=new JSTableModel(JSQXGLPanel.this);
			 			//初始化表格数据
			 			tableDataJS = new Vector<String[]>();		
			 			//将表中的数据进行一下暂时的备份  
			 			//作用是 ：当点"修改"按钮时 判断当前表格内数据 与  这个备份的数据 是否相同  相同的话 提示相关信息
			 			origindataJS =  new Vector<String[]>();	
			 			//通过循环将数据库中数据
			 			for(int i=0;i<rowCount;i++)
			 			{
			 				tableDataJS.add(list.get(i));
			 				origindataJS.add(list.get(i));
			 			}	
			 			//设置表格模型
			 			jtJS.setModel(tmJS);	
			 			//更新表格外观
			 			initTableJS();
			    	 }
			     }
			);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//角色权限
	public void flushDataJSQX(int jsid)
	{
		//发送消息获得数据
		String msg = GET_JBQX;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(jsid);
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
			 			tmQX=new QXTableModel(JSQXGLPanel.this);
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
	 * 记录状态参数赋值分析：
	 * 1.pressedRowInJS通过给表格添加鼠标监听器jtJS.getSelectedRow()赋值。
	 * 2.lastEditRowInJS通过角色表数据模型JSTableModel中的setValueAt
	 * 3.isDataChanged通过角色表数据模型JSTableModel中的setValueAt
	 */
	
	/*
	 * 修改表格值功能的实现：
	 * 通过JSTableModel中的setValueAt设置lastEditRowInJS和isDataChanged的值，
	 * 通过表格的鼠标监听器中的jtJS.getSelectedRow()设置pressedRowInJS的值，
	 * 每次单击都与pressedRowInJS比较，不同即单击不是同一行，查看isDataChanged值即数据是否改变
	 * 数据改变了就更新服务器数据，并且从服务器查询所点击行角色的权限
	 */
}
