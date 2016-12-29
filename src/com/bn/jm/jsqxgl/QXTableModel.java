package com.bn.jm.jsqxgl;

import javax.swing.table.AbstractTableModel;
/*
 * Ȩ�ޱ�ģ��
 */
@SuppressWarnings({"serial"})
public class QXTableModel extends AbstractTableModel
{
	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	JSQXGLPanel jsqxglpn;
	
	public QXTableModel(JSQXGLPanel jsqxglpn)
	{
		this.jsqxglpn=jsqxglpn;
	}
	//�������
	@Override
	public int getColumnCount() {
		return 2;
	}

	//�������
	@Override
	public int getRowCount() {
		return jsqxglpn.tableDataQX.size();
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {
		return jsqxglpn.tableDataQX.get(r)[c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return jsqxglpn.typeArrayQX[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return jsqxglpn.headQX[arg0];
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
