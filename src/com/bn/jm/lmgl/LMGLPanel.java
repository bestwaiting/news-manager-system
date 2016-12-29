package com.bn.jm.lmgl;

import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;

import static com.bn.core.Constant.ADD_LM;
import static com.bn.core.Constant.DEL_LM;
import static com.bn.core.Constant.bpicPath;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.subtitle;
import static com.bn.core.Constant.GET_LM;
import static com.bn.core.Constant.GET_LM_NEW;
import static com.bn.core.Constant.GET_DFB_NEW;
import static com.bn.core.Constant.XG_LM;
import static com.bn.core.Constant.XG_LMID;
import static com.bn.core.Constant.TRAN_LM;
import static com.bn.core.Constant.TRAN_XW;
import static com.bn.core.Constant.XG_FBZTID;
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.bn.jm.LoginWindow;
import com.bn.jm.MainJFrame;
import com.bn.jm.jsqxgl.JSQXGLPanel;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.jm.xwxz.XWXZPanel;
import com.bn.util.SocketUtil;

public class LMGLPanel extends JPanel
{
	private JLabel jlLM=new JLabel("栏目列表");
	private JLabel jlLMXW=new JLabel("栏目所含新闻");
	private JLabel jlXW=new JLabel("待发布新闻");
	
	
	//栏目
	private JToolBar jtb1=new JToolBar();
	private String[] jtbName1={"上移","下移","添加","删除"};
	private JButton[] jtbButton1={new JButton(new ImageIcon(bpicPath+"Up.png")),
			new JButton(new ImageIcon(bpicPath+"Down.png")),
			new JButton(new ImageIcon(bpicPath+"Add.png")),
			new JButton(new ImageIcon(bpicPath+"Del.png"))};
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
	
	
	//栏目所含新闻
	private JToolBar jtb2=new JToolBar();
	private String[] jtbName2={"上移","下移"};
	private JButton[] jtbButton2={new JButton(new ImageIcon(bpicPath+"Up.png")),
			new JButton(new ImageIcon(bpicPath+"Down.png"))};
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
	
	//新闻
	private JToolBar jtb3=new JToolBar();
	private String[] jtbName3={"上移","下移","过期"};
	private JButton[] jtbButton3={new JButton(new ImageIcon(bpicPath+"Up.png")),
			new JButton(new ImageIcon(bpicPath+"Down.png")),
			new JButton(new ImageIcon(bpicPath+"Bin.png"))};
	JTable jtXW = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
	JScrollPane jsXW=new JScrollPane(jtXW);
	
	private JButton leftBtn = new JButton(new ImageIcon(bpicPath+"Left1.png"));
	private JButton rightBtn = new JButton(new ImageIcon(bpicPath+"Right1.png"));
	
	
	XWTableModel tmXW;
	
	//记录栏目表中上次点击的行
	int pressedRowInLM=-1;
	//记录单元格数据是否改变
	boolean isDataChanged=false;
	//记录刚刚编辑了的行
    int lastEditRowInLM=-1;
    //记录单击行的栏目id
    int lmid=-1;
    //记录栏目新闻表中上次点击的行
	int pressedRowInLMXW=-1;
	//记录新闻表中上次点击的行
	int pressedRowInXW=-1;
	
	
	private MainJFrame mf;
	
	public LMGLPanel(MainJFrame mf)
	{
		this.setLayout(null);
		this.mf=mf;
		
		//栏目表
		this.add(jlLM);
		jlLM.setBounds(25, 15, 120, 20);
		jlLM.setFont(subtitle);
		this.add(jsLM);
		jsLM.setBounds(25, 70, 280, 580);
		//工具栏
		for(int i=0;i<jtbButton1.length;i++)
		{
			jtb1.add(jtbButton1[i]);
			jtbButton1[i].addActionListener(null);
			jtbButton1[i].setToolTipText(jtbName1[i]);
		}
		jtb1.addSeparator();
		jtb1.setFloatable(false);
		jtb1.setBounds(25, 40, 280, 30);
		this.add(jtb1);
		
		
		//栏目所含新闻表
		this.add(jlLMXW);
		jlLMXW.setBounds(365, 15, 120, 20);
		jlLMXW.setFont(subtitle);
		this.add(jsLMXW);
		jsLMXW.setBounds(365, 70, 280, 580);
		//工具栏
		for(int i=0;i<jtbButton2.length;i++)
		{
			jtb2.add(jtbButton2[i]);
			jtbButton2[i].addActionListener(null);
			jtbButton2[i].setToolTipText(jtbName2[i]);
		}
		jtb2.addSeparator();
		jtb2.setFloatable(false);
		jtb2.setBounds(365, 40, 280, 30);
		this.add(jtb2);
		
		
		
		this.add(leftBtn);
		leftBtn.setBounds(650, 280, 50, 30);
		leftBtn.setOpaque(false);
		this.add(rightBtn);
		rightBtn.setBounds(650, 350, 50, 30);
		rightBtn.setOpaque(false);
		
		
		//新闻表
		this.add(jlXW);
		jlXW.setBounds(705, 15, 120, 20);
		jlXW.setFont(subtitle);
		this.add(jsXW);
		jsXW.setBounds(705, 70, 280, 580);
		//工具栏
		for(int i=0;i<jtbButton3.length;i++)
		{
			jtb3.add(jtbButton3[i]);
			jtbButton3[i].addActionListener(null);
			jtbButton3[i].setToolTipText(jtbName3[i]);
		}
		jtb3.addSeparator();
		jtb3.setFloatable(false);
		jtb3.setBounds(705, 40, 280, 30);
		this.add(jtb3);
		
		
		addTableListeners();
		addToolBarListeners();
		addButtonListeners();
		
		
		
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
	
	
	
	/*
	 * 添加监听器方法
	 */
	public void addToolBarListeners()
	{
		//栏目工具栏******************上移
		jtbButton1[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择栏目！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此栏目已置顶！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM>0)
				{
					tranLM(tmLM.tableData[pressedRowInLM][0].toString(), tmLM.tableData[pressedRowInLM][1].toString(), 
						tmLM.tableData[pressedRowInLM-1][0].toString(), tmLM.tableData[pressedRowInLM-1][1].toString());
					
				}	
			}
			
		});
		//下移
		jtbButton1[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择栏目！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM>=(tmLM.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此栏目已置底！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM<(tmLM.tableData.length-1))
				{
					tranLM(tmLM.tableData[pressedRowInLM][0].toString(), tmLM.tableData[pressedRowInLM][1].toString(), 
						tmLM.tableData[pressedRowInLM+1][0].toString(), tmLM.tableData[pressedRowInLM+1][1].toString());
				}	
				
				
				
			}
			
		});
		//添加栏目
		jtbButton1[2].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				      addLM();
				      dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataLM();
								dataGeted=true;
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();     
			}
			
		});
		//删除栏目
		jtbButton1[3].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				  delLM(lmid);
			      dataGeted=false;
					new Thread()
					{
						public void run()
						{
							flushDataLM();
							dataGeted=true;
						}
					}.start();
					//监视线程
					LoginWindow.watchThread();    
				
			}
			
		});
		
		//栏目所含新闻工具栏******************
		jtbButton2[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此新闻已置顶！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW>0)
				{
					tranXW(tmLMXW.tableData[pressedRowInLMXW][0].toString(), tmLMXW.tableData[pressedRowInLMXW][1].toString(), 
							tmLMXW.tableData[pressedRowInLMXW-1][0].toString(), tmLMXW.tableData[pressedRowInLMXW-1][1].toString());
					
				}	
			}
			
		});
		//下移
		jtbButton2[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW>=(tmLMXW.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此新闻已置底！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW<(tmLM.tableData.length-1))
				{
					tranXW(tmLMXW.tableData[pressedRowInLMXW][0].toString(), tmLMXW.tableData[pressedRowInLMXW][1].toString(), 
							tmLMXW.tableData[pressedRowInLMXW+1][0].toString(), tmLMXW.tableData[pressedRowInLMXW+1][1].toString());
				}	
				
				
				
			}
			
		});
		
		
		//待发布新闻工具栏******************
		jtbButton3[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此新闻已置顶！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW>0)
				{
					tranDFBXW(tmXW.tableData[pressedRowInXW][0].toString(), tmXW.tableData[pressedRowInXW][1].toString(), 
							tmXW.tableData[pressedRowInXW-1][0].toString(), tmXW.tableData[pressedRowInXW-1][1].toString());
					
				}	
			}
			
		});
		//下移
		jtbButton3[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW>=(tmXW.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "此新闻已置底！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW<(tmXW.tableData.length-1))
				{
					tranDFBXW(tmXW.tableData[pressedRowInXW][0].toString(), tmXW.tableData[pressedRowInXW][1].toString(), 
							tmXW.tableData[pressedRowInXW+1][0].toString(), tmXW.tableData[pressedRowInXW+1][1].toString());
				}	
				
				
				
			}
			
		});
		
		//新闻过期
		jtbButton3[2].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "确认要此条新闻设置为过期吗?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(i==0)//如果点"是"
				{
					xgFBZTID(tmXW.tableData[pressedRowInXW][1].toString(), "2");
				}				
			}
			
		});	
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
						if(isDataChanged)//如果数据改变了  问是否保存数据
						{
							isDataChanged=false;
							
							if(tmLM.tableData[lastEditRowInLM][2].equals(""))
							{
								JOptionPane.showMessageDialog(LMGLPanel.this, "栏目名称不能为空！", "提示",JOptionPane.INFORMATION_MESSAGE);
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
								return;
							}
							int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "是否保存对栏目表的修改?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//如果点"是"
							{
								xgLM(tmLM.tableData[lastEditRowInLM][1].toString(),tmLM.tableData[lastEditRowInLM][2].toString());
							}else
							{//如果点"否"或者"×"
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
							}
						}
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
		
		jtLM.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//row实时记录单击的行
					final int row = jtLM.getSelectedRow();
					if(pressedRowInLM!=row)
					{//如果点击的不在同一行
						pressedRowInLM = row;
						lmid=Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString());
						if(isDataChanged)//如果数据改变了  问是否保存数据
						{
							isDataChanged=false;
							
							if(tmLM.tableData[lastEditRowInLM][2].equals(""))
							{
								JOptionPane.showMessageDialog(LMGLPanel.this, "栏目名称不能为空！", "提示",JOptionPane.INFORMATION_MESSAGE);
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
								return;
							}
							int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "是否保存对栏目表的修改?", "提示",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//如果点"是"
							{
								xgLM(tmLM.tableData[lastEditRowInLM][1].toString(),tmLM.tableData[lastEditRowInLM][2].toString());
							}else
							{//如果点"否"或者"×"
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
							}
						}
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
						mf.gotoXWCK(xwid, true);	
					}			
				}
			}
		);
		
		jtLMXW.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//row实时记录单击的行
					final int row = jtLMXW.getSelectedRow();
					if(pressedRowInLMXW!=row)
					{//如果点击的不在同一行
						pressedRowInLMXW = row;
					}
				}
			}
		);
		
		
		//给待发布新闻表添加表格监听器
		jtXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtXW.getSelectedRow();
					if(pressedRowInXW!=row)
					{//如果点击的不在同一行
						pressedRowInXW = row;
					}
				}
				
				@Override
				public void mouseClicked(MouseEvent e) 
				{
					if(e.getClickCount()==2)
					{
						String xwid=tmXW.tableData[pressedRowInXW][1].toString();
						mf.gotoXWCK(xwid, true);	
					}			
				}
			}
		);
		
		jtXW.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//row实时记录单击的行
					final int row = jtXW.getSelectedRow();
					if(pressedRowInXW!=row)
					{//如果点击的不在同一行
						pressedRowInXW = row;
					}
				}
			}
		);
		
		
		
		
		
		
		
		
	}
	
	public void addButtonListeners()
	{//添加按钮的监听器
		//发布新闻按钮，left
		leftBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择栏目！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				xgLMID(tmXW.tableData[pressedRowInXW][1].toString(), tmLM.tableData[pressedRowInLM][1].toString());
			}
			
		});
		
		
		//发布新闻按钮，right
		rightBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "请选择新闻！", "提示", JOptionPane.NO_OPTION);
					return;
				}
				xgLMID(tmLMXW.tableData[pressedRowInLMXW][1].toString(), "0");
			}
			
		});
		
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
				 			tmLM=new LMTableModel(LMGLPanel.this);
				 			//初始化表格数据
				 			tmLM.tableData = new Object[rowCount][colCount];
				 			tmLM.OritableData = new Object[rowCount][colCount];
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLM.tableData[i][j]=list.get(i)[j];
				 					tmLM.OritableData[i][j]=list.get(i)[j];
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
			String msg = GET_LM_NEW;
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
				 			tmLMXW=new LMXWTableModel(LMGLPanel.this);
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
				 			initTable(jtLMXW);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//更新栏目表数据的方法
	public void flushDataXW() 
	{
			//发送消息获得数据
			String msg = GET_DFB_NEW;
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
				 			tmXW=new XWTableModel(LMGLPanel.this);
				 			//初始化表格数据
				 			tmXW.tableData = new Object[rowCount][colCount];
				 			//通过循环将数据库中数据
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmXW.tableData[i][j]=list.get(i)[j];
				 				}
				 			}
				 			//设置表格模型
				 			jtXW.setModel(tmXW);	
				 			//更新表格外观
				 			initTable(jtXW);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//修改栏目名称
	public void xgLM(final String lmid,final String lmmc)//角色ID  角色名称
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = XG_LM;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(lmid);
				sb.append("<->");
				sb.append(lmmc);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "修改成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataLM();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "网络故障，修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	
	//添加栏目
	private void addLM()
	{
	  StringBuilder sb=new StringBuilder();
	  sb.append(ADD_LM);
	  sb.append(ADD_LM);
	  SocketUtil.sendAndGetMsg(sb.toString());	
	}
	
	
	//删除栏目
	public void delLM(final int lmid)//参数是 角色ID
	{
		if(lmid==-1)
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "请选择栏目！", "提示", JOptionPane.NO_OPTION);
			return;
		}
		//发送消息获得数据
		String msg = DEL_LM;//能否删除该角色
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(lmid);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());
		if(result.equals("ok"))
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "栏目删除成功！", "提示", JOptionPane.NO_OPTION);
		}else if(result.equals("hasNew"))
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "此栏目下含有新闻，删除栏目失败！", "提示", JOptionPane.WARNING_MESSAGE);
		}
				
	}
	
	//调整栏目顺序
	public void tranLM(final String sxid1,final String lmid1,final String sxid2,final String lmid2)//角色ID  角色名称
	{
		//sxid1 lmid2
		//sxid2 lmid1
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = TRAN_LM;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(lmid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(lmid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//更新栏目信息
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataLM();
				dataGeted=true;
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	
	//调整栏目顺序
	public void tranXW(final String sxid1,final String xwid1,final String sxid2,final String xwid2)//角色ID  角色名称
	{
		//sxid1 xwid2
		//sxid2 xwid1
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = TRAN_XW;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(xwid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(xwid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//更新栏目信息
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataLMXW(Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString()));
				dataGeted=true;
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	
	//调整待发布新闻顺序
	public void tranDFBXW(final String sxid1,final String xwid1,final String sxid2,final String xwid2)//角色ID  角色名称
	{
		//sxid1 xwid2
		//sxid2 xwid1
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = TRAN_XW;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(xwid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(xwid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//更新栏目信息
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataXW();
				dataGeted=true;
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	
	
	//修改栏目id
	public void xgLMID(final String xwid,final String lmid)//新闻ID  栏目id
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = XG_LMID;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(xwid);
				sb.append("<->");
				sb.append(lmid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataLMXW(Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString()));
					dataGeted=true;
					//更新栏目新闻
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataXW();
					dataGeted=true;
					
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "网络故障，修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
	}
	
	
	//修改新闻发布状态id
	public void xgFBZTID(final String xwid,final String fbztid)//角色ID  角色名称
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = XG_FBZTID;//修改角色名称
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(xwid);
				sb.append("<->");
				sb.append(fbztid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "新闻设置过期成功！", "提示", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataXW();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "网络故障，修改失败！", "提示", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();
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
