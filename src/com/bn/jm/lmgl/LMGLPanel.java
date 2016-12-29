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
	private JLabel jlLM=new JLabel("��Ŀ�б�");
	private JLabel jlLMXW=new JLabel("��Ŀ��������");
	private JLabel jlXW=new JLabel("����������");
	
	
	//��Ŀ
	private JToolBar jtb1=new JToolBar();
	private String[] jtbName1={"����","����","���","ɾ��"};
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
	
	
	//��Ŀ��������
	private JToolBar jtb2=new JToolBar();
	private String[] jtbName2={"����","����"};
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
	
	//����
	private JToolBar jtb3=new JToolBar();
	private String[] jtbName3={"����","����","����"};
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
	
	//��¼��Ŀ�����ϴε������
	int pressedRowInLM=-1;
	//��¼��Ԫ�������Ƿ�ı�
	boolean isDataChanged=false;
	//��¼�ոձ༭�˵���
    int lastEditRowInLM=-1;
    //��¼�����е���Ŀid
    int lmid=-1;
    //��¼��Ŀ���ű����ϴε������
	int pressedRowInLMXW=-1;
	//��¼���ű����ϴε������
	int pressedRowInXW=-1;
	
	
	private MainJFrame mf;
	
	public LMGLPanel(MainJFrame mf)
	{
		this.setLayout(null);
		this.mf=mf;
		
		//��Ŀ��
		this.add(jlLM);
		jlLM.setBounds(25, 15, 120, 20);
		jlLM.setFont(subtitle);
		this.add(jsLM);
		jsLM.setBounds(25, 70, 280, 580);
		//������
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
		
		
		//��Ŀ�������ű�
		this.add(jlLMXW);
		jlLMXW.setBounds(365, 15, 120, 20);
		jlLMXW.setFont(subtitle);
		this.add(jsLMXW);
		jsLMXW.setBounds(365, 70, 280, 580);
		//������
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
		
		
		//���ű�
		this.add(jlXW);
		jlXW.setBounds(705, 15, 120, 20);
		jlXW.setFont(subtitle);
		this.add(jsXW);
		jsXW.setBounds(705, 70, 280, 580);
		//������
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
	
	
	
	/*
	 * ��Ӽ���������
	 */
	public void addToolBarListeners()
	{
		//��Ŀ������******************����
		jtbButton1[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ����Ŀ��", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "����Ŀ���ö���", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM>0)
				{
					tranLM(tmLM.tableData[pressedRowInLM][0].toString(), tmLM.tableData[pressedRowInLM][1].toString(), 
						tmLM.tableData[pressedRowInLM-1][0].toString(), tmLM.tableData[pressedRowInLM-1][1].toString());
					
				}	
			}
			
		});
		//����
		jtbButton1[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ����Ŀ��", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM>=(tmLM.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "����Ŀ���õף�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLM<(tmLM.tableData.length-1))
				{
					tranLM(tmLM.tableData[pressedRowInLM][0].toString(), tmLM.tableData[pressedRowInLM][1].toString(), 
						tmLM.tableData[pressedRowInLM+1][0].toString(), tmLM.tableData[pressedRowInLM+1][1].toString());
				}	
				
				
				
			}
			
		});
		//�����Ŀ
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
						//�����߳�
						LoginWindow.watchThread();     
			}
			
		});
		//ɾ����Ŀ
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
					//�����߳�
					LoginWindow.watchThread();    
				
			}
			
		});
		
		//��Ŀ�������Ź�����******************
		jtbButton2[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "���������ö���", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW>0)
				{
					tranXW(tmLMXW.tableData[pressedRowInLMXW][0].toString(), tmLMXW.tableData[pressedRowInLMXW][1].toString(), 
							tmLMXW.tableData[pressedRowInLMXW-1][0].toString(), tmLMXW.tableData[pressedRowInLMXW-1][1].toString());
					
				}	
			}
			
		});
		//����
		jtbButton2[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW>=(tmLMXW.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "���������õף�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInLMXW<(tmLM.tableData.length-1))
				{
					tranXW(tmLMXW.tableData[pressedRowInLMXW][0].toString(), tmLMXW.tableData[pressedRowInLMXW][1].toString(), 
							tmLMXW.tableData[pressedRowInLMXW+1][0].toString(), tmLMXW.tableData[pressedRowInLMXW+1][1].toString());
				}	
				
				
				
			}
			
		});
		
		
		//���������Ź�����******************
		jtbButton3[0].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW<=0)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "���������ö���", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW>0)
				{
					tranDFBXW(tmXW.tableData[pressedRowInXW][0].toString(), tmXW.tableData[pressedRowInXW][1].toString(), 
							tmXW.tableData[pressedRowInXW-1][0].toString(), tmXW.tableData[pressedRowInXW-1][1].toString());
					
				}	
			}
			
		});
		//����
		jtbButton3[1].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW>=(tmXW.tableData.length-1))
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "���������õף�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW<(tmXW.tableData.length-1))
				{
					tranDFBXW(tmXW.tableData[pressedRowInXW][0].toString(), tmXW.tableData[pressedRowInXW][1].toString(), 
							tmXW.tableData[pressedRowInXW+1][0].toString(), tmXW.tableData[pressedRowInXW+1][1].toString());
				}	
				
				
				
			}
			
		});
		
		//���Ź���
		jtbButton3[2].addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "ȷ��Ҫ������������Ϊ������?", "��ʾ",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
				if(i==0)//�����"��"
				{
					xgFBZTID(tmXW.tableData[pressedRowInXW][1].toString(), "2");
				}				
			}
			
		});	
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
						if(isDataChanged)//������ݸı���  ���Ƿ񱣴�����
						{
							isDataChanged=false;
							
							if(tmLM.tableData[lastEditRowInLM][2].equals(""))
							{
								JOptionPane.showMessageDialog(LMGLPanel.this, "��Ŀ���Ʋ���Ϊ�գ�", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
								return;
							}
							int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "�Ƿ񱣴����Ŀ����޸�?", "��ʾ",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//�����"��"
							{
								xgLM(tmLM.tableData[lastEditRowInLM][1].toString(),tmLM.tableData[lastEditRowInLM][2].toString());
							}else
							{//�����"��"����"��"
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
						//�����߳�
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
					//rowʵʱ��¼��������
					final int row = jtLM.getSelectedRow();
					if(pressedRowInLM!=row)
					{//�������Ĳ���ͬһ��
						pressedRowInLM = row;
						lmid=Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString());
						if(isDataChanged)//������ݸı���  ���Ƿ񱣴�����
						{
							isDataChanged=false;
							
							if(tmLM.tableData[lastEditRowInLM][2].equals(""))
							{
								JOptionPane.showMessageDialog(LMGLPanel.this, "��Ŀ���Ʋ���Ϊ�գ�", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
								tmLM.tableData[lastEditRowInLM][2]=tmLM.OritableData[lastEditRowInLM][2];
								tmLM.fireTableCellUpdated(lastEditRowInLM, 2);
								return;
							}
							int i = JOptionPane.showConfirmDialog(LMGLPanel.this, "�Ƿ񱣴����Ŀ����޸�?", "��ʾ",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//�����"��"
							{
								xgLM(tmLM.tableData[lastEditRowInLM][1].toString(),tmLM.tableData[lastEditRowInLM][2].toString());
							}else
							{//�����"��"����"��"
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
					//rowʵʱ��¼��������
					final int row = jtLMXW.getSelectedRow();
					if(pressedRowInLMXW!=row)
					{//�������Ĳ���ͬһ��
						pressedRowInLMXW = row;
					}
				}
			}
		);
		
		
		//�����������ű���ӱ�������
		jtXW.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtXW.getSelectedRow();
					if(pressedRowInXW!=row)
					{//�������Ĳ���ͬһ��
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
					//rowʵʱ��¼��������
					final int row = jtXW.getSelectedRow();
					if(pressedRowInXW!=row)
					{//�������Ĳ���ͬһ��
						pressedRowInXW = row;
					}
				}
			}
		);
		
		
		
		
		
		
		
		
	}
	
	public void addButtonListeners()
	{//��Ӱ�ť�ļ�����
		//�������Ű�ť��left
		leftBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(pressedRowInLM==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ����Ŀ��", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				if(pressedRowInXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				xgLMID(tmXW.tableData[pressedRowInXW][1].toString(), tmLM.tableData[pressedRowInLM][1].toString());
			}
			
		});
		
		
		//�������Ű�ť��right
		rightBtn.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(pressedRowInLMXW==-1)
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ�����ţ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				xgLMID(tmLMXW.tableData[pressedRowInLMXW][1].toString(), "0");
			}
			
		});
		
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
				 			tmLM=new LMTableModel(LMGLPanel.this);
				 			//��ʼ���������
				 			tmLM.tableData = new Object[rowCount][colCount];
				 			tmLM.OritableData = new Object[rowCount][colCount];
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmLM.tableData[i][j]=list.get(i)[j];
				 					tmLM.OritableData[i][j]=list.get(i)[j];
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
				 			//���±��ģ��
				 			tmLMXW=new LMXWTableModel(LMGLPanel.this);
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
				 			initTable(jtLMXW);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//������Ŀ�����ݵķ���
	public void flushDataXW() 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmXW=new XWTableModel(LMGLPanel.this);
				 			//��ʼ���������
				 			tmXW.tableData = new Object[rowCount][colCount];
				 			//ͨ��ѭ�������ݿ�������
				 			for(int i=0;i<rowCount;i++)
				 			{
				 				for(int j=0;j<colCount;j++)
				 				{
				 					tmXW.tableData[i][j]=list.get(i)[j];
				 				}
				 			}
				 			//���ñ��ģ��
				 			jtXW.setModel(tmXW);	
				 			//���±�����
				 			initTable(jtXW);
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	
	//�޸���Ŀ����
	public void xgLM(final String lmid,final String lmmc)//��ɫID  ��ɫ����
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = XG_LM;//�޸Ľ�ɫ����
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
					JOptionPane.showMessageDialog(LMGLPanel.this, "�޸ĳɹ���", "��ʾ", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataLM();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "������ϣ��޸�ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	
	//�����Ŀ
	private void addLM()
	{
	  StringBuilder sb=new StringBuilder();
	  sb.append(ADD_LM);
	  sb.append(ADD_LM);
	  SocketUtil.sendAndGetMsg(sb.toString());	
	}
	
	
	//ɾ����Ŀ
	public void delLM(final int lmid)//������ ��ɫID
	{
		if(lmid==-1)
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "��ѡ����Ŀ��", "��ʾ", JOptionPane.NO_OPTION);
			return;
		}
		//������Ϣ�������
		String msg = DEL_LM;//�ܷ�ɾ���ý�ɫ
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(lmid);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());
		if(result.equals("ok"))
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "��Ŀɾ���ɹ���", "��ʾ", JOptionPane.NO_OPTION);
		}else if(result.equals("hasNew"))
		{
			JOptionPane.showMessageDialog(LMGLPanel.this, "����Ŀ�º������ţ�ɾ����Ŀʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
		}
				
	}
	
	//������Ŀ˳��
	public void tranLM(final String sxid1,final String lmid1,final String sxid2,final String lmid2)//��ɫID  ��ɫ����
	{
		//sxid1 lmid2
		//sxid2 lmid1
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = TRAN_LM;//�޸Ľ�ɫ����
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(lmid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(lmid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//������Ŀ��Ϣ
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataLM();
				dataGeted=true;
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	
	//������Ŀ˳��
	public void tranXW(final String sxid1,final String xwid1,final String sxid2,final String xwid2)//��ɫID  ��ɫ����
	{
		//sxid1 xwid2
		//sxid2 xwid1
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = TRAN_XW;//�޸Ľ�ɫ����
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(xwid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(xwid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//������Ŀ��Ϣ
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataLMXW(Integer.parseInt(tmLM.tableData[pressedRowInLM][1].toString()));
				dataGeted=true;
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	
	//��������������˳��
	public void tranDFBXW(final String sxid1,final String xwid1,final String sxid2,final String xwid2)//��ɫID  ��ɫ����
	{
		//sxid1 xwid2
		//sxid2 xwid1
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = TRAN_XW;//�޸Ľ�ɫ����
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(sxid1+"<->");
				sb.append(xwid2+"<->");
				sb.append(sxid2+"<->");
				sb.append(xwid1);
				sb.append(msg);
				SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				//������Ŀ��Ϣ
				dataGeted=false;
				LoginWindow.watchThread();
				flushDataXW();
				dataGeted=true;
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	
	
	//�޸���Ŀid
	public void xgLMID(final String xwid,final String lmid)//����ID  ��Ŀid
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = XG_LMID;//�޸Ľ�ɫ����
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
					//������Ŀ����
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataXW();
					dataGeted=true;
					
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "������ϣ��޸�ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	
	//�޸����ŷ���״̬id
	public void xgFBZTID(final String xwid,final String fbztid)//��ɫID  ��ɫ����
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = XG_FBZTID;//�޸Ľ�ɫ����
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
					JOptionPane.showMessageDialog(LMGLPanel.this, "�������ù��ڳɹ���", "��ʾ", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataXW();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(LMGLPanel.this, "������ϣ��޸�ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
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
