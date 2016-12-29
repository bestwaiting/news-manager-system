package com.bn.jm.shgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class SHJLTableModel extends AbstractTableModel
{

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	SHGLPanel shglpn;
	
	public SHJLTableModel(SHGLPanel shglpn)
	{
		this.shglpn=shglpn;
	}
	//�������
	@Override
	public int getColumnCount() {
		return shglpn.head.length;
	}

	//�������
	@Override
	public int getRowCount() {
		return shglpn.tableData.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {	
		return shglpn.tableData[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return shglpn.typeArray[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return shglpn.head[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c>7)return true;
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
	@Override
	public void setValueAt(Object value, int r, int c)
	{
					
	}	
	
	
}

