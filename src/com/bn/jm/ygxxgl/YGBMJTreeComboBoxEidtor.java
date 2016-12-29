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

	
	
	//展开指定节点开始=============================================================
	private BKJTreeNode target=null;
	public void expandNode(int jdid,JTree jt)//jdid-节点id
	{		
		//声明目标节点引用
		target=null;
		//获取根节点
		if(jt.getModel()==null)
		{
			jtBM.setModel(ygxxglPn.dtm);
		}
		BKJTreeNode root=(BKJTreeNode)jt.getModel().getRoot();
		if(root.getId()==jdid){//如果根节点是目标节点
			TreeNode[] tna=root.getPath();
			//根据节点的id得到选择路径
			treePathJcom=new TreePath(tna);
			jt.setSelectionPath(new TreePath(tna));
			jt.scrollPathToVisible(new TreePath(tna));
			return;
		}
		//递归遍历树寻找目标节点
		dgBl(root,jdid);
		//获取从根节点到目标节点的路径
		if(target==null)return;
		TreeNode[] tna=target.getPath();
		//选中指定的节点并展开
		treePathJcom=new TreePath(tna);
		jt.setSelectionPath(new TreePath(tna));
		jt.scrollPathToVisible(new TreePath(tna));
	}
	
	public void dgBl(BKJTreeNode subRoot,int jdid)//递归遍历寻找目标节点
	{
		//获取当前节点的孩子节点列表
		@SuppressWarnings("unchecked")
		Enumeration<BKJTreeNode> e=(Enumeration<BKJTreeNode>)subRoot.children();
		
		//遍历当前节点的孩子节点列表
		while(e.hasMoreElements())
		{
			//获取一个孩子节点
			BKJTreeNode tempNode=(BKJTreeNode)e.nextElement();
			//判断此孩子节点是否为目标节点
			if(tempNode.getId()==jdid)
			{
				//若为目标节点
				target=tempNode;
				return;
			}
			else
			{
				//若不为目标节点则继续遍历
				dgBl(tempNode,jdid);
			}
		}
	}
	//展开指定节点结束=============================================================



	
}
