package com.bn.jm.bmgl;

import javax.swing.table.AbstractTableModel;

/*
 * 基本权限查看表模型
 */
@SuppressWarnings({"serial"})
public class BMYGTableModel extends AbstractTableModel
{
	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	BMGLPanel bmglpn;
	
	public BMYGTableModel(BMGLPanel bmglpn)
	{
		this.bmglpn=bmglpn;
	}
	
	//获得列数
	@Override
	public int getColumnCount() {
		return 2;
	}

	//获得行数
	@Override
	public int getRowCount() {
		return bmglpn.tableData.length;
	}

	//获得某单元格的值
	@Override
	public Object getValueAt(int r, int c) {
		return bmglpn.tableData[r][c];
	}

	//获得每一列的类型
	@Override
	public Class<?> getColumnClass(int arg0) {
		return bmglpn.typeArray[arg0];
	}

	//获得每一列的列头
	@Override
	public String getColumnName(int arg0) {
		return bmglpn.head[arg0];
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
