package com.bn.jm.jsqxgl;



import static com.bn.core.Constant.SC_QX;
import static com.bn.core.Constant.ADD_JSQX_BY_QXMC;
import static com.bn.core.Constant.GET_BJYDQX;
import static com.bn.core.Constant.SC_JS;
import static com.bn.core.Constant.XG_JS;
import static com.bn.core.Constant.GET_JBQX;
import static com.bn.core.Constant.GET_JS;
import static com.bn.core.Constant.ADD_JS;

import static com.bn.core.Constant.dataGeted;
import static com.bn.core.Constant.C_END;
import static com.bn.core.Constant.C_START;
import static com.bn.core.Constant.subtitle;




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
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import com.bn.jm.jsqxgl.QXTableModel;
import com.bn.jm.tableheader.GroupableTableHeaderUI;
import com.bn.util.SocketUtil;
import com.bn.jm.jsqxgl.JSTableModel;
import com.bn.jm.tableheader.GroupableTableHeader;

@SuppressWarnings({"serial","rawtypes"})
public class JSQXGLPanel extends JPanel
{
	//======================================��ɫ��======================================
	//��ɫ���ÿһ�е�����
	Class[] typeArrayJS={Integer.class,String.class};
	//��ɫ��ͷ
	String[] headJS={"��ɫID","��ɫ����"};
	//��ɫ�������
	Vector<String[]> tableDataJS;
	Vector<String[]> origindataJS;
	
	JSTableModel tmJS;
	//��ɫ���
	JTable jtJS = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    //��ű���JScrollPane
	JScrollPane jspJS = new JScrollPane(jtJS);

	//��ɫ��ͷ
	JLabel jlJSTableHead = new JLabel("��ɫ��");
	
	JButton jbAddJS = new JButton("��ӽ�ɫ");
	JButton jbDeleteJS = new JButton("ɾ����ɫ");
	
	//��¼��ɫ�����ϴε������
	int pressedRowInJS=-1;
	//��¼��Ԫ�������Ƿ�ı�
	boolean isDataChanged=false;
	//��¼�ոձ༭�˵���
    int lastEditRowInJS=-1;
	//======================================��ɫ��======================================
	
	
	//======================================Ȩ�ޱ�======================================

	//Ȩ�ޱ��ÿһ�е�����
	Class[] typeArrayQX={Integer.class,String.class};
	//Ȩ�ޱ�ͷ
	String[] headQX={"Ȩ��ID","Ȩ������"};
	//��ɫ�������
	Vector<String[]> tableDataQX;
	Vector<String[]> origindataQX;
	
	QXTableModel tmQX;
	//Ȩ�ޱ��
	JTable jtQX = new JTable()
	{
        @Override
        protected JTableHeader createDefaultTableHeader() 
        {
            return new GroupableTableHeader(columnModel);
        }
    };
    //��ű���JScrollPane
	JScrollPane jspQX = new JScrollPane(jtQX);

	//Ȩ�ޱ�ͷ
	JLabel jlQXTableHead = new JLabel("Ȩ�ޱ�");

	JButton jbAddJBQX = new JButton("���Ȩ��");
	JComboBox jcbTJQX = new JComboBox();
    Object[] message = { "��ѡ�����Ȩ��", new JScrollPane(jcbTJQX)};
    JOptionPane paneTJQX = new JOptionPane(message,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
    JDialog dialogTJQX = paneTJQX.createDialog(LoginWindow.mf, "���Ȩ��");
	JButton jbDeleteJBQX = new JButton("ɾ��Ȩ��");
	//======================================Ȩ�ޱ�======================================
	
	
	
	
	
	
	
	
	public JSQXGLPanel()
	{
		this.setLayout(null);	

		//======================================��ɫ��======================================
		jspJS.setBounds(25,40, 496, 264);
		//��ͷ����
		jlJSTableHead.setBounds(250, 10, 120, 30);
		jlJSTableHead.setFont(subtitle); 
		//��ť����
		jbAddJS.setBounds(530, 60, 80, 30);
		jbAddJS.setOpaque(false);
		jbDeleteJS.setBounds(530, 120, 80, 30);
		jbDeleteJS.setOpaque(false);
		//======================================��ɫ��======================================
		
		
		//======================================Ȩ�ޱ�======================================
		jspQX.setBounds(25,340, 496, 264);
		//��ͷ����
		jlQXTableHead.setBounds(250, 310, 120, 30);
		jlQXTableHead.setFont(subtitle); 
		//��ť����
		jbAddJBQX.setBounds(530, 360, 80, 30);
		jbAddJBQX.setOpaque(false);
		jbDeleteJBQX.setBounds(530, 420, 80, 30);
		jbDeleteJBQX.setOpaque(false);
		//======================================Ȩ�ޱ�======================================
		addTableListeners();
		addButtonListeners();
		
		this.add(jbAddJS);
		this.add(jbDeleteJS);
		this.add(jspJS);
		this.add(jlJSTableHead);
		
		this.add(jbAddJBQX);
		this.add(jbDeleteJBQX);
		this.add(jspQX);
		this.add(jlQXTableHead);
	}
	
	
	//��ʼ����ɫ��
	public void initTableJS()
	{
		//���ñ�ͷ������
        jtJS.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
        jtJS.setRowHeight(30);
		//����ֻ�ܵ�ѡ
        jtJS.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtJS.setDefaultRenderer(Integer.class, dtcr);

		//��ñ�ͷ
		JTableHeader tableHeader = jtJS.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		

		//���ÿһ�е�����
		TableColumn tc0 = jtJS.getColumnModel().getColumn(0);
		TableColumn tc1 = jtJS.getColumnModel().getColumn(1);
		
		//����ÿһ�п��
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(155);

		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
	}
	
	//��ʼ��Ȩ�ޱ�
	public void initTableQX()
	{
		//���ñ�ͷ������
        jtQX.getTableHeader().setUI(new GroupableTableHeaderUI());
		//�����и�
        jtQX.setRowHeight(30);
		//����ֻ�ܵ�ѡ
        jtQX.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//���table��Ԫ�������
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		//���ñ�������ݾ���
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		//���õ�һ�о���
		jtQX.setDefaultRenderer(Integer.class, dtcr);
		//��ñ�ͷ
		JTableHeader tableHeader = jtQX.getTableHeader();  
		//��ñ�ͷ������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader .getDefaultRenderer(); 
		//��������
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		//����в����ƶ�    
		tableHeader.setReorderingAllowed(false);
		//���ÿһ�е�����
		TableColumn tc0 = jtQX.getColumnModel().getColumn(0);
		TableColumn tc1 = jtQX.getColumnModel().getColumn(1);
		//����ÿһ�п��
		tc0.setPreferredWidth(60);
		tc1.setPreferredWidth(155);
		//����ÿһ�д�С���ɱ�
		tc0.setResizable(false);
		tc1.setResizable(false);
	}
	

	/*
	 * ��Ӽ���������
	 */
	public void addTableListeners()
	{//��ӱ��ļ�����
		jtJS.addMouseListener
		(
			new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent arg0)
				{
					final int row = jtJS.getSelectedRow();
					if(pressedRowInJS!=row)
					{//�������Ĳ���ͬһ��
						pressedRowInJS = row;
						if(isDataChanged)//������ݸı���  ���Ƿ񱣴�����
						{
							isDataChanged=false;
							if(tableDataJS.get(lastEditRowInJS)[1].equals(""))
							{
								JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ɫ���Ʋ���Ϊ�գ�", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
								return;
							}
							int i = JOptionPane.showConfirmDialog(JSQXGLPanel.this, "�Ƿ񱣴�Խ�ɫ����޸�?", "��ʾ",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//�����"��"
							{
								xgJS(tableDataJS.get(lastEditRowInJS)[0],tableDataJS.get(lastEditRowInJS)[1]);
							}else
							{//�����"��"����"��"
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
							}
						}
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataJSQX(Integer.parseInt(tableDataJS.get(row)[0]));
								dataGeted=true;
							}
						}.start();
						//�����߳�
						LoginWindow.watchThread();
					}
				}
			}
		);
		
		jtJS.addKeyListener
		(
			new KeyAdapter() 
			{

				@Override
				public void keyReleased(KeyEvent e) 
				{
					//rowʵʱ��¼��������
					final int row = jtJS.getSelectedRow();
					if(pressedRowInJS!=row)
					{//�������Ĳ���ͬһ��
						pressedRowInJS = row;
						if(isDataChanged)//������ݸı���  ���Ƿ񱣴�����
						{
							isDataChanged=false;
							if(tableDataJS.get(lastEditRowInJS)[1].equals(""))
							{
								JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ɫ���Ʋ���Ϊ�գ�", "��ʾ",JOptionPane.INFORMATION_MESSAGE);
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
								return;
							}
							int i = JOptionPane.showConfirmDialog(JSQXGLPanel.this, "�Ƿ񱣴�Խ�ɫ����޸�?", "��ʾ",JOptionPane.CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
							if(i==0)//�����"��"
							{
								xgJS(tableDataJS.get(lastEditRowInJS)[0],tableDataJS.get(lastEditRowInJS)[1]);
							}else
							{//�����"��"����"��"
								tableDataJS.get(lastEditRowInJS)[1]=origindataJS.get(lastEditRowInJS)[1];
								tmJS.fireTableCellUpdated(lastEditRowInJS, 1);
							}
						}
						dataGeted=false;
						new Thread()
						{
							public void run()
							{
								flushDataJSQX(Integer.parseInt(tableDataJS.get(row)[0]));
								dataGeted=true;
							}
						}.start();
						//�����߳�
						LoginWindow.watchThread();
					}
				}
			}
		);
		
	}
	
	public void addButtonListeners()
	{//��Ӹ���Button�ļ�����
		jbAddJS.addActionListener
		(//��ӽ�ɫ
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					addJS();
				}
			}
		);
		jbDeleteJS.addActionListener
		(//ɾ����ɫ
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int row = jtJS.getSelectedRow();
					if(row!=-1)
					{
						scJS(tableDataJS.get(row)[0]);//ɾ����ɫ
					}
				}
			}
		);
		jbAddJBQX.addActionListener
		(//��ӻ���Ȩ��
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int rowJS = jtJS.getSelectedRow();
					if(rowJS==-1)
					{//Ĭ�ϵĽ�ɫ��ѡ����ǵ�һ��
						rowJS=0;
					}
					String jsid = tableDataJS.get(rowJS)[0];
					//��ӵ�ǰ��ɫ�����е�Ȩ��
					add_bjydqx(jsid);
				}
			}
		);
		jbDeleteJBQX.addActionListener
		(//ɾ������Ȩ��
			new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					int rowJS = jtJS.getSelectedRow();
					int rowQX = jtQX.getSelectedRow();
					if(rowJS==-1)
					{
						JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ѡ��ĳ�ֽ�ɫ��", "��ʾ", JOptionPane.NO_OPTION);
						return;
					}
					if(rowQX==-1)
					{
						JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ѡ��ĳ��Ȩ�ޣ�", "��ʾ", JOptionPane.NO_OPTION);
						return;
					}
					String jsid = tableDataJS.get(rowJS)[0];
					String qxid = tableDataQX.get(rowQX)[0];
					scQX(jsid,qxid);//ɾ��ĳ��ɫ��Ӧ��Ȩ��
				}
			}
		);
		
		
		
	}
	
	
	/*
	 * �޸ķ��������ݷ���
	 */
	//�޸Ľ�ɫ����
	public void xgJS(final String jsid,final String jsmc)//��ɫID  ��ɫ����
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = XG_JS;//�޸Ľ�ɫ����
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append("<->");
				sb.append(jsmc);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "�޸ĳɹ���", "��ʾ", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataJS();
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "������ϣ��޸�ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	//��ӽ�ɫ
	public void addJS()
	{
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = ADD_JS;//��ӽ�ɫ
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				if(result.equals("ok"))
				{
					flushDataJS();
				}
				else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "������ϣ����ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
				dataGeted=true;
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	//ɾ����ɫ
	//�жϸý�ɫ����û��Ա��  
	public void scJS(final String jsid)//������ ��ɫID
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = SC_JS;//�ܷ�ɾ���ý�ɫ
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.equals("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ɫɾ���ɹ���", "��ʾ", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					flushDataJS();
					dataGeted=true;
					flushDataJSQX(1000);
					
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "�ý�ɫ�»���Ա����ɾ��ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	//���Ȩ��(��Ӳ����е�)
	public void add_bjydqx(final String jsid)
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = GET_BJYDQX;//��ò�����Ȩ��
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				List<String[]> list= SocketUtil.strToList(result);
				dataGeted=true;
				if(list.size()==0)
				{//�����ǰ��ɫ��������Ȩ��
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "��ǰԱ���Ѿ�������Ȩ�ޣ�", "��ʾ", JOptionPane.NO_OPTION);
					return;
				}
				//�������е�Ȩ�޷Ž������б�
				jcbTJQX.removeAllItems();
				for(int i=0;i<list.size();i++)
				{
					jcbTJQX.addItem(list.get(i)[1]);
				}
				dialogTJQX.setVisible(true);
				dialogTJQX.setAlwaysOnTop(true);
				if(paneTJQX.getValue()!=null)//�����û���ѡֵ�� null ��ʾ�û�û��ѡȡ�κ����ر��˴��ڡ����򣬷���ֵ��Ϊ�ڴ˶������������ѡ��֮һ��
				{//���û�� ��
					if(Integer.parseInt(paneTJQX.getValue().toString())==0)
					{//������ "ȷ��"
						String selectedQX = jcbTJQX.getSelectedItem().toString();
						//���ѡ���Ȩ��
						msg = ADD_JSQX_BY_QXMC;//���Ȩ��
						sb = new StringBuilder();
						sb.append(msg);
						sb.append(jsid);
						sb.append("<->");
						sb.append(selectedQX);
						sb.append(msg);
						dataGeted=false;
						LoginWindow.watchThread();
						result =SocketUtil.sendAndGetMsg(sb.toString());
						dataGeted=true;
						if(result.equals("ok"))
						{
							JOptionPane.showMessageDialog(JSQXGLPanel.this, "Ȩ����ӳɹ���", "��ʾ", JOptionPane.NO_OPTION);
							dataGeted=false;
							LoginWindow.watchThread();
							flushDataJSQX(Integer.parseInt(tableDataJS.get(jtJS.getSelectedRow())[0]));
							dataGeted=true;
						}else
						{
							JOptionPane.showMessageDialog(JSQXGLPanel.this, "������ϣ����ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	//ɾ��Ȩ��
	public void scQX(final String jsid,final String qxid)//������ ��ɫID
	{
		//�����߳�
		dataGeted=false;
		new Thread()
		{
			public void run()
			{
				//������Ϣ�������
				String msg = SC_QX;//ɾ��Ȩ��
				StringBuilder sb = new StringBuilder();
				sb.append(msg);
				sb.append(jsid);
				sb.append("<->");
				sb.append(qxid);
				sb.append(msg);
				String result =SocketUtil.sendAndGetMsg(sb.toString());
				dataGeted=true;
				if(result.endsWith("ok"))
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "Ȩ��ɾ���ɹ���", "��ʾ", JOptionPane.NO_OPTION);
					dataGeted=false;
					LoginWindow.watchThread();
					int rowJS = jtJS.getSelectedRow();
					if(rowJS==-1)
					{//Ĭ�ϵĽ�ɫ��ѡ����ǵ�һ��
						rowJS=0;
					}
					flushDataJSQX(Integer.parseInt(tableDataJS.get(rowJS)[0]));
					dataGeted=true;
				}else
				{
					JOptionPane.showMessageDialog(JSQXGLPanel.this, "������ϣ�ɾ��ʧ�ܣ�", "��ʾ", JOptionPane.WARNING_MESSAGE);
				}
			}
		}.start();
		//�����߳�
		LoginWindow.watchThread();
	}
	
	








	/*
	 * ���ݸ��·���
	 */
	
    //���½�ɫ������ģ��
	public void flushDataJS()
	{
		//������Ϣ�������
		String msg = GET_JS;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(msg);
		String result =SocketUtil.sendAndGetMsg(sb.toString());//"<#GET_JS#>""<#GET_JS#>"
		final List<String[]> list = SocketUtil.strToList(result);
		try {
			SwingUtilities.invokeAndWait
			(
			     new Runnable()
			     {
			    	 public void run()
			    	 {
			 			int rowCount = list.size();
			 			//���±��ģ��
			 			tmJS=new JSTableModel(JSQXGLPanel.this);
			 			//��ʼ���������
			 			tableDataJS = new Vector<String[]>();		
			 			//�����е����ݽ���һ����ʱ�ı���  
			 			//������ ������"�޸�"��ťʱ �жϵ�ǰ��������� ��  ������ݵ����� �Ƿ���ͬ  ��ͬ�Ļ� ��ʾ�����Ϣ
			 			origindataJS =  new Vector<String[]>();	
			 			//ͨ��ѭ�������ݿ�������
			 			for(int i=0;i<rowCount;i++)
			 			{
			 				tableDataJS.add(list.get(i));
			 				origindataJS.add(list.get(i));
			 			}	
			 			//���ñ��ģ��
			 			jtJS.setModel(tmJS);	
			 			//���±�����
			 			initTableJS();
			    	 }
			     }
			);
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	//��ɫȨ��
	public void flushDataJSQX(int jsid)
	{
		//������Ϣ�������
		String msg = GET_JBQX;
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(jsid);
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
			 			int rowCount = list.size();
			 			//���±��ģ��
			 			tmQX=new QXTableModel(JSQXGLPanel.this);
			 			//��ʼ���������
			 			tableDataQX = new Vector<String[]>();		
			 			//�����е����ݽ���һ����ʱ�ı���  
			 			//������ ������"�޸�"��ťʱ �жϵ�ǰ��������� ��  ������ݵ����� �Ƿ���ͬ  ��ͬ�Ļ� ��ʾ�����Ϣ
			 			origindataQX =  new Vector<String[]>();	
			 			//ͨ��ѭ�������ݿ�������
			 			for(int i=0;i<rowCount;i++)
			 			{
			 				tableDataQX.add(list.get(i));
			 				origindataQX.add(list.get(i));
			 			}
			 			//���ñ��ģ��
			 			jtQX.setModel(tmQX);	
			 			//���±�����
			 			initTableQX();
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
	
	
	/*
	 * ��¼״̬������ֵ������
	 * 1.pressedRowInJSͨ������������������jtJS.getSelectedRow()��ֵ��
	 * 2.lastEditRowInJSͨ����ɫ������ģ��JSTableModel�е�setValueAt
	 * 3.isDataChangedͨ����ɫ������ģ��JSTableModel�е�setValueAt
	 */
	
	/*
	 * �޸ı��ֵ���ܵ�ʵ�֣�
	 * ͨ��JSTableModel�е�setValueAt����lastEditRowInJS��isDataChanged��ֵ��
	 * ͨ���������������е�jtJS.getSelectedRow()����pressedRowInJS��ֵ��
	 * ÿ�ε�������pressedRowInJS�Ƚϣ���ͬ����������ͬһ�У��鿴isDataChangedֵ�������Ƿ�ı�
	 * ���ݸı��˾͸��·��������ݣ����Ҵӷ�������ѯ������н�ɫ��Ȩ��
	 */
}
