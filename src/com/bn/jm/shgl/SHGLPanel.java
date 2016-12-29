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
	//======================================���������б�======================================
	//��������ÿһ�е�����
	Class[] typeArray={Integer.class,Integer.class,String.class,String.class,String.class,String.class,String.class,String.class,JButton.class,JButton.class};
	//��ɫ��ͷ
	String[] head={"��˱��","���ű��","���ű���","�ύ��","�ύʱ��","�����","���ʱ��","���״̬","�鿴����","���"};
	//��ɫ�������
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
    
    
    //��ű���JScrollPane
	JScrollPane jspSHJL = new JScrollPane(jtSHJL);
	
	JLabel jlTitle =new JLabel("��˼�¼�б�");
	
	MainJFrame mf;
	
	//=============================================ɸѡ��==============================================
	//ɸѡ
	JScrollPane jspSX = new JScrollPane(); 
	JLabel jlSX = new JLabel("��ѡ��ɸѡ������");
	JCheckBox jcbSHZT = new JCheckBox("���״̬");
	JLabel jlSHZT=new JLabel("��ѡ�����״̬��");
	
	
	JCheckBox jcbTJWSH = new JCheckBox("�ύδ���");
	JCheckBox jcbWTGSH = new JCheckBox("δͨ�����");
	JCheckBox jcbYTGSH = new JCheckBox("��ͨ�����");
	JCheckBox jcbFS = new JCheckBox("��ɱ");

	
	
	
	
	JCheckBox jcbRQ = new JCheckBox("�ύʱ��");
	JLabel jlTemp = new JLabel("��ѡ��ʱ�䷶Χ��");
	JLabel jlQSRQ = new JLabel("��ʼʱ��:");
	JLabel jlJZRQ = new JLabel("��ֹʱ��:");
	//��ʼʱ��
	DateChooserJButton dateChooserQSSJ=new DateChooserJButton();
	//��ֹʱ��
	DateChooserJButton dateChooserJZSJ=new DateChooserJButton();
	
	
	//��������idɸѡ
	JCheckBox jcbXWBH = new JCheckBox("���ű��");
	JLabel jlXWBH=new JLabel("���������ű�ţ�");
	JLabel jlXWID=new JLabel("���ű�ţ�");
	JTextField jtfXWBH=new JTextField(1);
	
	
	//����  ��ť
	JButton jbJS = new JButton("����");
	//=============================================ɸѡ��==============================================
	
	
	public String filter=null;
	public SHGLPanel(MainJFrame mf) 
	{
		this.mf=mf;
		this.setLayout(null);
		//��ͷ����
		jspSHJL.setBounds(25, 50, 1135, 470);
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);
		this.add(jspSHJL);
		initFilterPane();
		addFilterListeners();
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
		jspSX.add(jcbTJWSH);
		jcbTJWSH.setBounds(130, 55, 100, 20);
		jspSX.add(jcbWTGSH);
		jcbWTGSH.setBounds(260, 55, 100, 20);
		
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
		jspSX.add(jlQSRQ);
		jlQSRQ.setBounds(500+30, 55, 60, 20);
		dateChooserQSSJ.setBounds(560+30, 55, 170, 20);
		jspSX.add(dateChooserQSSJ);
		
		// "��ֹʱ��:" ��һ��
		jspSX.add(jlJZRQ);
		jlJZRQ.setBounds(500+30, 80, 60, 20);
		dateChooserJZSJ.setBounds(560+30, 80, 170, 20);
		jspSX.add(dateChooserJZSJ);

		// "���ű�ŵ�ѡ��ť"
		jspSX.add(jcbXWBH);
		jcbXWBH.setBounds(830, 5, 100, 20);
		
		// "���������ű�ţ�"
		jspSX.add(jlXWBH);
		jlXWBH.setBounds(830, 30, 100, 20);
		
		// "�������ű��"
		jspSX.add(jlXWID);
		jlXWID.setBounds(830, 55, 100, 20);
		jspSX.add(jtfXWBH);
		jtfXWBH.setBounds(890, 55, 130, 20);
		
		
		//Ĭ����û��ѡ�е�״̬�����п�ѡ�����
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
		
		// "����"��ť
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
					//���״̬
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
							JOptionPane.showMessageDialog(SHGLPanel.this,"���������ű�ţ�", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
							return;
						}
						if(!xwbh.matches("\\d+"))
						{
							JOptionPane.showMessageDialog(SHGLPanel.this,"��ȷ�����������������", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
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
					//�����߳�
					LoginWindow.watchThread();
				}
			}
		);
	}
	
	//��ʼ����˼�¼��
	public void initTable()
	{
		//���ñ�ͷ������
		jtSHJL.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
		jtSHJL.setRowHeight(30);
		//����ֻ�ܵ�ѡ
		jtSHJL.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtSHJL.setDefaultRenderer(Integer.class, dtcr);
		

		//Ϊ�޸İ�ť��ɾ����ť��ӻ�����
		SHJLButtonRenderer jButtonRenderer=new SHJLButtonRenderer();
		jtSHJL.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		//jtGRXW.setDefaultEditor(JButton.class, ygButtonEidtor);
		

		//��ñ�ͷ
		JTableHeader tableHeader = jtSHJL.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
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
		
		
		//����ÿһ�п��
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(60);
		tc2.setPreferredWidth(175);
		tc3.setPreferredWidth(100);
		tc4.setPreferredWidth(140);
		tc4.setCellRenderer(new JDateRenderer());
		
		
		tc5.setPreferredWidth(100);
		//���table��Ԫ�������
		tc6.setPreferredWidth(140);
		tc6.setCellRenderer(new JDateRenderer());
		tc7.setPreferredWidth(120);
		
		
		tc8.setPreferredWidth(120);
		CKButtonEditor ckButtonEidtor=new CKButtonEditor(this,mf);
		tc8.setCellEditor(ckButtonEidtor);
		
		tc9.setPreferredWidth(120);
		SHButtonEditor shButtonEidtor=new SHButtonEditor(this,mf);
		tc9.setCellEditor(shButtonEidtor);
		
		

		//����ÿһ�д�С���ɱ�
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
	 * ���ݸ��·���
	 */
	
	//������˼�¼�����ݵķ���
	public void flushData() 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmSHJL=new SHJLTableModel(SHGLPanel.this);
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
				 			jtSHJL.setModel(tmSHJL);	
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
				 			tmSHJL=new SHJLTableModel(SHGLPanel.this);
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
				 			jtSHJL.setModel(tmSHJL);	
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
