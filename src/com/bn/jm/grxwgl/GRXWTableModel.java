package com.bn.jm.grxwgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class GRXWTableModel extends AbstractTableModel
{

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	GRXWGLPanel grxwglpn;
	
	public GRXWTableModel(GRXWGLPanel grxwglpn)
	{
		this.grxwglpn=grxwglpn;
	}
	//�������
	@Override
	public int getColumnCount() {
		return grxwglpn.head.length;
	}

	//�������
	@Override
	public int getRowCount() {
		return grxwglpn.tableData.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {	
		return grxwglpn.tableData[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return grxwglpn.typeArray[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return grxwglpn.head[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c>5)return true;
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
	@Override
	public void setValueAt(Object value, int r, int c)
	{
					
	}	
	
	
}

