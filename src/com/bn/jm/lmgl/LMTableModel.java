package com.bn.jm.lmgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class LMTableModel extends AbstractTableModel
{
	//��������ÿһ�е�����
	Class[] typeArray={Integer.class,Integer.class,String.class};
	//��ɫ��ͷ
	String[] head={"˳��ID","��ĿID","��Ŀ����"};
	//��ɫ�������
	Object[][] tableData;
	Object[][] OritableData;
	
	LMGLPanel lmglPn;

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	
	public LMTableModel(LMGLPanel lmglPn) 
	{
		this.lmglPn=lmglPn;
	}
	
	//�������
	@Override
	public int getColumnCount() {
		return head.length;
	}

	//�������
	@Override
	public int getRowCount() {
		return tableData.length;
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {	
		return tableData[r][c];
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return typeArray[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return head[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c==2)return true;
		return false;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
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

