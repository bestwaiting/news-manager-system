package com.bn.jm.ygxxgl;

import java.awt.Component;
import java.util.Enumeration;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.bn.util.BKJTreeCellRenderer;
import com.bn.util.BKJTreeNode;
import com.sunking.swing.JTreeComboBox;

public class YGBMJTreeComboBoxEidtor extends AbstractCellEditor implements TableCellEditor, TreeSelectionListener
{
	
	YGXXGLPanel ygxxglPn;
	private JTree jtBM=new JTree();
	private JTreeComboBox jtcb=new JTreeComboBox(jtBM);
	TreePath treePathJcom=null;
	String data=null;
	
	
	
	public YGBMJTreeComboBoxEidtor(YGXXGLPanel ygxxglPn) 
	{
		this.ygxxglPn=ygxxglPn;
		jtBM.setModel(ygxxglPn.dtm);
		jtBM.setCellRenderer(new BKJTreeCellRenderer());
		jtBM.addTreeSelectionListener(this);
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) 
	{
		TreePath tp=e.getNewLeadSelectionPath();
		BKJTreeNode node=(BKJTreeNode) tp.getLastPathComponent();
		data=node.getId()+"";	
		jtcb.setSelectedItem(tp);
	}

	

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) 
	{
		String id=value.toString();
		int bmid=Integer.parseInt(id);
		expandNode(bmid, jtBM);
		if(treePathJcom!=null)
		{
			jtcb.setSelectedItem(treePathJcom);
		}
		return jtcb;
	}
	
	@Override
	public Object getCellEditorValue() 
	{
		return data;
	}

	
	
	//չ��ָ���ڵ㿪ʼ=============================================================
	private BKJTreeNode target=null;
	public void expandNode(int jdid,JTree jt)//jdid-�ڵ�id
	{		
		//����Ŀ��ڵ�����
		target=null;
		//��ȡ���ڵ�
		if(jt.getModel()==null)
		{
			jtBM.setModel(ygxxglPn.dtm);
		}
		BKJTreeNode root=(BKJTreeNode)jt.getModel().getRoot();
		if(root.getId()==jdid){//������ڵ���Ŀ��ڵ�
			TreeNode[] tna=root.getPath();
			//���ݽڵ��id�õ�ѡ��·��
			treePathJcom=new TreePath(tna);
			jt.setSelectionPath(new TreePath(tna));
			jt.scrollPathToVisible(new TreePath(tna));
			return;
		}
		//�ݹ������Ѱ��Ŀ��ڵ�
		dgBl(root,jdid);
		//��ȡ�Ӹ��ڵ㵽Ŀ��ڵ��·��
		if(target==null)return;
		TreeNode[] tna=target.getPath();
		//ѡ��ָ���Ľڵ㲢չ��
		treePathJcom=new TreePath(tna);
		jt.setSelectionPath(new TreePath(tna));
		jt.scrollPathToVisible(new TreePath(tna));
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



	
}
