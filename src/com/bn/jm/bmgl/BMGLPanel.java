package com.bn.jm.bmgl;

import static com.bn.core.Constant.ADD_BM;

import static com.bn.core.Constant.DELETE_BM;
import static com.bn.core.Constant.GET_MAX_BMID;
import static com.bn.core.Constant.GET_YG_BY_BMID;
import static com.bn.core.Constant.tpicPath;
import static com.bn.core.Constant.GET_BM;
import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.subtitle;


import com.bn.jm.bmgl.TreeTransferHandler;
import com.bn.jm.jbqxck.JBQXCKPanel;
import com.bn.jm.jbqxck.JBQXTableModel;
import com.bn.jm.tableheader.GroupableTableHeader;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.util.BKJTreeNode;
import com.bn.util.TreeModelUitl;
import com.bn.jm.LoginWindow;
import com.bn.util.SocketUtil;
import com.bn.util.BKJTreeCellRenderer;


import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class BMGLPanel extends JPanel
{
	private JLabel jLabel=new JLabel("请选择部门");
	private JLabel bmyg=new JLabel("部门所含员工");
	private JLabel bmmc=new JLabel("部门名称");
	private JLabel bmms=new JLabel("部门描述");
	
	private JTree jTree=new JTree();//部门树
	JScrollPane jSTree=new JScrollPane(jTree);
	
	
	
	
	private JTextField jTextMC=new JTextField(1);//部门名称
	private JTextArea  jAreaMS=new JTextArea();//部门描述
	private JScrollPane jSArea=new JScrollPane(jAreaMS);//部门描述
	private JButton submit=new JButton("保存");
	
	//******************右键菜单*********************
	private JMenuItem jMenu[]=//右键菜单
	{
		new JMenuItem("添加部门"),new JMenuItem("删除部门")		
	};
	private JPopupMenu jPop=new JPopupMenu();
	
	public  JLabel jl = new JLabel(new ImageIcon());//部门拖动动态图标
	private BKJTreeNode	currNode;//当前节点引用
	private BKJTreeNode	addPidNode;//新添加的节点的父节点
	private int flag=1;//0添加节点or1修改节点用，向服务器发送数据时，会发送出去，用于服务器端区分是添加部门还是更新部门信息
	//private String msgFlag="";//添加 or 修改部门标识变量
	private boolean isSubmit=true;//是否提交用。于控制右键菜单可用与否，一次添加了一个部门之后才能再用右键菜单
	
	
	//员工表
	Class[] typeArray={Integer.class,String.class};
	//权限表头
	String[] head={"员工ID","员工姓名"};
	//角色表格数据
	String[][] tableData;
	//部门员工的表格模型
	BMYGTableModel tmBMYG;
	//部门员工表格
	JTable jtYG = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    JScrollPane jspYG=new JScrollPane(jtYG);
	
	public BMGLPanel() 
	{
		//部门树
		this.setLayout(null);
		this.add(jl);//拖动树节点时产生的动态图片
		this.add(jLabel);
		jLabel.setBounds(25, 15, 100, 20);
		jLabel.setFont(subtitle);
		jTree.setModel(null);
		this.add(jSTree);
		jSTree.setBounds(25, 40, 180, 300);
		
		//员工table
		this.add(bmyg);
		bmyg.setBounds(225, 15, 120, 20);
		bmyg.setFont(subtitle);
		this.add(jspYG);
		jspYG.setBounds(225, 40, 180, 300);
		
		//部门名称
		this.add(bmmc);
		bmmc.setBounds(425,15, 100, 20);
		bmmc.setFont(subtitle);
		this.add(jTextMC);
		jTextMC.setBounds(425,40, 150, 20);
		
		//部门描述
		this.add(bmms);
		bmms.setBounds(425, 70, 100, 20);
		bmms.setFont(subtitle);
		jSArea.setBounds(425, 95, 150, 205);
		this.add(jSArea);
		jAreaMS.setLineWrap(true);
		
		//保存按钮
		this.add(submit);
		submit.setBounds(495, 310, 80, 30);
		submit.setOpaque(false);
		
		initTree();
		initRightMenu();
		intiSubmit();
	}

    //更新部门树的数据
	public void flushData() 
	{
		//任务线程
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//发送消息获得数据
				String msg = GET_BM;
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(msg);
				String result=SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				DefaultTreeModel dtm=TreeModelUitl.getTreeModel(SocketUtil.strToList(result));
				jTree.setModel(dtm);
				if(currNode!=null)
					expandNode(currNode.getId());
			}
		}.start();
		//监视线程
		LoginWindow.watchThread();	
	}
	
	
	//初始化部门树
	private void initTree()
	{
		jTree.setCellRenderer(new BKJTreeCellRenderer());
		
		//**********允许树节点拖拉功能的设置*********************
		jTree.setDragEnabled(true);//打开自动拖动处理
		jTree.setDropMode(DropMode.ON_OR_INSERT);//设置此组件的放置模式
		jTree.setTransferHandler(new TreeTransferHandler(this));//组件之间传输数据的机制
		//**********允许树节点拖拉功能的设置*********************
		
		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath=e.getNewLeadSelectionPath();//获得根节点到选着节点的路径
				if(treePath!=null)
				{
					currNode=(BKJTreeNode)treePath.getLastPathComponent();//记录选中的节点
					jTextMC.setText(currNode.getTitle().trim());
					jAreaMS.setText(currNode.getMsg());
					new Thread()
					{
						public void run() 
						{
							//更新部门员工表
							String msg=SocketUtil.sendAndGetMsg(GET_YG_BY_BMID+currNode.getId()+GET_YG_BY_BMID);
							final List<String[]> list=SocketUtil.strToList(msg);
							try {
								SwingUtilities.invokeAndWait
								(
								     new Runnable()
								     {
								    	 public void run()
								    	 {	
								           int rowCount = list.size();
								          
					 			           //更新表格模型
					 			            tmBMYG=new BMYGTableModel(BMGLPanel.this);
					 			           //初始化表格数据
					 			            tableData = new String[rowCount][];		
					 		
					 			           //通过循环将数据库中数据
					 			           for(int i=0;i<rowCount;i++)
					 			            {
					 				          tableData[i]=list.get(i);	
					 			            }
					 			            //设置表格模型
					 			            jtYG.setModel(tmBMYG);	
					 			            //更新表格外观
					 			            initTable();
								    	    }
								         }
								      );
							} catch (Exception e) 
							{
								e.printStackTrace();
							}
						};
						
					}.start();
							
						
					
					
					/*
					if(flag==0&&msgFlag.equals("update_bm_ok")){
						flag=1;//部门修改状态
						msgFlag="";//flag与msgFlag控制修改、添加、删除之间的切换
					}
					*/
				}
			}
		});
		//右键监听器
		jTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//如果点击的是右键
				 if(e.isPopupTrigger()){
					 TreePath path = jTree.getPathForLocation(e.getX(), e.getY()); // 关键是这个方法的使用
				 	 if (path !=null) {
				 		jTree.setSelectionPath(path);
				 		jPop.show(jTree,e.getX(), e.getY());
				 		//点击右键菜单后（选择 添加 or 删除），1，如是选择添加节点，则必须提交或是放弃，右键菜单才可用，
				 		//2.如果是点删除 也必须提交或是放弃右键菜单才可用
				 		if(!isSubmit){
				 			jMenu[0].setEnabled(false);
				 			jMenu[1].setEnabled(false);
				 			if(currNode!=null&&currNode.getTitle().equals("新建部门"))
				 			{
				 				jMenu[1].setEnabled(true);//删除可用
				 			}
				 		}else{
				 			jMenu[0].setEnabled(true);
				 			jMenu[1].setEnabled(true);
				 		}
				 	 }
				 }
			}
		});
		
		
	}
	
	
	//初始化员工表
	public void initTable()
	{
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
		
		//设置表头绘制器
		jtYG.getTableHeader().setUI(new GroupableTableHeaderUI());
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
		//设置每一列宽度
		tc0.setPreferredWidth(70);
		tc1.setPreferredWidth(110);
		//设置每一列大小不可变
		tc0.setResizable(false);
		tc1.setResizable(false);
	}
	
	
	
	//初始化右键菜单
	private void initRightMenu()
	{
		for(int i=0;i<jMenu.length;i++)
		{
   		 jPop.add(jMenu[i]);
   		 jMenu[i].addActionListener(new ActionListener() {
				@Override//右键菜单
				public void actionPerformed(ActionEvent e) {
					//新建部门
					if(e.getSource()==jMenu[0])
					{
						//任务线程
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								if(currNode!=null)
								{									
									//获得最大部门ＩＤ
									String msg=SocketUtil.sendAndGetMsg(GET_MAX_BMID);
									dataGeted=true;
									final int maxid=Integer.parseInt(msg)+1;
									Runnable runnable=new Runnable()
									{
										public void run()
										{
											jTextMC.setText("新建部门");
											jAreaMS.setText("新建部门描述");
											jTextMC.setFocusable(true);
											
											//********给常量表示赋值*********
											flag=0;//部门新建状态
											//msgFlag="";
											isSubmit=false;//没有提交数据，未点保存
											//*****************
											
											BKJTreeNode newNode=new BKJTreeNode
											(
												 "新建部门",
												  new ImageIcon(tpicPath+"new.png"),
												  maxid,//id///自己的id
												  currNode.getId()//pid
											);
						 					newNode.setMsg("新建部门描述");
											currNode.add(newNode);//在节点最后插入新节点
											addPidNode=currNode;									
							                jTree.updateUI();  //更新界面
							                expandNode(newNode.getId());//展开到新节点
										}
									};
									
									try {
										SwingUtilities.invokeAndWait(runnable);
									} catch (Exception e) {	e.printStackTrace();}									
							                
								}else
								{
									JOptionPane.showMessageDialog(BMGLPanel.this,"请输选择父部门！","提示",JOptionPane.WARNING_MESSAGE);
								}
								
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();	
					}else 
					//删除部门
					if(e.getSource()==jMenu[1])
					{
						//任务线程
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								if(currNode!=null)
								{
									//各种判断条件
									if(currNode.getChildCount()>0){
										JOptionPane.showMessageDialog(BMGLPanel.this,"该部门拥有子部门，不能删除！",
												"提示",JOptionPane.WARNING_MESSAGE);
										return;
									}else{
										String msg=SocketUtil.sendAndGetMsg(GET_YG_BY_BMID+currNode.getId()+GET_YG_BY_BMID);
										
										int num=SocketUtil.strToList(msg).size();
										if(num>0){
											dataGeted=true;
											JOptionPane.showMessageDialog(BMGLPanel.this,"该部门还有员工，不能删除！",
													"提示",JOptionPane.WARNING_MESSAGE);
											return;
										}else{
											dataGeted=true;
											int i=JOptionPane.showConfirmDialog(BMGLPanel.this,"您确定要删除该部门吗？",
													"确定",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
											if(i==0)
											{//删除部门
												
												if(isSubmit==false){
													currNode.removeFromParent();
									                jTree.updateUI();  
									                isSubmit=true;
													return;
												}
												
												//联网删除部门
												String str=SocketUtil.sendAndGetMsg(DELETE_BM+currNode.getId()+DELETE_BM);
												//删除部门成功
												flushData();
												JOptionPane.showMessageDialog(BMGLPanel.this,"部门删除成功！","提示",JOptionPane.NO_OPTION);
												
											}
										}
									}
								}
								dataGeted=true;
							}
						}.start();
						//监视线程
						LoginWindow.watchThread();
					}
				}
			});
   	 	}
	}
	
	//初始化提交/保存按钮
	public void intiSubmit()
	{
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isSubmit=true;
				if(currNode==null){
					JOptionPane.showMessageDialog(BMGLPanel.this,"请选择部门！",
							"提示",JOptionPane.WARNING_MESSAGE);
				}else if(jTextMC.getText().trim().length()<=0){
					JOptionPane.showMessageDialog(BMGLPanel.this,"请输入部门名称！",
							"提示",JOptionPane.WARNING_MESSAGE);
				}else if(jAreaMS.getText().trim().length()<=0){
					JOptionPane.showMessageDialog(BMGLPanel.this,"请输入部门描述！",
							"提示",JOptionPane.WARNING_MESSAGE);
				}else{
					//任务线程
					dataGeted=false;
					new Thread()
					{
						public void run()
						{
							StringBuffer sb=new StringBuffer();
							sb.append(ADD_BM);
							sb.append(flag+"<->");
							if(flag==0){//flag=0表示添加部门，!=0表示修改部门
								sb.append(addPidNode.getId()+"<->");
							}
							else{
								sb.append(currNode.getId()+"<->");
							}
							sb.append(jTextMC.getText().trim()+"<->");
							sb.append(jAreaMS.getText().trim());
							sb.append(ADD_BM);
							String msg=SocketUtil.sendAndGetMsg(sb.toString());
							
							//弹出提示窗口
							if(flag==0){
								flushData();//刷新数据
								flag=1;
								//msgFlag="update_bm_ok";
								dataGeted=true;
								JOptionPane.showMessageDialog(BMGLPanel.this,"恭喜您部门添加成功！",
									"信息",JOptionPane.NO_OPTION);
							}else
							if(flag==1){
								flushData();//刷新数据
								//msgFlag="add_bm_ok";
								dataGeted=true;
								JOptionPane.showMessageDialog(BMGLPanel.this,"恭喜您部门修改成功！",
									"信息",JOptionPane.NO_OPTION);
							}
						}
					}.start();
					//监视线程
					LoginWindow.watchThread();
				}
			}
		});

	}
	
	
	
	
	
	
	
	
	
	
	//展开指定节点开始=============================================================
	private BKJTreeNode target=null;
	public void expandNode(int jdid)//jdid-节点id
	{		
		//声明目标节点引用
		target=null;
		//获取根节点
		BKJTreeNode root=(BKJTreeNode)jTree.getModel().getRoot();
		if(root.getId()==jdid){//如果根节点是目标节点
			return;
		}
		//递归遍历树寻找目标节点
		dgBl(root,jdid);
		//获取从根节点到目标节点的路径
		System.out.println("target"+target);
		if(target==null)return;
		TreeNode[] tna=target.getPath();
		//选中指定的节点并展开
		jTree.setSelectionPath(new TreePath(tna));
		jTree.scrollPathToVisible(new TreePath(tna));
	}
	
	public void dgBl(BKJTreeNode subRoot,int jdid)//递归遍历寻找目标节点
	{
		//获取当前节点的孩子节点列表
		@SuppressWarnings("unchecked")
		Enumeration<BKJTreeNode> e=(Enumeration<BKJTreeNode>)subRoot.children();
		
		//遍历当前节点的孩子节点列表
		while(e.hasMoreElements())
		{
			//获取一个孩子节点
			BKJTreeNode tempNode=(BKJTreeNode)e.nextElement();
			//判断此孩子节点是否为目标节点
			if(tempNode.getId()==jdid)
			{
				//若为目标节点
				System.out.println("tempNode:"+tempNode);
				target=tempNode;
				return;
			}
			else
			{
				//若不为目标节点则继续遍历
				dgBl(tempNode,jdid);
			}
		}
	}
	//展开指定节点结束=============================================================
	
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;  
        // 绘制渐变     起始坐标  起始颜色
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
