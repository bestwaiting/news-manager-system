package com.bn.jm.lmgl;

import javax.swing.table.AbstractTableModel;
/*
 * 角色表模型
 */
@SuppressWarnings({"serial"})
public class LMTableModel extends AbstractTableModel
{
	//个人新闻每一列的类型
	Class[] typeArray={Integer.class,Integer.class,String.class};
	//角色表头
	String[] head={"顺序ID","栏目ID","栏目名称"};
	//角色表格数据
	Object[][] tableData;
	Object[][] OritableData;
	
	LMGLPanel lmglPn;

	//指定单元格的值是否改变
	boolean isCellChanged = false;
	
	//最近编辑过的行
	int lastEditRow ;
	
	public LMTableModel(LMGLPanel lmglPn) 
	{
		this.lmglPn=lmglPn;
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
		if(c==2)return true;
		return false;
	}

	//设置某一单元格的值   表格内的值变得时候调用
	@Override
	public void setValueAt(Object value, int r, int c)
	{
		lmglPn.lastEditRowInLM=r;
		String data=value.toString();
		tableData[r][2]=data;
		String newData = tableData[r][2].toString();
		String oldData = OritableData[r][2].toString();
		if(!newData.equals(oldData))
		{
			lmglPn.isDataChanged=true;
		}
					
	}	
	
	
}

