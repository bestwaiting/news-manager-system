package com.bn.jm.shgl;

import javax.swing.table.AbstractTableModel;
/*
 * 角色表模型
 */
@SuppressWarnings({"serial"})
public class SHJLTableModel extends AbstractTableModel
{

	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	SHGLPanel shglpn;
	
	public SHJLTableModel(SHGLPanel shglpn)
	{
		this.shglpn=shglpn;
	}
	//获得列数
	@Override
	public int getColumnCount() {
		return shglpn.head.length;
	}

	//获得行数
	@Override
	public int getRowCount() {
		return shglpn.tableData.length;
	}

	//获得某单元格的值
	@Override
	public Object getValueAt(int r, int c) {	
		return shglpn.tableData[r][c];
	}

	//获得每一列的类型
	@Override
	public Class<?> getColumnClass(int arg0) {
		return shglpn.typeArray[arg0];
	}

	//获得每一列的列头
	@Override
	public String getColumnName(int arg0) {
		return shglpn.head[arg0];
	}

	//设置是否可编辑
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c>7)return true;
		return false;
	}

	//设置某一单元格的值   表格内的值变得时候调用
	@Override
	public void setValueAt(Object value, int r, int c)
	{
					
	}	
	
	
}

