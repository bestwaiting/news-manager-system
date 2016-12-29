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
	
	//======================================�鿴������˼�¼�б�======================================
	//��������ÿһ�е�����
	Class[] typeArray={Integer.class,String.class,String.class,String.class};
	//��ɫ��ͷ
	String[] head={"��˱��","�����","���ʱ��","������"};
	//��ɫ�������
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
    
    
    //��ű���JScrollPane
	JScrollPane jspSHJL = new JScrollPane(jtCKSH);
	
	JLabel jlTitle =new JLabel("�������������˼�¼�б�");

	MainJFrame mf;
	
	JButton jbBack=new JButton("����");
	
	
	public CKSHPanel(final MainJFrame mf) 
	{
		this.mf=mf;
		this.setLayout(null);
		//��ͷ����
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
	
	//��ʼ����˼�¼��
	public void initTable()
	{
		//���ñ�ͷ������
		jtCKSH.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
		jtCKSH.setRowHeight(30);
		//����ֻ�ܵ�ѡ
		jtCKSH.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtCKSH.setDefaultRenderer(Integer.class, dtcr);
		

		//Ϊ�޸İ�ť��ɾ����ť��ӻ�����
		//SHJLButtonRenderer jButtonRenderer=new SHJLButtonRenderer();
		//jtSHJL.setDefaultRenderer(JButton.class, jButtonRenderer);
		
		//jtGRXW.setDefaultEditor(JButton.class, ygButtonEidtor);
		

		//��ñ�ͷ
		JTableHeader tableHeader = jtCKSH.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
		TableColumn tc0 = jtCKSH.getColumnModel().getColumn(0);
		TableColumn tc1 = jtCKSH.getColumnModel().getColumn(1);
		TableColumn tc2 = jtCKSH.getColumnModel().getColumn(2);
		TableColumn tc3 = jtCKSH.getColumnModel().getColumn(3);
		
		
		//����ÿһ�п��
		tc0.setPreferredWidth(100);
		tc1.setPreferredWidth(160);
		tc2.setPreferredWidth(160);
		tc2.setCellRenderer(new JDateRenderer());
		tc3.setPreferredWidth(710);
		
		
		
		

		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
		tc2.setResizable(false);
		tc3.setResizable(false);
		
	}
	
	
	

	/*
	 * ���ݸ��·���
	 */
	
	//������˼�¼�����ݵķ���
	public void flushData(String xwid) 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmCKSH=new CKSHTableModel(CKSHPanel.this);
				 			//��ʼ���������
				 			tableData = new Object[rowCount][colCount];		
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
			
				 			}
				 			//���ñ��ģ��
				 			jtCKSH.setModel(tmCKSH);	
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
