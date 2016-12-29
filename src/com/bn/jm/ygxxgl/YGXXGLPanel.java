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
 * Ա����Ϣ����
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
	//======================================Ա����Ϣ��======================================
	//��ɫ���ÿһ�е�����
	Class[] typeArrayYG={Integer.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,String.class,JButton.class};
	//��ɫ��ͷ
	String[] headYG={"Ա��ID","��½�˺�","��½����","��ʵ����","�Ա�","��ϵ��ʽ","���ڲ���","��ɫ","��ְ���","�޸�"};
	//��ɫ�������
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
    
    //��¼�ϴε�����У����ڴ������������У��޸ķ����������¼���
	int pressedRow=-1;
	//��¼�ոձ༭�˵��У����ڵõ�����ģ���еı༭�е����ݣ�
    int lastEditRow=-1;
    //��¼��ʾ����Ա����������ְԱ������ְԱ����int
    public int lzyf=2;//-1��ʾ���У�0ֻ��ʾ��ְԱ����1ֻ��ʾ��ְԱ��
    
    //���ڼ�¼�ӷ�������ȡ�Ľ�ɫ��Ϣ����ɫid,��ɫ���ƣ�
    Map<String,String> JSMapforRender=new HashMap<String,String>();
    //���ڼ�¼�ӷ�������ȡ�Ľ�ɫ��Ϣ����ɫ����,��ɫid��
    Map<String,String> JSMapforEditor=new HashMap<String,String>();
    
	//��ű���JScrollPane
	JScrollPane jspYG = new JScrollPane(jtYG);
	
	
	
	JLabel jlTitle =new JLabel("Ա����Ϣ�б�");
	String[] str={"ֻ��ʾ��ְԱ��","ֻ��ʾ��ְԱ��","��ʾ����Ա��"}; 
	JComboBox jcbLzyf=new JComboBox(str);

	JButton jbAddYG=new JButton("���Ա��");
	
	//���ڲ���ѡ������һЩ����
	DefaultTreeModel dtm;//����ѡ���������б�����ģ��
	List<String[]> listBM;//����������Ϣ��List
	
	
	public YGXXGLPanel() 
	{
		this.setLayout(null);
		//��ͷ����
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
	
	
	//��ʼ��Ա����
	public void initTableYG()
	{
		
		//���ñ�ͷ������
        jtYG.getTableHeader().setUI(new GroupableTableHeaderUI());
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
		//�޸İ�ť������
		YGButtonRenderer jButtonRenderer=new YGButtonRenderer();
		jtYG.setDefaultRenderer(JButton.class, jButtonRenderer);
		//�޸İ�ť�༭��
		YGButtonEditor ygButtonEidtor=new YGButtonEditor(this);
		jtYG.setDefaultEditor(JButton.class, ygButtonEidtor);
		

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
		TableColumn tc2 = jtYG.getColumnModel().getColumn(2);
		TableColumn tc3 = jtYG.getColumnModel().getColumn(3);
		TableColumn tc4 = jtYG.getColumnModel().getColumn(4);
		TableColumn tc5 = jtYG.getColumnModel().getColumn(5);
		TableColumn tc6 = jtYG.getColumnModel().getColumn(6);
		TableColumn tc7 = jtYG.getColumnModel().getColumn(7);
		TableColumn tc8 = jtYG.getColumnModel().getColumn(8);
		TableColumn tc9 = jtYG.getColumnModel().getColumn(9);
		
		
		//����ÿһ�п��
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
		
		
		//Ϊ��������ӱ༭���ͻ�����
		tc6.setCellEditor(new YGBMJTreeComboBoxEidtor(this));
		tc6.setCellRenderer(new YGBMRenderer(YGXXGLPanel.this));
		
		//Ϊ��ְ�������ӱ༭���ͻ�����
		tc8.setCellEditor(new YGLZJComboBoxEditor(this));
		tc8.setCellRenderer(new YGLZRenderer());
		
		//Ϊ��ɫ����ӱ༭���ͻ�����
		tc7.setCellEditor(new YGJSJComboBoxEidtor(this));
		tc7.setCellRenderer(new YGJSRenderer(this));
		

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
	
	//��ʼ��������ɫ�����б����ݵ�map�ķ���
	public void initJSMap()
	{
		//������Ϣ�������
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
	
	//Ϊ��ť�������б���Ӽ���
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
			final JFrame newjf = new JFrame("�����Ա��");
 			final NewYGPanel newygpanel=new NewYGPanel();
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				LoginWindow.mf.setEnabled(false);//MainJFrame�Ժ�Ҫ�޸�
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
						
						LoginWindow.mf.setEnabled(true);//MainJFrame�Ժ�Ҫ�޸�
						newjf.dispose();
						newjf.setAlwaysOnTop(false);
					}
				});
				
				
			}
			
		});
		
		
	}
	
	//Ϊ���������ͼ��̼���
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
								int i=JOptionPane.showConfirmDialog(YGXXGLPanel.this, "�Ƿ񱣴��Ա����Ϣ����޸ģ�", "��ʾ", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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
						int i=JOptionPane.showConfirmDialog(YGXXGLPanel.this, "�Ƿ񱣴��Ա����Ϣ����޸ģ�", "��ʾ", JOptionPane.CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
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
	 * ���ݸ��·���
	 */
	
	//����Ա�������ݵķ���
	public void flushData(int lzid) 
	{
			//������Ϣ�������
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
				 			//���±��ģ��
				 			tmYG=new YGTableModel(YGXXGLPanel.this);
				 			//��ʼ���������
				 			tableDataYG = new Object[rowCount][colCount+1];		
				 			//�����е����ݽ���һ����ʱ�ı���  
				 			//������ ������"�޸�"��ťʱ �жϵ�ǰ��������� ��  ������ݵ����� �Ƿ���ͬ  ��ͬ�Ļ� ��ʾ�����Ϣ
				 			origindataYG = new Object[rowCount][colCount+1];
				 			//ͨ��ѭ�������ݿ�������
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
				 			//���ñ��ģ��
				 			jtYG.setModel(tmYG);	
				 			//���±�����
				 			initTableYG();
				    	 }
				     }
				);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	
	//����Ա�������ݵķ���
	public void flushDataBM() 
	{
		//������Ϣ�������
		String msg = GET_BM;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(msg);
		String result=SocketUtil.sendAndGetMsg(sb.toString());
		listBM=SocketUtil.strToList(result);
		dtm=TreeModelUitl.getTreeModel(listBM);
		
	};
	
		
	/*
	 * �����������ݷ���
	 */
	
	//�ж�һ�е������Ƿ��޸���
	public boolean dataChangedForButton()
	{
		if(lastEditRow==-1)
		{
			JOptionPane.showMessageDialog(this,"��Ա�����޸�֮��,�����޸İ�ť��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		if(tableDataYG[lastEditRow][6].equals(origindataYG[lastEditRow][6])&&
		   tableDataYG[lastEditRow][7].equals(origindataYG[lastEditRow][7])&&
		   tableDataYG[lastEditRow][8].equals(origindataYG[lastEditRow][8]))
		{
			JOptionPane.showMessageDialog(this,"����û���޸�,���޸��˱������֮��,�����޸İ�ť����","��ʾ",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}
	
	//�ж�һ�е������Ƿ��޸���
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
	
	//�޸�Ա����Ϣ��Ա��id�����ţ���ɫ����ְ��
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
					JOptionPane.showMessageDialog(YGXXGLPanel.this,"Ա����Ϣ�޸ĳɹ���","��ʾ",JOptionPane.INFORMATION_MESSAGE );
					dataGeted=false;
					flushData(lzyf);
					LoginWindow.watchThread();	
					dataGeted=true;	
				}else
				{
					JOptionPane.showMessageDialog(YGXXGLPanel.this, "Ա����Ϣ�޸�ʧ�ܣ�","��ʾ",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}.start();
		
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
