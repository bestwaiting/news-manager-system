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
	//======================================���������б�======================================
	//��������ÿһ�е�����
	Class[] typeArray={Integer.class,String.class,String.class,String.class,String.class,String.class,JButton.class,JButton.class};
	//��ɫ��ͷ
	String[] head={"���ű��","���ű���","������Դ","�ύʱ��","����״̬","����״̬","�༭","ɾ��"};
	//��ɫ�������
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
    
    
    //��ű���JScrollPane
	JScrollPane jspGRXW = new JScrollPane(jtGRXW);
	
	JLabel jlTitle =new JLabel("���������б�");

	
	MainJFrame mf;
	String ygid;
	
	
	//=============================================ɸѡ��==============================================
	//ɸѡ
	JScrollPane jspSX = new JScrollPane(); 
	JLabel jlSX = new JLabel("��ѡ��ɸѡ������");
	JCheckBox jcbSHZT = new JCheckBox("���״̬");
	JLabel jlSHZT=new JLabel("��ѡ�����״̬��");
	
	
	JCheckBox jcbWTJSH = new JCheckBox("δ�ύ���");
	JCheckBox jcbTJWSH = new JCheckBox("�ύδ���");
	JCheckBox jcbWTGSH = new JCheckBox("δͨ�����");
	JCheckBox jcbYTGSH = new JCheckBox("��ͨ�����");
	JCheckBox jcbFS = new JCheckBox("��ɱ");
	
	
	JCheckBox jcbFBZT = new JCheckBox("����״̬");
	JLabel jlFBZT=new JLabel("��ѡ�񷢲�״̬��");
	JCheckBox jcbWFB = new JCheckBox("δ����");
	JCheckBox jcbYFB = new JCheckBox("�ѷ���");
	JCheckBox jcbYGQ = new JCheckBox("�ѹ���");

	
	
	
	
	JCheckBox jcbRQ = new JCheckBox("�ύʱ��");
	JLabel jlTemp = new JLabel("��ѡ��ʱ�䷶Χ��");
	JLabel jlQSSJ = new JLabel("��ʼʱ��:");
	JLabel jlJZSJ = new JLabel("��ֹʱ��:");
	//��ʼ����
	DateChooserJButton dateChooserQSSJ = new DateChooserJButton();
	//��ֹ����
	DateChooserJButton dateChooserJZSJ = new DateChooserJButton();
	
	
	//����  ��ť
	JButton jbJS = new JButton("����");
	//�鿴���������˼�¼  ��ť
	JButton jbCKSH = new JButton("�鿴�����˼�¼");
	//=============================================ɸѡ��==============================================
	
	//��¼�����ϴε������
	int pressedRow=-1;
	
	public String filter=null;
	public GRXWGLPanel(MainJFrame mf,String ygid) 
	{
		this.ygid=ygid;
		this.mf=mf;
		this.setLayout(null);
		jbCKSH.setBounds(25, 17, 130, 23);
		
		//��ͷ����
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
	
	
	//��ʼ��Ա����
	public void initTable()
	{
		//���ñ�ͷ������
		jtGRXW.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
		jtGRXW.setRowHeight(30);
		//����ֻ�ܵ�ѡ
		jtGRXW.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtGRXW.setDefaultRenderer(Integer.class, dtcr);
		

		//Ϊ�޸İ�ť��ɾ����ť��ӻ�����
		ButtonRenderer jButtonRenderer=new ButtonRenderer();
		jtGRXW.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		

		//��ñ�ͷ
		JTableHeader tableHeader = jtGRXW.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
		TableColumn tc0 = jtGRXW.getColumnModel().getColumn(0);
		TableColumn tc1 = jtGRXW.getColumnModel().getColumn(1);
		TableColumn tc2 = jtGRXW.getColumnModel().getColumn(2);
		TableColumn tc3 = jtGRXW.getColumnModel().getColumn(3);
		TableColumn tc4 = jtGRXW.getColumnModel().getColumn(4);
		TableColumn tc5 = jtGRXW.getColumnModel().getColumn(5);
		TableColumn tc6 = jtGRXW.getColumnModel().getColumn(6);
		TableColumn tc7 = jtGRXW.getColumnModel().getColumn(7);

		
		
		
		//����ÿһ�п��
		tc0.setPreferredWidth(80);
		tc1.setPreferredWidth(220);
		tc2.setPreferredWidth(140);
		tc3.setCellRenderer(new JDateRenderer());
		tc3.setPreferredWidth(175);
		tc4.setPreferredWidth(140);

		//���table��Ԫ�������
		tc5.setPreferredWidth(140);
		
		//Ϊ�޸İ�ť��ӱ༭��
		tc6.setPreferredWidth(120);
		XGButtonEditor xgButtonEidtor=new XGButtonEditor(this,mf);
		tc6.setCellEditor(xgButtonEidtor);
		//Ϊɾ����ť��ӱ༭��
		tc7.setPreferredWidth(120);
		SCButtonEditor scButtonEidtor=new SCButtonEditor(this);
		tc7.setCellEditor(scButtonEidtor);
		
		

		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		tc4.setResizable(false);
		tc5.setResizable(false);
		tc6.setResizable(false);
		tc7.setResizable(false);

		
	}
	
	
	//��ʼ��ɸѡPanel
	public void initFilterPane()
	{
		this.add(jspSX);
		jspSX.setLayout(null);
		jspSX.setBounds(25, 540, 1135, 140);
		
		// "��ѡ��ɸѡ����"
		jspSX.add(jlSX);
		jlSX.setBounds(5, 5, 100, 20);
		
		// "���״̬"
		jspSX.add(jcbSHZT);
		jcbSHZT.setBounds(130, 5, 100, 20);
		
		// "��ѡ�����״̬��"
		jspSX.add(jlSHZT);
		jlSHZT.setBounds(130, 30, 100, 20);
		
		// "�ύδ���"
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
		
		
		// "ʱ��"
		jspSX.add(jcbRQ);
		jcbRQ.setBounds(500+30, 5, 100, 20);
		
		// "��ѡ��ʱ�䷶Χ��"
		jspSX.add(jlTemp);
		jlTemp.setBounds(500+30, 30, 100, 20);
		
		// "��ʼʱ��:" ��һ��
		jspSX.add(jlQSSJ);
		jlQSSJ.setBounds(500+30, 55, 60, 20);
		dateChooserQSSJ.setBounds(560+30, 55, 170, 20);
		//jdpQSRQ.setEditable(false);
		jspSX.add(dateChooserQSSJ);
		
		// "��ֹʱ��:" ��һ��
		jspSX.add(jlJZSJ);
		jlJZSJ.setBounds(500+30, 80, 60, 20);
		dateChooserJZSJ.setBounds(560+30, 80, 170, 20);
		jspSX.add(dateChooserJZSJ);

		// "����״̬"
		jspSX.add(jcbFBZT);
		jcbFBZT.setBounds(830, 5, 100, 20);
		
		// "��ѡ�񷢲�״̬��"
		jspSX.add(jlFBZT);
		jlFBZT.setBounds(830, 30, 100, 20);
		
		// "δ����"
		jspSX.add(jcbWFB);
		jcbWFB.setBounds(830, 55, 100, 20);
		
		jspSX.add(jcbYFB);
		jcbYFB.setBounds(830+130, 55, 100, 20);
		
		jspSX.add(jcbYGQ);
		jcbYGQ.setBounds(830, 55+25, 100, 20);
		
		
		//Ĭ����û��ѡ�е�״̬�����п�ѡ�����
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
		
		
		// "����"��ť
		jspSX.add(jbJS);
		//jbJS.setBounds(650+30, 110, 80, 20);
		jbJS.setBounds(830+130-25+3, 110, 80, 20);
		
	}
	
	/*
	 * ��Ӽ���������
	 */
	public void addTableListeners()
	{//��ӱ��ļ�����
		jtGRXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtGRXW.getSelectedRow();
					if(pressedRow!=row)
					{//�������Ĳ���ͬһ��
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
					//rowʵʱ��¼��������
					final int row = jtGRXW.getSelectedRow();
					if(pressedRow!=row)
					{//�������Ĳ���ͬһ��
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
					JOptionPane.showMessageDialog(GRXWGLPanel.this,"��ѡ��һ�����ţ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
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
					//���״̬
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
					//����״̬
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
					//�����߳�
					LoginWindow.watchThread();
				}
			}
		);
	}
	
	
	

	/*
	 * ���ݸ��·���
	 */
	
	//��ø������ŵķ���
	public void flushData() 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmGRXW=new GRXWTableModel(GRXWGLPanel.this);
				 			//��ʼ���������
				 			tableData = new Object[rowCount][colCount+2];		
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 				   tableData[i][j]=list.get(i)[j];		
				 				}
				 				tableData[i][colCount]=new JButton();
				 				tableData[i][colCount+1]=new JButton();
				 			}
				 			//���ñ��ģ��
				 			jtGRXW.setModel(tmGRXW);	
				 			//���±�����
				 			initTable();
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//ɸѡ֮�������˼�¼�����ݵķ���
	public void flushDataFilter(String sb) 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmGRXW=new GRXWTableModel(GRXWGLPanel.this);
				 			//��ʼ���������
				 			tableData = new Object[rowCount][colCount+2];		
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					if(list.get(i)[j].equals("null"))
				 					{
				 						tableData[i][j]="��������";
				 					}else
				 					{
				 						tableData[i][j]=list.get(i)[j];
				 					}	
				 				}
				 				tableData[i][colCount]=new JButton();
				 				tableData[i][colCount+1]=new JButton();
				 			}
				 			//���ñ��ģ��
				 			jtGRXW.setModel(tmGRXW);	
				 			//���±�����
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
        // ���ƽ���     ��ʼ����  ��ʼ��ɫ
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}

}
