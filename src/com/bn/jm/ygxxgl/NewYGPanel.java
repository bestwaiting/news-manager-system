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
	
	private JLabel []jlYGXX=//员工信息JLabel
	{
			new JLabel("登陆账号"),new JLabel("登陆密码"), new JLabel("真实姓名"),new JLabel("联系方式"),new JLabel("员工性别"),
			new JLabel("所在部门"),new JLabel("员工角色")
	};
	
	String ZH="ZH";
	String MM="MM";
	String XM="XM";
	String LXFS="LXFS";
	
	
	 JTextField jtYGXX[]=//员工信息JTextField
	{
		new JTextField(1),new JTextField(1),new JTextField(1),new JTextField(1)
	};
	
	private String strXB[]={"男","女"};
	private JComboBox ygxb=new JComboBox(strXB);//员工性别
	private String XB="XB";
	
	
	private JTree jtBMXZ=new JTree();//部门选择树
	private JTreeComboBox jtcom = new JTreeComboBox(jtBMXZ);//部门选择树
	private String BMID="BMID";
	
	private JComboBox jcomYGJS = new JComboBox();//角色
	private String JS="JS";
	
	private JButton submit=new JButton("添加");
	
	
	public NewYGPanel() 
	{
		this.setLayout(null);
		//添加JLabel
		for(int i=0;i<jlYGXX.length;i++){
			this.add(jlYGXX[i]);
			jlYGXX[i].setBounds(25, 40+30*i-25, 60, 20);
			
		}
		//添加JTextFiled
		for(int i=0;i<jtYGXX.length;i++){
			this.add(jtYGXX[i]);
			jtYGXX[i].setBounds(78, 40+30*i-25, 120, 20);
			jtYGXX[i].setText(null);
		}
		
		//添加回车监听
		jtYGXX[0].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[1].requestFocus();}});
		jtYGXX[1].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[2].requestFocus();}});
		jtYGXX[2].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){jtYGXX[3].requestFocus();}});
		jtYGXX[3].addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){ygxb.requestFocus();}});
		
		
		//添加按钮
		this.add(submit);
		submit.setBounds(120, 40+30*7-17, 80, 25);
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				//输入验证
				for(int i=0;i<jtYGXX.length;i++){
					if(jtYGXX[i].getText().trim().length()<=0){
						JOptionPane.showMessageDialog(NewYGPanel.this,"请输入"+jlYGXX[i].getText()+"！",	"提示",JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(i==0)
					{
						String text=jtYGXX[i].getText().trim();
						if(!text.matches("\\w+"))
						{
							JOptionPane.showMessageDialog(NewYGPanel.this,"登陆账号只可以包含数字,字母,下划线！",	"提示",JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}
				if(jtcom.getSelectedItem()==null){
					JOptionPane.showMessageDialog(NewYGPanel.this,"选择员工所在部门！",	"提示",JOptionPane.WARNING_MESSAGE);
					return;
				}	
				
				ZH=jtYGXX[0].getText().trim();
				MM=jtYGXX[1].getText().trim();
				XM=jtYGXX[2].getText().trim();
				LXFS=jtYGXX[3].getText().trim();
				
				//验证通过，给没有选择的下拉列表赋初值
				if(XB.equals("XB"))
				{
					XB="男";
				}
				if(JS.equals("JS"))
				{
					JS="0";
				}
				
				//System.out.println("账号："+ZH+"密码："+MM+"联系方式："+LXFS+"性别："+XB+"部门id："+BMID+"角色id"+JS);
				//任务线程
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
						sb.append(BMID+"<->");//部门id、
						sb.append(JS);//角色
						sb.append(ADD_YG);
						String msg=SocketUtil.sendAndGetMsg(sb.toString());
							dataGeted=true;
							JOptionPane.showMessageDialog(NewYGPanel.this,"恭喜，员工添加成功！","提示",JOptionPane.NO_OPTION);
							for(int i=0;i<jtYGXX.length;i++)
							{
								jtYGXX[i].setText(null);
								ygxb.setSelectedIndex(0);
								jtcom.setSelectedItem(null);
								jcomYGJS.setSelectedIndex(0);
							}
					}
				}.start();
				//监视线程
				LoginWindow.watchThread();
				
				
			}
		});
		
		
		initXB();
		initJS();
		initBM();
		
		
	}
	
	
	//初始化性别下拉列表
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
	
	
	//初始化角色下拉列表
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
	
	
	//初始化部门下拉列表
	private void initBM()
	{
		    jtcom.setBounds(78, 40+30*5-25, 120, 20);
		    jtcom.setSelectedItem(null);
		    this.add(jtcom);
		    //发送消息获得数据
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
