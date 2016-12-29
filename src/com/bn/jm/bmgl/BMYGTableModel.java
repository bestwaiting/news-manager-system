package com.bn.jm.bmgl;

import javax.swing.table.AbstractTableModel;

/*
 * ����Ȩ�޲鿴��ģ��
 */
@SuppressWarnings({"serial"})
public class BMYGTableModel extends AbstractTableModel
{
	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	BMGLPanel bmglpn;
	
	public BMYGTableModel(BMGLPanel bmglpn)
	{
		this.bmglpn=bmglpn;
	}
	
	//�������
	@Override
	public int getColumnCount() {
		return 2;
	}

	//�������
	@Override
	public int getRowCount() {
		return bmglpn.tableData.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {
		return bmglpn.tableData[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return bmglpn.typeArray[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return bmglpn.head[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
	@Override
	public void setValueAt(Object value, int r, int c) 
	{
		
	}
}
