package com.bn.jm.ygxxgl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


import com.bn.jm.LoginWindow;
import com.bn.util.BKJTreeCellRenderer;
import com.bn.util.BKJTreeNode;
import com.bn.util.JCOMDataObj;
import com.bn.util.SocketUtil;
import com.bn.util.TreeModelUitl;
import com.sunking.swing.JTreeComboBox;

import static com.bn.core.Constant.ADD_YG;


import static com.bn.core.Constant.GET_BM;
import static com.bn.core.Constant.GET_JS;
import static com.bn.core.Constant.dataGeted;


public class NewYGPanel extends JPanel
{
	
	private JLabel []jlYGXX=//Ա����ϢJLabel
	{
			new JLabel("��½�˺�"),new JLabel("��½����"), new JLabel("��ʵ����"),new JLabel("��ϵ��ʽ"),new JLabel("Ա���Ա�"),
			new JLabel("���ڲ���"),new JLabel("Ա����ɫ")
	};
	
	String ZH="ZH";
	String MM="MM";
	String XM="XM";
	String LXFS="LXFS";
	
	
	 JTextField jtYGXX[]=//Ա����ϢJTextField
	{
		new JTextField(1),new JTextField(1),new JTextField(1),new JTextField(1)
	};
	
	private String strXB[]={"��","Ů"};
	private JComboBox ygxb=new JComboBox(strXB);//Ա���Ա�
	private String XB="XB";
	
	
	private JTree jtBMXZ=new JTree();//����ѡ����
	private JTreeComboBox jtcom = new JTreeComboBox(jtBMXZ);//����ѡ����
	private String BMID="BMID";
	
	private JComboBox jcomYGJS = new JComboBox();//��ɫ
	private String JS="JS";
	
	private JButton submit=new JButton("���");
	
	
	public NewYGPanel() 
	{
		this.setLayout(null);
		//���JLabel
		for(int i=0;i<jlYGXX.length;i++){
			this.add(jlYGXX[i]);
			jlYGXX[i].setBounds(25, 40+30*i-25, 60, 20);
			
		}
		//���JTextFiled
		for(int i=0;i<jtYGXX.length;i++){
			this.add(jtYGXX[i]);
			jtYGXX[i].setBounds(78, 40+30*i-25, 120, 20);
			jtYGXX[i].setText(null);
		}
		
		//��ӻس�����
		jtYGXX[0].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[1].requestFocus();}});
		jtYGXX[1].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[2].requestFocus();}});
		jtYGXX[2].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[3].requestFocus();}});
		jtYGXX[3].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ygxb.requestFocus();}});
		
		
		//��Ӱ�ť
		this.add(submit);
		submit.setBounds(120, 40+30*7-17, 80, 25);
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//������֤
				for(int i=0;i<jtYGXX.length;i++){
					if(jtYGXX[i].getText().trim().length()<=0){
						JOptionPane.showMessageDialog(NewYGPanel.this,"������"+jlYGXX[i].getText()+"��",	"��ʾ",JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(i==0)
					{
						String text=jtYGXX[i].getText().trim();
						if(!text.matches("\\w+"))
						{
							JOptionPane.showMessageDialog(NewYGPanel.this,"��½�˺�ֻ���԰�������,��ĸ,�»��ߣ�",	"��ʾ",JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}
				if(jtcom.getSelectedItem()==null){
					JOptionPane.showMessageDialog(NewYGPanel.this,"ѡ��Ա�����ڲ��ţ�",	"��ʾ",JOptionPane.WARNING_MESSAGE);
					return;
				}	
				
				ZH=jtYGXX[0].getText().trim();
				MM=jtYGXX[1].getText().trim();
				XM=jtYGXX[2].getText().trim();
				LXFS=jtYGXX[3].getText().trim();
				
				//��֤ͨ������û��ѡ��������б���ֵ
				if(XB.equals("XB"))
				{
					XB="��";
				}
				if(JS.equals("JS"))
				{
					JS="0";
				}
				
				//System.out.println("�˺ţ�"+ZH+"���룺"+MM+"��ϵ��ʽ��"+LXFS+"�Ա�"+XB+"����id��"+BMID+"��ɫid"+JS);
				//�����߳�
				dataGeted=false;
				new Thread()
				{
					public void run()
					{
						StringBuilder sb=new StringBuilder();
						sb.append(ADD_YG);
						sb.append(ZH+"<->");
						sb.append(MM+"<->");
						sb.append(XM+"<->");
						sb.append(LXFS+"<->");
						sb.append(XB+"<->");
						sb.append(BMID+"<->");//����id��
						sb.append(JS);//��ɫ
						sb.append(ADD_YG);
						String msg=SocketUtil.sendAndGetMsg(sb.toString());
							dataGeted=true;
							JOptionPane.showMessageDialog(NewYGPanel.this,"��ϲ��Ա����ӳɹ���","��ʾ",JOptionPane.NO_OPTION);
							for(int i=0;i<jtYGXX.length;i++)
							{
								jtYGXX[i].setText(null);
								ygxb.setSelectedIndex(0);
								jtcom.setSelectedItem(null);
								jcomYGJS.setSelectedIndex(0);
							}
					}
				}.start();
				//�����߳�
				LoginWindow.watchThread();
				
				
			}
		});
		
		
		initXB();
		initJS();
		initBM();
		
		
	}
	
	
	//��ʼ���Ա������б�
	private void initXB()
	{
		ygxb.setBounds(78, 40+30*4-25, 120, 20);
		this.add(ygxb);
		ygxb.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange()==ItemEvent.DESELECTED)
				{
					XB=ygxb.getSelectedItem().toString();
				}	
			}
		});
	}
	
	
	//��ʼ����ɫ�����б�
	private void initJS()
	{
		    jcomYGJS.setBounds(78, 40+30*6-25, 120, 20);
		    this.add(jcomYGJS);
			String msg1=SocketUtil.sendAndGetMsg(GET_JS+GET_JS);
			jcomYGJS.removeAllItems();
			List<String[]>list=SocketUtil.strToList(msg1);
			for(int i=0;i<list.size();i++)
			{
				jcomYGJS.addItem(new JCOMDataObj(list.get(i)[0],list.get(i)[1]));
			}
		
			jcomYGJS.addItemListener(new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				if(e.getStateChange()==ItemEvent.DESELECTED)
				{
					JCOMDataObj sel;
					sel=(JCOMDataObj)e.getItem();
					JS=sel.getId();
				}	
			}
		});
	}
	
	
	//��ʼ�����������б�
	private void initBM()
	{
		    jtcom.setBounds(78, 40+30*5-25, 120, 20);
		    jtcom.setSelectedItem(null);
		    this.add(jtcom);
		    //������Ϣ�������
			String msg = GET_BM;
			StringBuilder sb = new StringBuilder();
			sb.append(msg);
			sb.append(msg);
			String result=SocketUtil.sendAndGetMsg(sb.toString());
			DefaultTreeModel dtm=TreeModelUitl.getTreeModel(SocketUtil.strToList(result));
			jtBMXZ.setModel(dtm);
			jtBMXZ.setCellRenderer(new BKJTreeCellRenderer());
			jtcom.addItemListener(new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent e) 
				{
					if(e.getStateChange()==ItemEvent.DESELECTED)
					{
						TreePath tp=(TreePath)e.getItem();
						BKJTreeNode node=(BKJTreeNode) tp.getLastPathComponent();
						BMID=node.getId()+"";
					}
					
				}
			});
	
	}
	
	
	
	
	
	
	

}
