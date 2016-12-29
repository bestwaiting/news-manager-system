package com.bn.jm.ygxxgl;


import static com.bn.core.Constant.SCREEN_HEIGHT;
import static com.bn.core.Constant.SCREEN_WIDTH;
import static com.bn.core.Constant.winIcon;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.GET_BM;
import static com.bn.core.Constant.GET_YG;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.XG_YG;
import static com.bn.core.Constant.GET_JS;
import static com.bn.core.Constant.dataGeted;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
/*
 * 员工信息管理
 */
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultTreeModel;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.util.SocketUtil;
import com.bn.util.TreeModelUitl;

@SuppressWarnings("serial")
public class YGXXGLPanel extends JPanel
{
	//======================================员工信息表======================================
	//角色表格每一列的类型
	Class[] typeArrayYG={Integer.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,JButton.class};
	//角色表头
	String[] headYG={"员工ID","登陆账号","登陆密码","真实姓名","性别","联系方式","所在部门","角色","离职与否","修改"};
	//角色表格数据
	Object[][] tableDataYG;
	Object[][] origindataYG;
	
	YGTableModel tmYG;
	
	
	JTable jtYG = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    
    //记录上次点击的行（用于触发单击其他行，修改服务器数据事件）
	int pressedRow=-1;
	//记录刚刚编辑了的行（用于得到数据模型中的编辑行的数据）
    int lastEditRow=-1;
    //记录显示所有员工，还是在职员工，离职员工的int
    public int lzyf=2;//-1显示所有，0只显示在职员工，1只显示离职员工
    
    //用于记录从服务器读取的角色信息（角色id,角色名称）
    Map<String,String> JSMapforRender=new HashMap<String,String>();
    //用于记录从服务器读取的角色信息（角色名称,角色id）
    Map<String,String> JSMapforEditor=new HashMap<String,String>();
    
	//存放表格的JScrollPane
	JScrollPane jspYG = new JScrollPane(jtYG);
	
	
	
	JLabel jlTitle =new JLabel("员工信息列表");
	String[] str={"只显示在职员工","只显示离职员工","显示所有员工"}; 
	JComboBox jcbLzyf=new JComboBox(str);

	JButton jbAddYG=new JButton("添加员工");
	
	//用于部门选择树的一些引用
	DefaultTreeModel dtm;//部门选择树下拉列表数据模型
	List<String[]> listBM;//包含部门信息的List
	
	
	public YGXXGLPanel() 
	{
		this.setLayout(null);
		//表头设置
		jspYG.setBounds(25, 50, 1135, 595);
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);
		
		jcbLzyf.setBounds(25, 20, 120, 20);
		jcbLzyf.setOpaque(false);
		jbAddYG.setBounds(25, 660, 80, 30);
		jbAddYG.setOpaque(false);
		
		this.addButtonListener();
		this.addTableListener();
		this.add(jcbLzyf);
		this.add(jbAddYG);
		this.add(jlTitle);
		this.add(jspYG);
		initJSMap();
	}
	
	
	//初始化员工表
	public void initTableYG()
	{
		
		//设置表头绘制器
        jtYG.getTableHeader().setUI(new GroupableTableHeaderUI());
		//设置行高
        jtYG.setRowHeight(30);
		//设置只能单选
        jtYG.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//获得table单元格绘制器
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//设置表格里内容居中
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//设置第一列居中
		jtYG.setDefaultRenderer(Integer.class, dtcr);
		//修改按钮绘制器
		YGButtonRenderer jButtonRenderer=new YGButtonRenderer();
		jtYG.setDefaultRenderer(JButton.class, jButtonRenderer);
		//修改按钮编辑器
		YGButtonEditor ygButtonEidtor=new YGButtonEditor(this);
		jtYG.setDefaultEditor(JButton.class, ygButtonEidtor);
		

		//获得表头
		JTableHeader tableHeader = jtYG.getTableHeader();  
		//获得表头绘制器
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//列名居中
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//表格列不可移动    
		tableHeader.setReorderingAllowed(false);
		

		//获得每一列的引用
		TableColumn tc0 = jtYG.getColumnModel().getColumn(0);
		TableColumn tc1 = jtYG.getColumnModel().getColumn(1);
		TableColumn tc2 = jtYG.getColumnModel().getColumn(2);
		TableColumn tc3 = jtYG.getColumnModel().getColumn(3);
		TableColumn tc4 = jtYG.getColumnModel().getColumn(4);
		TableColumn tc5 = jtYG.getColumnModel().getColumn(5);
		TableColumn tc6 = jtYG.getColumnModel().getColumn(6);
		TableColumn tc7 = jtYG.getColumnModel().getColumn(7);
		TableColumn tc8 = jtYG.getColumnModel().getColumn(8);
		TableColumn tc9 = jtYG.getColumnModel().getColumn(9);
		
		
		//设置每一列宽度
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(100);
		tc2.setPreferredWidth(100);
		tc3.setPreferredWidth(100);
		tc4.setPreferredWidth(100);
		tc5.setPreferredWidth(220);
		tc6.setPreferredWidth(120);
		tc7.setPreferredWidth(120);
		tc8.setPreferredWidth(100);
		tc9.setPreferredWidth(115);
		
		
		//为部门列添加编辑器和绘制器
		tc6.setCellEditor(new YGBMJTreeComboBoxEidtor(this));
		tc6.setCellRenderer(new YGBMRenderer(YGXXGLPanel.this));
		
		//为离职与否列添加编辑器和绘制器
		tc8.setCellEditor(new YGLZJComboBoxEditor(this));
		tc8.setCellRenderer(new YGLZRenderer());
		
		//为角色列添加编辑器和绘制器
		tc7.setCellEditor(new YGJSJComboBoxEidtor(this));
		tc7.setCellRenderer(new YGJSRenderer(this));
		

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
	
	//初始化包含角色下拉列表数据的map的方法
	public void initJSMap()
	{
		//发送消息获得数据
		String msg = GET_JS;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());//"<#GET_JS#>""<#GET_JS#>"
		List<String[]> listJS = SocketUtil.strToList(result);
		JSMapforRender.clear();
		for(String[] str:listJS)
		{
			JSMapforRender.put(str[0], str[1]);
			JSMapforEditor.put(str[1], str[0]);
		}	
	}
	
	//为按钮，下拉列表添加监听
	public void addButtonListener()
	{
		jcbLzyf.setSelectedIndex(lzyf);
		jcbLzyf.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==ItemEvent.DESELECTED)
				{
					lzyf=jcbLzyf.getSelectedIndex();
					//jcbLzyf.setSelectedIndex(lzyf);
					new Thread()
					{
						public void run() 
						{
							flushData(lzyf);
						};
						
					}.start();	
				}
				
			}
			
		});
		
		jbAddYG.addActionListener(new ActionListener(){
			final JFrame newjf = new JFrame("添加新员工");
 			final NewYGPanel newygpanel=new NewYGPanel();
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				LoginWindow.mf.setEnabled(false);//MainJFrame以后要修改
				newjf.setAlwaysOnTop(true);
				newjf.setLayout(null);
				newjf.setLocation(550,110);
				newjf.setResizable(false);
				newjf.setIconImage(winIcon);
				newjf.add(newygpanel);
				newygpanel.setBounds(10, 10, 350, 300);
				newjf.setSize(250, 310);
				newjf.setLocation((int) (SCREEN_WIDTH -230 ) / 2,   (int) (SCREEN_HEIGHT -400) / 2);
				newjf.setVisible(true);
				newjf.addWindowListener( new WindowAdapter()
				{
					@Override
					public void windowClosing(WindowEvent e) 
					{
						
						LoginWindow.mf.setEnabled(true);//MainJFrame以后要修改
						newjf.dispose();
						newjf.setAlwaysOnTop(false);
					}
				});
				
				
			}
			
		});
		
		
	}
	
	//为表格添加鼠标和键盘监听
	public void addTableListener()
	{
		jtYG.addMouseListener(
				new MouseAdapter()
				{
					public void mousePressed(java.awt.event.MouseEvent e) 
					{
						int row=jtYG.getSelectedRow();
						if(pressedRow!=row)
						{
							pressedRow=row;
							if(dataChanged())
							{
								int i=JOptionPane.showConfirmDialog(YGXXGLPanel.this, "是否保存对员工信息表的修改？", "提示", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
								if(i==0)
								{
									xgYG(tableDataYG[lastEditRow][0].toString(),
										    tableDataYG[lastEditRow][6].toString(),
											tableDataYG[lastEditRow][7].toString(),
											tableDataYG[lastEditRow][8].toString());
									
								}else
								{
									tableDataYG[lastEditRow][6]=origindataYG[lastEditRow][6];
									tableDataYG[lastEditRow][7]=origindataYG[lastEditRow][7];
									tableDataYG[lastEditRow][8]=origindataYG[lastEditRow][8];
								}
							}
						}
						
						
					};
					
				}
		);
		
		jtYG.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyReleased(KeyEvent e) 
			{
				int row=jtYG.getSelectedRow();
				if(pressedRow!=row)
				{
					pressedRow=row;
					if(dataChanged())
					{
						int i=JOptionPane.showConfirmDialog(YGXXGLPanel.this, "是否保存对员工信息表的修改？", "提示", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
						if(i==0)
						{
							xgYG(tableDataYG[lastEditRow][0].toString(),
								    tableDataYG[lastEditRow][6].toString(),
									tableDataYG[lastEditRow][7].toString(),
									tableDataYG[lastEditRow][8].toString());
							
						}else
						{
							tableDataYG[lastEditRow][6]=origindataYG[lastEditRow][6];
							tableDataYG[lastEditRow][7]=origindataYG[lastEditRow][7];
							tableDataYG[lastEditRow][8]=origindataYG[lastEditRow][8];
						}
					}
				}
					
			}
		});
	}
	
	
	
	
	
	
	/*
	 * 数据更新方法
	 */
	
	//更新员工表数据的方法
	public void flushData(int lzid) 
	{
			//发送消息获得数据
			String msg = GET_YG;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(lzid);
			sb.append(msg);
			String result =SocketUtil.sendAndGetMsg(sb.toString());//"<#GET_YG#>""<#GET_YG#>"
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
				 			tmYG=new YGTableModel(YGXXGLPanel.this);
				 			//初始化表格数据
				 			tableDataYG = new Object[rowCount][colCount+1];		
				 			//将表中的数据进行一下暂时的备份  
				 			//作用是 ：当点"修改"按钮时 判断当前表格内数据 与  这个备份的数据 是否相同  相同的话 提示相关信息
				 			origindataYG = new Object[rowCount][colCount+1];
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tableDataYG[i][j]=list.get(i)[j];
					 				origindataYG[i][j]=list.get(i)[j];
				 				//System.out.println(tableDataYG[i][j]);	
				 				}
				 				tableDataYG[i][colCount]=new JButton();
				 				origindataYG[i][colCount]=new JButton();
				 			}
				 			//设置表格模型
				 			jtYG.setModel(tmYG);	
				 			//更新表格外观
				 			initTableYG();
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	//更新员工表数据的方法
	public void flushDataBM() 
	{
		//发送消息获得数据
		String msg = GET_BM;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(msg);
		String result=SocketUtil.sendAndGetMsg(sb.toString());
		listBM=SocketUtil.strToList(result);
		dtm=TreeModelUitl.getTreeModel(listBM);
		
	};
	
		
	/*
	 * 联网访问数据方法
	 */
	
	//判断一行的数据是否修改了
	public boolean dataChangedForButton()
	{
		if(lastEditRow==-1)
		{
			JOptionPane.showMessageDialog(this,"请对表格做修改之后,单击修改按钮！","提示",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if(tableDataYG[lastEditRow][6].equals(origindataYG[lastEditRow][6])&&
		   tableDataYG[lastEditRow][7].equals(origindataYG[lastEditRow][7])&&
		   tableDataYG[lastEditRow][8].equals(origindataYG[lastEditRow][8]))
		{
			JOptionPane.showMessageDialog(this,"数据没有修改,请修改了表格数据之后,单击修改按钮！！","提示",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}
	
	//判断一行的数据是否修改了
	public boolean dataChanged()
	{
		if(lastEditRow==-1)
		{
			return false;
		}
		if(tableDataYG[lastEditRow][6].equals(origindataYG[lastEditRow][6])&&
		   tableDataYG[lastEditRow][7].equals(origindataYG[lastEditRow][7])&&
		   tableDataYG[lastEditRow][8].equals(origindataYG[lastEditRow][8]))
		{
			return false;
		}
		return true;
	}
	
	//修改员工信息（员工id，部门，角色，离职）
	public void xgYG(final String ygid,final String bmid,final String jsid,final String lzid)
	{
		dataGeted=false;
		new Thread()
		{
			@Override
			public void run() 
			{
				String msg=XG_YG;
				StringBuffer sb=new StringBuffer();
				sb.append(msg);
				sb.append(ygid);
				sb.append("<->");
				sb.append(bmid);
				sb.append("<->");
				sb.append(jsid);
				sb.append("<->");
				sb.append(lzid);
				sb.append(msg);
				String result=SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(YGXXGLPanel.this,"员工信息修改成功！","提示",JOptionPane.INFORMATION_MESSAGE );
					dataGeted=false;
					flushData(lzyf);
					LoginWindow.watchThread();	
					dataGeted=true;	
				}else
				{
					JOptionPane.showMessageDialog(YGXXGLPanel.this, "员工信息修改失败！","提示",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}.start();
		
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
