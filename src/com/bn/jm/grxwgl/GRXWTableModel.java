package com.bn.jm.grxwgl;

import javax.swing.table.AbstractTableModel;
/*
 * 角色表模型
 */
@SuppressWarnings({"serial"})
public class GRXWTableModel extends AbstractTableModel
{

	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	GRXWGLPanel grxwglpn;
	
	public GRXWTableModel(GRXWGLPanel grxwglpn)
	{
		this.grxwglpn=grxwglpn;
	}
	//获得列数
	@Override
	public int getColumnCount() {
		return grxwglpn.head.length;
	}

	//获得行数
	@Override
	public int getRowCount() {
		return grxwglpn.tableData.length;
	}

	//获得某单元格的值
	@Override
	public Object getValueAt(int r, int c) {	
		return grxwglpn.tableData[r][c];
	}

	//获得每一列的类型
	@Override
	public Class<?> getColumnClass(int arg0) {
		return grxwglpn.typeArray[arg0];
	}

	//获得每一列的列头
	@Override
	public String getColumnName(int arg0) {
		return grxwglpn.head[arg0];
	}

	//设置是否可编辑
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c>5)return true;
		return false;
	}

	//设置某一单元格的值   表格内的值变得时候调用
	@Override
	public void setValueAt(Object value, int r, int c)
	{
					
	}	
	
	
}

