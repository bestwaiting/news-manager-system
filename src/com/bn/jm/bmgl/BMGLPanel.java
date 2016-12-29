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
	private JLabel jLabel=new JLabel("��ѡ����");
	private JLabel bmyg=new JLabel("��������Ա��");
	private JLabel bmmc=new JLabel("��������");
	private JLabel bmms=new JLabel("��������");
	
	private JTree jTree=new JTree();//������
	JScrollPane jSTree=new JScrollPane(jTree);
	
	
	
	
	private JTextField jTextMC=new JTextField(1);//��������
	private JTextArea  jAreaMS=new JTextArea();//��������
	private JScrollPane jSArea=new JScrollPane(jAreaMS);//��������
	private JButton submit=new JButton("����");
	
	//******************�Ҽ��˵�*********************
	private JMenuItem jMenu[]=//�Ҽ��˵�
	{
		new JMenuItem("��Ӳ���"),new JMenuItem("ɾ������")		
	};
	private JPopupMenu jPop=new JPopupMenu();
	
	public  JLabel jl = new JLabel(new ImageIcon());//�����϶���̬ͼ��
	private BKJTreeNode	currNode;//��ǰ�ڵ�����
	private BKJTreeNode	addPidNode;//����ӵĽڵ�ĸ��ڵ�
	private int flag=1;//0��ӽڵ�or1�޸Ľڵ��ã����������������ʱ���ᷢ�ͳ�ȥ�����ڷ���������������Ӳ��Ż��Ǹ��²�����Ϣ
	//private String msgFlag="";//��� or �޸Ĳ��ű�ʶ����
	private boolean isSubmit=true;//�Ƿ��ύ�á��ڿ����Ҽ��˵��������һ�������һ������֮����������Ҽ��˵�
	
	
	//Ա����
	Class[] typeArray={Integer.class,String.class};
	//Ȩ�ޱ�ͷ
	String[] head={"Ա��ID","Ա������"};
	//��ɫ�������
	String[][] tableData;
	//����Ա���ı��ģ��
	BMYGTableModel tmBMYG;
	//����Ա�����
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
		//������
		this.setLayout(null);
		this.add(jl);//�϶����ڵ�ʱ�����Ķ�̬ͼƬ
		this.add(jLabel);
		jLabel.setBounds(25, 15, 100, 20);
		jLabel.setFont(subtitle);
		jTree.setModel(null);
		this.add(jSTree);
		jSTree.setBounds(25, 40, 180, 300);
		
		//Ա��table
		this.add(bmyg);
		bmyg.setBounds(225, 15, 120, 20);
		bmyg.setFont(subtitle);
		this.add(jspYG);
		jspYG.setBounds(225, 40, 180, 300);
		
		//��������
		this.add(bmmc);
		bmmc.setBounds(425,15, 100, 20);
		bmmc.setFont(subtitle);
		this.add(jTextMC);
		jTextMC.setBounds(425,40, 150, 20);
		
		//��������
		this.add(bmms);
		bmms.setBounds(425, 70, 100, 20);
		bmms.setFont(subtitle);
		jSArea.setBounds(425, 95, 150, 205);
		this.add(jSArea);
		jAreaMS.setLineWrap(true);
		
		//���水ť
		this.add(submit);
		submit.setBounds(495, 310, 80, 30);
		submit.setOpaque(false);
		
		initTree();
		initRightMenu();
		intiSubmit();
	}

    //���²�����������
	public void flushData() 
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
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
		//�����߳�
		LoginWindow.watchThread();	
	}
	
	
	//��ʼ��������
	private void initTree()
	{
		jTree.setCellRenderer(new BKJTreeCellRenderer());
		
		//**********�������ڵ��������ܵ�����*********************
		jTree.setDragEnabled(true);//���Զ��϶�����
		jTree.setDropMode(DropMode.ON_OR_INSERT);//���ô�����ķ���ģʽ
		jTree.setTransferHandler(new TreeTransferHandler(this));//���֮�䴫�����ݵĻ���
		//**********�������ڵ��������ܵ�����*********************
		
		jTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				TreePath treePath=e.getNewLeadSelectionPath();//��ø��ڵ㵽ѡ�Žڵ��·��
				if(treePath!=null)
				{
					currNode=(BKJTreeNode)treePath.getLastPathComponent();//��¼ѡ�еĽڵ�
					jTextMC.setText(currNode.getTitle().trim());
					jAreaMS.setText(currNode.getMsg());
					new Thread()
					{
						public void run() 
						{
							//���²���Ա����
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
								          
					 			           //���±��ģ��
					 			            tmBMYG=new BMYGTableModel(BMGLPanel.this);
					 			           //��ʼ���������
					 			            tableData = new String[rowCount][];		
					 		
					 			           //ͨ��ѭ�������ݿ�������
					 			           for(int i=0;i<rowCount;i++)
					 			            {
					 				          tableData[i]=list.get(i);	
					 			            }
					 			            //���ñ��ģ��
					 			            jtYG.setModel(tmBMYG);	
					 			            //���±�����
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
						flag=1;//�����޸�״̬
						msgFlag="";//flag��msgFlag�����޸ġ���ӡ�ɾ��֮����л�
					}
					*/
				}
			}
		});
		//�Ҽ�������
		jTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				//�����������Ҽ�
				 if(e.isPopupTrigger()){
					 TreePath path = jTree.getPathForLocation(e.getX(), e.getY()); // �ؼ������������ʹ��
				 	 if (path !=null) {
				 		jTree.setSelectionPath(path);
				 		jPop.show(jTree,e.getX(), e.getY());
				 		//����Ҽ��˵���ѡ�� ��� or ɾ������1������ѡ����ӽڵ㣬������ύ���Ƿ������Ҽ��˵��ſ��ã�
				 		//2.����ǵ�ɾ�� Ҳ�����ύ���Ƿ����Ҽ��˵��ſ���
				 		if(!isSubmit){
				 			jMenu[0].setEnabled(false);
				 			jMenu[1].setEnabled(false);
				 			if(currNode!=null&&currNode.getTitle().equals("�½�����"))
				 			{
				 				jMenu[1].setEnabled(true);//ɾ������
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
	
	
	//��ʼ��Ա����
	public void initTable()
	{
		//�����и�
        jtYG.setRowHeight(30);
		//����ֻ�ܵ�ѡ
        jtYG.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtYG.setDefaultRenderer(Integer.class, dtcr);
		
		//���ñ�ͷ������
		jtYG.getTableHeader().setUI(new GroupableTableHeaderUI());
		//��ñ�ͷ
		JTableHeader tableHeader = jtYG.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		//���ÿһ�е�����
		TableColumn tc0 = jtYG.getColumnModel().getColumn(0);
		TableColumn tc1 = jtYG.getColumnModel().getColumn(1);
		//����ÿһ�п��
		tc0.setPreferredWidth(70);
		tc1.setPreferredWidth(110);
		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
	}
	
	
	
	//��ʼ���Ҽ��˵�
	private void initRightMenu()
	{
		for(int i=0;i<jMenu.length;i++)
		{
   		 jPop.add(jMenu[i]);
   		 jMenu[i].addActionListener(new ActionListener() {
				@Override//�Ҽ��˵�
				public void actionPerformed(ActionEvent e) {
					//�½�����
					if(e.getSource()==jMenu[0])
					{
						//�����߳�
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								if(currNode!=null)
								{									
									//�������ţɣ�
									String msg=SocketUtil.sendAndGetMsg(GET_MAX_BMID);
									dataGeted=true;
									final int maxid=Integer.parseInt(msg)+1;
									Runnable runnable=new Runnable()
									{
										public void run()
										{
											jTextMC.setText("�½�����");
											jAreaMS.setText("�½���������");
											jTextMC.setFocusable(true);
											
											//********��������ʾ��ֵ*********
											flag=0;//�����½�״̬
											//msgFlag="";
											isSubmit=false;//û���ύ���ݣ�δ�㱣��
											//*****************
											
											BKJTreeNode newNode=new BKJTreeNode
											(
												 "�½�����",
												  new ImageIcon(tpicPath+"new.png"),
												  maxid,//id///�Լ���id
												  currNode.getId()//pid
											);
						 					newNode.setMsg("�½���������");
											currNode.add(newNode);//�ڽڵ��������½ڵ�
											addPidNode=currNode;									
							                jTree.updateUI();  //���½���
							                expandNode(newNode.getId());//չ�����½ڵ�
										}
									};
									
									try {
										SwingUtilities.invokeAndWait(runnable);
									} catch (Exception e) {	e.printStackTrace();}									
							                
								}else
								{
									JOptionPane.showMessageDialog(BMGLPanel.this,"����ѡ�񸸲��ţ�","��ʾ",JOptionPane.WARNING_MESSAGE);
								}
								
							}
						}.start();
						//�����߳�
						LoginWindow.watchThread();	
					}else 
					//ɾ������
					if(e.getSource()==jMenu[1])
					{
						//�����߳�
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								if(currNode!=null)
								{
									//�����ж�����
									if(currNode.getChildCount()>0){
										JOptionPane.showMessageDialog(BMGLPanel.this,"�ò���ӵ���Ӳ��ţ�����ɾ����",
												"��ʾ",JOptionPane.WARNING_MESSAGE);
										return;
									}else{
										String msg=SocketUtil.sendAndGetMsg(GET_YG_BY_BMID+currNode.getId()+GET_YG_BY_BMID);
										
										int num=SocketUtil.strToList(msg).size();
										if(num>0){
											dataGeted=true;
											JOptionPane.showMessageDialog(BMGLPanel.this,"�ò��Ż���Ա��������ɾ����",
													"��ʾ",JOptionPane.WARNING_MESSAGE);
											return;
										}else{
											dataGeted=true;
											int i=JOptionPane.showConfirmDialog(BMGLPanel.this,"��ȷ��Ҫɾ���ò�����",
													"ȷ��",JOptionPane.OK_OPTION,JOptionPane.QUESTION_MESSAGE);
											if(i==0)
											{//ɾ������
												
												if(isSubmit==false){
													currNode.removeFromParent();
									                jTree.updateUI();  
									                isSubmit=true;
													return;
												}
												
												//����ɾ������
												String str=SocketUtil.sendAndGetMsg(DELETE_BM+currNode.getId()+DELETE_BM);
												//ɾ�����ųɹ�
												flushData();
												JOptionPane.showMessageDialog(BMGLPanel.this,"����ɾ���ɹ���","��ʾ",JOptionPane.NO_OPTION);
												
											}
										}
									}
								}
								dataGeted=true;
							}
						}.start();
						//�����߳�
						LoginWindow.watchThread();
					}
				}
			});
   	 	}
	}
	
	//��ʼ���ύ/���水ť
	public void intiSubmit()
	{
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				isSubmit=true;
				if(currNode==null){
					JOptionPane.showMessageDialog(BMGLPanel.this,"��ѡ���ţ�",
							"��ʾ",JOptionPane.WARNING_MESSAGE);
				}else if(jTextMC.getText().trim().length()<=0){
					JOptionPane.showMessageDialog(BMGLPanel.this,"�����벿�����ƣ�",
							"��ʾ",JOptionPane.WARNING_MESSAGE);
				}else if(jAreaMS.getText().trim().length()<=0){
					JOptionPane.showMessageDialog(BMGLPanel.this,"�����벿��������",
							"��ʾ",JOptionPane.WARNING_MESSAGE);
				}else{
					//�����߳�
					dataGeted=false;
					new Thread()
					{
						public void run()
						{
							StringBuffer sb=new StringBuffer();
							sb.append(ADD_BM);
							sb.append(flag+"<->");
							if(flag==0){//flag=0��ʾ��Ӳ��ţ�!=0��ʾ�޸Ĳ���
								sb.append(addPidNode.getId()+"<->");
							}
							else{
								sb.append(currNode.getId()+"<->");
							}
							sb.append(jTextMC.getText().trim()+"<->");
							sb.append(jAreaMS.getText().trim());
							sb.append(ADD_BM);
							String msg=SocketUtil.sendAndGetMsg(sb.toString());
							
							//������ʾ����
							if(flag==0){
								flushData();//ˢ������
								flag=1;
								//msgFlag="update_bm_ok";
								dataGeted=true;
								JOptionPane.showMessageDialog(BMGLPanel.this,"��ϲ��������ӳɹ���",
									"��Ϣ",JOptionPane.NO_OPTION);
							}else
							if(flag==1){
								flushData();//ˢ������
								//msgFlag="add_bm_ok";
								dataGeted=true;
								JOptionPane.showMessageDialog(BMGLPanel.this,"��ϲ�������޸ĳɹ���",
									"��Ϣ",JOptionPane.NO_OPTION);
							}
						}
					}.start();
					//�����߳�
					LoginWindow.watchThread();
				}
			}
		});

	}
	
	
	
	
	
	
	
	
	
	
	//չ��ָ���ڵ㿪ʼ=============================================================
	private BKJTreeNode target=null;
	public void expandNode(int jdid)//jdid-�ڵ�id
	{		
		//����Ŀ��ڵ�����
		target=null;
		//��ȡ���ڵ�
		BKJTreeNode root=(BKJTreeNode)jTree.getModel().getRoot();
		if(root.getId()==jdid){//������ڵ���Ŀ��ڵ�
			return;
		}
		//�ݹ������Ѱ��Ŀ��ڵ�
		dgBl(root,jdid);
		//��ȡ�Ӹ��ڵ㵽Ŀ��ڵ��·��
		System.out.println("target"+target);
		if(target==null)return;
		TreeNode[] tna=target.getPath();
		//ѡ��ָ���Ľڵ㲢չ��
		jTree.setSelectionPath(new TreePath(tna));
		jTree.scrollPathToVisible(new TreePath(tna));
	}
	
	public void dgBl(BKJTreeNode subRoot,int jdid)//�ݹ����Ѱ��Ŀ��ڵ�
	{
		//��ȡ��ǰ�ڵ�ĺ��ӽڵ��б�
		@SuppressWarnings("unchecked")
		Enumeration<BKJTreeNode> e=(Enumeration<BKJTreeNode>)subRoot.children();
		
		//������ǰ�ڵ�ĺ��ӽڵ��б�
		while(e.hasMoreElements())
		{
			//��ȡһ�����ӽڵ�
			BKJTreeNode tempNode=(BKJTreeNode)e.nextElement();
			//�жϴ˺��ӽڵ��Ƿ�ΪĿ��ڵ�
			if(tempNode.getId()==jdid)
			{
				//��ΪĿ��ڵ�
				System.out.println("tempNode:"+tempNode);
				target=tempNode;
				return;
			}
			else
			{
				//����ΪĿ��ڵ����������
				dgBl(tempNode,jdid);
			}
		}
	}
	//չ��ָ���ڵ����=============================================================
	
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		Graphics2D g2 = (Graphics2D) g;  
        // ���ƽ���     ��ʼ����  ��ʼ��ɫ
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
