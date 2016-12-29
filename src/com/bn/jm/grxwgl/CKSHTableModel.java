package com.bn.jm.grxwgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class CKSHTableModel extends AbstractTableModel
{

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	CKSHPanel ckshglpn;
	
	public CKSHTableModel(CKSHPanel ckshglpn)
	{
		this.ckshglpn=ckshglpn;
	}
	//�������
	@Override
	public int getColumnCount() {
		return ckshglpn.head.length;
	}

	//�������
	@Override
	public int getRowCount() {
		return ckshglpn.tableData.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {	
		return ckshglpn.tableData[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return ckshglpn.typeArray[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return ckshglpn.head[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c==3)return true;
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
	@Override
	public void setValueAt(Object value, int r, int c)
	{
					
	}	
	
	
}

