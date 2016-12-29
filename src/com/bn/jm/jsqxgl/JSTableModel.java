package com.bn.jm.jsqxgl;

import javax.swing.table.AbstractTableModel;
/*
 * 角色表模型
 */
@SuppressWarnings({"serial"})
public class JSTableModel extends AbstractTableModel
{

	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	JSQXGLPanel jsqxglpn;
	
	public JSTableModel(JSQXGLPanel jsqxglpn)
	{
		this.jsqxglpn=jsqxglpn;
	}
	//获得列数
	@Override
	public int getColumnCount() {
		return 2;
	}

	//获得行数
	@Override
	public int getRowCount() {
		return jsqxglpn.tableDataJS.size();
	}

	//获得某单元格的值
	@Override
	public Object getValueAt(int r, int c) {
		if(jsqxglpn.tableDataJS.get(r)[c]!=null)
		return jsqxglpn.tableDataJS.get(r)[c];
		return "";
	}

	//获得每一列的类型
	@Override
	public Class<?> getColumnClass(int arg0) {
		return jsqxglpn.typeArrayJS[arg0];
	}

	//获得每一列的列头
	@Override
	public String getColumnName(int arg0) {
		return jsqxglpn.headJS[arg0];
	}

	//设置是否可编辑
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c==0)return false;
		return true;
	}

	//设置某一单元格的值   表格内的值变得时候调用
	@Override
	public void setValueAt(Object value, int r, int c)
	{
		jsqxglpn.lastEditRowInJS=r;
		String[] s = new String[2];
		s[0]=jsqxglpn.tableDataJS.get(r)[0];
		s[1]=value.toString();
		jsqxglpn.tableDataJS.set(r,s);
		String newData = jsqxglpn.tableDataJS.get(r)[c];
		String oldData = jsqxglpn.origindataJS.get(r)[c];
		if(!newData.equals(oldData))
		{
			jsqxglpn.isDataChanged=true;
		}
	}
}
