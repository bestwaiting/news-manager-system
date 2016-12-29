package com.bn.jm.jsqxgl;

import javax.swing.table.AbstractTableModel;
/*
 * ��ɫ��ģ��
 */
@SuppressWarnings({"serial"})
public class JSTableModel extends AbstractTableModel
{

	//ָ����Ԫ���ֵ�Ƿ�ı�
	boolean isCellChanged = false;
	
	//����༭������
	int lastEditRow ;
	JSQXGLPanel jsqxglpn;
	
	public JSTableModel(JSQXGLPanel jsqxglpn)
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
		return jsqxglpn.tableDataJS.size();
	}

	//���ĳ��Ԫ���ֵ
	@Override
	public Object getValueAt(int r, int c) {
		if(jsqxglpn.tableDataJS.get(r)[c]!=null)
		return jsqxglpn.tableDataJS.get(r)[c];
		return "";
	}

	//���ÿһ�е�����
	@Override
	public Class<?> getColumnClass(int arg0) {
		return jsqxglpn.typeArrayJS[arg0];
	}

	//���ÿһ�е���ͷ
	@Override
	public String getColumnName(int arg0) {
		return jsqxglpn.headJS[arg0];
	}

	//�����Ƿ�ɱ༭
	@Override
	public boolean isCellEditable(int r, int c) {
		if(c==0)return false;
		return true;
	}

	//����ĳһ��Ԫ���ֵ   ����ڵ�ֵ���ʱ�����
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
