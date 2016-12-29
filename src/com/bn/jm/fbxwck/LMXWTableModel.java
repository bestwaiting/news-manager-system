package com.bn.jm.fbxwck;

import javax.swing.table.AbstractTableModel;
/*
 * 角色表模型
 */
@SuppressWarnings({"serial"})
public class LMXWTableModel extends AbstractTableModel
{
	//个人新闻每一列的类型
	Class[] typeArray={Integer.class,Integer.class,String.class,String.class,String.class,String.class};
	//角色表头
	String[] head={"顺序ID","新闻ID","新闻名称","新闻概述","发布时间","提交人"};
	//角色表格数据
	Object[][] tableData;
	
	FBXWCKPanel fbxwckPn;

	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	
	public LMXWTableModel(FBXWCKPanel fbxwckPn) 
	{
		this.fbxwckPn=fbxwckPn;
	}
	
	//获得列数
	@Override
	public int getColumnCount() {
		return head.length;
	}

	//获得行数
	@Override
	public int getRowCount() {
		return tableData.length;
	}

	//获得某单元格的值
	@Override
	public Object getValueAt(int r, int c) {	
		return tableData[r][c];
	}

	//获得每一列的类型
	@Override
	public Class<?> getColumnClass(int arg0) {
		return typeArray[arg0];
	}

	//获得每一列的列头
	@Override
	public String getColumnName(int arg0) {
		return head[arg0];
	}

	//设置是否可编辑
	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

	//设置某一单元格的值   表格内的值变得时候调用
	@Override
	public void setValueAt(Object value, int r, int c)
	{	
					
	}	
	
	
}

