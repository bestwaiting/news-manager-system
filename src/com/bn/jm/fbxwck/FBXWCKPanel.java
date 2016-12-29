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
	private JLabel jlTitle=new JLabel("�������Ų鿴");
	private JLabel jlLM=new JLabel("��Ŀ�б�");
	private JLabel jlLMXW=new JLabel("��Ŀ��������");
	
	
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
	
	
	//��¼��Ŀ�����ϴε������
	int pressedRowInLM=-1;
    //��¼��Ŀ���ű����ϴε������
	int pressedRowInLMXW=-1;
	
	int lmid;
	int xwid;
	
	
	MainJFrame mf;
	
	public FBXWCKPanel(MainJFrame mf) 
	{
		this.mf=mf;	
		this.setLayout(null);
		//��ͷ����
		jlTitle.setBounds(520, 20, 200, 20);
		jlTitle.setFont(subtitle);
		this.add(jlTitle);
		
		
		//��Ŀ��
		this.add(jlLM);
		jlLM.setBounds(25, 15+30+5, 120, 20);
		jlLM.setFont(jltitle);
		this.add(jsLM);
		jsLM.setBounds(25, 70+10, 280, 580);
		
		
		//��Ŀ�������ű�
		this.add(jlLMXW);
		jlLMXW.setBounds(365-10, 15+30+5, 120, 20);
		jlLMXW.setFont(jltitle);
		this.add(jsLMXW);
		jsLMXW.setBounds(365-10, 70+10, 780+20, 580);
		//280  780
		
		addTableListeners();
		
	}
	
	
	//��ʼ�����
	public void initTable(JTable jtLM)
	{
		//���ñ�ͷ������
		jtLM.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
		jtLM.setRowHeight(30);
		//����ֻ�ܵ�ѡ
		jtLM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtLM.setDefaultRenderer(Integer.class, dtcr);
		

		//��ñ�ͷ
		JTableHeader tableHeader = jtLM.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
		TableColumn tc0 = jtLM.getColumnModel().getColumn(0);
		TableColumn tc1 = jtLM.getColumnModel().getColumn(1);
		TableColumn tc2 = jtLM.getColumnModel().getColumn(2);
		
		
		//����ÿһ�п��
		tc0.setPreferredWidth(50);
		tc1.setPreferredWidth(50);
		tc2.setPreferredWidth(160);
		
		
		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
	}
	
	
	
	
	//��ʼ�����
	public void initTableLMXW(JTable jtLM)
	{
		//���ñ�ͷ������
		jtLM.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
		jtLM.setRowHeight(30);
		//����ֻ�ܵ�ѡ
		jtLM.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtLM.setDefaultRenderer(Integer.class, dtcr);
		

		//��ñ�ͷ
		JTableHeader tableHeader = jtLM.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
		TableColumn tc0 = jtLM.getColumnModel().getColumn(0);
		TableColumn tc1 = jtLM.getColumnModel().getColumn(1);
		TableColumn tc2 = jtLM.getColumnModel().getColumn(2);
		TableColumn tc3 = jtLM.getColumnModel().getColumn(3);
		TableColumn tc4 = jtLM.getColumnModel().getColumn(4);
		TableColumn tc5 = jtLM.getColumnModel().getColumn(5);
		
		
		//����ÿһ�п��
		tc0.setPreferredWidth(50);
		tc1.setPreferredWidth(50);
		tc2.setPreferredWidth(160);
		tc3.setPreferredWidth(290+20);
		tc4.setPreferredWidth(130);
		tc4.setCellRenderer(new JDateRenderer());
		tc5.setPreferredWidth(80);
		
		
		
		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		tc4.setResizable(false);
		tc5.setResizable(false);
	}
	
	
	public void addTableListeners()
	{//��ӱ��ļ�����
		jtLM.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtLM.getSelectedRow();
					if(pressedRowInLM!=row)
					{//�������Ĳ���ͬһ��
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
						//�����߳�
						LoginWindow.watchThread();
					}
				}
				
			}
		);
		
		
		//����Ŀ������ӱ�������
		jtLMXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtLMXW.getSelectedRow();
					if(pressedRowInLMXW!=row)
					{//�������Ĳ���ͬһ��
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
	 * ���ݸ��·���
	 */
	
	//������Ŀ�����ݵķ���
	public void flushDataLM() 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmLM=new LMTableModel(FBXWCKPanel.this);
				 			//��ʼ���������
				 			tmLM.tableData = new Object[rowCount][colCount];
				 			//tmLM.OritableData = new Object[rowCount][colCount];
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLM.tableData[i][j]=list.get(i)[j];
				 					//tmLM.OritableData[i][j]=list.get(i)[j];
				 				}
				 			}
				 			//���ñ��ģ��
				 			jtLM.setModel(tmLM);	
				 			//���±�����
				 			initTable(jtLM);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//������Ŀ�������ű����ݵķ���
	public void flushDataLMXW(int lmid) 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmLMXW=new LMXWTableModel(FBXWCKPanel.this);
				 			//��ʼ���������
				 			tmLMXW.tableData = new Object[rowCount][colCount];
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLMXW.tableData[i][j]=list.get(i)[j];
				 					
				 				}
				 			}
				 			//���ñ��ģ��
				 			jtLMXW.setModel(tmLMXW);	
				 			//���±�����
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
        // ���ƽ���     ��ʼ����  ��ʼ��ɫ
        g2.setPaint(new GradientPaint(0, 0, C_START,0,  getHeight(), C_END));   
        g2.fillRect(0, 0, getWidth(), getHeight());  
	}
	
}
