package com.bn.jm.jbqxck;

import javax.swing.table.AbstractTableModel;

/*
 * ����Ȩ�޲鿴��ģ��
 */
@SuppressWarnings({"serial"})
public class JBQXTableModel extends AbstractTableModel
{
	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	JBQXCKPanel jbqxckpn;
	
	public JBQXTableModel(JBQXCKPanel jbqxckpn)
	{
		this.jbqxckpn=jbqxckpn;
	}
	
	//�������
	@Override
	public int getColumnCount() {
		return 2;
	}

	//�������
	@Override
	public int getRowCount() {
		return jbqxckpn.tableDataQX.size();
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {
		return jbqxckpn.tableDataQX.get(r)[c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return jbqxckpn.typeArrayQX[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return jbqxckpn.headQX[arg0];
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
